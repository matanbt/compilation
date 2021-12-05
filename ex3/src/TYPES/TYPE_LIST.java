package TYPES;

public class TYPE_LIST
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public TYPE head;
	public TYPE_LIST next;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST(TYPE head,TYPE_LIST next)
	{
		this.head = head;
		this.next = next;
	}
}
