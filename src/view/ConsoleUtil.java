package view;

public class ConsoleUtil {

    /**
     * Prints a visual separator line to the console.
     * Used to frame room titles and section breaks.
     */
    public static void printSeparator() {
        System.out.println("============================================================");
    }

    /**
     * Wraps text to fit console width (80 characters per line).
     * Preserves word boundaries and doesn't break words mid-character.
     *
     * @param text The text to wrap
     * @return The wrapped text with newline characters
     */
    public static String wrapText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        final int lineWidth = 80;
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            // If adding this word would exceed the line width, start a new line
            if (currentLine.length() > 0 && currentLine.length() + word.length() + 1 > lineWidth) {
                result.append(currentLine.toString().trim()).append("\n");
                currentLine = new StringBuilder();
            }

            // Add word to current line
            if (currentLine.length() > 0) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }

        // Add the last line if it has content
        if (currentLine.length() > 0) {
            result.append(currentLine.toString().trim());
        }

        return result.toString();
    }
}
