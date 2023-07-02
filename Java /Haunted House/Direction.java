/**
 * A direction in the game.
 * There exist only a few different directions.
 * 
 * @author Cyrus Bharry
 * @version 14/2/2020
 */
public enum Direction
{
    NORTH("north"), 

    WEST("west"), 

    SOUTH("south"), 

    EAST("east"), 

    UP("up"), 

    DOWN("down");

    private String name;

    /**
     * Constructor with parameter.
     * Pre-condition: name is not null.
     */
    private Direction(String name)
    {
        assert name != null : "Direction.Direction has null name";
        this.name = name;
        assert toString().equals(name) : "Direction.Direction produces wrong toString";
    }

    /**
     * Return the direction name.
     */
    public String toString()
    {
        return name;
    }
    
    /** 
     * Return the dual (opposite) of this direction.
     */
    public Direction dual()
    {
        Direction direction = this;
        switch(this){
            case NORTH:
                direction = Direction.SOUTH;
                break;
            case SOUTH:
                direction = Direction.NORTH;
                break;
            case EAST:
                direction = Direction.WEST;
                break;
            case WEST:
                direction = Direction.EAST;
                break;
        }
        return direction;
    }
}
