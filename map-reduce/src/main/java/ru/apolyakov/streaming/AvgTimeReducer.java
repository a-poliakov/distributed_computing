package ru.apolyakov.streaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AvgTimeReducer {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        String lastKey = null;
        int count = 0;
        int sum = 0;
        while((line = reader.readLine()) != null) {
            String[] split = line.split("\t");
            String key = split[0];
            int value = Integer.parseInt(split[1]);

            if (lastKey != null && !lastKey.equals(key)) {
                System.out.println(lastKey + "\t" + sum / count);
                lastKey = key;
                sum = value;
                count = 1;
            } else {
                lastKey = key;
                sum += value;
                count++;
            }
        }

        if (lastKey != null) {
            System.out.println(lastKey + "\t" + sum / count);
        }
    }
}
