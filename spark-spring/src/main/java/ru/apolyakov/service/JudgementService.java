package ru.apolyakov.service;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class JudgementService implements Serializable {
    @Autowired
    private PopularWordsService popularWordsService;

    @Autowired
    private transient JavaSparkContext sc; // SparkHolder.getSc()

    public List<String> topX(String artistName, int x) {
        JavaRDD<String> lines = sc.textFile("data/songs/" + artistName + "/*");
        return popularWordsService.topX(lines, x);
    }
}
