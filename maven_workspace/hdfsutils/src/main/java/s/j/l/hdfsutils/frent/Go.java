package s.j.l.hdfsutils.frent;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Go {
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf);
    job.setJarByClass(Go.class);
    job.setMapperClass(s.j.l.hdfsutils.frent.FRMap.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(LongWritable.class);

    job.setSortComparatorClass(FRSolr.class);
    job.setReducerClass(s.j.l.hdfsutils.frent.FRRed.class);

    FileInputFormat.addInputPath(job, new Path("/haha/tf.txt"));

    Path outpath = new Path("/tmp/t.txt");
    FileSystem fs = FileSystem.get(conf);
    if (fs.exists(outpath)) {
      fs.delete(outpath, true);
    }
    FileOutputFormat.setOutputPath(job, outpath);
    boolean flag = job.waitForCompletion(true);
    if (flag) {
      System.out.println("job success");
    }
  }
}
