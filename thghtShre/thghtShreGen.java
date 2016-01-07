import org.json.*;

import java.io.*;

class thghtShreGen {

    private static JSONArray generateJSON(int numObjects) {
        JSONArray array = new JSONArray();
        JSONObject object;

        for (int i = 0; i < numObjects; i++) {
            object = new JSONObject();
        }

        return array;
    }

    private static void writeJSONtoFile(String fileName, JSONArray array) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            writer.println("hello");

            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JSONArray array;

        try {
            array = generateJSON(Integer.parseInt(args[1]));

            writeJSONtoFile(args[0], array);
        }
        catch (NumberFormatException e) {
            System.out.println(args[1] + " not a number");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough input parameters");
        }
    }
}
