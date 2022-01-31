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

public class SAVED extends TEMP
{
	public SAVED(int serial)
	{
		super(serial);
	}
	
	public int getSerialNumber()
	{
		return this.serial;
	}

	public String getRegisterName(){
		return String.format("$s%d", this.serial);
	}
}
