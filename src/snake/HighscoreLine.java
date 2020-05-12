package snake;

public class HighscoreLine {
	
	public HighscoreLine(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public String name;
	public int score;
	
	public int getScore() {
		return score;
	}
}
