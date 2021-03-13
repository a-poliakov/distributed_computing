package ru.apolyakov.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static org.apache.spark.sql.functions.*;

@Service
public class PopularWordsResolverWIthUDF {
    @Autowired
    private GarbageFilter garbageFilter;

    @Autowired
    private SQLContext sqlContext;

    public void registerUdf() {
        sqlContext.udf().register(garbageFilter.udfName(), garbageFilter, DataTypes.BooleanType);
    }

    public Set<String> mostUsedWords(Dataset<Row> lines, int amount) {
        Dataset<Row> sorted = lines.withColumn("words", lower(column("words")))
                .filter(not(column("words").isin()))
                .groupBy(column("words")).agg(count("words").as("count"))
                .sort(column("count").desc());

        sorted.show();
        Row[] rows = (Row[]) sorted.take(amount);
        Set<String> topX = new HashSet<>();
        for (Row row : rows) {
            topX.add(row.getString(0));
        }

        return topX;
    }
}
