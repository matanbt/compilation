package TYPES;

public class CFIELD_LIST
{
	public CFIELD head;
	public CFIELD_LIST next;
	
	public CFIELD_LIST(CFIELD head, CFIELD_LIST next)
	{
		this.head = head;
		this.next = next;
	}	
}
