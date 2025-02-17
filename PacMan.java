import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener{


    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction = 'U'; // Default direction
        int velocityX = 0;
        int velocityY = 0;

        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls){
                if (collision(this, wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection; // Revert to previous direction
                    updateVelocity(); // Update velocity based on the previous direction
                }
            }
    
        }
        void updateVelocity(){
            if (this.direction  == 'U'){
                this.velocityX = 0;
                this.velocityY = -tileSize/4;
            
            }
            else if (this.direction == 'D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if (this.direction == 'L'){
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }
            else if (this.direction == 'R'){
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }
            void reset() {
                this.x = startX;
                this.y = startY;
            
            }
    }



    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
   
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

//X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();   
    int score = 0;
    int lives = 3 ;
    boolean gameOver = false;

    PacMan(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK); // Set the background color of the panel
        addKeyListener(this);
        setFocusable(true); // Make the panel focusable to receive key events



        //load images
        try {
            wallImage = ImageIO.read(getClass().getResourceAsStream("/resources/wall.png"));
            blueGhostImage = ImageIO.read(getClass().getResourceAsStream("/resources/blueGhost.png"));
            orangeGhostImage = ImageIO.read(getClass().getResourceAsStream("/resources/orangeGhost.png"));
            pinkGhostImage = ImageIO.read(getClass().getResourceAsStream("/resources/pinkGhost.png"));
            redGhostImage = ImageIO.read(getClass().getResourceAsStream("/resources/redGhost.png"));
            pacmanUpImage = ImageIO.read(getClass().getResourceAsStream("/resources/pacmanUp.png"));
            pacmanDownImage = ImageIO.read(getClass().getResourceAsStream("/resources/pacmanDown.png"));
            pacmanLeftImage = ImageIO.read(getClass().getResourceAsStream("/resources/pacmanLeft.png"));
            pacmanRightImage = ImageIO.read(getClass().getResourceAsStream("/resources/pacmanRight.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        loadMap();
        for(Block ghost : ghosts){
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection); // Change ghost's direction randomly
        }
       gameLoop = new Timer(50, this); // Set the delay for the timer (in milliseconds) 20 fps
       gameLoop.start(); // Start the game loop
        
    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        
        for (int r = 0; r < rowCount; r++){
            for (int c = 0; c < columnCount; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if(tileMapChar == 'X'){
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b'){ //blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o'){ //orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p'){ //pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r'){  //red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P'){ //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' '){ //food
                    Block food = new Block(null, x + 4, y + 4, 4, 4);
                    foods.add(food);
                }

         }       
     }
 }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }
    
public void draw(Graphics g){
    g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

    for (Block ghost : ghosts){
        g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
    }

    for (Block wall : walls){
        g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
    }

        g.setColor(Color.WHITE);
    for (Block food : foods){
        g.fillRect(food.x, food.y, food.width, food.height);
    }
    //score
    g.setFont(new Font("Arial", Font.BOLD, 20));
    if (gameOver){
        g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
    }
    else {
        g.drawString("x" + String.valueOf(lives) + " Score:" + String.valueOf(score), tileSize/2, tileSize/2);
    }

}

public void move(){
    pacman.x += pacman.velocityX; // Update pacman's x position
    pacman.y += pacman.velocityY; // Update pacman's y position

    // Check for collision with walls
    for(Block wall : walls){
        if (collision( pacman, wall)){
            pacman.x -= pacman.velocityX;
            pacman.y -= pacman.velocityY;
        }
    }

    for (Block ghost : ghosts){
        if (collision(ghost, pacman)){
            lives -= 1;
            if (lives == 0){
                gameOver = true;
                return;
            }
            resetPositions();
        }
        if (ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D'){
            ghost.updateDirection('U');
        }
        ghost.x += ghost.velocityX; // Update ghost's x position
        ghost.y += ghost.velocityY; // Update ghost's y position

        // Check for collision with walls
        for(Block wall : walls)  {
            if (collision(ghost, wall)|| ghost.x <= 0 || ghost.x + ghost.width >= boardWidth){
                ghost.x -= ghost.velocityX;
                ghost.y -= ghost.velocityY;
                char newDirection = directions[random.nextInt(4)];
                ghost.updateDirection(newDirection); // Change ghost's direction randomly
            }
        }
    }

    // Check for collision with food
    Block foodEaten = null;
    for (Block food : foods){
        if (collision(pacman, food)){
            foodEaten = food; // Store the food that was eaten
            score += 10; // Increase score by 10
            
        }
    }
    foods.remove(foodEaten); // Remove the eaten food from the set

    if (foods.isEmpty()){
        gameOver = true;
        gameLoop.stop(); // Stop the game loop
    }
}
    
public void checkCollisionWithGhosts() {
    for (Block ghost : ghosts) {
        if (collision(pacman, ghost)) {
            lives--;
            if (lives <= 0) {
                gameOver = true;
                gameLoop.stop(); // Stop the game loop
                JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
            } else {
                // Reset pacman's position
                pacman.x = pacman.startX;
                pacman.y = pacman.startY;
            }
        }
    }
}
public boolean collision(Block a, Block b) { // Check for collision between two blocks
    return a.x < b.x +b.width &&
    a.x + a.width > b.x &&
    a.y < b.y + b.height &&
    a.y + a.height > b.y;
}

    public void resetPositions() {
        pacman.reset(); // Reset pacman's position
        for (Block ghost : ghosts) {
            ghost.reset(); // Reset each ghost's position
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);    // Change ghost's direction randomly
        }
    }

@Override
public void actionPerformed(ActionEvent e) { // Update the game state
    move(); // Update pacman's position
    repaint();
    if (gameOver) {
        gameLoop.stop();
    }
}

@Override
public void keyTyped(KeyEvent e) {}

@Override
public void keyPressed(KeyEvent e) {}

@Override
public void keyReleased(KeyEvent e) {
    if(gameOver){
        loadMap();
        resetPositions();
        lives = 3;
        score = 0;
        gameOver = false;
        gameLoop.start(); // Restart the game loop
    }
    System.out.println("KeyEvent: " + e.getKeyCode());
    if (e.getKeyCode() == KeyEvent.VK_UP) {
        pacman.updateDirection('U');
    } 
    else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        pacman.updateDirection('D');
    } 
    else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        pacman.updateDirection('L');
    } 
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        pacman.updateDirection('R');

        }

        if(pacman.direction == 'U'){
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'D'){
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'L'){
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'R'){
            pacman.image = pacmanRightImage;
        }
    }
    }

