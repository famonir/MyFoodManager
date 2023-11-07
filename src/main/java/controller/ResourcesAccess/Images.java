package controller.ResourcesAccess;

/**Enum used for all the images used by the view
 *
 */
public enum Images {
    /**
     * Main Path
     */
    MAINPATH {public String toString() {return "../src/main/resources/img/";}},

    /**
     * Plus img Path
     */
    PLUS {public String toString() {return MAINPATH + "plus.png";}},

    /**
     * Minus img Path
     */
    MINUS {public String toString() {return MAINPATH + "Minus.png";}},

    /**
     * Edit img Path
     */
    EDIT {public String toString() {return MAINPATH + "pencil_icon.png";}},

    /**
     * People img Path
     */
    PEOPLE {public String toString() {return MAINPATH + "peopleIcon.png";}},

    /**
     * Star img Path
     */
    STAR {public String toString() {return MAINPATH + "star.png";}},

    /**
     * Save img Path
     */
    SAVE {public String toString() {return MAINPATH + "save.png";}},

    /**
     * View img Path
     */
    VIEW {public String toString() {return MAINPATH + "view.png";}},

    /**
     * Delete img Path
     */
    DELETE {public String toString() {return MAINPATH + "cross.png";}},
}
