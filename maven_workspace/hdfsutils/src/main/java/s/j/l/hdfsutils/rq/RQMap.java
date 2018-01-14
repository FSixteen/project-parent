package s.j.l.hdfsutils.rq;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RQMap extends Mapper<LongWritable, Text, Weather, IntWritable> {
  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    String[] list = value.toString().split("\t");
    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar c = Calendar.getInstance();
    try {
      c.setTime(s.parse(list[0].trim()));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Weather w = new Weather();
    w.setYear(c.get(Calendar.YEAR));
    w.setMonth(c.get(Calendar.MONTH) + 1);
    w.setDay(c.get(Calendar.DAY_OF_MONTH));
    w.setW(Integer.valueOf(list[1]));
    context.write(w, new IntWritable(w.getW()));
  }
}
