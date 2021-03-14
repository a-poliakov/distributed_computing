## Развернуть дистрибутив Cloudera

**Цель:** 

Цель этого ДЗ - научиться выполнять базовые операции на кластере Hadoop. 
В его ходе нужно будет развернуть свой мини-кластер в Google Cloud Platform и создать таблицу в Hive.

### Инструкция:

1. Создадим новую виртуальную машину со следующими параметрами (обратите внимание на размер диска!)

2. Самый простой способ подключения к созданной машине - просто открыть консоль в браузере. Для более удобного подключения рекомендуется использовать gcloud

3. Установим Docker:
   
```commandline
curl -sSL https://get.docker.com/ | sh
```

4. Скачаем докер-образ Cloudera QuickstartVM:
```commandline
sudo wget https://downloads.cloudera.com/demo_vm/docker/cloudera-quickstart-vm-5.13.0-0-beta-docker.tar.gz
tar xzf cloudera-quickstart-vm-*-docker.tar.gz
rm -f cloudera-quickstart-vm-5.13.0-0-beta-docker.tar.gz
sudo docker import cloudera-quickstart-vm-5.13.0-0-beta-docker/cloudera-quickstart-vm-5.13.0-0-beta-docker.tar cloudera-quickstart:latest
```

5. Запустим наш докер-контейнер с Cloudera на борту:
```commandline
sudo docker run --hostname=quickstart.cloudera --privileged=true -t -i -p 8888:8888 -p 7180:7180 -p 80:80 cloudera-quickstart:latest /usr/bin/docker-quickstart
```
6. После всех этих манипуляций у вас должна запуститься командная строка внутри контейнера

7. (Уже внутри контейнера) /home/cloudera/cloudera-manager --express --force

8. Настройте firewall, чтобы иметь доступ к веб-интерфейсам

### Создание таблицы в Hive

1. Скачайте датасет
https://storage.googleapis.com/otus_sample_data/athlete.snappy.parquet

2. Через HUE загрузите файл в папку /user/cloudera/athlete
   
    2.1 В навигационном меню выберите “Files”
   
    2.2 Создайте папку 
   
    2.3 Загрузите файл, нажав Upload
   
    2.4 Перейдите в “Editor > Hive” и выполните запрос

```hiveql
CREATE EXTERNAL TABLE athlete (
    ID INT,
    Name STRING,
    Sex STRING,
    Age INT,
    Height INT,
    Weight INT,
    Team STRING,
    NOC STRING,
    Games STRING,
    `Year` INT,
    Season STRING,
    City STRING,
    Sport STRING,
    Event STRING,
    Medal STRING
)
STORED AS PARQUET
LOCATION '/user/cloudera/athlete'
```


### Полезная информация
Немного полезных команд по докеру

`docker ps` - выдаст список запущенных контейнеров

`docker ps -a` - выдаст список всех контейнеров (включая остановленные)

`docker exec -it container_id bash` - запустит консоль внутри контейнера container_id (container_id - хэш, который идет в первом столбце вывода команды ps)

**Не забудьте остановить виртуальную машину после использования!**
