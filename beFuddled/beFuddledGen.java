import org.json.*;
import java.io.*;
import java.util.*;

class beFuddledGen {

    private static HashMap<Integer, beFuddledGame> currentGames = new HashMap<Integer, beFuddledGame>();
    private static HashMap<String, String> currentUsers = new HashMap<String, String>();
    private static int gameCount = 1;
    private static int gameCap;
    private static int currentLogEvents;
    private static int maxGameId;
    private static int minGameId;

    /* Returns a random number in the range specified by min and max
        with min and max inclusive */
    public static int randomNumber(int min, int max) {
        Random rand = new Random();

        return rand.nextInt(max - min + 1) + min;
    }

    /* Returns the number of games that will be started/ongoing at the start of runtime */
    public static int initialOngoingGames(int logTotal) {
        int gamePercent = randomNumber(5, 15);

        return gamePercent * logTotal / 100;
    }

    public static String getUser() {
        String tempUserId = "u" + randomNumber(1, 10000);
        while (currentUsers.containsKey(tempUserId)) {
            tempUserId = "u" + randomNumber(1, 10000);
        }

        currentUsers.put(tempUserId, tempUserId);
        return tempUserId;
    }

    public static JSONObject createGameStartAction() {
        JSONObject gameStart = new JSONObject();
        try {
            gameStart.put("actionNumber", 1);
            gameStart.put("actionType", "GameStart");
        }
        catch (Exception e) {

        }

        return gameStart;
    }

    public static JSONObject createStartLogRecord(String user, int game) {
        JSONObject startRecord = new JSONObject();
        try {
            startRecord.put("user", user);
            startRecord.put("game", game);
            JSONObject startAction = createGameStartAction();
            startRecord.put("action", startAction);
        }
        catch (Exception e) {

        }

        return startRecord;
    }

    /* Finds the total number of moves a game will have, based on the probabilities
        specified in the README */
    public static int totalGameMoves() {
        int movePercentage = randomNumber(1, 100);

        if (movePercentage <= 10) {
            return randomNumber(9, 20);
        }
        else if (movePercentage > 10 && movePercentage <= 30) {
            return randomNumber(21, 30);
        }
        else if (movePercentage > 30 && movePercentage <= 80) {
            return randomNumber(31, 50);
        }
        else if (movePercentage > 80 && movePercentage <= 95) {
            return randomNumber(51, 70);
        }
        else {
            return randomNumber(71, 100);
        }
    }

    /* Finds the total number of special moves a game will have based on the total
        moves that game has as specified in the README */
    public static int totalSpecialMoves(int totalMoves) {
        if (totalMoves >= 9 && totalMoves <= 30) {
            return 1;
        }
        else if (totalMoves >= 31 &&  totalMoves <= 50) {
            return 2;
        }
        else {
            return 3;
        }
    }

