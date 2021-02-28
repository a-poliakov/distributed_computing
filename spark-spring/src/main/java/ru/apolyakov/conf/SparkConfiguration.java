package ru.apolyakov.conf;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ru.apolyakov"})
@EnableConfigurationProperties(SongsProperties.class)
public class SparkConfiguration {
    @Bean
    public JavaSparkContext sc() {
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SQLContext sqlContext() {
        return new SQLContext(sc());
    }

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName("music analyst")
                .setMaster("local[*]");
    }
}
