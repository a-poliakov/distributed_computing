package ru.apolyakov.streaming.td_idf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IdfMapper {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("\t");
            String word = split[0];
            int docId = Integer.parseInt(split[1]);
            int count = Integer.parseInt(split[2]);
            System.out.printf("%s\t%s;%s;1%n", word, docId, count);
        }
    }
}
