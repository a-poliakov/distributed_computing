package ru.apolyakov.service;

import lombok.RequiredArgsConstructor;
import org.apache.spark.api.java.JavaRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.apolyakov.WordsUtil;
import ru.apolyakov.conf.SongsProperties;
import scala.Tuple2;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularWordsServiceImpl implements PopularWordsService {
//    @AutowiredBroadcast
//    private Broadcast<SongsProperties> songsProperties;

    @Autowired
    private SongsProperties songsProperties;

    @Override
    public List<String> topX(JavaRDD<String> lines, int x) {
        return lines.map(String::toLowerCase)
                .flatMap(WordsUtil::getWords)
                .filter(word -> !songsProperties.getGarbage().contains(word))
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey(Integer::sum)
                .mapToPair(Tuple2::swap)
                .sortByKey(false)
                .map(Tuple2::_2)
                .take(x);
    }
}
