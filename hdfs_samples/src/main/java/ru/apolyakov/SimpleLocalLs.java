package ru.apolyakov;

import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;

public class SimpleLocalLs {
    public static void main(String[] args) throws IOException {
        Path path =  new Path("/");
        if (args.length == 1) {
            path =  new Path(args[0]);
        }
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        FileStatus[] files = fs.listStatus(path);

        for (FileStatus file: files) {
            System.out.println(file.getPath().getName());
        }

        String text = "Hello world in HDFS!\n";
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(text.getBytes()));
        Path file = new Path("file:/C/text.txt");

        FSDataOutputStream fsDataOutputStream = fs.create(file);
        IOUtils.copyBytes(inputStream, fsDataOutputStream, conf);
    }

    public void ls(String pathToDir) throws IOException {
        Path path = new Path(pathToDir);
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        FileStatus[] files = fs.listStatus(path);

        for (FileStatus file: files) {
            System.out.println(file.getPath().getName());
        }
    }

    public void cp(String pathFrom, String pathTo) throws IOException {
        Path source_path = new Path(pathFrom);
        Path target_path = new Path(pathTo);

        Configuration conf = new Configuration();
        FileSystem source_fs = FileSystem.get(source_path.toUri(), conf);
        FileSystem target_fs = FileSystem.get(target_path.toUri(), conf);

        FSDataOutputStream output = target_fs.create(target_path);
        InputStream input = null;
        try{
            input = source_fs.open(source_path);
            IOUtils.copyBytes(input, output, conf);
        } finally {
            IOUtils.closeStream(input);
            IOUtils.closeStream(output);
        }
    }
}
