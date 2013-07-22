import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;


public class Bullet implements Runnable{

	double x;
	double y;
	double width;
	double height;
	double speed;
	double cos;
	double sin;
	double angle;
	boolean shotFromEnemy;
	int damage;
	int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	boolean destroy = false;
	boolean paused;
	
	public Bullet(double _x, double _y, double _w, double _h, boolean _FromEnemy, int _d, double _s, double _a)
	{
		x = _x;
		y = _y;
		width = _w;
		height = _h;
		speed = _s;
		angle = _a;
		cos = Math.cos(_a);
		sin = Math.sin(_a);
		shotFromEnemy = _FromEnemy;
		damage = _d;
		paused = false;
	}
	
	
	public Ellipse2D getBullet()
	{
		return new Ellipse2D.Double(x - width/2, y -height/2, width, height);
	}

	public void DrawBullet(Graphics2D g2d, Color color)
	{
		g2d.setColor(color);
		g2d.fill(getBullet());
	}
	
	public void DestroyBullet(boolean _destroy)
	{
		destroy = _destroy;
	}
	public void Pause()
	{
		paused = true;
	}
	public void UnPause()
	{
		paused = false;
	}
	
	public Rectangle2D GetHitBox()
	{
		return new Rectangle2D.Double(x - width/2.0, y - height/2.0, width, height);
	}
	
	public int CheckCollision(Shape hitbox)
	{
		if(hitbox.intersects(new Rectangle2D.Double(x-width/2.0, y-height/2.0, width, height)))
		{
			DestroyBullet(true);
			return damage;
		}
		return 0;
	}
	@Override
	public void run() {
		
		while(x > -25 && x < SCREEN_WIDTH + 25 && y > -25 && y < SCREEN_HEIGHT + 25)
		{
			if(!paused)
			{
				x += cos * speed;
				y += sin * speed;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		destroy = true;
	}
}
