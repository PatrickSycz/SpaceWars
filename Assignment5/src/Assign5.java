/***********************************************************************
 * Name:	Patrick Sycz	********************************************
 * Date:	04/08/2013		********************************************
 * Class:	Advanced OO		********************************************
 * Program:	Space game. Demonstrate thread use in a simple space game **
 **********************************************************************/

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Assign5 extends JFrame implements Runnable{
	
	public DrawPanel drawPanel;
	public Ship player;
	public static double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public Boolean [] isKeyDown = {false, false, false, false, false, false}; // up, down, left, right, space, C
	public int spaceCounter = 0;
	public Boolean inMenu;
	public static int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, SPACE = 4, C = 5;
	public Menu StartMenu, PauseMenu;
	public static Boolean gameQuit = false;
	public static Vector<Bullet> bulletList;
	public static Vector<Color> colorList;
	public static Vector<Enemy> enemyList;
	public static Vector<Explosion> explosionList;
	public static int bulletListSize, colorListSize, enemeyListSize, explosionListSize;
	public Enemy e;
	public Bullet b;
	BufferedImage background;
	public int BackgroundX = 0, BackgroundY = 0, BackgroundX2 = 0, BackgroundY2=0;
	boolean checkedHighScores;
	int stage;
	int EnemyAI;
	int EnemyStrength;
	int EnemyDamage;
	String [] monsterType = {"Monster", "AlienShip", "MonsterShip"};
	int numOfEnemies;
	
	public Assign5(GraphicsDevice device)
	{
		// Set up window //
		super("Spaceship");	
		if(device.isFullScreenSupported())
		{
			KeyHandler handler = new KeyHandler();
			this.setUndecorated(true);
			device.setFullScreenWindow(this);
			validate();
			this.setFocusable(true);
			this.requestFocus();
			addKeyListener(handler);
			
		}
		else
		{
			Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
			setPreferredSize(windowSize);
			pack();
		    setLocation(0,0);
		    setResizable(false);
		}
	    // Add a panel to draw on. Initialize to menu //
	    drawPanel = new DrawPanel();
	    add(drawPanel, BorderLayout.CENTER);
		inMenu = true;
		
		// Initialize environment //
		bulletList = new Vector<Bullet>();
		colorList = new Vector<Color>();
		enemyList = new Vector<Enemy>();
		explosionList = new Vector<Explosion>();
		stage = 0;
		EnemyAI = 0;
		EnemyStrength = 5;
		EnemyDamage = 5;
		numOfEnemies = 5;

		try {
			background = ImageIO.read(new File("Background/SpaceWarsBackground.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Initialize start menu and pause menu //
		StartMenu = new Menu(SCREEN_WIDTH, SCREEN_HEIGHT, 3, "StartMenu/SpaceWarsStartup.png", true);
		StartMenu.items[0] = new Rectangle(((int)SCREEN_WIDTH/2)-(int)(SCREEN_WIDTH * 0.075), ((int)SCREEN_HEIGHT/2)-(int)(SCREEN_HEIGHT * 0.1), (int)(SCREEN_WIDTH * 0.14), (int)(SCREEN_HEIGHT * 0.14));
		StartMenu.items[1] = new Rectangle(((int)SCREEN_WIDTH/2)-(int)(SCREEN_WIDTH * 0.155), ((int)SCREEN_HEIGHT/2) +(int)(SCREEN_HEIGHT * 0.08), (int)(SCREEN_WIDTH * 0.32), (int)(SCREEN_HEIGHT * 0.15));
		StartMenu.items[2] = new Rectangle(((int)SCREEN_WIDTH/2)-(int)(SCREEN_WIDTH * 0.075), ((int)SCREEN_HEIGHT/2)+(int)(SCREEN_HEIGHT * 0.26), (int)(SCREEN_WIDTH * 0.14), (int)(SCREEN_HEIGHT * 0.13));
		PauseMenu = new Menu(SCREEN_WIDTH, SCREEN_HEIGHT, 3, "StartMenu/SpaceWarsPause.png", false);
		PauseMenu.items[0] = new Rectangle(((int)SCREEN_WIDTH/2)-(int)(SCREEN_WIDTH * 0.13), ((int)SCREEN_HEIGHT/2)-(int)(SCREEN_HEIGHT * 0.1), (int)(SCREEN_WIDTH * 0.26), (int)(SCREEN_HEIGHT * 0.14));
		PauseMenu.items[1] = new Rectangle(((int)SCREEN_WIDTH/2)-(int)(SCREEN_WIDTH * 0.155), ((int)SCREEN_HEIGHT/2) +(int)(SCREEN_HEIGHT * 0.08), (int)(SCREEN_WIDTH * 0.32), (int)(SCREEN_HEIGHT * 0.15));
		PauseMenu.items[2] = new Rectangle(((int)SCREEN_WIDTH/2)-(int)(SCREEN_WIDTH * 0.065), ((int)SCREEN_HEIGHT/2)+(int)(SCREEN_HEIGHT * 0.26), (int)(SCREEN_WIDTH * 0.13), (int)(SCREEN_HEIGHT * 0.13));
		player = new Ship();
		Thread game = new Thread(this);
	    game.start();
	    new Thread(drawPanel).start();
	    new Thread(new PlayAudio("Theme")).start();
		new Thread(player).start();
		PauseGame();
		checkedHighScores = false;
		setVisible(true);
		//setAlwaysOnTop(true);
	}
	
	@Override
	public void run()
	{
		while(!gameQuit)
		{
			while(!gameQuit && player.lives > 0)
			{
				checkedHighScores = false;
				if(isKeyDown[LEFT])
				{
					player.rads -= Math.PI/64.0;
					player.CalculateDirection();
				}
				if(isKeyDown[RIGHT])
				{
					player.rads += Math.PI/64.0;
					player.CalculateDirection();
				}
				if(isKeyDown[UP])
				{
					player.IncreaseSpeed();
				}
				if(isKeyDown[DOWN])
				{
					player.DecreaseSpeed();
				}
				if(isKeyDown[C])
				{
					if(player.getFocus() > 0)
					{
						player.setFocus(player.getFocus() - 5);
						spaceCounter+=3;
						try {
							Thread.sleep(35);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				else if(player.getFocus() < 500)
				{
					player.setFocus(player.getFocus() + 1);
				}
					
				if(isKeyDown[SPACE])
				{
					if(spaceCounter > 20)
					{
						new Thread(new PlayAudio("")).start();
						b = new Bullet(player.getX(), player.getY(), 10, 10, false, player.damage, 10, player.rads);
						bulletList.add(b);
						colorList.add(Color.yellow);
						new Thread(b).start();
						spaceCounter = 0;
					}
					else
						spaceCounter++;
				}
				else
					spaceCounter = 0;
				
				BackgroundX -= 3 * Math.cos(player.rads) ;
				BackgroundY -= 3 * Math.sin(player.rads);
				if(BackgroundX < -SCREEN_WIDTH)
					BackgroundX = 0;
				if(BackgroundX > 0)
					BackgroundX  = (int)-SCREEN_WIDTH;
				if(BackgroundY < -SCREEN_HEIGHT)
					BackgroundY = 0;
				if(BackgroundY > 0)
					BackgroundY = (int)-SCREEN_HEIGHT;

				try{
					if(enemyList.size() < 1)
					{
						LoadEnemies();
						player.ResetPosition();
						Thread.sleep(200);
						PauseGame();
						Thread.sleep(1000);
					}
					CheckCollisions();
					RemoveDeadBullets();
					RemoveDeadEnemies();
					ShootEnemyBullets();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if (!checkedHighScores)
			{
				CheckHighScore();
				checkedHighScores = true;
				drawPanel.inHighScores = true;
				inMenu = true;
				StartMenu.isActive = false;
				stage = 0;
				numOfEnemies = 5;
				EnemyStrength = 5;
				EnemyDamage = 5;
				synchronized(enemyList){
					for(int i = 0; i < enemyList.size(); i++)
					{
						enemyList.elementAt(i).isAlive = false;
					}}
			}
			
			// send to main menu
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.exit(0);
	}
		
	public void DestroyBullets()
	{
		synchronized(bulletList)
		{
			for(int i = 0; i < bulletList.size(); i++)
			{
				bulletList.elementAt(i).DestroyBullet(true);
			}
		}
	}
	public void LoadEnemies()
	{
		DestroyBullets();
		RemoveDeadBullets();
		player.IncreaseDamage();
		String Monster = "AlienShip";
		int tempStr = EnemyStrength;
		int tempDmg = EnemyDamage;
		if(stage > 2)
		{
			if (numOfEnemies < 25)
				numOfEnemies += 5;
			EnemyStrength += 5;
			EnemyDamage += 5;
		}
		else
		{
			Monster = monsterType[stage];
			EnemyAI = stage;
		}
		
		synchronized(enemyList){
			for(int i = 0; i < numOfEnemies; i++)
			{
				if(stage > 2)
				{	
					tempDmg = EnemyDamage;
					tempStr = EnemyStrength;
					EnemyAI = (int)(Math.random() * 3);
					switch(EnemyAI)
					{
						case 2:
							EnemyStrength = EnemyStrength / 2;
							EnemyDamage = (int) (EnemyDamage * 1.5);
							Monster = Math.random() > 0.5 ? "MonsterShip" : "AlienShip";
							break;
						case 3:
							EnemyStrength = (int)(EnemyStrength * 1.5);
							EnemyDamage = EnemyDamage / 2;
							Monster = Math.random() > 0.5 ? "AlienShip" : "Monster";
						default:
							Monster = monsterType[(int)(Math.random() * 3)];
					}
				}
				enemyList.add(new Enemy(Monster, player.x, player.y, EnemyStrength, EnemyDamage, EnemyAI));
				EnemyStrength = tempStr;
				EnemyDamage = tempDmg;
			}
			for(int i = 0; i < numOfEnemies; i++)
			{
				new Thread(enemyList.elementAt(i)).start();
			}
		}
		stage++;
	}
	
	public void RemoveDeadBullets()
	{
		synchronized(bulletList){
			for(int i = 0; i < bulletList.size(); i++)
			{
				if(bulletList.elementAt(i).destroy)
				{
					bulletList.remove(i);
					colorList.remove(i);
				}
			}
		}
	}

	public void RemoveDeadEnemies()
	{
		synchronized(enemyList){
		for(int i = 0; i < enemyList.size(); i++)
		{
			if(!enemyList.elementAt(i).isAlive)
			{
				Explosion exp = new Explosion(enemyList.elementAt(i).x - enemyList.elementAt(i).GetCenterX(), enemyList.elementAt(i).y- enemyList.elementAt(i).GetCenterY(),
											  enemyList.elementAt(i).width, enemyList.elementAt(i).height);
				explosionList.add(exp);
				new Thread(exp).start();
				enemyList.remove(i);
				new Thread(new PlayAudio("EnemyExplosion")).start();
			}
		}
		}
	}

	public void RemoveDeadExplosions()
	{
		synchronized(explosionList){
		for(int i = 0; i < explosionList.size(); i++)
		{
			if(explosionList.elementAt(i).isDead)
			{
				explosionList.remove(i);
			}
		}
		}
	}
	public void CheckCollisions()
	{
		int points = 0;
		synchronized(bulletList){
		synchronized(enemyList){
			for(int i = 0; i < bulletList.size(); i++)
			{
				if(!bulletList.elementAt(i).shotFromEnemy)
				{	
					for(int j = 0; j < enemyList.size(); j++)
					{
						if(enemyList.elementAt(j).IsAlive())
						{
							if(!(points > 0))
							{
								enemyList.elementAt(j).health -= bulletList.elementAt(i).CheckCollision(enemyList.elementAt(j).GetHitBox());
								points = enemyList.elementAt(j).UpdateEnemy();
								player.score += points;
							}
						}
					}
					points = 0;
				}
				else
				{
					if(player.getInvisibilityCounter() < 0)
						player.setHealth(player.getHealth() - bulletList.elementAt(i).CheckCollision(player.GetHitBox()));
					
					if(player.getHealth() < 1)
					{
						Explosion exp = new Explosion(player.getX() - player.getCenterX(), player.getY() - player.getCenterX(), player.ship.getWidth(), player.ship.getHeight());
						explosionList.add(exp);
						new Thread(exp).start();
						new Thread(new PlayAudio("EnemyExplosion")).start();
						player.setInvisibilityCounter(700);
					}
					player.UpdateStatus();
				}
			}
			for(int i = 0; i < enemyList.size(); i++)
			{
				if (enemyList.elementAt(i).IsAlive())
				{
					if(player.getInvisibilityCounter() < 0)
					{
						player.setHealth(player.getHealth() - enemyList.elementAt(i).CheckPlayerCollision(player.GetHitBox(), 200));
						player.setScore(player.getScore() + enemyList.elementAt(i).UpdateEnemy());
					}
					if(player.getHealth() < 1)
					{
						Explosion exp = new Explosion(player.getX() - player.getCenterX(), player.getY() - player.getCenterX(), player.ship.getWidth(), player.ship.getHeight());
						explosionList.add(exp);
						new Thread(exp).start();
						new Thread(new PlayAudio("EnemyExplosion")).start();
						player.setInvisibilityCounter(700);
						player.UpdateStatus();
					}
				}
			}
			if (player.invisibilityCounter >= 0)
				player.setInvisibilityCounter(player.getInvisibilityCounter() - 1);
			player.UpdateStatus();
		}//end enemyList sync
		}//end bulletList sync
	}

	public void ShootEnemyBullets()
	{
		synchronized(enemyList)
		{
		for(int i = 0; i < enemyList.size(); i++)
		{
			enemyList.elementAt(i).UpdatePlayerInfo(player.x,  player.y, player.rads);
			if(enemyList.elementAt(i).bulletFired)
			{
				new Thread( new PlayAudio("")).start();
				b = new Bullet(enemyList.elementAt(i).x, enemyList.elementAt(i).y, 12, 12, true, enemyList.elementAt(i).damage, 5, enemyList.elementAt(i).getBulletAngle());
				bulletList.add(b);
				colorList.add(Color.cyan);
				new Thread(b).start();
				enemyList.elementAt(i).bulletFired = false;
			}
		}
		}
	}
	
	public synchronized void CheckHighScore()
	{
		File fileName = new File("HighScores.dat");
		HighScores[] highScoresList = new HighScores[21];
		int index = 0;
		ObjectInputStream oistream;
		ObjectOutputStream  oostream;
			try {
				oistream = new ObjectInputStream(new FileInputStream(fileName));
				while(index < 20)
				{
					highScoresList[index] = new HighScores();
					highScoresList[index] = (HighScores)oistream.readObject();
					index++;
				}
				highScoresList[20] = new HighScores(player);
				Arrays.sort(highScoresList);
				oistream.close();
				index = 0;
				oostream = new ObjectOutputStream(new FileOutputStream(fileName));
				while(index < 20)
				{
					if(highScoresList[index] != null)
						oostream.writeObject(highScoresList[index]);
					index++;
				}
				oostream.flush();
				oostream.close();
			} 
			catch (FileNotFoundException e) {
				try {
					oostream = new ObjectOutputStream(new FileOutputStream(fileName));
					for(int i = 0; i < 25; i++)
					{
						oostream.writeObject(new HighScores(player));
					}
					oostream.flush();
					oostream.close();
				}
				catch (Exception ex) {	
					ex.printStackTrace();
				}	
			}
			catch(Exception e)
			{
				e.printStackTrace();
		}
		
	}
	
	public synchronized void DisplayHighScore(Graphics2D g2d)
	{
		float x = ((float)SCREEN_WIDTH/2 - 150), y = 150;
		File fileName = new File("HighScores.dat");
		HighScores ship = new HighScores();
		int index = 0;
		ObjectInputStream oistream = null;
		boolean failed = true;
		g2d.setColor(Color.red);
		g2d.setFont(new Font(g2d.getFont().getFontName(), Font.BOLD, 42));
		g2d.drawString("HIGH SCORES", x, y);
		y+=50;
		g2d.setColor(Color.cyan);
		g2d.setFont(new Font(g2d.getFont().getFontName(), Font.BOLD, 24));
		while(failed)
		{
			try {
				oistream = new ObjectInputStream(new FileInputStream(fileName));
				while(index < 20)
				{
					ship = (HighScores)oistream.readObject();
					g2d.drawString(ship.toString(), x, y);
					y+= 25;
					index++;
				}
				oistream.close();
				failed = false;
			} 
			catch (FileNotFoundException e) {
				failed = false;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void PauseGame()
	{
		player.Pause();
		synchronized(bulletList){
		synchronized(enemyList){
			for(int i = 0; i < bulletList.size(); i++)
			{
				bulletList.elementAt(i).Pause();
			}
			for(int i = 0; i < enemyList.size(); i++)
			{
				enemyList.elementAt(i).Pause();
			}
		}//end enemyList sync
		}// end bulletList sync
	}//end PauseGame()
	
	public void UnPauseGame()
	{
		player.UnPause();
		synchronized(bulletList){
		synchronized(enemyList){
			for(int i = 0; i < bulletList.size(); i++)
			{
				bulletList.elementAt(i).UnPause();
			}
			for(int i = 0; i < enemyList.size(); i++)
			{
				enemyList.elementAt(i).UnPause();
			}
		}// end enemyList sync
		}//end bulletLIst sync
	}
	
	
	public static void main(String Args[])
	{		
		GraphicsEnvironment env = GraphicsEnvironment.
                getLocalGraphicsEnvironment();
            GraphicsDevice device = env.getDefaultScreenDevice();
		Assign5 app = new Assign5(device);
		app.addWindowListener(
				new WindowAdapter(){
					@Override
					public void windowClosing(WindowEvent e)
					{ 
						System.exit(0); }
				});
		//app.run();
	}
	
	private class DrawPanel extends JPanel implements Runnable{
		KeyHandler keyHandler = new KeyHandler();
		boolean getPlayerInfo = false;
		boolean inHighScores = false;
		private DrawPanel()
		{
			addKeyListener(keyHandler);
			setFocusable(true);
			setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
			setBackground(Color.BLACK);
		}

		@Override
		public void run()
		{
			while(!gameQuit)
			{
				repaint();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public synchronized void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
		try{
			if(!inMenu)
			{
				
				g2d.drawImage(background, BackgroundX, BackgroundY, (int)SCREEN_WIDTH * 2, (int)SCREEN_HEIGHT * 2, null);
				// checkout swing constants
				// Draw any bullets //
				synchronized(bulletList){
					for(int i = 0; i < bulletList.size(); i++)
					{	
						bulletList.elementAt(i).DrawBullet(g2d, colorList.elementAt(i));
					}
				}// end bulletList sync
				
				// Draw the enemies //
				synchronized(enemyList){
					for(int i = 0; i < enemyList.size(); i++)
					{
						enemyList.elementAt(i).Draw(g2d);
						//g2d.draw(enemyList.elementAt(i).GetHitBox());
						//enemyList.elementAt(i).direction = player.rads;
					}
				}// end enemyList sync
				
				// Draw the explosions //
				synchronized(explosionList){
				for(int i = 0; i < explosionList.size(); i++)
				{
					explosionList.elementAt(i).Draw(g2d);
				}
				}
				// Draw the ship and the Heads up display //
				if (!player.isPlayerDead())
					player.DrawShip(g2d);

				player.DrawHUD(g2d);	
			} // end environment drawing
			else
			{
				if(StartMenu.isActive)
				{
					StartMenu.DrawMenu(g2d);
				}
				else if(PauseMenu.isActive)
				{
					PauseMenu.DrawMenu(g2d);
				}
				else if(inHighScores)
				{
					DisplayHighScore(g2d);
				}
				else
				{
					g2d.setColor(Color.red);
					g2d.setFont(new Font(g2d.getFont().getFontName(), Font.BOLD, 42));
					g2d.drawString("NAME: ", ((int)SCREEN_WIDTH/2) - 100, (int)SCREEN_HEIGHT/2 - 100);
					g2d.setColor(Color.cyan);
					g2d.drawString(player.name, ((int)SCREEN_WIDTH/2) - 100, (int)SCREEN_HEIGHT/2 - 50);
				}
			} // end menu drawing
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		}
	} // end draw panel
	
	private class KeyHandler implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e) {
			if(inMenu)
			{
				if(e.getKeyCode() == KeyEvent.VK_LEFT)
					//move through boxes left
					return;
				else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
					//move though boxes right
					return;
				else if(e.getKeyCode() == KeyEvent.VK_UP)
				{
					//move through menu up
					if(StartMenu.isActive)
						StartMenu.selected = (--StartMenu.selected%StartMenu.items.length > - 1) ? StartMenu.selected%StartMenu.items.length : 2;
					else if(PauseMenu.isActive)
						PauseMenu.selected = (--PauseMenu.selected%PauseMenu.items.length > - 1) ? PauseMenu.selected%PauseMenu.items.length : 2;
				}
				else if(e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					//move through menu down
					if(StartMenu.isActive)
						StartMenu.selected = ++StartMenu.selected%StartMenu.items.length;
					else if(PauseMenu.isActive)
						PauseMenu.selected = ++PauseMenu.selected%PauseMenu.items.length;
				}
				else if(e.getKeyCode() == KeyEvent.VK_SPACE)
				{
					if(StartMenu.isActive)
					{
						switch(StartMenu.selected)
						{
							case 0:
								player = new Ship();
								new Thread(player).start();
								StartMenu.isActive = false;
								drawPanel.getPlayerInfo = true;
								//UnPauseGame();
								break;
							case 1:
								drawPanel.inHighScores = true;
								StartMenu.isActive = false;
								break;
							case 2:
								if (player != null)
									player.setLives(-1);
								gameQuit = true;
								break;
						}
						
					}
					else if(PauseMenu.isActive)
					{
						switch(PauseMenu.selected)
						{
							case 0:
								inMenu = false;
								PauseMenu.isActive = false;
								UnPauseGame();
								break;
							case 1:
								PauseMenu.isActive = false;
								StartMenu.isActive = true;
								if (player != null)
									player.setLives(-1);
								break;
							case 2:
								if (player != null)
									player.setLives(-1);
								gameQuit = true;
						}
					}
				}
				else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					if(!drawPanel.inHighScores && !drawPanel.getPlayerInfo)
						gameQuit = true;
					else
					{
						drawPanel.inHighScores = false;
						drawPanel.getPlayerInfo = false;
						StartMenu.isActive = true;
					}
				}
			}
			else
			{
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					if(!isKeyDown[LEFT])
						isKeyDown[LEFT] = true;
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					if(!isKeyDown[RIGHT])
						isKeyDown[RIGHT] = true;
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP)
				{
					if(!isKeyDown[UP])
						isKeyDown[UP] = true;
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					if(!isKeyDown[DOWN])
						isKeyDown[DOWN] = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					PauseGame();
					inMenu = true;
					PauseMenu.isActive = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
				{
					if(!isKeyDown[SPACE] && !player.isPlayerDead())
					{
						spaceCounter = 25;
						isKeyDown[SPACE] = true;
					}	
				}
				if(e.getKeyCode() == KeyEvent.VK_C)
				{
					if(!isKeyDown[C])
						isKeyDown[C] = true;
				}
				if(player.paused && !inMenu)
				{
					UnPauseGame();
				}
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP)
				isKeyDown[UP] = false;
			else if(e.getKeyCode() == KeyEvent.VK_DOWN)
				isKeyDown[DOWN] = false;
			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
				isKeyDown[LEFT] = false;
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				isKeyDown[RIGHT] = false;
			else if(e.getKeyChar() == KeyEvent.VK_SPACE)
				isKeyDown[SPACE] = false;
			else if(e.getKeyCode() == (KeyEvent.VK_C))
				isKeyDown[C] = false;
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if(drawPanel.getPlayerInfo)
			{
				if (e.getKeyChar() == KeyEvent.VK_SPACE)
				{
					if (player.name.length() < 1)
						player.name = "";
					else
						player.name += " ";
				}
				else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE)
				{
					if (player.name.length() > 0)
						player.name = player.name.substring(0, player.name.length()-1);
				}
				else if (e.getKeyChar() == KeyEvent.VK_ENTER)
				{
					inMenu = false;
					drawPanel.getPlayerInfo = false;
				}
				else
					player.name += e.getKeyChar();
			}
			else if(drawPanel.inHighScores)
			{
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
				{
					drawPanel.inHighScores = false;
					StartMenu.isActive = true;
				}
			}
		}
		
	}
	
}

