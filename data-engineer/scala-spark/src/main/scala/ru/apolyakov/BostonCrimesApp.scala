package ru.apolyakov

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
    <h2>Задание</h2>
    <br/>
    С помощью Spark соберите агрегат по районам (поле district) со следующими метриками:
    <ol>
      <li><b>crimes_total</b> - общее количество преступлений в этом районе</li>
      <li><b>crimes_monthly</b> - медиана числа преступлений в месяц в этом районе</li>
      <li><b>frequent_crime_types</b> - три самых частых crime_type за всю историю наблюдений в этом районе, объединенных через запятую с одним пробелом “, ” ,
          расположенных в порядке убывания частоты <b>crime_type</b> - первая часть NAME из таблицы offense_codes, разбитого по разделителю “-”
          (например, если NAME “BURGLARY - COMMERICAL - ATTEMPT”, то crime_type “BURGLARY”)
      </li>
      <li><b>lat</b> - широта координаты района, расчитанная как среднее по всем широтам инцидентов</li>
      <li><b>lng</b> - долгота координаты района, расчитанная как среднее по всем долготам инцидентов</li>
    </ol>
*/
object BostonCrimesApp extends App {
  val spark = SparkSession.builder()
    .master("yarn")
    //.master("local[*]")
    .appName("CrimeStats")
    .getOrCreate()

  val crimeCsv = args(0)

  val codesCsv = args(1)

  val parquetFilePath = args(2)

  val dfCrime = spark.read.option("header", "true").option("inferSchema", "true").csv(crimeCsv)

  val dfCode = spark.read.option("header", "true").option("inferSchema", "true").csv(codesCsv)

  import spark.implicits._

  val Crimes =
    dfCrime
      .withColumn("district", when($"district".isNull, "N/A").otherwise($"district"))
      .withColumnRenamed("Lat", "Latitude")
      .withColumn("Latitude", when($"Latitude".isNull, 0).otherwise($"Latitude"))
      .withColumn("Long", when($"Long".isNull, 0).otherwise($"Long"))
      .withColumn("Latitude", 'Latitude.cast(DoubleType))
      .withColumn("Long", 'Long.cast(DoubleType))
      .withColumn("offense_code", 'offense_code.cast(DoubleType))

  val Offense_codes =
    dfCode
      .withColumn("code", 'code.cast(DoubleType))
      .withColumn("name",substring_index($"name", "-", 1))

  val offenseCodesBroadcast = broadcast(Offense_codes)

  val window_district = Window.partitionBy("district")
  val mean_percentile = expr("percentile_approx(crimes_in_month, 0.5)")

  /**
   * ><b>crimes_total</b> - общее количество преступлений в этом районе
   * <br/>
   * +--------+------------+-----+<br/>
   * |district|crimes_total|count|<br/>
   * +--------+------------+-----+
   */
  val crimes_total = Crimes
    .withColumn("crimes_total", count("incident_number") over(window_district))
    .groupBy("district", "crimes_total")
    .count()
  crimes_total.show(false)

  /**
   * <b>crimes_monthly</b> - медиана числа преступлений в месяц в этом районе
   * <br/>
   * +----------------+--------------+<br/>
   * |district_monthly|crimes_monthly|<br/>
   * +----------------+--------------+
   */
  val crimes_by_monthly =
    Crimes
      .groupBy("district", "year","month").agg(count($"incident_number").alias("crimes_in_month"))
      // В спарке есть функция percentile_approx, которая может посчитать медиану.
      //.groupBy("district").agg(callUDF("percentile_approx", $"crimes_in_month", lit(0.5)).as("crimes_monthly"))
      .groupBy("district").agg(mean_percentile.alias("crimes_monthly"))
      .withColumnRenamed("district", "district_monthly")

  crimes_by_monthly.show(false)

  /**
   * <b>frequent_crime_types</b> - три самых частых crime_type за всю историю наблюдений в этом районе, объединенных через запятую с одним пробелом “, ” ,
          расположенных в порядке убывания частоты <b>crime_type</b> - первая часть NAME из таблицы offense_codes, разбитого по разделителю “-”
          (например, если NAME “BURGLARY - COMMERICAL - ATTEMPT”, то crime_type “BURGLARY”)
     <br/>
   * +---------------------+----------------------------------------------------------------+<br/>
   * |district_frequent_crime_types|frequent_crime_types                                            |<br/>
   * +---------------------+----------------------------------------------------------------+
   */
  val frequent_crime_types = Crimes
    .join(offenseCodesBroadcast, Crimes("offense_code") === Offense_codes("code"))
    .groupBy("district", "name")
    .agg(count($"name").alias("offense_qty"))
    .withColumn("rn", row_number().over(window_district.orderBy(desc("offense_qty"))))
    .where("rn <= 3")
    .groupBy("district")
    .agg(collect_list("name").alias("frequent_crime_types"))
    .withColumnRenamed("district", "district_frequent_crime_types")
  frequent_crime_types.show(false)

  /**
   * <b>lat</b> - широта координаты района, расчитанная как среднее по всем широтам инцидентов<br/>
   * <b>lng</b> - долгота координаты района, расчитанная как среднее по всем долготам инцидентов<br/>
   * +--------+------------------+------------------+<br/>
   * |district|lat               |lng               |<br/>
   * +--------+------------------+------------------+
   */
  val latLng = Crimes
    .withColumn("lat", avg("Latitude") over(window_district))
    .withColumn("lng", avg("Long") over(window_district))
    .groupBy("district", "lat", "lng").count()
    .withColumnRenamed("district", "district_lat_lng")

  latLng.show(false)


  /**
   * +--------+------------+--------------+----------------------------------------------------------------+------------------+------------------+<br/>
   * |district|crimes_total|crimes_monthly|frequent_crime_types                                            |lat               |lng               |<br/>
   * +--------+------------+--------------+----------------------------------------------------------------+------------------+------------------+
   */
  crimes_total
    .join(crimes_by_monthly, crimes_total("district") === crimes_by_monthly("district_monthly"),"left_outer")
    .join(frequent_crime_types, crimes_total("district") === frequent_crime_types("district_frequent_crime_types"),"left_outer")
    .join(latLng, crimes_total("district") === latLng("district_lat_lng"),"left_outer")
    .select("district", "crimes_total", "crimes_monthly", "frequent_crime_types", "lat", "lng")
    .orderBy("district")
    .repartition(1)
    .write.format("parquet").mode(SaveMode.Overwrite).save(parquetFilePath)

  spark.read.parquet(parquetFilePath).show(false)

  spark.stop()
}
