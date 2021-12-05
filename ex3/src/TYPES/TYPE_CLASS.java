package TYPES;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public CFIELD_LIST cfield_list;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,CFIELD_LIST cfield_list)
	{
		this.name = name;
		this.father = father;
		this.cfield_list = cfield_list;
	}

	public TYPE findInClassAndSuperClasses(String cfield_name){
		/* search for cfield_name in this class and it's super classes and return it's TYPE */
		TYPE_CLASS curr_class = this;
		while (curr_class != null){
			CFIELD_LIST curr_cfield_list = curr_class.cfield_list;
			for(CFIELD_LIST node = curr_cfield_list; node != null; node = node.next){
				CFIELD cfield = node.head;
				if (cfield.name.equals(cfield_name)){
					return cfield.type;
				}
			}
			// name is not in caller_class, search in it's super class
			curr_class = curr_class.father;
		}
		// didn't find cfield_name
		return null;
	}
}
