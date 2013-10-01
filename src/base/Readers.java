/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package base;

import javax.swing.JOptionPane;

/**
 * Class that contains readers in various forms
 * 
 * @example BaseProgram
 * 
 *          (the tag @example followed by the name of an example included in
 *          folder 'examples' will automatically include the example in the
 *          javadoc.)
 * 
 */

public class Readers
{

	public final static String VERSION = "##library.prettyVersion##";

	/**
	 * Open a dialog so that the user can enter an integer value
	 * 
	 * @return -1 in case of error, the entered number otherwise
	 */
	public static int readInt(String pMessage)
	{
		while (true)
		{
			try
			{
				String lInput = JOptionPane.showInputDialog(pMessage);
				if (lInput == null)
				{
					return -1;
				}
				return Integer.parseInt(lInput);
			}
			catch (NumberFormatException pExc)
			{
				continue;
			}
		}
	}

	/**
	 * Convenience function to read an integer
	 * 
	 * @return -1 in case of error, the entered number otherwise
	 */
	public static int readInt()
	{
		return readInt("Please enter an integer");
	}

	/**
	 * Open a dialog so that the user can enter double value
	 * 
	 * @return -1.0 in case of error, the entered number otherwise
	 */
	public static double readDouble(String pMessage)
	{
		while (true)
		{
			try
			{
				String lInput = JOptionPane.showInputDialog(pMessage);
				if (lInput == null)
				{
					return -1;
				}
				return Double.parseDouble(lInput);
			}
			catch (NumberFormatException pExc)
			{
				continue;
			}
		}
	}

	/**
	 * Convenience function to read a double value
	 * 
	 * @return -1.0 in case of error, the entered number otherwise
	 */
	public static double readDouble()
	{
		return readDouble("Please enter a double value");
	}

	/**
	 * Open a dialog so that the user can enter character value
	 * 
	 * @return '\0' in case of error, the entered character at position 0
	 *         otherwise
	 */
	public static char readChar(String pMessage)
	{
		while (true)
		{
			String lInput = JOptionPane.showInputDialog(pMessage);
			if (lInput == null)
			{
				return '\0';
			}
			else if (lInput.length() < 1)
			{
				continue;
			}
			return lInput.charAt(0);
		}
	}

	/**
	 * Convenience function to read a single character
	 * 
	 * @return '\0' in case of error, the entered character at position 0
	 *         otherwise
	 */
	public static char readChar()
	{
		return readChar("Please enter a character");
	}

	/**
	 * Open a dialog so that the user can enter string
	 * 
	 * @return The entered string
	 */
	public static String readString(String pMessage)
	{
		return JOptionPane.showInputDialog(pMessage);
	}

	/**
	 * Convenience function to read a string
	 * 
	 * @return The entered string
	 */
	public static String readString()
	{
		return readString("Please enter a string");
	}

	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version()
	{
		return VERSION;
	}
}
