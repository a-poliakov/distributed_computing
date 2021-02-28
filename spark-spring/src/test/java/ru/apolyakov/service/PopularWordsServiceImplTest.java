package ru.apolyakov.service;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.apolyakov.conf.SparkConfiguration;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SparkConfiguration.class)
public class PopularWordsServiceImplTest {
    @Autowired
    private JavaSparkContext sc;

    @Autowired
    private PopularWordsService popularWordsService;

    @Test
    public void testTopX() {
        List<String> testData = Arrays.asList("java", "java", "java", "scala", "groovy");
        JavaRDD<String> rdd = sc.parallelize(testData);
        List<String> top1 = popularWordsService.topX(rdd, 1);
        Assert.assertEquals("java", top1.get(0));
    }
}
