package TYPES;

public class CFIELD_LIST
{
	public CFILD head;
	public CFIELD_LIST next;
	
	public CFIELD_LIST(CFILD head, CFIELD_LIST next)
	{
		this.head = head;
		this.next = next;
	}	
}
