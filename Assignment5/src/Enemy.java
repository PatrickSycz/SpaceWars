import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;


public class Enemy implements Runnable {
	
	BufferedImage enemy[];
	int health;
	int damage;
	int imgIndex;
	boolean backwards;
	boolean isAlive;
	boolean paused;
	double x;
	double y;
	double width;
	double height;
	int speed;
	double direction;
	int points;
	int AIType;
	int step;
	double playerx, playery;
	public static final int CONFUSED = 0, MIMIC = 1, PHYSICAL_ATTACKER = 3, DISTANCE_ATTACKER = 2;
	boolean bulletFired;
	int bulletSpeed;
	boolean followPlayer;
	
	AffineTransform transform;
	
	public Enemy(String fileName)
	{
		step = 100;
		enemy = new BufferedImage[6];
		for(int i = 0; i < 6; i++)
		{
			try {
				enemy[i] = ImageIO.read(new File("Monsters/"+ fileName + i + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		width = enemy[0].getWidth();
		height = enemy[0].getHeight();
		imgIndex = 0;
		health = 10;
		points = 100;
		backwards = false;
		isAlive = true;
		x =((int)((2 * Math.random()) % 2) == 1) ? 
				Math.random() * (Assign5.SCREEN_WIDTH * 0.30) : 
				(Assign5.SCREEN_WIDTH * 0.7) + (Math.random() * (Assign5.SCREEN_WIDTH * 0.30));

		y = ((int)((2 * Math.random()) % 2) == 1) ?  
			Math.random() * (Assign5.SCREEN_HEIGHT * 0.30) : 
			(Assign5.SCREEN_HEIGHT * 0.7) + (Math.random() * (Assign5.SCREEN_HEIGHT * 0.3));
		speed = 5 + (int) (Math.random() * 15.0);
		paused = false;
		damage = 20;
		AIType = CONFUSED;
	}
	
	public Enemy(String fileName, double _playerx, double _playery, int _health, int _strength, int _AI)
	{
		step = 100;
		enemy = new BufferedImage[6];
		for(int i = 0; i < 6; i++)
		{
			try {
				enemy[i] = ImageIO.read(new File("Monsters/"+ fileName + i + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		width = enemy[0].getWidth();
		height = enemy[0].getHeight();
		imgIndex = 0;
		health = _health;
		points = 100;
		backwards = false;
		isAlive = true;
		AIType = _AI;
		damage = _strength;		
		x =((int)((2 * Math.random()) % 2) == 1) ? 
				Math.random() * (Assign5.SCREEN_WIDTH * 0.30) : 
				(Assign5.SCREEN_WIDTH * 0.7) + (Math.random() * (Assign5.SCREEN_WIDTH * 0.30));

		y = ((int)((2 * Math.random()) % 2) == 1) ?  
			Math.random() * (Assign5.SCREEN_HEIGHT * 0.30) : 
			(Assign5.SCREEN_HEIGHT * 0.7) + (Math.random() * (Assign5.SCREEN_HEIGHT * 0.3));		
		playerx = _playerx;
		playery = _playery;
		paused = false;
		if(AIType == DISTANCE_ATTACKER)
		{
			speed = 3 + (int)(Math.random() * 5);
			followPlayer = (Math.random() > 0.5) ? true : false;
		}
		else
			speed = 5 + (int) (Math.random() * 12.0);
		bulletFired = false;
		bulletSpeed = 10;
	}
	
	
	public void Draw(Graphics2D g2d)
	{
		if(imgIndex < 6 && imgIndex > -1)
		g2d.drawImage(enemy[imgIndex], transform, null);	
	}

	public void Pause()
	{
		paused = true;
	}
	public void UnPause()
	{
		paused = false;
	}
	
	public double GetCenterX()
	{
		return enemy[0].getWidth()/2.0;
	}
	public double GetCenterY()
	{
		return enemy[0].getHeight()/2.0;
	}
	public Shape GetHitBox()
	{
		if(transform != null)
			return transform.createTransformedShape(new Rectangle2D.Double(0, 0, enemy[0].getWidth(), enemy[0].getHeight()));
		return new Rectangle2D.Double(0,0,-100,-100);
	}
	public int getBulletDamage()
	{
		return damage;
	}
	public double getBulletDirection()
	{
		return 2.0;
	}
	public int getBulletSpeed()
	{
		return bulletSpeed;
	}
	public double getBulletAngle()
	{
		double a, b, c;
		double offset = (Math.random() > 0.5) ? Math.random() * 0.2 : -Math.random() * 0.2;
		a = x - playerx;
		b = y - playery;
		c = Math.sqrt((a * a) + (b * b));
		if (a > 0 && b > 0  )
			return -(Math.PI/2 + Math.acos((-(a * a) + (b * b) + (c * c))/(2 * b * c))) + offset;
		if (a < 0 && b > 0)
			return 3*Math.PI/2.0 + Math.acos((-(a * a) + (b * b) + (c * c))/(2 * b * c)) + offset;
		if (a < 0 && b < 0)
			return  -Math.PI/2 + Math.acos((-(a * a) + (b * b) + (c * c))/(2 * b * c)) + offset;
		else
			return  3*Math.PI/2-Math.acos((-(a * a) + (b * b) + (c * c))/(2 * b * c)) + offset;
		
	}
	
	public int UpdateEnemy()
	{
		if(health < 1)
		{
			isAlive = false;
			return points;
		}
		return 0;
	}
	public void UpdatePlayerInfo(double x, double y, double rads)
	{
		playerx = x;
		playery = y;
		if(AIType == MIMIC)
		{
			direction = rads;
		}
	}
	
	public boolean IsAlive()
	{
		return isAlive;
	}
	public boolean FiredBullet()
	{
		return bulletFired;
	}
	public int CheckPlayerCollision(Shape player, int playerDamage)
	{
		if(player.intersects(new Rectangle2D.Double(x-GetCenterX(), y-GetCenterY(), enemy[0].getWidth(), enemy[0].getHeight())))
		{
			health -= playerDamage;
			if (health < 0)
				isAlive = false;
			return damage;
		}
		return 0;
	}
	

	public void AITypePhysicalAttacker()
	{
		if(step > 15)
		{
			if(followPlayer)
				direction = getBulletAngle();
			else
			{
				direction = -getBulletAngle();
			}
			step = 0;
		}
		step++;
		x += speed * Math.cos(direction);
		y += speed * Math.sin(direction);
	}
	
	public void AITypeDistanceAttacker()
	{
		
		if(step > 5)
		{
			if (followPlayer)
				direction = getBulletAngle();
			else if (Math.random() > 0.75)
				direction = (Math.random() > 0.50) ? (direction + (Math.random() * 0.3)) : -(direction + (Math.random() * 0.3)); 
						
			if((Math.random() * 1000) > 950)
			{
				bulletFired = true;
				//getBulletDirection();
			}	
			step = 0;
		}
		step++;
		x += speed * Math.cos(direction);
		y += speed * Math.sin(direction);
		UpdateBoundaries();
	}
	
	public void AITypeMimic()
	{
		x += speed * Math.cos(direction);
		y += speed * Math.sin(direction);
		UpdateBoundaries();
	}
	
	public void AITypeConfused()
	{
		if(step > 15)
		{
			direction = Math.random() * Math.PI * 2.0;
			step = 0;
		}
		step++;
		x += speed *Math.cos(direction);
		y += speed * Math.sin(direction);
		UpdateBoundaries();
	}
	
	public void UpdateBoundaries()
	{
		if(x > Assign5.SCREEN_WIDTH)
			x = -width;
		else if (x < -enemy[0].getWidth())
			x = Assign5.SCREEN_WIDTH;
		if (y > Assign5.SCREEN_HEIGHT)
			y = -height;
		else if (y < -enemy[0].getHeight())
			y = Assign5.SCREEN_HEIGHT;
	}
	@Override
	public void run() {
		while(isAlive)
		{
			if(!paused)
			{
				switch(AIType)
				{
					case MIMIC:
						AITypeMimic();
						break;
					case CONFUSED:	
						AITypeConfused();
						break;
					case PHYSICAL_ATTACKER:
						AITypePhysicalAttacker();
						break;
					case DISTANCE_ATTACKER:
						AITypeDistanceAttacker();
						break;
					default:
						AITypeConfused();
				}
				// TODO Auto-generated method stub
				if(!backwards)
					imgIndex++;
				else
					imgIndex--;
				if(imgIndex > 5)
				{
					backwards = true;
					imgIndex = 5;
				}
				else if(imgIndex < 0)
				{
					backwards = false;
					imgIndex = 0;
				}
				transform = new AffineTransform();
				transform.translate(x, y);
				transform.rotate(direction - Math.PI/2.0);
				transform.translate(-GetCenterX(), -GetCenterY());
			}
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
