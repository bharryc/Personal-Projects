/**
 * Class DualGhost
 * A dual ghost in the castle.
 * 
 * @author Cyrus Bharry
 * @version 14/2/2020
 */

public class DualGhost extends Ghost
{
    /**
     * Constructor initialising location and description.
     * Pre-condition: location not null.
     * Pre-condition: description not null.
     */
    public DualGhost(Room loc, String desc)
    {
        super(loc, desc);
        loc.dual();
    }

    /**
     * Method to move the dual ghost in the game.
     * Pre-Condition: location not null.
     */
    public void move(Room loc)
    {
        // calls the dual method which swaps the exits on the current location
        getLocation().dual();
        super.move(loc);
        // calls the dual method again after the ghost has been moved to reset the exits
        getLocation().dual();
    }

}
