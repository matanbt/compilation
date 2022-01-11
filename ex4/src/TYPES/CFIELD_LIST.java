package TYPES;

public class CFIELD_LIST
{
	public CFIELD head;  // invariant: not null
	public CFIELD_LIST next;
	public CFIELD_LIST tail;  // updated only for the the first CFIELD_LIST node

	private CFIELD_LIST(CFIELD head, CFIELD_LIST next, CFIELD_LIST tail)
	{
		this.head = head;
		this.next = next;
		this.tail = tail;
	}

	public CFIELD_LIST(CFIELD head, CFIELD_LIST next)
	{
		this(head, next, null);
		this.tail = this;
	}

	public void addLast(CFIELD cfield) {
		// should be used only on the first CFIELD_LIST node (only its tail is updated)
		this.tail.next = new CFIELD_LIST(cfield, null);
		this.tail = this.tail.next;
	}



}
