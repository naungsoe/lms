package com.hsystems.lms.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.ResultSerialization;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.serializer.WritableSerialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 4/10/16.
 */
public class FileReader {

  FileReader() {

  }

  public static List<Result> read(List<String> fileUris)
      throws IOException {

    List<Result> results = new ArrayList<>();

    for(String fileUri : fileUris) {
      results.addAll(read(fileUri));
    }

    return results;
  }

  public static List<Result> read(String fileUri)
      throws IOException {

    Path path = new Path(fileUri);
    Configuration configuration = new Configuration();
    configuration.set("io.serializations",
        WritableSerialization.class.getName() + ","
            + ResultSerialization.class.getName());

    List<Result> results = new ArrayList<>();
    SequenceFile.Reader reader = null;

    try {
      reader = new SequenceFile.Reader(
          configuration, SequenceFile.Reader.file(path));
      ImmutableBytesWritable key = new ImmutableBytesWritable();

      if (reader.next(key)) {
        Result result = (Result) reader.getCurrentValue(new Result());
        results.add(result);
      }
    } finally {
      IOUtils.closeStream(reader);
    }

    return results;
  }
}
