import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Menu {
	
	BufferedImage background;
	double width;
	double height;
	Rectangle [] items;
	int selected;
	boolean isActive;
	
	public Menu()
	{
		width = 0;
		height = 0;
		selected = 0;
	}
	
	public Menu(double _w, double _h, int _items, String _bg, boolean _active)
	{
		width = _w;
		height = _h;
		selected = 0;
		items = new Rectangle[_items];
		isActive = _active;
		try {
			background = ImageIO.read(new File(_bg));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void DrawMenu(Graphics2D g2d)
	{
		g2d.drawImage(background, 0, 0, (int)Assign5.SCREEN_WIDTH, (int)Assign5.SCREEN_HEIGHT, null);
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(Color.red);
		g2d.draw(items[selected]);
	}
	


}
