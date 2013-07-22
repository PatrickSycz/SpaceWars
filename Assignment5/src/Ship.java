import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Ship implements Runnable, Serializable, Comparable<Ship>{

	BufferedImage ship;
	BufferedImage accelerateShip;
	BufferedImage livesImage;
	String name;
	int SCREEN_WIDTH;
	int SCREEN_HEIGHT;
	double x;
	double y;
	int lives;
	int health;
	double rads;
	int score;
	double sin;
	double cos;
	double speed;
	boolean paused;
	double width;
	double height;
	int invisibilityCounter;
	boolean died;
	int focus;
	int damage;
	
	AffineTransform transform;
	
	public Ship()
	{
		SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
		SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
		ResetPosition();
		invisibilityCounter = 25;
		setLives(5);
		setHealth(100);
		loadShip();
		loadLives();
		setFocus(500);
		paused = true;
		setScore(0);
		died = false;
		damage = 8;
		name = "";
	}
	
	public void setX(double _x)
	{
		x = _x;
	}
	public void setY(double _y)
	{
		y = _y;
	}
	public void setLives(int _lives)
	{
		lives = _lives;
	}
	public void setHealth(int _health)
	{
		health = _health;
	}
	public void setDirection(double _d)
	{
		rads = _d;
	}
	public void setScore(int _score)
	{
		score = _score;
	}
	public void ResetPosition()
	{
		rads = 0.0;
		sin = 0.0;
		cos = 0.0;
		speed = 0;
		setX(SCREEN_WIDTH / 2);
		setY(SCREEN_HEIGHT / 2);
	}
	public void setInvisibilityCounter(int c)
	{
		invisibilityCounter = c;
	}
    public void setFocus(int f)
    {
    	focus = f;
    }

    public int getFocus()
    {
    	return focus;
    }
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public int getLives()
	{
		return lives;
	}
	public int getHealth()
	{
		return health;
	}
	public double getDirection()
	{
		return rads;
	}
	public double getSpeed()
	{
		return speed;
	}
	public double getCenterX()
	{
		return (Math.abs(speed*cos) > 0.1) ? (accelerateShip.getWidth()/2.0) : (ship.getWidth()/2.0);
	}
	public double getCenterY()
	{
		return (Math.abs(speed*sin) > 0.1) ? (accelerateShip.getHeight()/2.0) : (ship.getHeight()/2.0);
	}
	public int getScore()
	{
		return score;
	}
	public double getXSpeed()
	{
		return cos*speed;
	}
	public double getYSpeed()
	{
		return sin*speed;
	}
	public int getInvisibilityCounter()
	{
		return invisibilityCounter;
	}
	public void loadShip()
	{
		try{
			ship = ImageIO.read(new File("RocketShipNoFlame/RocketShipNoFlame4.png"));
			accelerateShip = ImageIO.read(new File("RocketShipFlame/RocketshipFlame4.png"));
		}
		catch (IOException e)
		{
			JOptionPane.showConfirmDialog(null, e.getMessage() + " New ship file is missing.");
		}
		transform = new AffineTransform();
		transform.translate(getX(), getY());
		transform.rotate(rads);
		transform.translate(-getCenterX(), -getCenterY());
	}
	public boolean isPlayerDead()
	{
		return died;
	}
	public void resetDeath()
	{
		died = false;
	}
	public void loadLives()
	{
		try{
			livesImage = ImageIO.read(new File("RocketShipNoFlame/RocketShipNoFlame16.png"));
		}
		catch(IOException e)
		{
			e.setStackTrace(null);
		}
	}

    public void DrawShip(Graphics2D g2d)
    {
    	if (speed > 0.1)
			g2d.drawImage(accelerateShip, transform, null);
		else
			g2d.drawImage(ship, transform, null);	
    }
    
    public void DrawHUD(Graphics2D g2d)
    {
    	int x = 0;
    	int y = SCREEN_HEIGHT - 65;
    	for(int i = 0; i < lives; i++)
    	{
    		g2d.drawImage(livesImage, x, y, livesImage.getWidth(),  livesImage.getHeight(),  null);
    		x += 20;
    	}
    	x=150;
    	y+=20;
    	g2d.setColor(Color.cyan);
    	g2d.setFont(new Font(g2d.getFont().getFontName(), Font.BOLD, 20));
    	g2d.drawString("Health: ", x, y);
    	g2d.setColor(Color.yellow);
    	g2d.draw(new Rectangle2D.Double(x, y, 99, 15));
    	g2d.setColor(Color.red);
    	g2d.fill(new Rectangle2D.Double(x, y, health, 15));
    	x+=125; 
    	
    	g2d.setColor(Color.cyan);
    	g2d.drawString("Focus: ", x, y);
    	g2d.setColor(Color.yellow);
    	g2d.draw(new Rectangle2D.Double(x, y, 99, 15));
    	g2d.setColor(Color.green);
    	g2d.fill(new Rectangle2D.Double(x, y, focus/5, 15));
    	x+=125;
    	
    	g2d.setColor(Color.lightGray);
    	g2d.drawString("Score: " + score, x, y);
    	
    }
    
	private void CalculateSpeed()
	{		
		x += speed * cos;
		y += speed * sin;
		if(y > SCREEN_HEIGHT)
			y = 0;
		if (x > SCREEN_WIDTH)
			x = 0;
		if (y < 0)
			y = SCREEN_HEIGHT;
		if (x < 0)
			x = SCREEN_WIDTH;
		transform = new AffineTransform();
		transform.translate(getX(), getY());
		transform.rotate(rads);
		transform.translate(-getCenterX(), -getCenterY());
	}
	public void CalculateDirection()
	{
		sin = Math.sin(rads);
		cos = Math.cos(rads);
	}
	public void IncreaseSpeed()
	{
		if (speed < 5.5)
			speed+= 0.35;
		CalculateDirection();
	}
	public void DecreaseSpeed()
	{
		if (speed > 1.1)
			speed-= 0.35;
		else
			speed = 0.0;
		CalculateDirection();
	}
	public void IncreaseDamage()
	{
		damage += 2;
	}
	public void Pause()
	{
		paused = true;
	}
	public void UnPause()
	{
		paused = false;
	}
	
	public Shape GetHitBox()
	{
		if (speed > 0.1)
			return transform.createTransformedShape(new Rectangle2D.Double(15, 5, accelerateShip.getWidth() * 0.65, accelerateShip.getHeight() * 0.7));
		return transform.createTransformedShape(new Rectangle2D.Double(9, 5, ship.getWidth() * 0.7, ship.getHeight() * 0.7));
	}
	
	public void UpdateStatus()
	{
		if(health < 1)
		{
			died = true;
			lives--;
			if (lives > 0)
				health = 100;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(lives > 0)
		{
			if(!paused)
				CalculateSpeed();
			if(isPlayerDead())
			{
				try{
					Thread.sleep(2000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				resetDeath();
				ResetPosition();
			}
			try {
			Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}

	@Override
	public int compareTo(Ship ship) {
		return (this.score - ship.score);
	}
}// end Ship
