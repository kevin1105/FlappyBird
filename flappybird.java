import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

public class flappybird implements ActionListener, MouseListener, KeyListener {

	public static flappybird flappyBird;
	
	public final int WIDTH = 800, HEIGHT = 800;
	
	public Renderer renderer;
	
	public Rectangle bird;
	
	public int ticks, yMotion;

	public static int score;
	
	public ArrayList<Rectangle> columns;
	
	public Random random;
	
	public boolean gameOver, started;
	
	public Player player;
	
	public String playerName;
	
	public flappybird() {
		
		JFrame jFrame = new JFrame();
		javax.swing.Timer timer = new javax.swing.Timer(20, this);
		
		renderer =  new Renderer();
		random = new Random();
		
		jFrame.add(renderer);
		jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
		jFrame.setSize(WIDTH, HEIGHT);
		jFrame.addMouseListener(this);
		jFrame.addKeyListener(this);
		jFrame.setResizable(false);
		jFrame.setVisible(true);
		
		
		bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		
		timer.start();
		
	}
	
	public void addColumn(boolean start) {
		
		int space = 300;
		int width = 100;
		int height = 50 + random.nextInt(300);
		
		if (start) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}
		else {
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
		
	}
	
	public void paintColumn(Graphics g, Rectangle column) {
		
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width,column.height);
	}
	
	public void jump() {
		if (gameOver) {
			bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;
		}
		
		if (!started) {
			started = true;
		}
		
		else if (!gameOver) {
			if (yMotion > 0) {
				yMotion = 0;
			}
			yMotion -= 10;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		int speed = 10;
		
		ticks++;
		
		if (started && !gameOver) {
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= speed;
			}
			
			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}
			
			for (int i = 0; i < columns.size(); i++) {
				
				Rectangle column = columns.get(i);
				
				if (column.x + column.width < 0) {
					columns.remove(column);
					
					if (column.y == 0) {
						addColumn(false);
					}
				}
			}
			
			bird.y += yMotion;
			
			for (Rectangle column : columns) {
				
				if (column.y == 00 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
					score++;
				}
				
				if (column.intersects(bird)) {
					gameOver = true;
					
					if (bird.x <= column.x) {
						bird.x = column.x - bird.width;
					}
					else {
						if (column.y  != 0) {
							bird.y = column.y - bird.height;
						}
						else if (bird.y < column.height) {
							bird.y = column.height;
						}
					}
				}
			}
			
			// if bird lands on the ground or top
			if (bird.y > HEIGHT - 120 || bird.y < 0) {
				gameOver = true;
			}
			
			if (bird.y + yMotion >= HEIGHT - 120) {
				bird.y = HEIGHT - 120 - bird.height;
			}
		}
		
		// Record Final Score for Player
		if (gameOver && started) {
			player = new Player(score);
			player.getScore();
		}
		
		
		renderer.repaint();
		
	}
	
	public void repaint(Graphics g) {
		
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 150, WIDTH, 150);

		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 150, WIDTH, 20);

		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);

		for (Rectangle column : columns) {
			paintColumn(g, column);
		}

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));
		
		
		if (!started) {
			g.drawString("Click to Start!", 75, HEIGHT/2 - 50);
			g.setColor(Color.black);
			g.drawRect(75, 75, 50, 50);
			
		}
		
		
		if (!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
		
		// If Game ends
		if (gameOver) {
			g.drawString("Game Over!", 100, HEIGHT/2 - 50);
			
			// view score at the end
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Final Score = ", WIDTH/2 - 250, HEIGHT/4);
			g.drawString(String.valueOf(score), WIDTH / 2 + 80, HEIGHT/4);
			
			// player info and score
			Player player2 = new Player(playerName);
			g.drawRect(10, 10, 10, 50);
			
			System.out.println(score);
			
		}
		
		
	}
	
	public static void main(String[] args) {
		
		flappyBird = new flappybird();
		Player player = new Player(score);
		player.setScore(score);
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
