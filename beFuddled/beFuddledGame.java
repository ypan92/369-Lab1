
class beFuddledGame {
	private String userId;
	private int gameId;
	private int points;
	private int actionNum;

	public beFuddledGame() {
		userId = "";
		gameId  = 0;
		points = 0;
		actionNum = 1;
	}

	public beFuddledGame(String user, int game) {
		userId = user;
		gameId = game;
		points = 0;
		actionNum = 1;
	}

	public String getUserId() {
		return userId;
	}

	public int getGameId() {
		return gameId;
	}

	public int getPoints() {
		return points;
	}

	public int getActionNum() {
		return actionNum;
	}

	public void setUserId(String id) {
		userId = id;
	}

	public void setGameId(int id) {
		gameId = id;
	}

	public void updatePoints(int update) {
		points += update;
	}

	public void updateActionNum() {
		++actionNum;
	}
}
