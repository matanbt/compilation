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
	public TYPE_LIST data_members;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
	}

	public TYPE findInClassAndSuperClasses(String name){
		/* search for name in this class and it's super classes */
		TYPE_CLASS curr_class = this;
		while (curr_class != null){
			TYPE_LIST class_data_member_list = curr_class.data_members;
			for(TYPE_LIST node = class_data_member_list; node != null; node = node.next){
				TYPE class_data_member = node.head;
				if (class_data_member.name.equals(name)){
					return class_data_member;
				}
			}
			// name is not in caller_class, search in it's super class
			curr_class = curr_class.father;
		}
		// didn't find name
		return null;
	}
}
