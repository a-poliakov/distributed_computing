package ru.apolyakov.streaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AvgTimeCombiner {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        String lastKey = null;
        int count = 0;
        int sum = 0;
        while((line = reader.readLine()) != null) {
            String[] split = line.split("\t");
            String key = split[0];
            int spentTime = Integer.parseInt(split[1].split(";")[0]);
            int cnt = Integer.parseInt(split[1].split(";")[1]);

            if (lastKey != null && !lastKey.equals(key)) {
                System.out.println(lastKey + "\t" + sum + ";" + count);
                lastKey = key;
                sum = spentTime;
                count = cnt;
            } else {
                lastKey = key;
                sum += spentTime;
                count += cnt;
            }
        }

        if (lastKey != null) {
            System.out.println(lastKey + "\t" + sum + ";" + count);
        }
    }
}
