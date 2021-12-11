package TYPES;

import java.util.ArrayList;
import java.util.List;

public class TYPE_LIST
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public List<TYPE> lst;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST()
	{
		lst = new ArrayList<>();
	}

	public TYPE_LIST(TYPE t) {
		this();
		this.add(t);
	}

	public int size() {
		return lst.size();
	}

	public TYPE get(int i) {
		return lst.get(i);
	}

	/* adds to the end of the list */
	public void add(TYPE t) {
		lst.add(t);
	}
}
