package asia.fourtitude.interviewq.jumble.core;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class JumbleEngine {

    private final List<String> wordList;
    public JumbleEngine() {
        this.wordList = initializeWordList();
    }
    private List<String> initializeWordList() {
        List<String> words = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("src/main/resources/words.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    words.add(line.toLowerCase());
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to initialize word list: file not found", e);
        }
        return words;
    }

    /**
     * From the input `word`, produces/generates a copy which has the same
     * letters, but in different ordering.
     *
     * Example: from "elephant" to "aeehlnpt".
     *
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#scramble()
     * b) scrambled letters/output must not be the same as input
     *
     * @param word  The input word to scramble the letters.
     * @return  The scrambled output/letters.
     */
    public String scramble(String word) {
        if (word == null || word.isEmpty() || word.length() == 1) {
            throw new IllegalArgumentException("Word must not be null or empty");
        }

        char[] letters = word.toCharArray();
        Random random = new Random();

        do {
            for (int i = letters.length - 1; i > 0; i--) {
                int randomIndex = random.nextInt(i + 1);
                char temp = letters[i];
                letters[i] = letters[randomIndex];
                letters[randomIndex] = temp;
            }
        } while (word.equals(new String(letters)));
        return new String(letters);
    }


    /**
     * Retrieves the palindrome words from the internal
     * word list/dictionary ("src/main/resources/words.txt").
     *
     * Word of single letter is not considered as valid palindrome word.
     *
     * Examples: "eye", "deed", "level".
     *
     * Evaluation/Grading:
     * a) able to access/use resource from classpath
     * b) using inbuilt Collections
     * c) using "try-with-resources" functionality/statement
     * d) pass unit test: JumbleEngineTest#palindrome()
     *
     * @return  The list of palindrome words found in system/engine.
     * @see //https://www.google.com/search?q=palindrome+meaning
     */
    public Collection<String> retrievePalindromeWords() {
        List<String> palindromes = new ArrayList<>();
        try {
            for (String word : wordList) {
                if (word.length() > 1 && isPalindrome(word)) {
                    palindromes.add(word);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while retrieving palindrome words", e);
        }
        return palindromes;
    }

    private boolean isPalindrome(String word) {
        int left = 0;
        int right = word.length() - 1;

        while (left < right) {
            if (word.charAt(left) != word.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Picks one word randomly from internal word list.
     *
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#randomWord()
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param length  The word picked, must of length.
     *                When length is null, then return random word of any length.
     * @return  One of the word (randomly) from word list.
     *          Or null if none matching.
     */
    public String pickOneRandomWord(Integer length) {
        if (length != null && length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer or null.");
        }
        List<String> matchingWords;
        if (length == null) {
            matchingWords = new ArrayList<>(wordList);
        } else {
            matchingWords = new ArrayList<>();
            for (String word : wordList) {
                if (word.length() == length) {
                    matchingWords.add(word);
                }
            }
        }
        if (matchingWords.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return matchingWords.get(random.nextInt(matchingWords.size()));
    }

    /**
     * Checks if the `word` exists in internal word list.
     * Matching is case insensitive.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word  The input word to check.
     * @return  true if `word` exists in internal word list.
     */
    public boolean exists(String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("Word must not be null, empty, or blank.");
        }
        String formattedWord = word.trim().toLowerCase();

        return wordList.contains(formattedWord);
    }

    /**
     * Finds all the words from internal word list which begins with the
     * input `prefix`.
     * Matching is case insensitive.
     *
     * Invalid `prefix` (null, empty string, blank string, non letter) will
     * return empty list.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param prefix  The prefix to match.
     * @return  The list of words matching the prefix.
     */
    public Collection<String> wordsMatchingPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty() || !prefix.chars().allMatch(Character::isLetter)) {
            return Collections.emptyList();
        }
        String lowerPrefix = prefix.toLowerCase();
        if (wordList == null || wordList.isEmpty()) {
            return Collections.emptyList();
        }
        return wordList.stream()
                .filter(word -> word.toLowerCase().startsWith(lowerPrefix))
                .collect(Collectors.toList());
    }

    /**
     * Finds all the words from internal word list that is matching
     * the searching criteria.
     *
     * `startChar` and `endChar` must be 'a' to 'z' only. And case insensitive.
     * `length`, if have value, must be positive integer (>= 1).
     *
     * Words are filtered using `startChar` and `endChar` first.
     * Then apply `length` on the result, to produce the final output.
     *
     * Must have at least one valid value out of 3 inputs
     * (`startChar`, `endChar`, `length`) to proceed with searching.
     * Otherwise, return empty list.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param startChar  The first character of the word to search for.
     * @param endChar    The last character of the word to match with.
     * @param length     The length of the word to match.
     * @return  The list of words matching the searching criteria.
     */
    public Collection<String> searchWords(Character startChar, Character endChar, Integer length) {
        if (startChar == null && endChar == null && length == null) {
            return Collections.emptyList();
        }
        return wordList.stream()
                .filter(word -> startChar == null || word.toLowerCase().charAt(0) == Character.toLowerCase(startChar))
                .filter(word -> endChar == null || word.toLowerCase().charAt(word.length() - 1) == Character.toLowerCase(endChar))
                .filter(word -> length == null || word.length() == length)
                .collect(Collectors.toList());
    }

    /**
     * Generates all possible combinations of smaller/sub words using the
     * letters from input word.
     *
     * The `minLength` set the minimum length of sub word that is considered
     * as acceptable word.
     *
     * If length of input `word` is less than `minLength`, then return empty list.
     *
     * The sub words must exist in internal word list.
     *
     * Example: From "yellow" and `minLength` = 3, the output sub words:
     *     low, lowly, lye, ole, owe, owl, well, welly, woe, yell, yeow, yew, yowl
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word       The input word to use as base/seed.
     * @param minLength  The minimum length (inclusive) of sub words.
     *                   Expects positive integer.
     *                   Default is 3.
     * @return  The list of sub words constructed from input `word`.
     */
    public Collection<String> generateSubWords(String word, Integer minLength) {
        if (word == null || minLength == null || minLength <= 0 || word.length() < minLength) {
            return Collections.emptyList();
        }
        Set<String> subWords = new HashSet<>();
        generateSubWordsHelper(word.toCharArray(), "", minLength, subWords);

        return subWords.stream()
                .filter(wordList::contains)
                .collect(Collectors.toList());
    }

    private void generateSubWordsHelper(char[] chars, String current, int minLength, Set<String> subWords) {
        if (current.length() >= minLength) {
            subWords.add(current);
        }
        for (int i = 0; i < chars.length; i++) {
            char[] remaining = new char[chars.length - 1];
            System.arraycopy(chars, 0, remaining, 0, i);
            System.arraycopy(chars, i + 1, remaining, i, chars.length - i - 1);
            generateSubWordsHelper(remaining, current + chars[i], minLength, subWords);
        }
    }


    /**
     * Creates a game state with word to guess, scrambled letters, and
     * possible combinations of words.
     *
     * Word is of length 6 characters.
     * The minimum length of sub words is of length 3 characters.
     *
     * @param length     The length of selected word.
     *                   Expects >= 3.
     * @param minLength  The minimum length (inclusive) of sub words.
     *                   Expects positive integer.
     *                   Default is 3.
     * @return  The game state.
     */
    public GameState createGameState(Integer length, Integer minLength) {
        Objects.requireNonNull(length, "length must not be null");
        if (minLength == null) {
            minLength = 3;
        } else if (minLength <= 0) {
            throw new IllegalArgumentException("Invalid minLength=[" + minLength + "], expect positive integer");
        }
        if (length < 3) {
            throw new IllegalArgumentException("Invalid length=[" + length + "], expect greater than or equals 3");
        }
        if (minLength > length) {
            throw new IllegalArgumentException("Expect minLength=[" + minLength + "] greater than length=[" + length + "]");
        }
        String original = this.pickOneRandomWord(length);
        if (original == null) {
            throw new IllegalArgumentException("Cannot find valid word to create game state");
        }
        String scramble = this.scramble(original);
        Map<String, Boolean> subWords = new TreeMap<>();
        for (String subWord : this.generateSubWords(original, minLength)) {
            subWords.put(subWord, Boolean.FALSE);
        }
        return new GameState(original, scramble, subWords);
    }

}
