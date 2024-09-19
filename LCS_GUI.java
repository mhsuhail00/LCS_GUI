import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LCS_GUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("LONGEST COMMON SUBSEQUENCE GUI");
        String[] X;// = {"A","B","C","B","D","A","B"};
        String[] Y;// = {"B","D","C","A","B","A"};
        // Inputs X & Y from dialog box 
       while (true) {
            String XSeq = JOptionPane.showInputDialog(frame, "Enter the X Sequence (space-separated): ");
            if (XSeq != null && XSeq.length()!=0) {
                X = XSeq.split(" ");
                break;
            } else {
                JOptionPane.showMessageDialog(frame, "It is a Required Field", "Input X Sequence",JOptionPane.WARNING_MESSAGE);
            }
        }
        while (true) {
            String YSeq = JOptionPane.showInputDialog(frame, "Enter the Y Sequence (space-separated): ");
            if (YSeq != null && YSeq.length()!=0) {
                Y = YSeq.split(" ");
                break;
            } else {
                JOptionPane.showMessageDialog(frame, "It is a Required Field", "Input Y Sequence",JOptionPane.WARNING_MESSAGE);
            }
        }
        LCS_Algo backend = new LCS_Algo(X.length, Y.length, X, Y);
        backend.algo();
        backend.traceLCS(X.length, Y.length, 0);
        BackgroundPanel mainPanel = new BackgroundPanel(X.length, Y.length, X, Y, backend.trace, backend.direct, backend.lcsSTR, backend.lcsX, backend.lcsY, backend.select);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1000, 500));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    static class BackgroundPanel extends JPanel{
        private String map(String str){
            if(str==" UP ")
                return "\u2191";
            else if(str=="LEFT")
                return "\u2190";
            else if(str==" YES")
                return "\u2196";
            else
                return "+";
        }
        BackgroundPanel(int lengthX, int lengthY, String[] x, String[] y, int[][] trace, String[][] direction, String[] lcstr, String[] lcsx, String[] lcsy, String[] select){
            GridBagConstraints gc = new GridBagConstraints();            
            gc.weightx = 1;
            this.setBackground(Color.WHITE);
            this.setLayout(new GridBagLayout());
            JButton RUN = new JButton("FIND LCS");
                RUN.setFont(new Font("Cambria", Font.BOLD, 14)); // Set custom font
                RUN.setFocusPainted(false); // Prevent focus highlight
                gc.gridx = 0;
                gc.gridy = 0;
                gc.weighty = 0.03;
            this.add(RUN, gc);
            JPanel Main = new JPanel();
                Main.setBackground(Color.WHITE);
                gc.gridx = 0;
                gc.gridy = 1;
                gc.weighty = 0.97;
                gc.fill = GridBagConstraints.BOTH;
                Main.setLayout(new GridBagLayout());  // Use GridBagLayout for Main
                GridBagConstraints mainGc = new GridBagConstraints();
            
                JPanel gridContainer = new JPanel();
                    gridContainer.setBackground(Color.WHITE);
                    // Add Trace panel to the Main panel
                    JPanel Trace = new JPanel();
                        Trace.setLayout(new GridLayout(lengthX + 1, lengthY + 1, 2, 2));
                        JButton[][] traceButtons = new JButton[lengthX + 1][lengthY + 1];
                        for (int i = 0; i < (lengthX + 1); i++) {
                            for (int j = 0; j < (lengthY + 1); j++) {
                                traceButtons[i][j] = new JButton(String.valueOf(trace[i][j]));
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
                
                    JPanel Direct = new JPanel();
                        Direct.setLayout(new GridLayout(lengthX + 1, lengthY + 1, 2, 2));
                        JButton[][] directButtons = new JButton[lengthX + 1][lengthY + 1];
                        for (int i = 0; i < (lengthX + 1); i++) {
                            for (int j = 0; j < (lengthY + 1); j++) {
                                if(i==0 || j==0)
                                    directButtons[i][j] = new JButton("X");
                                else
                                    directButtons[i][j] = new JButton(map(direction[i][j]));
                                directButtons[i][j].setFocusPainted(false); // No focus effect when clicked
                                directButtons[i][j].setContentAreaFilled(false); // No click area background
                                directButtons[i][j].setBackground(Color.WHITE);
                                directButtons[i][j].setForeground(Color.BLACK);
                                directButtons[i][j].setOpaque(true);
                                Direct.add(directButtons[i][j]);
                            }
                        }
                        
                        // Set constraints for Trace
                        mainGc.gridx = 1;
                        mainGc.gridy = 0;
                        mainGc.weightx = 0.5;
                        mainGc.weighty = 0.8;  // Most of the space should be taken by Trace
                        gridContainer.add(Direct, mainGc);  // Add Trace to Main with constraints
                    mainGc.gridx = 0;
                    mainGc.gridy = 0;
                    mainGc.weightx = 1;
                    mainGc.weighty = 0.8;
                    Main.add(gridContainer, mainGc);
                // Add Element panel to the Main panel
                JPanel Element = new JPanel();
                    Element.setLayout(new GridLayout(2, 1));
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
                textBar.append("Table Shows Memoized Values of Longest Commom Subsequence Algorithm\n");
                textBar.append("Now We will Retrace Back in the Matrix Shown that conatins Memoized Directions\n");
                mainGc.gridx = 0;
                mainGc.gridy = 1;
                mainGc.weighty = 0.1;  // Less space for the Element panel
                mainGc.fill = GridBagConstraints.BOTH;
                Main.add(Element, mainGc);  // Add Element to Main with constraints
            RUN.addMouseListener(new MouseAdapter() {
                ArrayList<JButton> reset = new ArrayList<>();
                Timer timer = new Timer(1300, null);  // 500ms delay
                final int[] outerIndex = {0};  // Tracks the outer loop index (i)
                final int[] innerIndex = {0};  // Tracks the inner loop index (j)

                @Override
                public void mouseClicked(MouseEvent evt) {
                    // Ensure timer is reset before each mouse click
                    timer.stop();
                    outerIndex[0] = 0;  // outerloop variable like i
                    innerIndex[0] = 0;  // inner loop variable like j
                    reset.clear();  // Clear the reset array

                    timer.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (outerIndex[0] < lcsx.length && lcsx[outerIndex[0]] != null) {
                                if (innerIndex[0] < lcsx[outerIndex[0]].length()) {
                                    // Extract row and col based on the current indices
                                    int row = Character.getNumericValue(lcsx[outerIndex[0]].charAt(innerIndex[0]));
                                    int col = Character.getNumericValue(lcsy[outerIndex[0]].charAt(innerIndex[0]));
                                    int sel = Character.getNumericValue(select[outerIndex[0]].charAt(innerIndex[0]));
            
                                    // Color the buttons
                                    directButtons[row][col].setBackground(Color.ORANGE);
                                    reset.add(directButtons[row][col]);
                                    if(sel == 1){
                                        traceButtons[row][col].setBackground(Color.CYAN);
                                        reset.add(traceButtons[row][col]);
                                        traceButtonxy[0][row].setBackground(Color.RED);
                                        reset.add(traceButtonxy[0][row]);
                                        traceButtonxy[1][col].setBackground(Color.RED);
                                        reset.add(traceButtonxy[1][col]);
                                    }
                                    // Move to the next inner loop index (next button in sequence)
                                    innerIndex[0]++;
                                } else {
                                    // When all buttons in the current sequence are processed, print the LCS
                                    textBar.append("LCS " + (outerIndex[0] + 1) + ". " + lcstr[outerIndex[0]] + "\n");
                                    textBar.setCaretPosition(textBar.getDocument().getLength());
                                    // Reset the colors for the current sequence
                                    for (JButton btn : reset) {
                                        btn.setBackground(Color.WHITE);
                                    }
                                    reset.clear();  // Clear the reset list
                                    // Move to the next outer loop index (next LCS sequence)
                                    outerIndex[0]++;
                                    innerIndex[0] = 0;  // Reset inner loop for the next outer loop iteration
                                }
                            } else {
                                // Stop the timer when all LCS sequences are processed
                                timer.stop();
                            }
                        }
                    });
                    timer.setInitialDelay(1000);  // No delay before the first run
                    timer.start();  // Start the timer after the button click
                }
            });           
            this.add(Main, gc);
        }
    }
}
class LCS_Algo {
    public int[][] trace;
    public String[][] direct;
    public String[] lcsSTR = new String[20];
    public String[] lcsX = new String[20];
    public String[] lcsY = new String[20];
    public String[] select = new String[20];
    private int index = 0;
    private int lenghtX;
    private int lenghtY;
    private String[] X;
    private String[] Y;
    public LCS_Algo(int lenghtX, int lenghtY, String[] x, String[] y){
        this.X = x;
        this.Y = y;
        this.lenghtX = lenghtX;
        this.lenghtY = lenghtY;
        trace = new int[lenghtX+1][lenghtY+1];
        direct = new String[lenghtX+1][lenghtY+1];
        // initialize Base Condition
        for( int i=0; i<=lenghtX; i++){
            trace[i][0] = 0;
        }
        for( int j=0; j<=lenghtY; j++){
            trace[0][j] = 0;
        }
        lcsSTR[0] = "";
        lcsX[0] = "";
        lcsY[0] = "";
        select[0] = "";
    }
    public void algo(){
        for( int i=1; i<=lenghtX; i++){
            for( int j=1; j<=lenghtY; j++){
                if(X[i-1].equals(Y[j-1])){
                    trace[i][j] = trace[i-1][j-1] + 1;
                    direct[i][j] = " YES";
                }
                else if(trace[i-1][j] >= trace[i][j-1]){ // if equal then go up
                    trace[i][j] = trace[i-1][j];
                    if(trace[i-1][j] == trace[i][j-1])
                        direct[i][j] = " U/L";
                    else
                        direct[i][j] = " UP ";
                
                }
                else{
                    trace[i][j] = trace[i][j-1];
                    direct[i][j] = "LEFT";
                }
            }
        }
        System.out.println();
        printDirect();
    }
    public void traceLCS(int i, int j, int index){
        while(direct[i][j] != null){
            if(direct[i][j].equals(" YES")){
                lcsSTR[index] = lcsSTR[index] + X[i-1];
                lcsX[index] = lcsX[index] + i;
                lcsY[index] = lcsY[index] + j;
                select[index] = select[index] + 1;
                i--;
                j--;
            }
            else if(direct[i][j].equals(" UP ")){
                lcsX[index] = lcsX[index] + i;
                lcsY[index] = lcsY[index] + j;
                select[index] = select[index] + 0;
                i--;
            }
            else if(direct[i][j].equals("LEFT")){
                lcsX[index] = lcsX[index] + i;
                lcsY[index] = lcsY[index] + j;
                select[index] = select[index] + 0;
                j--;
            }
            else if(direct[i][j].equals(" U/L")){
                lcsX[index] = lcsX[index] + i;
                lcsY[index] = lcsY[index] + j;
                select[index] = select[index] + 0;
                this.index++;
                lcsSTR[this.index] = lcsSTR[index];
                lcsX[this.index] = lcsX[index];
                lcsY[this.index] = lcsY[index];
                select[this.index] = select[index];
                traceLCS(i, j-1, this.index);
                i--;
            }
        }
    }
    public void printTrace(){
        for( int i=0; i<=lenghtX; i++){
            for( int j=0; j<=lenghtY; j++){
                System.out.print(trace[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();      
    }
    public void printLCS(){
        for(int tmp=0; tmp<=index; tmp++){
            System.out.print(new StringBuilder(lcsSTR[tmp]).reverse() + " ");
            System.out.print(new StringBuilder(lcsX[tmp]).reverse() + " ");
            System.out.println(new StringBuilder(lcsY[tmp]).reverse());
        }
        System.out.println();      
    }
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
