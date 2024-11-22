import javax.swing.*;
import java.awt.*;

public class GradientGridPanel extends JPanel
{
    private int[][] myGrid;

    final int GRID_SIZE = 16;
    final int NUM_POSSIBLE_MODES = 5; // adjust this if you add to the list in the GradientGridFrame.


    private int mode; // which coloring scheme are we using?
    private Font mainFont; // the font for the numbers in the boxes.

    public GradientGridPanel()
    {
        super();
        myGrid = new int[GRID_SIZE][GRID_SIZE];
        setMode(0);
    }

    /**
     * update which mode we are in, recalculate the values and refresh the screen. This method gets called at the start
     * of the program, as well as any time the user changes the popup menu.
     * @param m - the number of the new mode selected.
     */
    public void setMode(int m)
    {
        if (m>-1 && m<NUM_POSSIBLE_MODES)
        {
            System.out.println(STR."Setting mode to \{m}.");
            mode = m;
            recalculate();
            repaint();
        }
        else
            throw new RuntimeException(STR."Set a mode that is out of bounds. Max value is currently \{NUM_POSSIBLE_MODES}. See line 8.");
    }

    /**
     * draws the grid, coloring and numbering the boxes based on the values in myGrid.
     * @param g the <code>Graphics</code> object to draw with
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int squareSize = Math.min(getWidth(), getHeight())/16;
        mainFont = new Font("Garamond",Font.BOLD, squareSize/3);
        g.setFont(mainFont);
        double numCells = Math.pow(GRID_SIZE, 2);
        for (int r = 0; r < GRID_SIZE; r++)
            for (int c = 0; c< GRID_SIZE; c++)
            {
                // color in this box.
                // Note: for "1.0f" the "f" means 1.0 as a float, rather than a double, since getHSBColor takes floats.
                g.setColor(Color.getHSBColor((float)(myGrid[r][c]/numCells), 1.0f, 1.0f));
                g.fillRect(c*squareSize, r*squareSize, squareSize, squareSize);
                // draw outline of box
                g.setColor(Color.GRAY);
                g.drawRect(c*squareSize, r*squareSize, squareSize, squareSize);
                // figure out where to draw the number
                int numWidth = g.getFontMetrics().stringWidth(STR."\{myGrid[r][c]}");
                int numX = (int)((c+0.5)*squareSize-numWidth/2);
                int numY = (int)((r+0.5)*squareSize+squareSize/6);
                // draw the number in dark gray over a light gray highlight, for contrast.
                g.setColor(Color.LIGHT_GRAY);
                g.drawString(""+myGrid[r][c], numX+1, numY+1);
                g.setColor(Color.DARK_GRAY);
                g.drawString(""+myGrid[r][c], numX, numY);

            }
    }

    /**
     * checks that each cell is adjacent (N, S, E, W, NE, SE, SW, NW) to a cell with
     * the number below it (unless the original cell holds zero), and every number 0-255 is used exactly once.
     * --> This should return true for the default case and return false for the bad examples. <--
     * @return whether all cells containing values 1-254 meet this requirement.
     */
    public boolean confirmGridMeetsSpecifications()
    {
        // suggested variable to track whether you have duplicate numbers in the grid. This defaults to all falses.
        boolean[] used = new boolean[GRID_SIZE * GRID_SIZE];

        //TODO: you write this method.
        //checking if every color is used
        for (int r=0; r<GRID_SIZE; r++){
            for (int c=0; c<GRID_SIZE; c++){
                used[myGrid[r][c]]=true;
            }
        }
        for (int i=0; i<used.length; i++){
            if(!used[i]){
                System.out.println(STR."Failed because \{i} wasn't used");
                return false;
            }
        }
        //checking if the next number is adjacent
        for (int r=0; r<GRID_SIZE; r++){
            for (int c=0; c<GRID_SIZE; c++){
                boolean isAdjacent = false;
                for (int i=-1; i<2; i++){
                    for (int j=-1; j<2; j++){
                        if ((isInBounds(r+i,c+j) && myGrid[r+i][c+j]==myGrid[r][c]+1)||myGrid[r][c]==255){
                            isAdjacent = true;
                        }
                    }
                }
                if (!isAdjacent){
                    System.out.println(STR."Failed at \{r}, \{c}.");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * based on which mode is active, update the values stored in myGrid.
     */
    public void recalculate()
    {

        switch(mode)
        {
            case 0:
                makeGoodExample();
                break;
            case 1:
                makeBadExample();
                break;
            case 2:
                makeAnotherBadExample();
                break;
            case 3:
                // TODO write code for case 2, either here or in its own method.
                break;
            case 4:
                // TODO write code for case 3, either here or in its own method.
                break;
            // you may add more cases, if you wish!
        }
    }

    /**
     * an example of a loop structure that fills in all the cells but does not meet the criteria.
     */
    private void makeBadExample()
    {
        int counter = 0;
        for (int r = 0; r < GRID_SIZE; r++)
        {
            for (int c=0; c<GRID_SIZE; c++)
            {
                myGrid[r][c] = counter;
                counter++;
            }
        }
    }
    private boolean isInBounds(int r, int c){
        if(r<0||r>GRID_SIZE-1){
            return false;
        }
        if(c<0||c>GRID_SIZE-1){
            return false;
        }
        return true;
    }

    /**
     * another example of a grid that shouldn't meet the grade... this one has the continuity (sort of), but it has a lot
     * of duplicates. (The continuity passes, but there are a LOT of zeroes.)
     */
    private void makeAnotherBadExample()
    {
        int counter = 0;
        for (int r = 0; r < GRID_SIZE; r++)
        {
            for (int c=0; c<GRID_SIZE; c++)
            {
                myGrid[r][c] = counter % 15;
                counter++;
            }
        }
    }

    /**
     * a brute-force example of a grid that does meet the criteria.
     */
    private void makeGoodExample() {
        // a brute force way of setting the starter values of the grid. You should use loops for yours.
        myGrid = new int[][]
                {{  0,  1,  4,  5,  8,  9, 12, 13, 16, 17, 20, 21, 24, 25, 28, 29},
                {  2,  3,  6,  7, 10, 11, 14, 15, 18, 19, 22, 23, 26, 27, 30, 31},
                { 61, 60, 57, 56, 53, 52, 49, 48, 45, 44, 41, 40, 37, 36, 33, 32},
                { 63, 62, 59, 58, 55, 54, 51, 50, 47, 46, 43, 42, 39, 38, 35, 34},
                { 64, 65, 68, 69, 72, 73, 76, 77, 80, 81, 84, 85, 88, 89, 92, 93},
                { 66, 67, 70, 71, 74, 75, 78, 79, 82, 83, 86, 87, 90, 91, 94, 95},
                {125,124,121,120,117,116,113,112,109,108,105,104,101,100, 97, 96},
                {127,126,123,122,119,118,115,114,111,110,107,106,103,102, 99, 98},
                {128,129,132,133,136,137,140,141,144,145,148,149,152,153,156,157},
                {130,131,134,135,138,139,142,143,146,147,150,151,154,155,158,159},
                {189,188,185,184,181,180,177,176,173,172,169,168,165,164,161,160},
                {191,190,187,186,183,182,179,178,175,174,171,170,167,166,163,162},
                {192,193,196,197,200,201,204,205,208,209,212,213,216,217,220,221},
                {194,195,198,199,202,203,206,207,210,211,214,215,218,219,222,223},
                {253,252,249,248,245,244,241,240,237,236,233,232,229,228,225,224},
                {255,254,251,250,247,246,243,242,239,238,235,234,231,230,227,226}};
    }



}
