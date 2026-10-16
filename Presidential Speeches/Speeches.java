import java.io.File;
import java.io.IOException;
import java.util.Scanner;
/**
 * Analyze the presidential speeches given by US presidents.
 *
 * Aryaman Subramanian
 * Oct 15 2026
 */

public class Speeches
{
    public static void main(String[] args) throws IOException
    {
        String fileName = "George_Washington_1789-04-30T13:03:58-04:56_April_30,_1789:_First_Inaugural_Address.json";

        File speechFile = new File("speeches/" + fileName);
        Scanner fileScan = new Scanner(speechFile);
        String rawJson = "";
        while (fileScan.hasNextLine())
        {
            rawJson += fileScan.nextLine() + " ";
        }
        fileScan.close();

        String president = extractJsonValue(rawJson, "president");
        String date = extractJsonValue(rawJson, "date");
        String speechText = extractJsonValue(rawJson, "transcript");

        // The JSON stores line breaks as \n, so turn them into spaces
        speechText = speechText.replaceAll("\\\\n", " ");

        String[] words = speechText.split("\\s+");

        int wordCount = 0;
        int totalLetters = 0;
        int peopleCount = 0;
        int governmentCount = 0;
        int unitedCount = 0;
        int ourCount = 0;

        for (String w : words)
        {
            String word = w.replaceAll("[^a-zA-Z]", "").toLowerCase();

            if (word.length() > 0)
            {
                wordCount++;
                totalLetters += word.length();

                if (word.equals("people"))
                {
                    peopleCount++;
                }
                if (word.equals("government"))
                {
                    governmentCount++;
                }
                if (word.equals("united"))
                {
                    unitedCount++;
                }
                if (word.equals("our"))
                {
                    ourCount++;
                }
            }
        }

        // Round to 2 decimal places by casting
        double averageLength = (int) ((double) totalLetters / wordCount * 100 + 0.5) / 100.0;
        double peoplePercent = (int) (peopleCount * 10000.0 / wordCount + 0.5) / 100.0;
        double governmentPercent = (int) (governmentCount * 10000.0 / wordCount + 0.5) / 100.0;
        double unitedPercent = (int) (unitedCount * 10000.0 / wordCount + 0.5) / 100.0;
        double ourPercent = (int) (ourCount * 10000.0 / wordCount + 0.5) / 100.0;

        System.out.println("President: " + president);
        System.out.println("Date: " + date.substring(0, 10));
        System.out.println("Word Count: " + wordCount);
        System.out.println("Average Word Length: " + averageLength);
        System.out.println();
        System.out.println("people: " + peopleCount + " (" + peoplePercent + "%)");
        System.out.println("government: " + governmentCount + " (" + governmentPercent + "%)");
        System.out.println("united: " + unitedCount + " (" + unitedPercent + "%)");
        System.out.println("our: " + ourCount + " (" + ourPercent + "%)");
    }

    /**
     * Extracts the String value associated with a JSON key.
     * Example: extractJsonValue(jsonString, "president") returns "George Washington"
     */
    public static String extractJsonValue(String json, String key)
    {
        String keyTarget = "\"" + key + "\":";
        int keyIndex = json.indexOf(keyTarget);
        if (keyIndex == -1)
        {
            return "";
        }

        // Locate the first opening quote after the key
        int startQuote = json.indexOf("\"", keyIndex + keyTarget.length());
        if (startQuote == -1)
        {
            return "";
        }

        // Locate the matching closing quote
        int endQuote = json.indexOf("\"", startQuote + 1);

        // Handle escaped quotes inside the speech text (e.g., \")
        while (endQuote != -1 && json.charAt(endQuote - 1) == '\\')
        {
            endQuote = json.indexOf("\"", endQuote + 1);
        }

        if (endQuote == -1)
        {
            return "";
        }

        return json.substring(startQuote + 1, endQuote);
    }
}
