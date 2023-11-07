package model.Planning;

import model.FundamentalClasses.Recipe;
import model.FundamentalClasses.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**Class responsible for generating a food planning week
 *
 */
public class FoodPlanningGenerator {
    User user;
    List<DayPlanning> plannedDays;
    int vegBreakfasts;
    int vegLunches;
    int vegSuppers;
    int fishBreakfasts;
    int fishLunches;
    int fishSuppers;

    /**
     * Constructor FoodPlanningGenerator
     * @param plannedDaysToSet planned days for the FoodPlanningGenerator
     * @param vegBreakfastsToSet vegetarian breakfasts for the FoodPlanningGenerator
     * @param vegLunchesToSet vegetarian lunches for the FoodPlanningGenerator
     * @param vegSuppersToSet vegetarian suppers for the FoodPlanningGenerator
     * @param fishBreakfastsToSet fishy breakfasts for the FoodPlanningGenerator
     * @param fishLunchesToSet fishy lunches for the FoodPlanningGenerator
     * @param fishSuppersToSet fishy suppers for the FoodPlanningGenerator
     * @param userToSet users to set
     * @throws SQLException sql exception which can occur when calling the generatePlanning() method
     */
    public FoodPlanningGenerator(
            List<DayPlanning> plannedDaysToSet,
            int vegBreakfastsToSet,
            int vegLunchesToSet,
            int vegSuppersToSet,
            int fishBreakfastsToSet,
            int fishLunchesToSet,
            int fishSuppersToSet,
            User userToSet
    ) throws SQLException {
        plannedDays = plannedDaysToSet;
        vegBreakfasts = vegBreakfastsToSet;
        vegLunches = vegLunchesToSet;
        vegSuppers = vegSuppersToSet;
        fishBreakfasts = fishBreakfastsToSet;
        fishLunches = fishLunchesToSet;
        fishSuppers = fishSuppersToSet;
        user = userToSet;
        generatePlanning();
    }

    /**
     * Method to get a list Recipe
     * @param oldList old list of recipes
     * @param targetSize size as an int
     * @return new list of recipes
     */
    private List<Recipe> shortenList(List<Recipe> oldList, int targetSize) {
        List<Recipe> newList = new ArrayList<>();
        if (oldList.size() > 0) {
            int numberOfMissionMeals = targetSize - oldList.size();
            if (targetSize > oldList.size()) {
                for (int missingMealIndex = 0; missingMealIndex < numberOfMissionMeals; ++missingMealIndex) {
                    oldList.add(oldList.get(0));
                }
            }
            for (int index = 0; index < targetSize; ++index) {
                newList.add(oldList.get(index));
            }
        }

        return newList;
    }

    /**
     * Method to generate planning
     * @throws SQLException sql exception which can occur when calling the addRecipe() methods
     */
    private void generatePlanning() throws SQLException {
        List<Recipe> breakfasts = generateListsOfMeals("Breakfast", vegBreakfasts, fishBreakfasts);
        List<Recipe> lunches = generateListsOfMeals("Lunch", vegLunches, fishLunches);
        List<Recipe> suppers = generateListsOfMeals("Supper", vegSuppers, fishSuppers);

        for (int dayIndex = 0; dayIndex < 7; ++dayIndex) {
            plannedDays.get(dayIndex).addRecipe(breakfasts.get(dayIndex), plannedDays.get(dayIndex).getID());
            plannedDays.get(dayIndex).addRecipe(lunches.get(dayIndex), plannedDays.get(dayIndex).getID());
            plannedDays.get(dayIndex).addRecipe(suppers.get(dayIndex), plannedDays.get(dayIndex).getID());
        }
    }

    /**
     * Method to generate list of meals
     * @param dishType type of the dish
     * @param vegMeals number of vegetarian meals
     * @param fishMeals number of fishy meals
     * @return all the vegetarian meals
     */
    private List<Recipe> generateListsOfMeals(String dishType, int vegMeals, int fishMeals) {
        List<Recipe> allVegetarianMeals = new ArrayList<>();
        List<Recipe> allFishMeals = new ArrayList<>();
        List<Recipe> allOtherMeals = new ArrayList<>();

        for (Recipe recipe : user.getUsersRecipes()) {
            if (Objects.equals(recipe.getDishType(), dishType)) {
                if (recipe.checkFish()) {
                    allFishMeals.add(recipe);
                } else if (recipe.checkVegetarian()) {
                    allVegetarianMeals.add(recipe);
                }
                allOtherMeals.add(recipe);
            }
        }

        Collections.shuffle(allVegetarianMeals);
        Collections.shuffle(allFishMeals);
        Collections.shuffle(allOtherMeals);

        if (allVegetarianMeals.size() == 0) {
            vegMeals = 0;
        }

        if (allFishMeals.size() == 0) {
            fishMeals = 0;
        }

        allVegetarianMeals = shortenList(allVegetarianMeals, vegMeals);
        allFishMeals = shortenList(allFishMeals, fishMeals);
        allOtherMeals = shortenList(allOtherMeals, 7 - vegMeals - fishMeals);
        allFishMeals.addAll(allOtherMeals);
        allVegetarianMeals.addAll(allFishMeals);

        return allVegetarianMeals;
    }
}
