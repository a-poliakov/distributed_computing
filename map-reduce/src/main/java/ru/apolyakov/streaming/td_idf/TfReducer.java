package ru.apolyakov.streaming.td_idf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

public class TfReducer {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        String lastKey = null;

        Map<Integer, Integer> wordInDocumentsCount = new TreeMap<>();
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("#");
            String word = split[0];
            int docId = Integer.parseInt(split[1].split("\t")[0]);
            int count = Integer.parseInt(split[1].split("\t")[1]);

            if (lastKey != null && !lastKey.equals(word)) {
                for (Map.Entry<Integer, Integer> entry : wordInDocumentsCount.entrySet()) {
                    System.out.println(lastKey + "\t" + entry.getKey() + "\t" + entry.getValue());
                }
                lastKey = word;
                wordInDocumentsCount = new TreeMap<>();
                wordInDocumentsCount.put(docId, count);
            } else {
                wordInDocumentsCount.compute(docId, (key, value) -> value == null ? count : value + count);
                lastKey = word;
            }
        }

        if (lastKey != null) {
            for (Map.Entry<Integer, Integer> entry : wordInDocumentsCount.entrySet()) {
                System.out.println(lastKey + "\t" + entry.getKey() + "\t" + entry.getValue());
            }
        }
    }
}
