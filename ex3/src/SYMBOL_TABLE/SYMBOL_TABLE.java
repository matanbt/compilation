/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import AST.AST_DEC;
import AST.AST_DEC_CLASS;
import AST.AST_DEC_FUNC;
import AST.AST_Node;
import TYPES.*;
import TYPES.TYPE_FOR_SCOPE_BOUNDARIES.*;

import static TYPES.TYPE_FOR_SCOPE_BOUNDARIES.*;


/****************/
/* SYMBOL TABLE */
/****************/
public class SYMBOL_TABLE
{
	private int hashArraySize = 13; // must be in sync with hash()
	
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top = null;
	private int top_index = 0;

	/**********************************************/
	/* Additional useful data structures ... */
	/**********************************************/
	/*
	 keeps track on the main and most essential scopes: GLOBAL, FUNCTION, CLASS, METHOD
	 NOTE: this means that the longest the stack can get is 3
	 This will allow getMainScope() in O(1)
	 */
	List<TYPE_FOR_SCOPE_BOUNDARIES> main_scopes_stack;
	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s)
	{
		char first_letter = s.toLowerCase().charAt(0);
		int hash_letter = (first_letter - (int)('a')) / 2;
		return (0 <= hash_letter && hash_letter < hashArraySize) ? hash_letter : 0;
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name,TYPE t)
	{
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SYMBOL_TABLE_ENTRY next = table[hashValue];
	
		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++);
		// Note: each entry back-pointer on the last entered entry (used when removing scope)
		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		PrintMe();
	}

	/***********************************************/
	/* Find the inner-most scope element with name */
	/* if it DOESN'T find the given name, returns null - this case should be handled! */
	/***********************************************/
	public TYPE find(String name)
	{
		SYMBOL_TABLE_ENTRY e;
				
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if (name.equals(e.name))
			{
				return e.type;
			}
		}
		
		return null;
	}
	/**********************************************************************************************/
	/* Performs find, BUT considering ONLY the scope of the current function */
	/* VERY USEFUL for resolutions in Classes Methods (before checking class-fields) */
	/* returns the TYPE of the var if found in the function, else null */
	/**********************************************************************************************/
	public TYPE findWhenInFunctionScope(String name) {

		SYMBOL_TABLE_ENTRY curr = this.top;

		// iterates until a function-scope is found or until we find the name
		while (!(curr.name.equals("SCOPE-BOUNDARY")
				&& curr.type.name.equals(FUNC_SCOPE)))
		{
			curr = curr.prevtop;
			if (curr.name.equals(name)) {
				return curr.type; // FOUND
			}
		}

		return null; // NOT FOUND
	}

	/*
	 * Like 'find' BUT considering ONLY the current scope (could be global, if, etc)
	 */
	public TYPE findInCurrentScope(String name) {
		SYMBOL_TABLE_ENTRY curr = this.top;

		// iterates until a function-scope is found or until we find the name
		while (!curr.name.equals("SCOPE-BOUNDARY"))
		{
			curr = curr.prevtop;
			if (curr.name.equals(name)) {
				return curr.type; // FOUND
			}
		}

		return null; // NOT FOUND
	}
	/*
	 * returns the class in which we are in its scope
	 */
	public TYPE_CLASS findScopeClass() {
		TYPE_FOR_SCOPE_BOUNDARIES curr_main_scope = main_scopes_stack.get(main_scopes_stack.size() - 1);
		return curr_main_scope.name.equals(CLASS_SCOPE) ?
				(TYPE_CLASS) (curr_main_scope.scopeContextType) : null;
	}

	/*
	 * Finds the function in which we're in its scope
	 * (it's the latest and only function that was declared, because no nested-functions are allowed)
	 * returns the desired TYPE_FOR_SCOPE or null if fails.
	 */
	public TYPE_FUNCTION findScopeFunc()
	{
		TYPE_FOR_SCOPE_BOUNDARIES curr_main_scope = main_scopes_stack.get(main_scopes_stack.size() - 1);
		return curr_main_scope.name.equals(FUNC_SCOPE) ?
				(TYPE_FUNCTION)(curr_main_scope.scopeContextType) : null;
	}

	/*
	 * Returns whether we're in the global scope right now.
	 */
	public boolean isGlobalScope()
	{
		return main_scopes_stack.get(main_scopes_stack.size() - 1).name.equals(GLOB_SCOPE);
	}

	/***************************************************************************/
	/* begin scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	/* each scope can also have a context represented by the relevant AST_Node*/
	/* scopeName is a static field in TYPE_FOR_SCOPE_BOUNDARIES */
	public void beginScope(String scopeName, AST_Node contextScopeAST, TYPE contextScopeType)
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be ablt to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: NONE     */
		/************************************************************************/
		TYPE_FOR_SCOPE_BOUNDARIES scope_boundry = new TYPE_FOR_SCOPE_BOUNDARIES(scopeName, contextScopeAST, contextScopeType);
		enter(
			"SCOPE-BOUNDARY",
				scope_boundry);

		if(MAIN_SCOPES.contains(scopeName)) {
			// If we encounter a 'main'-scope we'll add it to the mini-stack
			main_scopes_stack.add(scope_boundry);
		}
		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
	}


	// overloading of beginScope
	public void beginScope(String scopeName)

	{
		beginScope(scopeName, null, null);
	}
	// overloading of beginScope
	public void beginScope(String scopeName, AST_Node contextScopeAST)
	{
		beginScope(scopeName, contextScopeAST, null);
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope()
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
		/**************************************************************************/
		while (!top.name.equals("SCOPE-BOUNDARY"))
		{
			table[top.index] = top.next;
			top_index = top_index - 1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */		
		/**************************************/
		if(MAIN_SCOPES.contains(top.type.name)) {
			// If we encounter a 'main'-scope we'll pop it from the mini-stack
			// assert that we pop the last scope we pushed (LIFO), otherwise BUG
			assert this.main_scopes_stack.get(main_scopes_stack.size() - 1) == top.type; // TODO remove assertion, it's for internal usage
			main_scopes_stack.remove(main_scopes_stack.size() - 1);
		}

		table[top.index] = top.next;
		top_index = top_index - 1;
		top = top.prevtop;

		/*********************************************/
		/* Print the symbol table after every change */		
		/*********************************************/
		PrintMe();
	}
	
	public static int n = 0;
	
	public void PrintMe()
	{
		int i = 0;
		int j = 0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt", n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0; i<hashArraySize-1; i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n", hashArraySize-1, hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SYMBOL_TABLE_ENTRY it=table[i];it!=null;it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtop_index);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SYMBOL_TABLE instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SYMBOL_TABLE() {
		// relies on default initialization
	}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SYMBOL_TABLE getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SYMBOL_TABLE();
			instance.main_scopes_stack = new ArrayList<>();
			/*****************************************/
			/* Our language builtin identifiers of functions and params will be HERE! */
			/*****************************************/
			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			/* Create global scope */
			instance.beginScope(GLOB_SCOPE);
			instance.enter("void",   TYPE_VOID.getInstance());
			instance.enter("int",   TYPE_INT.getInstance());
			instance.enter("string",TYPE_STRING.getInstance());

			/***************************************/
			/* [3] Enter library functions         */
			/***************************************/
			instance.enter(
				"PrintInt",
				new TYPE_FUNCTION(
					TYPE_VOID.getInstance(),
					"PrintInt",
					new TYPE_LIST(
						TYPE_INT.getInstance(),
						null)));

			instance.enter(
					"PrintString",
					new TYPE_FUNCTION(
							TYPE_VOID.getInstance(),
							"PrintString",
							new TYPE_LIST(
									TYPE_STRING.getInstance(),
									null
							)
					)
			);

			instance.enter(
					"PrintTrace",
					new TYPE_FUNCTION(
							TYPE_VOID.getInstance(),
							"PrintTrace",
							new TYPE_LIST(
									null, null
							)
					)
			);

    }

		return instance;
	}
}
