import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static String[] readGuessWords() throws IOException {
        Path guessWordPath = FileSystems.getDefault().getPath("src", "guessWords.txt");
        List<String> guessWordLines = Files.readAllLines(guessWordPath, Charset.defaultCharset());
        return guessWordLines.toArray(new String[0]);
    } // reads the guessWords txt document into an array

    public static String[] readEndWords() throws IOException {
        Path endWordPath = FileSystems.getDefault().getPath("src", "endWords.txt");
        List<String> endWordLines = Files.readAllLines(endWordPath, Charset.defaultCharset());
        return endWordLines.toArray(new String[0]);
    } // reads the endWords txt document into an array

    public static void writeString(String s) throws AWTException {
        Robot robot = new Robot();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }
            robot.keyPress(Character.toUpperCase(c));
            robot.keyRelease(Character.toUpperCase(c));

            if (Character.isUpperCase(c)) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        }
    } // outputs characters as if they were typed on a keyboard THIS COULD BE DANGEROUS USE WITH CAUTION

    public static void rightArrow() {
        try {
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);

        } catch (AWTException e) {
            e.printStackTrace();
        }
    } // types right arrow on keyboard

    public static void leftAndDownArrow() {
        try {
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);

        } catch (AWTException e) {
            e.printStackTrace();
        }
    } // types the left arrow then down arrow on keyboard

    public static void printArray(String[] arrayToPrint) {
        for (String s : arrayToPrint) {
            System.out.println(s);
        }
    } // used for debugging

    public static void printList(List<String> listToPrint) {
        for (int i = 0; i < listToPrint.size(); i++) {
            System.out.println(listToPrint.get(i));
        }
    } // used for debugging

    public static String[] presence(String guessWord, String endWord) {
        //
        //
        // string[] = ["gray", + "p", + "gray", + "e", + "yellow", + "n", + "gray", + "i", + "green", + "s"]
        String[] presence = new String[10];
        // so that it says gray unless specified otherwise
        Arrays.fill(presence, "gray");
        char[] guessChars = new char[guessWord.length()];
        char[] endChars = new char[endWord.length()];
        for (int i = 0; i < guessChars.length; i++) {
            guessChars[i] = guessWord.charAt(i);
            endChars[i] = endWord.charAt(i);
        }
        for (int i = 0; i < guessChars.length; i++) {
            boolean present = false;
            // check if any of guess letters are in the word
            for (int e = 0; e < endChars.length; e++) {
                if (guessChars[i] == endChars[e]) {
                    if (e == i) presence[i * 2] = "green"; // if it's correct position give it the status green
                    else presence[i * 2] = "yellow"; // if it's there but not in correct position
                    // will say "gray" if not there at all
                    present = true;
                    break;
                }
            }
            presence[(i * 2) + 1] = String.valueOf(guessChars[i]);
        }
        return presence;
    } // simulates the results of Wordle

    public static int size(String[] list) {
        int size = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] == null) {
                size = i;
                break;
            }
        }
        return size;
    } // gets the size of any given String[] not including undefined elements

    public static List<String> stringToChar(String stringToConvert) {
        char[] charArrayFromString = new char[stringToConvert.length()];
        List<String> endWordGrayTestCharsList = new ArrayList<>();
        for (int a = 0; a < charArrayFromString.length; a++) {
            charArrayFromString[a] = stringToConvert.charAt(a);
            endWordGrayTestCharsList.add(String.valueOf(charArrayFromString[a]));
        }
        return endWordGrayTestCharsList;
    } // takes any string into a list of its chars

    public static List<String> grayTest(String letter, List<String> endWordsToCheck) {
        List<String> TestedWords = new ArrayList<>();
        for (int i = 0; i < endWordsToCheck.size(); i++) { // grabs word from previously filtered ones
            String endWordGrayTest = endWordsToCheck.get(i); // the word to be tested for gray letters
            List<String> endWordGrayTestCharsList = stringToChar(endWordGrayTest); //creates characters for the chosen word
            if (!endWordGrayTestCharsList.contains(letter)) {
                TestedWords.add(endWordGrayTest);
            }
        }
        return TestedWords;
    } // filters a letter from a list and returns filtered list

    public static List<String> yellowTest(String letter, List<String> endWordsToCheck) {
        List<String> TestedWords = new ArrayList<>();
        for (int i = 0; i < endWordsToCheck.size(); i++) { // grabs word from previously filtered ones
            String endWordGrayTest = endWordsToCheck.get(i); // the word to be tested for yellow letters
            List<String> endWordGrayTestCharsList = stringToChar(endWordGrayTest); //creates characters for the chosen word
            if (endWordGrayTestCharsList.contains(letter)) {
                TestedWords.add(endWordGrayTest);
            }
        }
        return TestedWords;
    } // filters a letter from a list and returns filtered list

    public static List<String> greenTest(int position, String letter, List<String> endWordsToCheck) {
        List<String> TestedWords = new ArrayList<>();
        for (int i = 0; i < endWordsToCheck.size(); i++) { // grabs word from previously filtered ones
            String endWordGreenTest = endWordsToCheck.get(i); // the word to be tested for green letters
            List<String> endWordGreenTestCharsList = stringToChar(endWordGreenTest); //creates characters for the chosen word
            if (endWordGreenTestCharsList.get(position - 1).equals(letter)) {
                TestedWords.add(endWordGreenTest);
            }
        }
        return TestedWords;
    } // filters a list for a position with a green letter

    public static double rating(String guessWord, String endWord) throws IOException { //percent removed
        String[] presence = presence(guessWord, endWord);
        String[] endWords = readEndWords();

        List<String> filter1 = new ArrayList<>();
        List<String> filter2 = new ArrayList<>();
        List<String> filter3 = new ArrayList<>();
        List<String> filter4 = new ArrayList<>();
        List<String> filter5 = new ArrayList<>();


        if (presence[0].equals("gray")) {  // when a status is gray, check all endWordGrayTestChars for the letter after the gray
            filter1 = grayTest(presence[1], Arrays.asList(endWords));
        } else if (presence[0].equals("yellow")) {
            filter1 = yellowTest(presence[1], Arrays.asList(endWords));
        } else if (presence[0].equals("green")) {
            filter1 = greenTest(1, presence[1], Arrays.asList(endWords));
        }

        if (presence[2].equals("gray")) {
            filter2 = grayTest(presence[3], filter1);
        } else if (presence[2].equals("yellow")) {
            filter2 = yellowTest(presence[3], filter1);
        } else if (presence[2].equals("green")) {
            filter2 = greenTest(2, presence[3], filter1);
        }

        if (presence[4].equals("gray")) {
            filter3 = grayTest(presence[5], filter2);
        } else if (presence[4].equals("yellow")) {
            filter3 = yellowTest(presence[5], filter2);
        } else if (presence[4].equals("green")) {
            filter3 = greenTest(3, presence[5], filter2);
        }

        if (presence[6].equals("gray")) {
            filter4 = grayTest(presence[7], filter3);
        } else if (presence[6].equals("yellow")) {
            filter4 = yellowTest(presence[7], filter3);
        } else if (presence[6].equals("green")) {
            filter4 = greenTest(4, presence[7], filter3);
        }

        if (presence[8].equals("gray")) {
            filter5 = grayTest(presence[9], filter4);
        } else if (presence[8].equals("yellow")) {
            filter5 = yellowTest(presence[9], filter4);
        } else if (presence[8].equals("green")) {
            filter5 = greenTest(5, presence[9], filter4);
        }

        return 100.0 - (100 * (filter5.size() / (double) 2315));
    } // returns a percent of answers removed from the list possible answers

    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        TimeUnit.SECONDS.sleep(5); // give time to prepare to type
        String[] endWords = readEndWords();
        String[] guessWords = readGuessWords();

        for (int i = 0; i < guessWords.length; i++) {
            if (i % 250 == 0 && i != 0) {
                TimeUnit.SECONDS.sleep(20); // gives time for Google sheets to save
            }
            double totalScore = 0;

            for (int e = 0; e < endWords.length; e++) {
                totalScore += rating(guessWords[i], endWords[e]);
            }

            double averageRating = totalScore / 2315;
            writeString(guessWords[i]); // prints the guessWord
            rightArrow();
            writeString(String.valueOf(averageRating)); // prints the following value applied to it
            leftAndDownArrow();
        }
    }
}
