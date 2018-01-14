package s.j.l.hdfsutils.frent;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FRMap extends Mapper<LongWritable, Text, Text, LongWritable> {
  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    String[] list = value.toString().split(" ");
    for (int i = 1; i < list.length; i++) {
      for (int j = i + 1; j < list.length; j++) {
        context.write(new Text(list[i] + "-" + list[j]), new LongWritable(1));
        context.write(new Text(list[j] + "-" + list[i]), new LongWritable(1));
      }
    }
  }
}
