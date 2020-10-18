package ru.apolyakov.streaming.count_words;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class CountWordsInMapperCombiningV1 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        reader.readLine();
        while((line = reader.readLine()) != null) {
            Map<String, Integer> associativeArray = new TreeMap<>();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                associativeArray.compute(tokenizer.nextToken(), (key, value) -> {
                    if (value == null) {
                        return 1;
                    }
                    return value + 1;
                });
            }
            associativeArray.keySet().stream()
                    .forEach(key -> System.out.println(key + "\t" + associativeArray.get(key)));
        }
    }
}
