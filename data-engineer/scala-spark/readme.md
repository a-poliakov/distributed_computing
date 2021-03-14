## Задание

В этом задании предлагается собрать статистику по криминогенной обстановке в разных районах Бостона. В качестве исходных данных используется датасет
https://www.kaggle.com/AnalyzeBoston/crimes-in-boston

С помощью Spark соберите агрегат по районам (поле district) со следующими метриками:

1.**crimes_total** - общее количество преступлений в этом районе
2. **crimes_monthly** - медиана числа преступлений в месяц в этом районе
3. **frequent_crime_types** - три самых частых crime_type за всю историю наблюдений в этом районе, объединенных через запятую с одним пробелом “, ” ,
расположенных в порядке убывания частоты <b>crime_type</b> - первая часть NAME из таблицы offense_codes, разбитого по разделителю “-”
(например, если NAME “BURGLARY - COMMERICAL - ATTEMPT”, то crime_type “BURGLARY”)
4. **lat** - широта координаты района, расчитанная как среднее по всем широтам инцидентов</li>
5. **lng** - долгота координаты района, расчитанная как среднее по всем долготам инцидентов</li>


Программа должна упаковываться в **uber-jar** (с помощью `sbt-assembly`), и запускаться командой
```commandline
spark-submit --master local[*] --class com.example.BostonCrimesMap /path/to/jar {path/to/crime.csv} {path/to/offense_codes.csv} {path/to/output_folder}
```
где {...} - аргументы, передаваемые пользователем.

Результатом её выполнения должен быть один файл в формате .parquet в папке path/to/output_folder.
Для джойна со справочником необходимо использовать broadcast.

## Результат:

```text
+--------+------------+--------------+----------------------------------------------------------------+------------------+------------------+
|district|crimes_total|crimes_monthly|frequent_crime_types                                            |lat               |lng               |
+--------+------------+--------------+----------------------------------------------------------------+------------------+------------------+
|A1      |35717       |904           |[PROPERTY , SICK/INJURED/MEDICAL , WARRANT ARREST]              |38.28264090449283 |-64.22752184110504|
|A15     |6505        |160           |[INVESTIGATE PERSON, SICK/INJURED/MEDICAL , VANDALISM]          |40.006977386336715|-67.10145331325728|
|A7      |13544       |344           |[SICK/INJURED/MEDICAL , INVESTIGATE PERSON, VANDALISM]          |40.73433186115182 |-68.27786643938899|
|B2      |49945       |1298          |[VERBAL DISPUTE, SICK/INJURED/MEDICAL , INVESTIGATE PERSON]     |39.14897551142543 |-65.75622860861748|
|B3      |35442       |907           |[VERBAL DISPUTE, INVESTIGATE PERSON, SICK/INJURED/MEDICAL ]     |40.20600943408275 |-67.58737275067854|
|C11     |42530       |1115          |[SICK/INJURED/MEDICAL , INVESTIGATE PERSON, VERBAL DISPUTE]     |40.64687406755117 |-68.28639197640372|
|C6      |23460       |593           |[SICK/INJURED/MEDICAL , DRUGS , INVESTIGATE PERSON]             |39.14427650574699 |-65.70604425025476|
|D14     |20127       |505           |[TOWED MOTOR VEHICLE, SICK/INJURED/MEDICAL , INVESTIGATE PERSON]|40.22075677010926 |-67.5653264655867 |
|D4      |41915       |1084          |[PROPERTY , INVESTIGATE PERSON, SICK/INJURED/MEDICAL ]          |38.78645706114195 |-65.10991531858863|
|E13     |17536       |445           |[SICK/INJURED/MEDICAL , INVESTIGATE PERSON, DRUGS ]             |40.05389202146484 |-67.30713831538627|
|E18     |17348       |435           |[SICK/INJURED/MEDICAL , INVESTIGATE PERSON, VERBAL DISPUTE]     |40.537872110375424|-68.21644158365613|
|E5      |13239       |337           |[SICK/INJURED/MEDICAL , INVESTIGATE PERSON, PROPERTY ]          |40.8146390043961  |-68.67674691704556|
|N/A     |1765        |41            |[DRUGS , M/V ACCIDENT , M/V ]                                   |21.178304357994307|-36.45758349417001|
+--------+------------+--------------+----------------------------------------------------------------+------------------+------------------+

```
