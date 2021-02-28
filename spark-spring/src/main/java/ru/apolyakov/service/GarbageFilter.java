package ru.apolyakov.service;

import org.apache.arrow.flatbuf.Bool;
import org.apache.spark.sql.api.java.UDF1;
import org.springframework.stereotype.Component;
import ru.apolyakov.conf.SongsProperties;

@Component
public class GarbageFilter implements UDF1<String, Boolean> {
    private SongsProperties songsProperties;

    public String udfName() {return "notGarbage";}

    @Override
    public Boolean call(String word) throws Exception {
        return !songsProperties.getGarbage().contains(word);
    }
}
