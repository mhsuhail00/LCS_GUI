/**
 * Project Name: Longest Common SubSequence GUI
 * Description: This Algorithm is based on Dynamic-Programming Approach with Memoization. 
 *              It takes Sequence that is String of any length, Need Not Single Character
 * Created by: Mohammad Suhail
 * Date: 19-September-2024
 * Contact: mhsuhail00@gmail.com
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// It is GUI class that don't Implements Frame but Creates frame Object and uses it
public class LCS_GUI {
    // Entry Point of Code
    public static void main(String[] args) {
        // Main Frame that contains Only one JPanel "mainPanel" in ScrollPane "scrollMain"
        JFrame frame = new JFrame("LONGEST COMMON SUBSEQUENCE GUI");

            // To capture Input sequence from InputDialog
            // Assumed two Sequences X = <x1 x2 ...> & Y = <y1 y2 ...>
            String[] X;
            String[] Y;
            
            // Example Inputs - {"A","B","C","B","D","A","B"} & {"B","D","C","A","B","A"}
            // Example Inputs - {"E","L","E","C","T","R","O","N","I","C","S","C","I","E","N","C","E"};
            //                  & {"C","O","M","P","U","T","E","R","E","N","G","I","N","E","E","R","I","N","G"};
            
            // Remain Open Until Closed or Given Valid Inputs
            while (true) {
                String XSeq = JOptionPane.showInputDialog(frame, "Enter the X Sequence (space-separated): ");
                // If Someone Closes InputDialog
                if (XSeq == null) {
                    int option = JOptionPane.showConfirmDialog(frame, "Do you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        System.exit(0);  // Close the program if the user wants to exit
                    } else {
                        continue;  // Go back to asking for input if the user doesn't want to exit
                    }
                }
                // If there is Some Input
                if (XSeq.length()!=0) {
                    X = XSeq.split(" ");
                    break;
                // Input is Empty !WARNING
                } else {
                    JOptionPane.showMessageDialog(frame, "It is a Required Field", "Input X Sequence",JOptionPane.WARNING_MESSAGE);
                }
            }
            while (true) {
                String YSeq = JOptionPane.showInputDialog(frame, "Enter the Y Sequence (space-separated): ");
                // If Someone Closes InputDialog
                if (YSeq == null) {
                    int option = JOptionPane.showConfirmDialog(frame, "Do you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        System.exit(0);  // Close the program if the user wants to exit
                    } else {
                        continue;  // Go back to asking for input if the user doesn't want to exit
                    }
                }
                // If there is Some Input
                if (YSeq.length()!=0) {
                    Y = YSeq.split(" ");
                    break;
                // Input is Empty !WARNING
                } else {
                    JOptionPane.showMessageDialog(frame, "It is a Required Field", "Input Y Sequence",JOptionPane.WARNING_MESSAGE);
                }
            }
            // Calling Object of Backend Algorithm, Passed parameters
            LCS_Algo backend = new LCS_Algo(X.length, Y.length, X, Y);
            // Execute Algorithm to get the Direction-Table & Trace-Table
            backend.algo();
            // This traces all Possible LCS's
            backend.traceLCS(X.length, Y.length, 0);
        BackgroundPanel mainPanel = new BackgroundPanel(X.length, Y.length, X, Y, backend.trace, backend.direct, backend.lcsSTR, backend.lcsX, backend.lcsY, backend.select);
        JScrollPane scrollMain = new JScrollPane(mainPanel);
            scrollMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollMain.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 500));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(scrollMain);
        frame.setVisible(true);
    }
    // Implementaion of mainPanel
    static class BackgroundPanel extends JPanel{
        // It maps direction Matrix made in backend(usinng string) to Direction Matrix containing Arrows
        private String map(String str){
            // It means You need to go Up to Trace
            if(str==" UP ")
                return "\u2191";
            // It means You need to go Left to Trace
            else if(str=="LEFT")
                return "\u2190";
            // It means You need to Select this cell and go Diagonal
            else if(str==" YES")
                return "\u2196";
            // It means You need to go in both direction Either Up or Left
            else
                return "+";
        }
        // Constructor of Panel
        BackgroundPanel(int lengthX, int lengthY, String[] x, String[] y, int[][] trace, String[][] direction, ArrayList<String> lcstr, ArrayList<ArrayList<Integer>> lcsx, ArrayList<ArrayList<Integer>> lcsy, ArrayList<ArrayList<Boolean>> select){
            GridBagConstraints gc = new GridBagConstraints(); // Panels layout is made usin GridBagLayout           
            gc.weightx = 1; //
            this.setBackground(Color.WHITE); // Set Backound color to White
            this.setLayout(new GridBagLayout()); // Use GridBagLayout
            // Main Button to Run Animation
            JButton RUN = new JButton("FIND LCS");
                RUN.setFont(new Font("Cambria", Font.BOLD, 14)); // Set custom font
                RUN.setFocusPainted(false); // Prevent focus highlight
                gc.gridx = 0;
                gc.gridy = 0;
                gc.weighty = 0.03;
            this.add(RUN, gc);
            // Main Panel Contains 2 Panels("gridContainer" & "Element") and 1 TextArea("textBar") in ScrollPane("scrollPane")
            JPanel Main = new JPanel();
                Main.setBackground(Color.WHITE);
                gc.gridx = 0;
                gc.gridy = 1;
                gc.weighty = 0.97;
                gc.fill = GridBagConstraints.BOTH; // It Extends to fill any Empty Space in height and Width
                Main.setLayout(new GridBagLayout());  // Use GridBagLayout for Main
                GridBagConstraints mainGc = new GridBagConstraints();
                // 1st Panel which contains Another Panel To hold Dynamic No. of Buttons
                JPanel gridContainer = new JPanel();
                    gridContainer.setBackground(Color.WHITE);
                    // This Contains Dynamic Number of Buttons Arranged in Grid 
                    JPanel Trace = new JPanel();
                        Trace.setLayout(new GridLayout(lengthX + 1, lengthY + 1, 2, 2));
                        // Array of Buttons to be Placed 
                        JButton[][] traceButtons = new JButton[lengthX + 1][lengthY + 1];
                        for (int i = 0; i < (lengthX + 1); i++) {
                            for (int j = 0; j < (lengthY + 1); j++) {
                                traceButtons[i][j] = new JButton(String.valueOf(trace[i][j])+this.map(direction[i][j]));
                                traceButtons[i][j].setFocusPainted(false); // No focus effect when clicked
                                traceButtons[i][j].setContentAreaFilled(false); // No click area background
                                traceButtons[i][j].setBackground(Color.WHITE);
                                traceButtons[i][j].setForeground(Color.BLACK);
                                traceButtons[i][j].setOpaque(true);
                                Trace.add(traceButtons[i][j]);
                            }
                        } 
                        // Set constraints for Trace
                        mainGc.gridx = 0;
                        mainGc.gridy = 0;
                        mainGc.weightx = 0.5;
                        mainGc.weighty = 0.8;  // Most of the space should be taken by Trace
                        gridContainer.add(Trace, mainGc);  // Add Trace to Main with constraints
                    // Set constraints for gridContainer
                    mainGc.gridx = 0;
                    mainGc.gridy = 0;
                    mainGc.weightx = 1;
                    mainGc.weighty = 0.8;
                    Main.add(gridContainer, mainGc);
                // This Panel Contains 2 Panels that Contains Sequences
                JPanel Element = new JPanel();
                    Element.setLayout(new GridLayout(2, 1));
                    // This contains Button to show X Sequence
                    JPanel firstRowPanel = new JPanel();
                        firstRowPanel.setBackground(Color.WHITE);
                        JButton[][] traceButtonxy = new JButton[2][Math.max(lengthX + 1, lengthY + 1)];
                        traceButtonxy[0][0] = new JButton("X");
                        firstRowPanel.add(traceButtonxy[0][0]);
                        traceButtonxy[0][0].setBackground(Color.BLACK);
                        traceButtonxy[0][0].setForeground(Color.WHITE);
                        for (int i = 1; i < (lengthX + 1); i++) {
                            traceButtonxy[0][i] = new JButton(x[i - 1]);
                            traceButtonxy[0][i].setFocusPainted(false);
                            traceButtonxy[0][i].setContentAreaFilled(false);
                            traceButtonxy[0][i].setBackground(Color.WHITE);
                            traceButtonxy[0][i].setForeground(Color.BLACK);
                            traceButtonxy[0][i].setOpaque(true);
                            firstRowPanel.add(traceButtonxy[0][i]);
                        }
                    // This contains Button to show Y Sequence
                    JPanel secondRowPanel = new JPanel();
                        secondRowPanel.setBackground(Color.WHITE);
                        traceButtonxy[1][0] = new JButton("Y");
                        secondRowPanel.add(traceButtonxy[1][0]);
                        traceButtonxy[1][0].setBackground(Color.BLACK);
                        traceButtonxy[1][0].setForeground(Color.WHITE);
                        for (int i = 1; i < (lengthY + 1); i++) {
                            traceButtonxy[1][i] = new JButton(y[i - 1]);
                            traceButtonxy[1][i].setFocusPainted(false);
                            traceButtonxy[1][i].setContentAreaFilled(false);
                            traceButtonxy[1][i].setBackground(Color.WHITE);
                            traceButtonxy[1][i].setForeground(Color.BLACK);
                            traceButtonxy[1][i].setOpaque(true);
                            secondRowPanel.add(traceButtonxy[1][i]);
                        }
                    Element.add(firstRowPanel);
                    Element.add(secondRowPanel);
                // TextArea at Bottom to Print Longest Subsequences
                JTextArea textBar = new JTextArea(5, 30);  // 5 rows and 30 columns
                textBar.setEditable(false);  // Make it read-only
                textBar.setFont(new Font("Cambria", Font.PLAIN, 14));  // Set font
                textBar.setBackground(Color.BLACK);  // Set background color for visibility
                textBar.setForeground(Color.WHITE);  // Set text color
                
                // Adding JScrollPane in case the text overflows
                JScrollPane scrollPane = new JScrollPane(textBar);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    // Add the scrollPane (with the textBar) to the layout
                    gc.gridy = 2;  // Place below the existing grid
                    gc.weighty = 0.2;  // Allocate space for the text bar
                    gc.fill = GridBagConstraints.BOTH;
                    Main.add(scrollPane, gc);
                    
                // To dynamically update the text bar:
                textBar.append("Table Shows Memoized Values & Direction calculated using Algorithm\n");
                textBar.append("Click on FIND_LCS button to BackTrack in the Matrix and get all Longest Common Subsequences\n");
                mainGc.gridx = 0;
                mainGc.gridy = 1;
                mainGc.weighty = 0.1;  // Less space for the Element panel
                mainGc.fill = GridBagConstraints.BOTH;
                Main.add(Element, mainGc);  // Add Element to Main with constraints
            // This is Trigger to Start Animation
            RUN.addMouseListener(new MouseAdapter() {
                // It Contains the Reference to Buttons who's Color needs to be Resetted
                ArrayList<JButton> reset = new ArrayList<>();
                Timer timer = new Timer(1300, null);  // 1.3s delay
                int outerIndex;  // Tracks the outer loop index (i)
                int innerIndex;  // Tracks the inner loop index (j)

                @Override
                public void mouseClicked(MouseEvent evt) {
                    // Ensure timer is reset before each mouse click
                    timer.stop();
                    outerIndex = 0;  // outerloop variable like i
                    innerIndex = 0;  // inner loop variable like j
                    reset.clear();  // Clear the reset array
                    // Action listener that triggers after every Period of 1.3s
                    timer.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Check Termination Condition
                            if (outerIndex < lcsx.size()) {
                                // Check If Coloring is Going on or Done
                                if (innerIndex < lcsx.get(outerIndex).size()) {
                                    // Extract row and col based on the current indices
                                    int row = lcsx.get(outerIndex).get(innerIndex);
                                    int col = lcsy.get(outerIndex).get(innerIndex);
                                    Boolean sel = select.get(outerIndex).get(innerIndex); // Is it Selected
            
                                    // Color the buttons
                                    traceButtons[row][col].setBackground(Color.ORANGE);
                                    reset.add(traceButtons[row][col]);
                                    // If selected Color RED
                                    if(sel){
                                        traceButtons[row][col].setBackground(Color.RED);
                                        traceButtonxy[0][row].setBackground(Color.CYAN);
                                        reset.add(traceButtonxy[0][row]);
                                        traceButtonxy[1][col].setBackground(Color.CYAN);
                                        reset.add(traceButtonxy[1][col]);
                                    }
                                    // Move to the next inner loop index (next button in sequence)
                                    innerIndex++;
                                } else {
                                    // When all buttons in the current sequence are processed, print the LCS
                                    textBar.append("LCS " + (outerIndex + 1) + ". " + lcstr.get(outerIndex) + "\n");
                                    textBar.setCaretPosition(textBar.getDocument().getLength());
                                    // Reset the colors for the current sequence
                                    for (JButton btn : reset) {
                                        btn.setBackground(Color.WHITE);
                                    }
                                    reset.clear();  // Clear the reset list
                                    // Move to the next outer loop index (next LCS sequence)
                                    outerIndex++;
                                    innerIndex = 0;  // Reset inner loop for the next outer loop iteration
                                }
                            } else {
                                // Stop the timer when all LCS sequences are processed
                                timer.stop();
                            }
                        }
                    });
                    timer.setInitialDelay(1000);
                    timer.start();  // Start the timer after the button click
                }
            });           
            this.add(Main, gc);
        }
    }
}

// Backend Class that handle's Algorithm
class LCS_Algo {
    public int[][] trace; // To store Memoized Table
    public String[][] direct; // To store Simultanious Direction for each Step 
    public ArrayList<String> lcsSTR =  new ArrayList<>(); // It Contains the LCS as String, used to Print LCS on Text Area
    public ArrayList<ArrayList<Integer>> lcsX = new ArrayList<>(); // It is Array of Arrays containing x-Indexes that represent path traversed on Table
    public ArrayList<ArrayList<Integer>> lcsY = new ArrayList<>(); // It is Array of Arrays containing y-Indexes that represent path traversed on Table
    public ArrayList<ArrayList<Boolean>> select = new ArrayList<>(); // // It is Array of Arrays containing True/False that represent the cell is Selected or Not
    private int index = 0; // It Represent the Maximum Possible LCS's
    private int lenghtX; // No of terms in Sequence X
    private int lenghtY; // No of terms in Sequence Y
    private String[] X; // String having terms(space separated) of Sequence X
    private String[] Y; // // String having terms(space separated) of Sequence Y
    public LCS_Algo(int lenghtX, int lenghtY, String[] x, String[] y){
        this.X = x;
        this.Y = y;
        this.lenghtX = lenghtX;
        this.lenghtY = lenghtY;
        trace = new int[lenghtX+1][lenghtY+1];
        direct = new String[lenghtX+1][lenghtY+1];
        // initialize with Base Condition
        for( int i=0; i<=lenghtX; i++){
            trace[i][0] = 0;
        }
        for( int j=0; j<=lenghtY; j++){
            trace[0][j] = 0;
        }
        lcsSTR.add("");
        lcsX.add(new ArrayList<Integer>());
        lcsY.add(new ArrayList<Integer>());
        select.add(new ArrayList<Boolean>());
    }
    // It fills the Trace Table
    public void algo(){
        for( int i=1; i<=lenghtX; i++){
            for( int j=1; j<=lenghtY; j++){
                // If Both terms of Sequences are equal Choose Them
                if(X[i-1].equals(Y[j-1])){
                    trace[i][j] = trace[i-1][j-1] + 1;
                    direct[i][j] = " YES";
                }
                // Choose Larger going towards large Value
                else if(trace[i-1][j] >= trace[i][j-1]){ 
                    trace[i][j] = trace[i-1][j];
                    // If Going UP or LEFT we get same Value, Choose two Ways And do Recursion
                    if(trace[i-1][j] == trace[i][j-1])
                        direct[i][j] = " U/L";
                    // Going UP gets Larger value
                    else
                        direct[i][j] = " UP ";
                
                }
                // Going LEFT gets Larger value
                else{
                    trace[i][j] = trace[i][j-1];
                    direct[i][j] = "LEFT";
                }
            }
        }
        System.out.println();
        // Print Trace on Terminal
        printDirect();
    }
    // It is Recursive because when 2 Possible ways it Makes another Instance an call it
    // i,j is position on Table & index represent the index at which we are storing current LCS
    public void traceLCS(int i, int j, int index){
        // Until reached Base Condition
        while(direct[i][j] != null){
            lcsX.get(index).add(i); // Add path traversed in x direction on Table
            lcsY.get(index).add(j); // Add path traversed in y direction on Table
            // If Both terms of Sequences are equal Choose Them 
            if(direct[i][j].equals(" YES")){
                // Add the term of Sequenece to String
                lcsSTR.set(index, lcsSTR.get(index)+X[i-1]);
                // append True(means to be selected) in 2D Select array at index
                select.get(index).add(true);
                // Go in diagonal back direction
                i--;
                j--;
            }
            // If Both terms of Sequences are Unequal
            // Going UP gets large value in Table
            else if(direct[i][j].equals(" UP ")){
                // append False(means not selected) in 2D Select array at index
                select.get(index).add(false);
                // Go UP
                i--;
            }
            // Going LEFT gets large value in Table
            else if(direct[i][j].equals("LEFT")){
                // append False(means not selected) in 2D Select array at index
                select.get(index).add(false);
                // Go LEFT
                j--;
            }
            // Going LEFT or UP gets same value in Table
            else if(direct[i][j].equals(" U/L")){
                // append False(means not selected) in 2D Select array at index
                select.get(index).add(false);
                // Make a copy of Instance
                // This Increase Maximum LCS's Index
                this.index++;
                // Append copy of Current
                lcsSTR.add(lcsSTR.get(index));
                lcsX.add(new ArrayList<>(lcsX.get(index)));
                lcsY.add(new ArrayList<>(lcsY.get(index)));
                select.add(new ArrayList<>(select.get(index)));
                // Call Another Instance to Go in LEFT Direction
                traceLCS(i, j-1, this.index);
                // It goes UP
                i--;
            }
        }
    }
    // To Print Trace Table on Terminal
    public void printTrace(){
        for( int i=0; i<=lenghtX; i++){
            for( int j=0; j<=lenghtY; j++){
                System.out.print(trace[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();      
    }
    // To print All possible LCS's on Terminal
    public void printLCS(){
        for(int tmp=0; tmp<=index; tmp++){
            System.out.println(lcsSTR.get(tmp));
        }    
    }
    // To Print Direction Table on Terminal
    public void printDirect(){
        for( int i=0; i<=lenghtX; i++){
            for( int j=0; j<=lenghtY; j++){
                System.out.print(direct[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
