/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class TEMP
{
	protected int serial=0;
	
	public TEMP(int serial)
	{
		this.serial = serial;
	}
	
	public int getSerialNumber()
	{
		return serial;
	}

	public String getRegisterName(){
		return null;
//		return String.format("$t%d", mapped_register_num);  // TODO
	}
}
