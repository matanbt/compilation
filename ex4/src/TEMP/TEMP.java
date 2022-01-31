/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import REG_ALLOC.Palette;

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
		int mapped_register_num = Palette.getInstance().getColor(serial);
		return String.format("$t%d", mapped_register_num);
	}
}
