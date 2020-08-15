import java.text.DecimalFormat;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is the main program. It holds all the information of asteroids,
 * spaceship, lasers and other instances.
 * 
 * @author Ram Reddy and Eric Feng
 * @version 5.26.2020
 */
public class AsteroidGame extends JFrame {

	// keyHeld to check if keys are pressed

	public static boolean keyHeld = false;

	// Height of the window
	public static int windowHeight = 600;

	// Width of the window
	public static int windowWidth = 800;

	// No of Asteroids
	public static int numberOfAsteroids;

	// keyHeldCode to take the codes for the keys from the KeyValues class

	public static int keyHeldCode;

	// Sets coordinates from LaserShoot class

	public static ArrayList<LaserShoot> lasers = new ArrayList<>();

	// store score throughout game

	public static int score = 0;

	// FirstName and lastName of the player.

	public static String firstName, lastName;

	// Label for points.
	public static JLabel pointsLabel;

	// Panels for point and final scores.
	public static JPanel pointsPanel, finalPanel;

	// Final points
	public static JLabel finalPoints;

	// layered pane lpane to have the multiple panes on a JPanel

	public static JLayeredPane lpane;

	// Exit and restart buttons
	public static JButton exit, restart;

	// numberOfLasers to have number of shots fired

	static int numberOfLasers = 0;

	// Start screen variable
	static StartScreen startScreen;

	/**
	 * Main method of asteroid game
	 * 
	 * @param args is passing argument
	 */

	public static void main(String[] args) {
		// creates StartScreen instance
		startScreen = new StartScreen();

	}

	/**
	 * Asteroid Game constructor. The sizes and titles of frame elements and close
	 * operation are set.
	 *
	 * @param numberOfObstacles Gives number of obstacles to be used in the game
	 */

