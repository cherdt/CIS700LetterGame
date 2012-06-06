/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package seven.ui;

/**
 *
 * @author Satyajeet
 */
public class Letter implements Cloneable{

	protected Object clone(){
		Letter l = new Letter(character,value);
		return l;
	}
    Character character;
    int value;
    public Letter(Character c, int s)
    {
        character = c;
        value = s;
    }
	/**
	 * @return the alphabet
	 */
	public Character getCharacter() {
		return character;
	}
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return character.toString();
	}

}
