/**
 * @fichier GrapheCFF.java
 * @titre Réseau CFF
 * @description  This class is used to store the information of the cities in order to print them on the graphic program.
 * @auteurs Kevin Estalella & Federico Lerda
 * @date 21 Mars 2017
 * @version 1.0
 */

package ch.cff;

public class Ville {

	private String name;
	private int coordX;
	private int coordY;

	/**
	 * Constructors.
	 */
	public Ville () {
		super();
	}

	public Ville(String name, int coordX, int coordY) {
		super();
		this.name = name;
		this.coordX = coordX;
		this.coordY = coordY;
	}

	/*************************************
	 *
	 * GETTERS AND SETTERS
	 *
	 *************************************
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCoordX() {
		return coordX;
	}
	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}
	public int getCoordY() {
		return coordY;
	}
	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}
	/****************************************/

}