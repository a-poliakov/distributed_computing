package ru.apolyakov.streaming;

import java.io.*;
import java.util.StringTokenizer;

public class CountMapStreaming {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                System.out.println(tokenizer.nextToken() + "\t1");
            }
        }
    }
}
