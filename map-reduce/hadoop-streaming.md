## Hadoop Streaming

Пример запуска:

```shell script
$HADOOP_HOME/bin/hadoop  jar $HADOOP_HOME/hadoop-streaming.jar \
    -Dmapreduce.job.name="Word python" \
    -files countMap.py,countReduce.py \
    -input /data/hello.txt \
    -output /data/result5 \
    -mapper /bin/countMap.py \
    -combiner /bin/countReduce.py \
    -reducer /bin/countReduce.py
```

Получилось запустить задачу в VM от Cloudera, некоторые подводные камни, о которые я спотыкался:

1. $HADOOP_HOME не задана в системе, hadoop-streaming.jar нашелся по адресу /usr/lib/hadoop-mapreduce/hadoop-streaming.jar
2. На mapred.job.name слегка ругается, просит заменить на современный вариант mapreduce.job.name
3. Отказывается запускаться с флагом -files ... ругается на unexpected arguments on the command line, пришлось удалить, но вроде бы проблем не возникло (зачем мы передаем countMap.py и countReduce.py здесь?)
4. Файл text.txt должен лежать в HDFS (это обязательно?)
5. /tmp/wordCount/ не должно существовать (это HDFS, а можно попросить сразу на локальный диск положить?)

В итоге:

1. По мере выполнения команды выводит в консоль прогресс в формате map x% reduce y% ﻿Сначала проходит map до 100, потом reduce как обещали = )
2. После завершения выводится целая пачка различных Counter со статистикой
3. В папке /tmp/wordCount/ имеем два файла: _SUCCESS и part-00000 (я правильно понимаю, что для большой задачи файлов part-* может быть много? 
зачем нужен _SUCCESS? просто флаг успешного завершения задачи?)

## Ресурсы

+ [Hadoop Streaming Docs](https://hadoop.apache.org/docs/r1.2.1/streaming.html)