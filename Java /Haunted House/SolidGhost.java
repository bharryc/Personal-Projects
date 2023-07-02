import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Class SolidGhost
 * A solid ghost in the castle.
 * 
 * @author Olaf Chitil and Cyrus Bharry
 * @version 14/2/2020
 */

public class SolidGhost extends Ghost
{
    /**
     * Constructor initialising location and description.
     * Pre-condition: location not null.
     * Pre-condition: description not null.
     */
    public SolidGhost(Room loc, String desc)
    {
        super(loc, desc);
    }

    /**
     * Go to a random neighbouring room.
     * If there is no neighbour, then stay in current room.
     * @param rooms all rooms available - actually ignored
     */
     
    public void goRandom(List<Room> rooms)
    {
        // creates a local linked list containing all the possible exits of a room
        LinkedList<Room> randomRooms = new LinkedList();
        Room location = this.getLocation();
       
        // adds the exits for a particular room into the linked list
        for(Direction direction : Direction.values()){
            Room exits = location.getExit(direction);
            if (exits != null){
                randomRooms.add(exits);
            }
        }

        // randomly selects a room and moves the solid ghost to one
        int randRoom = (int)(Math.random() * randomRooms.size() -1);
        move(randomRooms.get(randRoom));
    }
}
