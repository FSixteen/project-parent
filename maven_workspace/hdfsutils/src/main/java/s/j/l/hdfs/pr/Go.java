package s.j.l.hdfs.pr;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Go {
  public static int haha = 20;

  public static enum MyCounter {
    my
  }

  public static void main(String[] args) throws Exception {
    int i = 0;
    while (true) {
      i++;
      Configuration conf = new Configuration();
      conf.setInt("runCount", i);
      Job job = Job.getInstance(conf);
      job.setJobName("pr" + i);
      job.setJarByClass(Go.class);
      job.setMapperClass(PRMapper.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(Text.class);
      job.setInputFormatClass(KeyValueTextInputFormat.class);
      job.setReducerClass(PRReducer.class);

      if (i == 1) {
        FileInputFormat.addInputPath(job, new Path("/haha/pr.txt"));
      } else {
        FileInputFormat.addInputPath(job, new Path("/tmp/pr" + (i - 1)));
      }

      Path outpath = new Path("/tmp/pr" + i);
      FileSystem fs = FileSystem.get(conf);
      if (fs.exists(outpath)) {
        fs.delete(outpath, true);
      }
      FileOutputFormat.setOutputPath(job, outpath);
      boolean flag = job.waitForCompletion(true);
      if (flag) {
        System.out.println("job success");
        long sum = job.getCounters().findCounter(MyCounter.my).getValue();
        if ((sum / 4000.0) < 0.04) {
          break;
        }
      }
      if (i == haha) {
        break;
      }
    }
  }

  static class PRMapper extends Mapper<Text, Text, Text, Text> {
    @Override
    protected void map(Text key, Text value, Context context)
        throws IOException, InterruptedException {
      int runCount = context.getConfiguration().getInt("runCount", 1);
      String page = key.toString();
      Node node = null;
      if (runCount == 1) {
        node = Node.fromMR("1.0" + "\t" + value.toString());
      } else {
        node = Node.fromMR(value.toString());
      }
      context.write(new Text(page), new Text(node.toString()));
      if (node.containsAdjacentNodes()) {
        double outValue = node.getPageRank() / node.getAdjacentNodeNames().length;
        for (int i = 0; i < node.getAdjacentNodeNames().length; i++) {
          String outPage = node.getAdjacentNodeNames()[i];
          context.write(new Text(outPage), new Text(outValue + ""));
        }
      }
    }
  }

  static class PRReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text text, Iterable<Text> iterable, Context context)
        throws IOException, InterruptedException {
      int runCount = context.getConfiguration().getInt("runCount", 1);
      double sum = 0.0;
      Node sourceNode = null;
      for (Text i : iterable) {
        Node node = Node.fromMR(i.toString());
        if (node.containsAdjacentNodes()) {
          sourceNode = node;
        } else {
          sum = sum + node.getPageRank();
        }
        if (runCount == haha) {
          context.write(text, new Text(node.toString()));
        }
      }
      if (runCount == haha) {
        context.write(new Text(text.toString() + ":pr:"), new Text(sum + ""));
      }
      double newPR = (0.15 / 4.0) + (0.85 * sum);
      System.out.println("***************************new pageRank value is " + newPR);
      double d = newPR - sourceNode.getPageRank();
      int j = (int) (d * 1000.0);
      j = Math.abs(j);
      System.out.println("____________j = " + j);
      context.getCounter(MyCounter.my).increment(j);
      sourceNode.setPageRank(newPR);
      context.write(text, new Text(sourceNode.toString()));
    }
  }
}

class Node extends WritableComparator {
  private double pageRank = 1.0;
  private String[] adjacentNodeNames;
  public static final char fieldSeparator = '\t';

  public double getPageRank() {
    return pageRank;
  }

  public boolean containsAdjacentNodes() {
    return (adjacentNodeNames != null && adjacentNodeNames.length > 0);
  }

  public Node setPageRank(double pageRank) {
    this.pageRank = pageRank;
    return this;
  }

  public String[] getAdjacentNodeNames() {
    return adjacentNodeNames;
  }

  public Node setAdjacentNodeNames(String[] adjacentNodeNames) {
    this.adjacentNodeNames = adjacentNodeNames;
    return this;
  }

  public static Node fromMR(String string) {
    String[] p = string.split("\t");
    Node node = new Node().setPageRank(Double.valueOf(p[0]));
    if (p.length > 1) {
      node.setAdjacentNodeNames(Arrays.copyOfRange(p, 1, p.length));
    }
    return node;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(pageRank + "");
    if (getAdjacentNodeNames() != null) {
      for (int i = 0; i < adjacentNodeNames.length; i++) {
        sb.append(fieldSeparator).append(adjacentNodeNames[i]);
      }
    }
    return sb.toString();
  }

}
