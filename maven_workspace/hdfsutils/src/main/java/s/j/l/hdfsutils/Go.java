package s.j.l.hdfsutils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Go {
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    // conf.set("fs.defaultFS", "hdfs://master:8020");
    // conf.set("yarn.resourcemanager.hostname", "slave2");

    Job job = Job.getInstance(conf);

    job.setJarByClass(Go.class);

    job.setMapperClass(WCMap.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    
    job.setCombinerClass(WCRed.class);

    job.setReducerClass(WCRed.class);

    FileInputFormat.addInputPath(job, new Path("/haha/t.txt"));

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
