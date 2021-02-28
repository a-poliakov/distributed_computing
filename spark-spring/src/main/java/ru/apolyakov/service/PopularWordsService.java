package ru.apolyakov.service;

import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;
import java.util.List;

public interface PopularWordsService extends Serializable {
    List<String> topX(JavaRDD<String> lines, int x);
}