    /* Checks if the next action should be starting a new game. 
        Only start if a new game if the currentGames is less than the gameCap
        AND the random number generator is between 71-100 */
    public static boolean shouldStartNewGame() {
        if (currentGames.size() == gameCap) {
            return false;
        }
        else {
            int startPercentage = randomNumber(1, 100);
            if (startPercentage <= 70) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    public static JSONObject initializeNewGameStart() {
        String user = getUser();
        int gameNum = gameCount++;

        JSONObject startLog = createStartLogRecord(user, gameNum);
        beFuddledGame gameObj = new beFuddledGame(user, gameNum);
        gameObj.updateActionNum();

        int totalMoves = totalGameMoves();
        int totalSpecials = totalSpecialMoves(totalMoves);
        gameObj.setTotalMoves(totalMoves);
        gameObj.setTotalSpecialMoves(totalSpecials);
        gameObj.createSpecialMoveIndices();

        currentGames.put(new Integer(gameNum), gameObj);

        return startLog;
    }

    /* Finds a random game id that is currently ongoing for the next move */
    public static int getGameIdToPlay() {
        int tempGameId = randomNumber(minGameId, maxGameId);

        while (!currentGames.containsKey(new Integer(tempGameId))) {
            tempGameId = randomNumber(minGameId, maxGameId);
        }

        return tempGameId;
    }

    public static JSONObject createGameEndAction(int actionNum, int points) {
        JSONObject endAction = new JSONObject();
        try {
            endAction.put("actionNumber", actionNum);
            endAction.put("actionType", "GameEnd");
            endAction.put("points", points);
            if (randomNumber(0, 1) == 1) {
                endAction.put("gameStatus", "Win");
            }
            else {
                endAction.put("gameStatus", "Loss");
            }
        }
        catch (Exception e) {

        }

        return endAction;
    }

    public static JSONObject createMoveLocation() {
        JSONObject location = new JSONObject();
        try {
            int dice = randomNumber(1, 100);
            if (dice <= 3) {
                location.put("x", randomNumber(1, 2));
            }
            else if (dice > 3 && dice <= 15) {
                location.put("x", randomNumber(3, 6));
            }
            else if (dice > 15 && dice <= 85) {
                location.put("x", randomNumber(7, 13));
            }
            else if (dice > 85 && dice <= 97) {
                location.put("x", randomNumber(14, 17));
            }
            else {
                location.put("x", randomNumber(18, 20));
            }

            int dice2 = randomNumber(1, 100);
            if (dice2 <= 3) {
                location.put("y", randomNumber(1, 2));
            }
            else if (dice2 > 3 && dice2 <= 15) {
                location.put("y", randomNumber(3, 6));
            }
            else if (dice2 > 15 && dice2 <= 85) {
                location.put("y", randomNumber(7, 13));
            }
            else if (dice2 > 85 && dice2 <= 97) {
                location.put("y", randomNumber(14, 17));
            }
            else {
                location.put("y", randomNumber(18, 20));
            }
        }
        catch (Exception e) {

        }

        return location;
    }

    private static String getSpecialMoveType() {
        int dice = randomNumber(1, 100);
        String type;

        if (dice <= 50)
            type = "Shuffle";
        else if (dice <= 80)
            type = "Invert";
        else if (dice <= 95)
            type = "Clear";
        else
            type = "Rotate";

        return type;
    }

    public static JSONObject createRegMoveLogRecord(beFuddledGame gameObj) {
        JSONObject moveRecord = new JSONObject();
        try {
            String userId = gameObj.getUserId();
            int gameId = gameObj.getGameId();
            moveRecord.put("user", userId);
            moveRecord.put("game", gameId);

            JSONObject regMoveAction = new JSONObject();
            int actionNum = gameObj.getActionNum();
            int oldPoints = gameObj.getPoints();
            regMoveAction.put("actionNumber", actionNum);
            regMoveAction.put("actionType", "Move");
            int pointsAdded = randomNumber(-20, 20);
            regMoveAction.put("pointsAdded", pointsAdded);
            regMoveAction.put("points", oldPoints + pointsAdded);
            regMoveAction.put("location", createMoveLocation());

            gameObj.updateActionNum();
            gameObj.updatePoints(pointsAdded);


            moveRecord.put("action", regMoveAction);
        }
        catch (Exception e) {

        }

        return moveRecord;
    }

    public static JSONObject createSpecialMoveLogRecord(beFuddledGame gameObj) {
        JSONObject moveRecord = new JSONObject();
        try {
            moveRecord.put("user", gameObj.getUserId());
            moveRecord.put("game", gameObj.getGameId());

            JSONObject specialMoveAction = new JSONObject();
            specialMoveAction.put("actionNumber", gameObj.getActionNum());
            specialMoveAction.put("actionType", "SpecialMove");
            int pointsAdded = randomNumber(-20, 20);
            specialMoveAction.put("pointsAdded", pointsAdded);
            specialMoveAction.put("points", gameObj.getPoints() + pointsAdded);

            specialMoveAction.put("move", getSpecialMoveType());

            gameObj.updateActionNum();
            gameObj.updatePoints(pointsAdded);

            moveRecord.put("action", specialMoveAction);
        }
        catch (Exception e) {

        }

        return moveRecord;
    }


    public static void main (String[] args) {

        if (args.length < 2) {
        	System.out.println("\nERROR: You did not specify a file name AND a total number of record logs!");
            System.out.println("Please run the following command: \n\n\tjava beFuddledGen fileName logNum\n");
            System.out.println("\t\tWhere fileName is the name of the output file to create, \n\t\tand logNum is the number of log elements to contain\n");
        }
        else {
	        String fileName = args[0];
                PrintWriter writer = null;
	        try {
	        	int logTotal = Integer.parseInt(args[1]);

                //JSONArray outputLog = new JSONArray();

                int startGames = initialOngoingGames(logTotal);
                gameCap = startGames;

                try {
                    writer = new PrintWriter(new FileWriter(fileName));
                    writer.println("[");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                //Create the initial Game Start logs and construct their associated
                //beFuddledGame objects
                /*
                for (int i = 1; i <= startGames; i++) {
                    JSONObject startLog = initializeNewGameStart();
                    //outputLog.put(startLog);
                    writer.println(startLog.toString(2));
                }
                */
                
                writer.println(initializeNewGameStart().toString(2));

                minGameId = 1;
                maxGameId = 1;
                //maxGameId = gameCap;

                //update the number of log events that have occurred
                //currentLogEvents = startGames;
                currentLogEvents = 1;

                //Create the rest of the log events as either existing game moves,
                //ending a game, or starting a new game
                while (currentLogEvents < logTotal) {
                    if (shouldStartNewGame()) {
                        JSONObject newStartLog = initializeNewGameStart();
                        //outputLog.put(newStartLog);
                        writer.println(newStartLog.toString(2));

                        //gameCount was incremented in initializeNewGameStart. 
                        //decrement it by one to get the current max game id that 
                        //was just created.
                        maxGameId = gameCount - 1;
                    }
                    else {
                        int gameIdToPlay = getGameIdToPlay();
                        beFuddledGame gameObjToPlay = currentGames.get(gameIdToPlay);

                        if (gameObjToPlay.isSpecialMove()) {
                            JSONObject specialMoveLog = createSpecialMoveLogRecord(gameObjToPlay);
                            //outputLog.put(specialMoveLog);
                            writer.println(specialMoveLog.toString(2));
                        }
                        else {
                            JSONObject regMoveLog = createRegMoveLogRecord(gameObjToPlay);
                            //outputLog.put(regMoveLog);
                            writer.println(regMoveLog.toString(2));
                        }
                    }

                    ++currentLogEvents;
                }
                writer.println("]");
                writer.close();

                /*
                File outputFile = new File(fileName);
                outputFile.createNewFile();
                FileWriter writer = new FileWriter(outputFile);
                writer.write(outputLog.toString(2));
                writer.flush();
                writer.close();
                */
	    	}
	    	catch(Exception e) {
	    		System.out.println("\nERROR: The given number of record logs to create was not a number!");
                System.out.println("Please run the following command: \n\n\tjava beFuddledGen fileName logNum\n");
                System.out.println("\t\tWhere fileName is the name of the output file to create, \n\t\tand logNum is the number of log elements to contain\n");
	    	}
    	}


    }
}
