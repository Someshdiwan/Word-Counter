import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {
    public static String readFile(String filepath) {
        try {
            return Files.readString(Paths.get(filepath));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return "";
        }
    }

    // Split method: split on whitespace, sanitize tokens by
    // trimming punctuation at ends, and collect normalized words.
    public static ArrayList<String> countWordsSplit(String text) {
        ArrayList<String> words = new ArrayList<>();
        if (text == null || text.isEmpty()) return words;

        String[] tokens = text.split("\\s+"); // split on whitespace
        for (String t : tokens) {
            String w = normalizeToken(t);
            if (!w.isEmpty()) words.add(w);
        }
        return words;
    }

    // Regex method: use \b\w+\b to extract word tokens (letters, digits, underscore)
    public static ArrayList<String> countWordsRegex(String text) {
        ArrayList<String> words = new ArrayList<>();
        if (text == null || text.isEmpty()) return words;

        Matcher matcher = Pattern.compile("\\b\\w+\\b").matcher(text);
        while (matcher.find()) {
            String w = matcher.group().toLowerCase();
            words.add(w);
        }
        return words;
    }

    // Normalize token by removing leading/trailing non-alphanumeric characters
    // and converting to lower-case. Keeps internal punctuation (e.g., hyphens) removed.
    private static String normalizeToken(String token) {
        if (token == null) return "";
        // remove leading/trailing non-alphanumeric characters (keep internal letters/numbers)
        String cleaned = token.replaceAll("^[^\\p{Alnum}]+|[^\\p{Alnum}]+$", "");
        return cleaned.toLowerCase();
    }

    public static int wordFrequency(ArrayList<String> words, String searchWord) {
        if (searchWord == null || searchWord.isEmpty()) return 0;
        String key = searchWord.toLowerCase();
        int count = 0;
        for (String w : words) {
            if (w.equals(key)) count++;
        }
        return count;
    }

    public static void displayResults(String methodName, ArrayList<String> words, String searchWord) {
        int total = words.size();
        int freq = wordFrequency(words, searchWord);
        System.out.println("---- " + methodName + " ----");
        System.out.println("Total Words: " + total);
        System.out.println("Frequency of '" + searchWord + "': " + freq);
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Word Counter!\n");

        // Change this path if you want to point to a different input file.
        String filepath = "/Users/somesh/Side Hustle/Word-Counter/Word Counter - Reading Text from File/src/input.txt";
        String text = readFile(filepath);

        if (text.isEmpty()) {
            System.out.println("No text to process. Exiting.");
            return;
        }

        System.out.println("File contents (first 500 chars):");
        System.out.println(text.length() > 500 ? text.substring(0, 500) + "..." : text);
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the word to search (case-insensitive): ");
        String searchWord = scanner.nextLine().trim();
        if (searchWord.isEmpty()) {
            System.out.println("No search word provided. Exiting.");
            scanner.close();
            return;
        }

        // Split method
        ArrayList<String> splitWords = countWordsSplit(text);
        displayResults("Split Method", splitWords, searchWord);

        // Regex method
        ArrayList<String> regexWords = countWordsRegex(text);
        displayResults("Regex Method", regexWords, searchWord);

        scanner.close();
    }
}
