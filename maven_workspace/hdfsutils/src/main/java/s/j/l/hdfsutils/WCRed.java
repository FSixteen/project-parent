package s.j.l.hdfsutils;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WCRed extends Reducer<Text, IntWritable, Text, IntWritable> {
  @Override
  protected void reduce(Text text, Iterable<IntWritable> iterable, Context context)
      throws IOException, InterruptedException {
    int size = 0;
    for (IntWritable longWritable : iterable) {
      size = size + longWritable.get();
    }
    context.write(text, new IntWritable(size));
  }
}
