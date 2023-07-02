/**
 * Class Player
 * A single object represents the single player.
 * 
 * @author Olaf Chitil and Cyrus Bharry
 * @version 14/2/2020
 */

public class Player extends Character
{
    public Room goal;
    private int timeOut = 12;

    /**
     * Constructor, taking start room and goal room.
     * Pre-condition: start location is not null.
     */
    public Player(Room start, Room goal)
    {
        super(start);
        this.goal = goal;

    }

    /**
     * Check whether time limit has been reached.
     */
    public boolean isAtTimeLimit(int moveCount)
    {
        if(moveCount == timeOut){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Check whether goal has been reached.
     */
    public boolean isAtGoal()
    {
        if(getLocation().equals(goal)){
            return true;

        }else{
            return false;
        }
    }

    /**
     * Return a description.
     */
    public String toString()
    {
        return "you";
    }
}
