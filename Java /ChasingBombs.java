import java.awt.*;
import javax.swing.*;
import javax.swing.JPanel.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.util.Random;

/**
 * Chasing bombs is a game similar to Minesweeper where a user must uncover panels without finding a hidden bomb, each successful uncover
 * merrits a point, until all panels have been uncovered or the bomb is found. Difficulty is scalable by the users choice which will set the 
 * number of panels needed to be unconvered to win the game.
 *
 * @author Cyrus Bharry
 * @version 29/03/2020
 */
public class ChasingBombs extends JFrame 
{


    public static void main(String[] args){

        ChasingBombs game = new ChasingBombs();

    }

    //Layout manager for the three main panels in the game.
    private JPanel panel1 = new JPanel(new GridLayout(2,5,2,2));
    private JPanel panel2 = new JPanel(new GridBagLayout());
    private JPanel panel3 = new JPanel(new GridBagLayout());

    //Array of panels to be added to the game to act as a game panels.
    private JPanel[] panel = new JPanel[10];

    //Output message to display score.
    private JLabel output;

    //The buttons that the user can use to control the game, the name suggests thier function.
    private JButton play, exit, easy, intermediate, difficult;

    //The default value for how many panels are needed to win the game.
    private int safeLives = 5;

    //The users score.
    private int score = 0;

    //Flag to be set to true when the bomb is found.
    public boolean found = false;

    //Counts the number of panels that have been uncovered successfully.
    private int count = 0;

    /**
     * Constructor for the ChasingBombs class.
     */
    public ChasingBombs(){
        super("frame");
        setSize(800,400);
        makeFrame();
        setVisible(true);

    }

    /**
     * Changes the amount of safe panels that are needed to be uncovered before the game can end.
     * @Param number based from the button pressed.
     */
    private void changeDifficulty(int num){
        safeLives = num;
    }

    /**
     * Accessor method to return the number of panels needed to win the game. By default this value is 5.
     */
    public int getSafeLives(){
        return safeLives; 
    }

    /**
     * Method to set the boolean value of found to true when the bomb is found.
     */
    private void bombFound(){       
        found = true;
    }

    /**
     * Accessor method to get the score of the player.
     */
    public int getScore(){
        return score;
    }

    /**
     * Accessor method to get the number of panels that have been clicked.
     */
    public int getCount(){
        return count;
    }

    /**
     * Method to create the game frame, adding the three main game panels as well as the buttons and giving them fuctions.
     */
    private void makeFrame(){
        //Adds the three panels to the main frame and gives them different colours so they can be differentiated.
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout());

        panel1.setBackground(Color.BLACK);
        panel2.setBackground(Color.BLUE);
        panel3.setBackground(Color.GREEN);

        add(panel1);
        add(panel2);
        add(panel3);

        //Creates the buttons and text field for the game, giving them a identifier that the user can understand.
        play = new JButton("Play A Game");
        exit = new JButton("Exit");
        easy = new JButton("Easy");
        intermediate = new JButton("Intermediate");
        difficult = new JButton("Difficult");
        output = new JLabel("");
        output.setForeground(Color.WHITE);

        //Adds an action listener that calls methods based on the buttons functions.
        easy.addActionListener(source -> changeDifficulty(5));
        intermediate.addActionListener(source -> changeDifficulty(7));
        difficult.addActionListener(source -> changeDifficulty(9));

        play.addActionListener(source -> reset());
        exit.addActionListener(source -> System.exit(0));

        //Layout manager on the two panels for the buttons and text field.
        GridBagConstraints grid = new GridBagConstraints();

        grid.insets = new Insets(10,10,10,10);
        grid.gridx = 0;
        grid.gridy = 1;
        panel2.add(play, grid);
        grid.gridx = 0;
        grid.gridy = 2;
        panel2.add(exit, grid);
        grid.gridx = 0;
        grid.gridy = 3;
        panel2.add(output, grid);

        grid.gridx = 0;
        grid.gridy = 1;
        panel3.add(easy, grid);
        grid.gridx = 0;
        grid.gridy = 2;
        panel3.add(intermediate, grid);
        grid.gridx = 0;
        grid.gridy = 3;
        panel3.add(difficult, grid);

        createGamePanels();

    } 

    /**
     * Method that adds 10 panels to the first panel of the game. Each panel is clickable, earning a score for every click and one panel is 
     * assigned to be the bomb panel at random.
     */
    private void createGamePanels(){
        //Adds the 10 panels to the first panel by looping through an array of panels, sets the background colour to red.    
        int random = new Random().nextInt(10);      
        for(int i = 0; i < 10; i++){
            panel[i] = new JPanel();
            panel1.add(panel[i]);
            panel[i].setBackground(Color.RED);

            //Assigns the name of the panel, if the index of the panel matches the random value then it becomes the bomb.
            if(i == random){
                panel[i].setName("bomb");
            }else{
                String string = Integer.toString(i);
                panel[i].setName(string);
            }  

            //Adds the listener to each of the game panels giving them functionality.
            panel[i].addMouseListener(listener);

        }

    }

    /**
     * Method to reset the game when the 'Play A Game' button is pressed. Resets the score, the number of panels that have been clicked, the 
     * text field as well as creating a fresh set of panels to become the new game.
     */
    public void reset(){
        //Resets all tha variables in the game.
        found = false;
        score = 0;
        count = 0;
        safeLives = 5;
        output.setText("");

        //Methods called to remove the game panels and creates 10 new ones to act as a new game.
        panel1.removeAll();
        createGamePanels();

    }

    /**
     * Method to increase the score of the player when a panel is clicked.
     * Either ends the game if the panel name equals bomb, or adds values to score and count and changes the colour to indicate a successful 
     * panel selection.
     * @Param panel is the panel that has been clicked.
     */
    private void addScore(Component panel){
        String panelName = panel.getName();
        if (panelName.equals("bomb")){
            //Game eding condtions.
            bombFound();
            endGameLoss();
        }else{
            if(found == false ){
                score = score + 1;
                count =  count + 1;
                panel.setBackground(Color.YELLOW);   
                //Removes the listener so the panel cannot be clicked again once selected.
                panel.removeMouseListener(listener);
            }
        }

    }

    /**
     * Method that checks the winning condtions of the game. Checks the boolean flag to see if the bomb has been found, if it is false then 
     * compares the user score with either the amount of panels needed to win, or the total amount of panels (9) and changes the output 
     * text accordingly.
     */
    public void checkWin(){
        if(found == false){
            if(getScore() == getSafeLives()){
                endGameWin();
            }else if(getCount() == 9){
                output.setText("You win, you got all 9 points!");
            }
        }
    }

    /**
     * Method that is called if the game has been won. Updates the text field to display a winning message based on the amount of panels that
     * were needed to win the game and the score that the player achieved.
     */
    private void endGameWin(){
        if(getSafeLives() == 9){
            output.setText("You won, you got all the points!");
        }else{
            output.setText("You win, you got " + getScore() + " point(s)");
        }
    }

    /**
     * Method that is called if the game has been lost by finding the bomb, displays the score that the user achieved before finding the bomb
     */
    private void endGameLoss(){
        output.setText("You lost, you got " + getScore() + " point(s)");
    }

    //Nested class that creates a mouse listener that is used to act as a selection for a panel.
    MouseListener listener = new MouseAdapter()
    {
            @Override 
            public void mouseClicked(MouseEvent e){
                //Gets the panel name and calls method to add a point to the score and check for a win after every click of a panel.
                Component panel = (Component)e.getSource();
                String panelName = panel.getName();
                addScore(panel);
                checkWin();

            }
        };
}
