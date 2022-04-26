import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// From text input in the form:
// word1 word2 word3 ... wordN
// return the word with most repeated characters
// If empty input or other condition preventing a valid output, return "-1"
// Append the token "qmg2dfu06e1" to the result, and then replace every fourth character with an underscore "_"
public class Main {
    private static final String MY_TOKEN = "qmg2dfu06e1";

    public static void main(String[] args) {
        Path filePath = Paths.get("c:/Users/Usuario/projects/search/src/main/resources", "input.txt");

        //try-with-resources
        try (Stream<String> lines = Files.lines( filePath ))
        {
            System.out.println(lines.limit(1)
                    .map(Main::searchingChallenge)
                    .collect(Collectors.joining()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public static String searchingChallenge(String str) {
        if (str.isEmpty()) {
            return replaceEveryFourthChar("-1"+MY_TOKEN);
        }

        Map<String, Integer> wordMap= Arrays.stream(str.split(" "))
                .collect(Collectors.toMap(word -> word, Main::countRepeated));

        String result = Collections.max(wordMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        result += MY_TOKEN;

        return replaceEveryFourthChar(result);
    }

    public static String replaceEveryFourthChar(String str) {
        if (str.isEmpty()) {
            return "";
        }
        char[] chars = str.toCharArray();
        return IntStream.range(0, chars.length)
                .mapToObj((pos) -> replaceFourth(chars[pos], pos))
                .map(c -> Character.toString(c))
                .collect(Collectors.joining());

    }

    public static char replaceFourth(char c, int position) {
        if ((position+1)%4 == 0) {
            return '_';
        }
        return c;
    }

    public static int countRepeated(String str) {
        if (str.isEmpty()) {
            return 0;
        }
        return Math.toIntExact(str.toLowerCase(Locale.ROOT).chars()
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values()
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(1)
                .collect(Collectors.toList())
                .get(0));
    }
}
