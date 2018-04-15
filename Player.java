
public class Player {

	int score;
	String playername;
	
	public Player(int score) {
		this.score = score;
	}
	
	public Player(String playername) {
		this.playername = playername;
	}
	
	public String getPlayerName() {
		return playername;
	}
	
	public void setPlayerName(String playername1) {
		playername = playername1;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score1) {
		score = score1;
	}
	
}
