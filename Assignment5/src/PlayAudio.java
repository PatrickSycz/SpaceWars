import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class PlayAudio implements Runnable
{
	AudioFormat audioFormat;
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;
	File soundFile;
	String name;
	public PlayAudio(String _name)
	{
		name = _name;
		
    	switch(name)
    	{
    		case "Theme":
    			soundFile = new File("Theme.wav");
    			break;
    		case "EnemyExplosion":
    			soundFile = new File("Explosion.wav");
    			break;
    		default:
    			soundFile = new File("bullet.wav");
    	}
    	try {
    		audioInputStream = AudioSystem.getAudioInputStream(soundFile);
    	} 
    	catch (UnsupportedAudioFileException | IOException e) {
		e.printStackTrace();
    	}
    	audioFormat = audioInputStream.getFormat();
    	//System.out.println(audioFormat);
	    	DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
	    	try {
    		sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
    	} 
    	catch (LineUnavailableException e) {
    		e.printStackTrace();
    	}
      
	}
	@Override
	public void run()
	{
	      int count;
	      byte[] buffer = new byte[10000];
	      
	      try {
		      sourceDataLine.open(audioFormat);
		      sourceDataLine.start();
		      while((count = audioInputStream.read(buffer, 0, buffer.length)) != -1)
			  {
				  sourceDataLine.write(buffer, 0, count);
			  }
		      sourceDataLine.close();
	      } 
	      catch (IOException | LineUnavailableException e) {
	    	  e.printStackTrace();
	      }
	      if (name == "Theme")
	    	  new Thread (new PlayAudio("Theme")).start();
	}		
}