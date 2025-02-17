This is the base model of the code. It will run as is. There may be a problem with the codes ability to load the images, I am currently working on a solution to this by possibly manipulating 
the classpath to better suit.


To be added:
Ghost/PacMan teleportation from left hallway and right hallway.
Superpowered pacman mode where the ghost enter a frightened state and are edible to pacman for bonus points. Addition of cherries.

Current isues:
png imports
"Exception in thread "main" java.lang.IllegalArgumentException: input == null!
        at java.desktop/javax.imageio.ImageIO.read(ImageIO.java:1356)
        at PacMan.<init>(PacMan.java:146)
        at App.main(App.java:21)"
