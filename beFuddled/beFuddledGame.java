import java.util.Random;
import java.util.ArrayList;

class beFuddledGame {
	private String userId;
	private int gameId;
	private int points;
	private int actionNum;
	private int totalMoves;
	private int totalSpecialMoves;
	private ArrayList<Integer> specialMoveIndices;

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

	public int getTotalSpecialMoves() {
		return totalSpecialMoves;
	}

	public int getTotalMoves() {
		return totalMoves;
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

	public void setTotalMoves(int moves) {
		totalMoves = moves;
	}

	public void setTotalSpecialMoves(int num) {
		totalSpecialMoves = num;
	}

	public void createSpecialMoveIndices() {
		Random rand = new Random();

		specialMoveIndices = new ArrayList<Integer>();
		Integer index;
		for (int i = 0; i < totalSpecialMoves; i++) {
			index = new Integer(rand.nextInt(totalMoves - 1 + 1) + 1);
			while (specialMoveIndices.contains(index)) {
				index = new Integer(rand.nextInt(totalMoves - 1 + 1) + 1);
			}
			specialMoveIndices.add(index);
		}
		
	}

	public boolean isSpecialMove() {
		if (specialMoveIndices.contains(new Integer(actionNum))) {
			return true;
		}
		return false;
	}
}
