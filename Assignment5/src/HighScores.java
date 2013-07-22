import java.io.Serializable;

public class HighScores implements Serializable, Comparable<HighScores>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5922499563013960535L;
	int score;
	String name;
	public HighScores()
	{
		score = 0;
		name = "";
	}
	public HighScores(Ship player)
	{
		score = player.score;
		name = player.name;
	}
	@Override
	public int compareTo(HighScores other) {
		return other.score - this.score;
	}
	@Override
	public String toString()
	{
		int dots = 15;
		String ret = name;
		for(int i = 0; i < 15 - name.length(); i++)
		{
			ret+= " ";
		}
		for(int i = 0; i < dots; i++)
		{
			ret += ". ";
		}
		ret += score;
		return ret;	
	}
}