package ru.apolyakov.streaming.td_idf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class IdfReducer {
    public static class Pair<K, V> {
        K key;
        V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return key.equals(pair.key) &&
                    value.equals(pair.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    public static class WordStatistics {
        Pair<String, Integer> wordDockIdKey;
        Map<Integer, Integer> tfAndDocsCountMap = new TreeMap<>();
        int docsCount;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        String lastKey = null;
        WordStatistics lastStat = new WordStatistics();
        while((line = reader.readLine()) != null) {
            String[] split = line.split("\t");
            String word = split[0];
            int docId = Integer.parseInt(split[1].split(";")[0]);
            int tf = Integer.parseInt(split[1].split(";")[1]);
            int docsCount = Integer.parseInt(split[1].split(";")[2]);

            if (lastKey != null && !lastKey.equals(word)) {
                for (Map.Entry<Integer, Integer> entry: lastStat.tfAndDocsCountMap.entrySet()) {
                    System.out.printf("%s#%s\t%s\t%s%n", lastKey,
                            entry.getKey(),
                            entry.getValue(), lastStat.docsCount);
                }

                lastKey = word;
                lastStat = new WordStatistics();
                lastStat.wordDockIdKey = new Pair<>(word, docId);
                lastStat.tfAndDocsCountMap.put(docId, tf);
                lastStat.docsCount = docsCount;
            } else {
                lastKey = word;
                lastStat.tfAndDocsCountMap.put(docId, tf);
                lastStat.docsCount += docsCount;
            }
        }

        if (lastKey != null) {
            for (Map.Entry<Integer, Integer> entry: lastStat.tfAndDocsCountMap.entrySet()) {
                System.out.printf("%s#%s\t%s\t%s%n", lastKey,
                        entry.getKey(),
                        entry.getValue(), lastStat.docsCount);
            }
        }
    }
}
