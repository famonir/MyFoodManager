package controller.WeekPlanningControllers;

/**Days enum type to use the day DAO
 *
 */
public enum DaysEnum {
    /**Monday enum
     *
     */
    MONDAY(0),
    /**Tuesday enum
     *
     */
    TUESDAY(1),
    /**Wednesday enum
     *
     */
    WEDNESDAY(2),
    /**Thursday enum
     *
     */
    THURSDAY(3),
    /**Friday enum
     *
     */
    FRIDAY(4),
    /**Saturday enum
     *
     */
    SATURDAY(5),
    /**Sunday enum
     *
     */
    SUNDAY(6);

    private final int dayIndex;

    /**
     * Constructor setting up the global var
     */
    DaysEnum(int i) {
        this.dayIndex = i;
    }

    /**
     * Getter for the day Index
     * @return Day Index
     */
    public int getDayIndex() {
        return this.dayIndex;
    }
}
