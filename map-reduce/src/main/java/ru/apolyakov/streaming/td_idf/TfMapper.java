package ru.apolyakov.streaming.td_idf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.Locale;
import java.util.StringTokenizer;

public class TfMapper {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while((line = reader.readLine()) != null) {
            String[] split = line.split(":", 2);
            int docId = Integer.parseInt(split[0]);

            BreakIterator bi = BreakIterator.getWordInstance(Locale.US);

            // Set the text string to be scanned.
            bi.setText(split[1]);

            int count = 0;
            int lastIndex = bi.first();
            while (lastIndex != BreakIterator.DONE) {
                int firstIndex = lastIndex;
                lastIndex = bi.next();

                if (lastIndex != BreakIterator.DONE
                        && Character.isLetterOrDigit(split[1].charAt(firstIndex))) {
                    String word = split[1].substring(firstIndex, lastIndex);
                    System.out.println(word + "#" + docId + "\t1");
                }
            }
        }
    }
}