	public AsteroidGame(int numberOfObstacles) {
		// Create Exit and Points panels.

		AsteroidGame.numberOfAsteroids = numberOfObstacles;
		this.setSize(windowWidth, windowHeight);
		this.setTitle("Destroy the Obstacles");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.centerWindow();

		pointsLabel = new JLabel();
		pointsLabel.setFont(new Font("Comic-sans", Font.BOLD, 25));
		pointsLabel.setForeground(Color.RED);

		firstName = startScreen.getFirstName();
		lastName = startScreen.getLastName();

		pointsLabel.setText("Score: " + firstName + " " + lastName + " : " + score);

		finalPoints = new JLabel();
		finalPoints.setFont(new Font("Comic-sans", Font.BOLD, 25));
		finalPoints.setForeground(Color.WHITE);

		finalPanel = new JPanel(new BorderLayout());
		finalPanel.setBackground(Color.BLACK);

		pointsPanel = new JPanel(new BorderLayout());
		pointsPanel.setBackground(Color.BLACK);

		exit = new JButton("Exit Game");
		exit.setBackground(Color.GRAY);
		exit.setForeground(Color.BLACK);
		exit.setFont(new Font("Comic-sans", Font.BOLD, 25));
		restart = new JButton("Restart the Game");
		restart.setBackground(Color.GRAY);
		restart.setForeground(Color.BLACK);
		restart.setFont(new Font("Comic-sans", Font.BOLD, 25));

		// Actionlistener for buttons

		restart.addActionListener(new ActionListener() {

			// e is Key Pressed action

			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == restart) {
					closeWindow();
					AsteroidGame.main(null);
				}
			}
		});

		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == exit) {
					JOptionPane.showMessageDialog(null, "Thank You! Please Play Again!");
					System.exit(0);
				}
			}
		});

		this.addKeyListener(new KeyListener() {
			// Checks for key presses in game
			public void keyTyped(KeyEvent e) {
			}

			/*
			 * Based on the key pressed spaceship direction will be decided.
			 *
			 * @param KeyEvent is key pressed
			 */
			public void keyPressed(KeyEvent e) {
				// keyValues decides the spaceship interaction

				if (e.getKeyCode() == KeyValues.W) {
					SpaceShip.interaction = true;
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				} else if (e.getKeyCode() == KeyValues.S) {
					SpaceShip.interaction = true;
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				} else if (e.getKeyCode() == KeyValues.D) {
					SpaceShip.interaction = true;
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				} else if (e.getKeyCode() == KeyValues.A) {
					SpaceShip.interaction = true;
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				} else if (e.getKeyCode() == KeyValues.E) {
					System.out.println("Ship is Stopping");
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				} else if (e.getKeyCode() == KeyValues.ENTER) {
					SpaceShip.interaction = true;
					lasers.add(new LaserShoot(GameShipPanel.spaceShip.getShipFrontX(),
							GameShipPanel.spaceShip.getShipFrontY(), GameShipPanel.spaceShip.getRotatingAngle()));
					numberOfLasers += 1;
				}
			}

			/**
			 * If the key is released then changes boolean value to false
			 * 
			 * @param keyEvent is released key
			 */
			public void keyReleased(KeyEvent e) {
				keyHeld = false;
			}

		});

		// Create new game panels
		GameShipPanel gamePanel = new GameShipPanel();
		this.add(gamePanel, BorderLayout.CENTER);

		// Create panels for points
		pointsPanel.add(pointsLabel, BorderLayout.WEST);
		this.add(finalPanel, BorderLayout.SOUTH);
		this.add(pointsPanel, BorderLayout.NORTH);

		// Threads so that t repaints the game
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
		executor.scheduleAtFixedRate(new RepaintTheBoard(this), 0L, 20L, TimeUnit.MILLISECONDS);
		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * 
	 * Displays Results of the player.
	 * 
	 * @param numberObstaclesDestroyed Number of Aseteroids destroyed.
	 * @param shipExplosions           Spaceship hit by asteroids.
	 */
	public static void displayResults(int numberObstaclesDestroyed, int shipExplosions) {
		// Displays your accuracy and the number of obstacles destroyed, along with the
		// number of times Spaceship was destroyed by obstacles.
		DecimalFormat twoDecimalPlaces = new DecimalFormat("#.##");
		double accuracyRate = ((double) numberObstaclesDestroyed / (double) AsteroidGame.numberOfLasers) * 100;
		accuracyRate = Double.valueOf(twoDecimalPlaces.format(accuracyRate));
		finalPoints.setText("Number of Ship Explosions: " + shipExplosions + " Number of Obstacles Destroyed: "
				+ numberObstaclesDestroyed + "  Shots Fired: " + AsteroidGame.numberOfLasers + "  Accuracy: "
				+ accuracyRate + "%");
		finalPanel.add(finalPoints, BorderLayout.WEST);

		pointsPanel.add(exit, BorderLayout.EAST);
		pointsPanel.add(AsteroidGame.restart, BorderLayout.CENTER);

		WriteToFile.writeFile(firstName, lastName, accuracyRate);

	}

	/**
	 * Closes window and resets all variables when window exit button is clicked. It
	 * initializes score and number of lasers to 0.
	 */
	private void closeWindow() {

		this.dispose();
		AsteroidGame.score = 0;
		AsteroidGame.numberOfLasers = 0;

		AsteroidObstacle.resetdestroyedObstacles();
		AsteroidObstacle.resetTimesExploded();

	}

	/**
	 * Center the window by getting the screen size from the toolkit API.
	 */
	private void centerWindow() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = this.getSize().width;
		int height = this.getSize().height;
		int x = (dim.width - width) / 2;
		int y = (dim.height - height) / 2;
		this.setLocation(x, y);
	}

}

/**
 * Repaints the screen constantly
 */
class RepaintTheBoard implements Runnable {
	AsteroidGame game;

	/**
	 * This method makes the old board as the new board
	 *
	 * @param game - Repaint the game board
	 */
	public RepaintTheBoard(AsteroidGame game) {
		this.game = game;
	}

	/**
	 * When this method is called it constantly repaints the board
	 */
	public void run() {
		game.repaint();
	}

}

/**
 * GameshipPanel gets the X and Y arrays of the asteroids and interaction of the
 * spaceship with the asteroids.
 */
class GameShipPanel extends JComponent {

	// An ArrayList that holds all current positions of Obstacles

	public ArrayList<AsteroidObstacle> obstacles = new ArrayList<>();

	// polyXValues Sets initial X coordinate points for obstacles

	int[] polyYValues = AsteroidObstacle.getStartingObstacleXArray();

	// polyXValues Sets initial Y coordinate points for obstacles

	int[] polyXValues = AsteroidObstacle.getStartingObstacleYArray();

	static SpaceShip spaceShip = new SpaceShip();
	// Get the games height and width

	int width = AsteroidGame.windowWidth;

	int height = AsteroidGame.windowHeight;

	/**
	 * Assign the random x and y values for asteroids.
	 */

