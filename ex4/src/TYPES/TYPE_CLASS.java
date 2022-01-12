package TYPES;

import AST.AST_DEC_FUNC;
import AST.AST_DEC_VAR;

import java.util.ArrayList;
import java.util.List;

public class TYPE_CLASS extends TYPE implements I_SYMBOL_TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;
  	// the type this L-class defines
	public TYPE_CLASS_INSTANCE classInstanceType;
  
  	public CFIELD_LIST cfield_list;

	/*********************************************************************/
	/* Data structures to keep track of the class and its cfield  */
	/*********************************************************************/
	// TODO the following lists could also be of type `String`, for the moment i keep it
	//      ASTs in case we'd need to extract more info

	/* Lists all the method, includes those inherited / overridden
	 * Will correspond to vtable in runtime. */
	public final List<AST_DEC_FUNC> methods_list;
	// func starting label saved in AST_DEC_FUNC.funcStartingLabel after AST_DEC_FUNC.IRme()

	/* Lists all the field, includes those inherited /overridden
	 * Will correspond to the object-struct in runtime. */
	// TODO - a note for shir: if you want to access to the initialized value of a field, you can access AST_DEC_VAR.exp
	//                         (which by semantic assumption should be int / string / nil)
	public final List<AST_DEC_VAR> fields_list;

	public String vt_name;


	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
  /****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father, String name, CFIELD_LIST cfield_list)
	{
		this.name = name;
		this.father = father;
		this.cfield_list = cfield_list;
		this.classInstanceType = new TYPE_CLASS_INSTANCE(this);

		if (father == null) {
			this.fields_list = new ArrayList<>();
			this.methods_list = new ArrayList<>();
		} else {
			// In case there is inheritance we perform 'prefixing'
			this.fields_list = new ArrayList<>(father.fields_list);
			this.methods_list = new ArrayList<>(father.methods_list);
		}
	}
  

	public TYPE findInClassAndSuperClasses(String cfield_name)
	{
		/* search for cfield_name in this class and its super classes and return its TYPE */
		TYPE_CLASS curr_class = this;
		while (curr_class != null) {
			CFIELD_LIST curr_cfield_list = curr_class.cfield_list;
			for(CFIELD_LIST node = curr_cfield_list; node != null; node = node.next){
				CFIELD cfield = node.head;
				if (cfield.name.equals(cfield_name)){
					return cfield.type;
				}
			}
			// name is not in caller_class, search in its super class
			curr_class = curr_class.father;
		}
		// didn't find cfield_name
		return null;
  	}

  	public boolean isSubClassOf(TYPE_CLASS potentialFather) {
		// note: a class can subclass itself
		// search for potentialFather in this class's fathers tree
		TYPE_CLASS curr_class = this;
		while (curr_class != null) {
			if (curr_class.name.equals(potentialFather.name))
				return true;
			// this father is not potentialFather, continue search
			curr_class = curr_class.father;
		}
		// didn't find potentialFather
		return false;
	}
  
	public TYPE getInstanceType() {
		return this.classInstanceType;
	}

	/* adds a given field-ast-node to the fields-list */
	public void addToFieldList(AST_DEC_VAR fieldToAdd) {
		String fieldNameToAdd = fieldToAdd.name;

		for (int i = 0; i < fields_list.size(); i++) {
			AST_DEC_VAR existingField = fields_list.get(i);

			// If the new field-name existed in the father, we'll override this field
			if (fieldNameToAdd.equals(existingField.name)) {
				fields_list.set(i, fieldToAdd);
				return;
			}
		}
		// If new field-name didn't exist in father, simply append it
		fields_list.add(fieldToAdd);
	}

	/* adds a given method-ast-node to the methods-list */
	public void addToMethodList(AST_DEC_FUNC methodToAdd) {
		String methodNameToAdd = methodToAdd.funcName;

		for (int i = 0; i < methods_list.size(); i++) {
			AST_DEC_FUNC existingMethod = methods_list.get(i);

			// If the new method-name existed in the father, we'll override this field
			// by the semantic assumption this is a valid override
			if (methodNameToAdd.equals(existingMethod.funcName)) {
				methods_list.set(i, methodToAdd);
				return;
			}
		}
		// If new method-name didn't exist in father, simply append it
		methods_list.add(methodToAdd);
	}

	/* return the offset of the field, or 0 if failed to find */
	public int getFieldOffset(String fieldNameToFind) {
		int offset = 1; // start with offset 1, as vtable is found in offset 0

		for (AST_DEC_VAR field : fields_list) {
			if (field.name.equals(fieldNameToFind)) {
				return offset;
			}
			offset++;
		}
		// failure to find the field results with 0 offset
		return 0;
	}

	/* return the offset of the method, or -1 if failed to find*/
	public int getMethodOffset(String methodNameToFind) {
		int offset = 0;

		for (AST_DEC_FUNC method : methods_list) {
			if (method.funcName.equals(methodNameToFind)) {
				return offset;
			}
			offset++;
		}
		// failure to find the method results with -1 offset
		return -1;
	}

	public String getStringFieldGlobalName(String fieldName) {
		return String.format("class_%s_field_%s", this.name, fieldName);
	}

}
