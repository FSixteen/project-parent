package s.j.l.hdfsutils.rq;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

public class RQPartition extends HashPartitioner<Weather, IntWritable> {
  @Override
  public int getPartition(Weather key, IntWritable value, int numReduceTasks) {
    return (key.getYear() - 1949) % numReduceTasks;
    // return super.getPartition(key, value, numReduceTasks);
  }
}