	public GameShipPanel() {
		// Randomly put all the Obstacles

		for (int i = 0; i < AsteroidGame.numberOfAsteroids; i++) {
			int randomStartXPos = (int) (Math.random() * (AsteroidGame.windowWidth - 40) + 1);
			int randomStartYPos = (int) (Math.random() * (AsteroidGame.windowHeight - 40) + 1);
			// Create the obstacle using the constructor and add it to our array
			obstacles.add(new AsteroidObstacle(AsteroidObstacle.getObstacleXArray(randomStartXPos),
					AsteroidObstacle.getObstacleYArray(randomStartYPos), 13, randomStartXPos, randomStartYPos));
			AsteroidObstacle.obstacles = obstacles;
		}
	}

	/**
	 * Creates object g that renders all asteroids, lasers and space ship. Identity
	 * is Makes sure all objects are staying where they are supposed to be at any
	 * given moment.
	 */
	public void paint(Graphics g) {
		Graphics2D graphicSettings = (Graphics2D) g;
		// Makes sure all objects stay in their correct plane and preserves the points
		// of all objects.
		AffineTransform identity = new AffineTransform();
		// Fill the background with black using the height and width of the game board
		graphicSettings.setColor(Color.BLACK);
		graphicSettings.fillRect(0, 0, getWidth(), getHeight());

		// Set the rendering rules to make sure there is less distortion in the movement
		// of the objects
		graphicSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphicSettings.setPaint(Color.WHITE);

		for (AsteroidObstacle obstacle : obstacles) {
			if (obstacle.obstacleOnScreen) {
				obstacle.move(spaceShip, AsteroidGame.lasers);
				graphicSettings.draw(obstacle);
			}
		}
		/**
		 * If key D is pressed then it will increase the rotating angle.
		 * 
		 * If key A is pressed then it will decrease the rotating angle.
		 * 
		 * If key W is pressed then it will get rotating angle and then it will call the
		 * increasing velocity method for both x and y. It will set the ship angle to
		 * the rotating angle times 0.1. This works as a exponential function because
		 * each millisecond this is called it increases.
		 * 
		 * If key S is pressed then it will get rotating angle and then it will call the
		 * decreasing velocity method for both x and y. It will set the ship angle to
		 * the rotating angle times 0.1.
		 * 
		 * This works as a logarithmic function because each millisecond this is called
		 * it decreases.If key E is pressed then it will immediately stop the ship.
		 */
		if (AsteroidGame.keyHeld == true && AsteroidGame.keyHeldCode == KeyValues.D) {
			spaceShip.increaseRotatingAngle();
		} else if (AsteroidGame.keyHeld == true && AsteroidGame.keyHeldCode == KeyValues.A) {
			spaceShip.decreaseRotatingAngle();
		} else if (AsteroidGame.keyHeld == true && AsteroidGame.keyHeldCode == KeyValues.W) {
			spaceShip.setShipAngle(spaceShip.getRotatingAngle());
			spaceShip.increaseXVelocity(spaceShip.shipXAngle(spaceShip.getRotatingAngle()) * 0.1);
			spaceShip.increaseYVelocity(spaceShip.shipYAngle(spaceShip.getRotatingAngle()) * 0.1);
		} else if (AsteroidGame.keyHeld == true && AsteroidGame.keyHeldCode == KeyValues.S) {
			spaceShip.setShipAngle(spaceShip.getRotatingAngle());
			spaceShip.decreaseXVelocity(spaceShip.shipXAngle(spaceShip.getRotatingAngle()) * 0.1);
			spaceShip.decreaseYVelocity(spaceShip.shipYAngle(spaceShip.getRotatingAngle()) * 0.1);
		} else if (AsteroidGame.keyHeld == true && AsteroidGame.keyHeldCode == KeyValues.E) {
			spaceShip.stopShip();
		}

		spaceShip.move();

		// Sets the spawn point of ship
		// Spawns ship back to the spawn point

		graphicSettings.setTransform(identity);
		graphicSettings.translate(spaceShip.getXShip(), spaceShip.getYShip());
		graphicSettings.rotate(Math.toRadians(spaceShip.getRotatingAngle()));

		graphicSettings.draw(spaceShip);

		// Redraws the laser position

		for (LaserShoot laser : AsteroidGame.lasers) {
			laser.move();
			if (laser.laserOnScreen) {
				graphicSettings.setTransform(identity);
				graphicSettings.translate(laser.getlaserX1(), laser.getlaserY1());
				graphicSettings.draw(laser);
			}
		}
	}

}
