import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {

        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        
        JFrame frame = new JFrame("Pac Man");
        //frame.setVisible(true); 
        frame.setSize(boardWidth, boardHeight); // Set the size of the window
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setResizable(true); // Set the window to be non-resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when the window is closed


        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame); // Add the PacMan panel to the frame
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true); // Make the window visible
}
}
