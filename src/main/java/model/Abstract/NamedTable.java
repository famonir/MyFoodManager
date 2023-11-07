package model.Abstract;

import java.sql.SQLException;

/**
 * Abstract class for all classes with id's and names
 */
public class NamedTable {
    private int id;
    private String name;

    /**Constructor of named table with an id
     * @param idToSet the id of the table
     * @param nameToSet the name of the table
     */
    public NamedTable(int idToSet, String nameToSet) {
        this.id = idToSet;
        this.name = nameToSet;
    }

    /**Constructor of named table without an id
     * @param nameToSet the name of the new table
     */
    public NamedTable(String nameToSet) { this.name = nameToSet;}

    /**
     * ID getter
     * @return id
     */
    public int getID() {return this.id;}

    /**
     * Method to set id
     * @param newId the new id of the table
     */
    public void setID(int newId) {this.id = newId;}

    /**
     * Name getter
     * @return name
     */
    public String getName() {return this.name;}

    /**
     * Name setter
     * @param nameToSet New Name
     * @throws SQLException database exception
     */
    public void setName(String nameToSet) throws SQLException {this.name = nameToSet;}
}
