package model.FundamentalClasses;

/**
 * record recipe to day planning
 * @param dayID ID of the day to add
 * @param guestsCount number of guests to the recipe
 * @param recipeID ID of the recipe
 * @param oldGuestsCount old count in case of guests count changing
 */
public record RecipeToDayPlanning(int dayID, int recipeID, int guestsCount, int oldGuestsCount) {
    /**
     * Day ID getter
     * @return day id
     */
    public int getDayID() {return this.dayID;}

    /**
     * Recipe ID getter
     * @return recipe id
     */
    public int getRecipeID() {return this.recipeID;}

    /**
     * Guests count getter
     * @return guests count int
     */
    public int getGuestsCount() {return this.guestsCount;}

    /**
     * Old guests count getter
     * @return old guests count int
     */
    public int getOldGuestsCount() {return this.oldGuestsCount;}
}
