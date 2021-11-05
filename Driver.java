package a9;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Driver extends JPanel implements ActionListener, MouseListener{

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Random rand = new Random();
	private boolean actorType;
	private ArrayList<Actor> actors; // Plants and zombies all go in here
	BufferedImage frogImage1; // Maybe these images should be in those classes, but easy to change here.
	BufferedImage frogImage2;
	BufferedImage bearImage1;
	BufferedImage bearImage2_1;
	BufferedImage bearImage2_2;
	BufferedImage coin;
	BufferedImage frogIcon;
	BufferedImage roboFrogIcon;
	int numRows;
	int numCols;
	int cellSize;
	int enemySpawnrate = 150;
	int strong_Enemy_Spawnrate = 0;
	int coinCount = 0;
	int spawniterate = 0;
	private JButton baseFrog_button;
	private JButton roboFrog_button;
	private JLabel coins;
	static JPanel mainPanel;


	public Driver() {
		super();

		numRows = 10;
		numCols = 15;
		cellSize = 50;
		setPreferredSize(new Dimension(100 + numCols * cellSize, 100 + numRows * cellSize));
		this.addMouseListener(this);

		actors = new ArrayList<>();

		try {
			frogImage1 = ImageIO.read(new File("src/a9/frog5050.png"));
			frogImage2 = ImageIO.read(new File("src/a9/robo-frog-off.png"));
			bearImage1 = ImageIO.read(new File("src/a9/Base_bear.png"));
			bearImage2_1 = ImageIO.read(new File("src/a9/Beardolf-grey.png"));
			bearImage2_2 = ImageIO.read(new File("src/a9/Beardolf-white.png"));
			coin = ImageIO.read(new File("src/a9/coin.png"));
			frogIcon = ImageIO.read(new File("src/a9/frog_icon.png"));
			roboFrogIcon = ImageIO.read(new File("src/a9/robotoken.png"));
		} catch (Exception e) {
			System.out.println("A file was not found");
			System.exit(0);
		}
		
		roboFrog_button = new JButton();
		baseFrog_button = new JButton();
		coins = new JLabel();
		roboFrog_button.setText("400 coins");
		baseFrog_button.setText("200 coins");
		
		
		baseFrog_button.setIcon(new ImageIcon(frogIcon));
		roboFrog_button.setIcon(new ImageIcon(roboFrogIcon));
		baseFrog_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actorType = true;	
			}
		});
		
		roboFrog_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actorType = false;
			}
		});
		mainPanel = new JPanel();
		mainPanel.add(baseFrog_button);
		mainPanel.add(roboFrog_button);
		mainPanel.add(coins);
		timer = new Timer(20, this);
		timer.start();

	}
	
	public void spawnEnemy() {
		if (rand.nextInt(100) < strong_Enemy_Spawnrate)
			actors.add(new Bear_beardolf_grey(new Point2D.Double(numCols * 50, (rand.nextInt(numRows) * 50) + 100),
					new Point2D.Double(bearImage2_1.getWidth(), bearImage2_1.getHeight()-5), bearImage2_1, 100, 1, -3, 5));	// I don't know why the speed needs to be negative for them to go the right direction and I don't feel like finding out why.
																											  
		else
			actors.add(new Bear_base(new Point2D.Double(numCols * 50, (rand.nextInt(numRows) * 50) + 100),
					new Point2D.Double(bearImage1.getWidth(), bearImage1.getHeight()-5), bearImage1, 100, 1, -3, 5));
	}
	
	
	
	
	public static JPanel getMainPanel() {
		return mainPanel;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (Actor actor : actors) {
			actor.draw(g, 0);
			actor.drawHealthBar(g);
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		for (Actor actor : actors) {
			if (actor.getX() <=0) {
//				System.exit(0);
			}
			actor.update();
		}

		// Try to attack
		for (Actor actor : actors) {
			for (Actor other : actors) {
				actor.attack(other);
			}
		}

		double xPos;
		double yPos;

		// Remove plants and zombies with low health
		int iterate = 0;
		ArrayList<Actor> nextTurnActors = new ArrayList<>();
		iterate++;
		for (Actor actor : actors) {
			if (actor.isAlive())
				nextTurnActors.add(actor);
			else if (actor instanceof Bear_beardolf_grey && (!actor.isAlive())) { // checks if the dead actor is
																					// beardolf the grey. if it is the
																					// bear is reincarnated as beardolf
																					// the white
				xPos = actor.getPosition().getX();
				yPos = actor.getPosition().getY();
				nextTurnActors.add(iterate, new Bear_beardolf_white(new Point2D.Double(xPos, yPos),
						new Point2D.Double(bearImage2_2.getWidth(), bearImage2_2.getHeight()), bearImage2_2, 200, 1, -3,
						10));
				coinCount += 100;
			} else {
				actor.removeAction(actors); // any special effect or whatever on removal
				coinCount += 50;
			}
		}
		actors = nextTurnActors;

		// Check for collisions between zombies and plants and set collision status
		for (Actor actor : actors) {
			for (Actor other : actors) {
				actor.setCollisionStatus(other);
			}
		}
		spawniterate++;
		coinCount += 2;
		coins.setText("Coins: " + String.valueOf(coinCount));
		
		if (spawniterate == enemySpawnrate) {
			spawniterate = 0;
			strong_Enemy_Spawnrate += 2;
			spawnEnemy();
			enemySpawnrate -=2;
			if (enemySpawnrate < 100) {
				enemySpawnrate = 100;
			}
			if (strong_Enemy_Spawnrate > 30) {
				strong_Enemy_Spawnrate = 30;
			}
		}

		// Move the actors.
		for (Actor actor : actors) {
			actor.move(); // for Zombie, only moves if not colliding.
		}

		
		// Redraw the new scene
		repaint();
		

	}


	@Override
	public void mouseClicked(MouseEvent e) {
		double xPos = e.getX();
		double yPos = e.getY();
		
		xPos = Math.round(xPos / 50) * 50;
		yPos = Math.round(yPos / 50) * 50;
		
		
		Actor frogtype;
		if (actorType && coinCount >= 300) {
			frogtype = (new Frog_base(new Point2D.Double(xPos, yPos),new Point2D.Double(frogImage1.getWidth(), frogImage1.getHeight() -5) , frogImage1, 100, 1, 10));
			addFrog(xPos, yPos, frogtype);
		}
		else if (!actorType && coinCount >= 600) {
			frogtype = (new Frog_robot(new Point2D.Double(xPos, yPos),new Point2D.Double(frogImage2.getWidth(), frogImage2.getHeight() -5), frogImage2, 200, 1, 10));
			addFrog(xPos, yPos, frogtype);
		}
		
		
		
	}
	
	public void addFrog(double xPos, double yPos, Actor frog) {
		int count = 0;
		for (int i = 0; i < actors.size(); i++) {	
			if (actors.get(i).getX() != xPos || actors.get(i).getY() != yPos) {
				count++;
			}
		}
		if (count == actors.size())
			actors.add(frog);
			coinCount -= 300;
			if (frog instanceof Frog_robot)
				coinCount -=300;

	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame app = new JFrame("Frogs and Bears");
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Driver panel = new Driver();
				app.setResizable(false);
				app.setContentPane(panel);
				app.add(getMainPanel());
				app.pack();
				app.setVisible(true);
			}
		});
	}

}
