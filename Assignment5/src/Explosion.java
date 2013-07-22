import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class Explosion implements Runnable{

	BufferedImage[] animation;
	double x, y, width, height;
	int index;
	boolean isDead;
	public Explosion(double _x, double _y, double _w, double _h)
	{
		index = -1;
		x = _x;
		y = _y;
		width = _w;
		height = _h;
		isDead = false;
		animation = new BufferedImage[6];
		for(int i = 0; i < 6; i++)
		{
			try {
				animation[i] = ImageIO.read(new File("Explosion/Explosion" + i +".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void Draw(Graphics2D g2d)
	{
		if(index < 6  && index >= 0)
			g2d.drawImage(animation[index], (int)x, (int)y, (int)width, (int)height,  null);
	}
	@Override
	public void run() {
		while(index < 6)
		{
			index++;
			try{
				Thread.sleep(125);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}	
		isDead = true;
	}
}
