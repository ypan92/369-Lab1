import org.json.*;
import java.io.*;
import java.util.*;

class beFuddledGen {

    private static HashMap<String, beFuddledGame> currentGames = new HashMap<String, beFuddledGame>();
    private static HashMap<String, String> currentUsers = new HashMap<String, String>();
    private static int gameCount = 1;

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

    /*public static JSONObject createActionObject() {
        int moveType = randomNumber(1, 100);

        if (moveType <= 90) {
            //regular move

        }
        else {
            //special move
        }
    }*/

    public static JSONObject createGameStart() {
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
            JSONObject startAction = createGameStart();
            startRecord.put("action", startAction);
        }
        catch (Exception e) {

        }

        return startRecord;
    }

    public static JSONObject createMoveLogRecord() {
        JSONObject logRecord = new JSONObject();
        try {
            
        }
        catch (Exception e) {

        }

        return logRecord;
    }


    public static void main (String[] args) {

        if (args.length < 2) {
        	System.out.println("\nERROR: You did not specify a file name AND a total number of record logs!");
            System.out.println("Please run the following command: \n\n\tjava beFuddledGen fileName logNum\n");
            System.out.println("\t\tWhere fileName is the name of the output file to create, \n\t\tand logNum is the number of log elements to contain\n");
        }
        else {
	        String fileName = args[0];
	        try {
	        	int logTotal = Integer.parseInt(args[1]);

                JSONArray outputLog = new JSONArray();

                int startGames = initialOngoingGames(logTotal);
                for (int i = 1; i <= startGames; i++) {
                    String user = getUser();
                    int gameNum = gameCount++;
                    JSONObject startLog = createStartLogRecord(user, gameNum);
                    beFuddledGame gameObj = new beFuddledGame(user, gameNum);
                    gameObj.updateActionNum();

                    currentGames.put(Integer.toString(i), gameObj);

                    outputLog.put(startLog);
                }

                File outputFile = new File(fileName);
                outputFile.createNewFile();
                FileWriter writer = new FileWriter(outputFile);
                writer.write(outputLog.toString(2));
                writer.flush();
                writer.close();
	    	}
	    	catch(Exception e) {
	    		System.out.println("\nERROR: The given number of record logs to create was not a number!");
                System.out.println("Please run the following command: \n\n\tjava beFuddledGen fileName logNum\n");
                System.out.println("\t\tWhere fileName is the name of the output file to create, \n\t\tand logNum is the number of log elements to contain\n");
	    	}
    	}


    }
}
