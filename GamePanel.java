import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener{
	
	JButton buttonStart;
	JButton buttonMenu;
	JButton buttonQuit;
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 150;
	final int x[] = new int [GAME_UNITS]; 
	final int y[] = new int [GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	Boolean running = false;
	Timer timer;
	Random random;
	boolean inmenu = true;
	BufferedImage head = null;
	BufferedImage head90 = null;
	BufferedImage head180 = null;
	BufferedImage head270 = null;
	BufferedImage body = null;
	boolean menu = false;
	boolean resetOK = false;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setFocusable(true);
		this.setBackground(Color.WHITE);
		try {
			head = ImageIO.read(new File("c:\\Images\\head.png"));
			head90 = ImageIO.read(new File("c:\\Images\\head90.png"));
			head180 = ImageIO.read(new File("c:\\Images\\head180.png"));
			head270 = ImageIO.read(new File("c:\\Images\\head270.png"));
			body = ImageIO.read(new File("c:\\Images\\body.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startGame() {
		newApple();
		this.requestFocus();
		this.addKeyListener(new Listener());
		timer = new Timer(DELAY, this);
		timer.start();
		
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		reset(g);
	}
	
	public void reset(Graphics g) {
		if (resetOK) {
			g.dispose();
			resetOK = false;
		}
	}
	public void draw(Graphics g) {
		if (inmenu == true && running == false) {
			g.setColor(Color.black);
			g.setFont(new Font("Ink Free", Font.BOLD, 50));
			g.drawString("Welcome to Snake!", GamePanel.SCREEN_WIDTH/2 - 200, 100);
			
			buttonStart = new JButton();
			buttonStart.setBounds(GamePanel.SCREEN_WIDTH/2 - 75, 175, 150, 75);
			buttonStart.setText("Start");
			buttonStart.addActionListener(new Listener()); 
			this.add(buttonStart);
			
			
			buttonQuit = new JButton();
			buttonQuit.setBounds(GamePanel.SCREEN_WIDTH/2 - 75, 325, 150, 75);
			buttonQuit.addActionListener(new Listener());
			buttonQuit.setText("Quit");
			this.add(buttonQuit);
		}
		
		if (running == true && inmenu == false) {
			/*for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

			}*/
			this.remove(buttonStart);
			this.remove(buttonQuit);
			g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for (int i = 0; i < bodyParts; ++i) {
				if (i == 0) {
					switch (direction) {
					case 'U':
						g.drawImage(head, x[i], y[i], null);
						break;
					case 'D':
						g.drawImage(head180, x[i], y[i], null);
						break;
					case 'R':
						g.drawImage(head90, x[i], y[i], null);
						break;
					case 'L':
						g.drawImage(head270, x[i], y[i], null);
						break;
					
					}
					
					//g.drawImage(head, x[i], y[i], null);
					//g.setColor(Color.green);
					//g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.drawImage(body, x[i], y[i], null);
					//g.setColor(new Color(45, 180, 0));
					//g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				g.setColor(Color.black);
				g.setFont(new Font("Ink Free", Font.BOLD, 25));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, 
						50);
			}
		}
		else if (running == false && inmenu == false){
			gameOver(g);
		}
	}
	
	public void newApple() {
		boolean ok = false;
		boolean checker = true;
		while (!ok) {
			appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE);
			appleX *= UNIT_SIZE;
			appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE);
			appleY *= UNIT_SIZE;
			for (int i = 0; i <bodyParts; ++i) {
				if (appleX == x[i] && appleY == y[i]) {
					checker = false; 
					break;
				}
			}
			if (checker == true) {
				ok = true;
			}
		}
		
	}
	
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if (x[0] == appleX && y[0] == appleY) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
		
	}
	public void checkCollisions() {
		for (int i = bodyParts; i >0; --i) {
			if (x[0] == x[i] && y[0] == y[i]) {
				running = false;
			}
			if (x[0] < 0 || x[0] > SCREEN_WIDTH) {
				running = false;
			}
			if (y[0] < 0 || y[0] > SCREEN_HEIGHT) {
				running = false;
			}
			if (!running && !inmenu) {
				timer.stop();
			}
		}
	}
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, 
				SCREEN_HEIGHT/2);
		
		buttonQuit = new JButton();
		buttonQuit.setBounds(GamePanel.SCREEN_WIDTH/2 - 75, 325, 150, 75);
		buttonQuit.addActionListener(new Listener());
		buttonQuit.setText("Quit");
		this.add(buttonQuit);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (running == true && inmenu == false) {
			move();
			checkApple();
			checkCollisions();
			
		}
		repaint();
		
	}
	public class Listener implements ActionListener, KeyListener {
		@Override 
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
					break;
				}
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
					break;
				}
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
					break;
				}
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
					break;
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}


		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==buttonStart) {
				inmenu = false;
				running = true;
				buttonStart.repaint();
				resetOK = true;
				timer = new Timer(DELAY, this);
				timer.start();
				startGame();
			}
			if (e.getSource() == buttonQuit){
				System.exit(0);
			}
			if (e.getSource() == buttonMenu) {
				inmenu = true;
				running = false;
				bodyParts = 6;
				x[0] =0;
				y[0] =0;
				resetOK = true;
				buttonMenu.remove(buttonMenu);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
