package s.j.l.hdfsutils.frent;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FRRed extends Reducer<Text, LongWritable, Text, LongWritable> {
  @Override
  protected void reduce(Text text, Iterable<LongWritable> iterable, Context context)
      throws IOException, InterruptedException {
    int s = 0;
    for (LongWritable longWritable : iterable) {
      s += longWritable.get();
    }
    context.write(text, new LongWritable(s));
  }
}
