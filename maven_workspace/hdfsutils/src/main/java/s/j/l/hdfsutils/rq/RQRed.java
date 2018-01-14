package s.j.l.hdfsutils.rq;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class RQRed extends Reducer<Weather, IntWritable, Text, NullWritable> {
  @Override
  protected void reduce(Weather weather, Iterable<IntWritable> iterable, Context context)
      throws IOException, InterruptedException {
    int size = 0;
    for (IntWritable intWritable : iterable) {
      size++;
      if (size > 2) {
        break;
      }
      String m = weather.getYear() + "-" + weather.getMonth() + "-" + weather.getDay() + "-"
          + intWritable.get();
      context.write(new Text(m), NullWritable.get());
    }
  }
}
