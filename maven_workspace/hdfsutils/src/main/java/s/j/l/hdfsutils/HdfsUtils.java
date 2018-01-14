package s.j.l.hdfsutils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HdfsUtils {
  FileSystem fs = null;
  Configuration conf = null;

  @Before
  public void start() throws IOException {
    conf = new Configuration();
    fs = FileSystem.get(conf);
  }

  @After
  public void end() throws IOException {
    fs.close();
  }

  @Test
  public void mkdirs() throws IOException {
    fs.mkdirs(new Path("/haha"));
  }

  @Test
  public void upload() throws IllegalArgumentException, IOException {
    FSDataOutputStream fos = fs.create(new Path("/haha/test.txt"));
    FileUtils.copyFile(new File("E://1.mp3"), fos);
  }

  @Test
  public void download() throws IllegalArgumentException, IOException {
    Path src = new Path("/haha/test.txt");
    Path dst = new Path("E://test.mp3");
    fs.copyToLocalFile(src, dst);
  }

  @Test
  public void ls() throws IOException {
    FileStatus[] s = fs.listStatus(new Path("/haha"));
    for (int i = 0; i < s.length; i++) {
      FileStatus fileStatus = s[i];
      System.out.println(fileStatus.getPath());
      System.out.println(fileStatus.getLen());
    }
  }

  @Test
  public void upload1() throws IllegalArgumentException, IOException {
    @SuppressWarnings("deprecation")
    SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf,
        new Path("/haha/SequenceFile.txt"), Text.class, Text.class);
    File file = new File("F://新建文本文档.txt");
    writer.append(new Text("1"), new Text(FileUtils.readFileToString(file)));
    writer.append(new Text("2"), new Text(FileUtils.readFileToString(file)));
  }

  @Test
  public void download1() throws IllegalArgumentException, IOException {
    @SuppressWarnings("deprecation")
    SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path("/haha/SequenceFile.txt"),
        conf);
    Text key = new Text();
    Text value = new Text();
    while (reader.next(key, value)) {
      System.out.println(key);
      System.out.println(value);
      System.out.println("____________");
    }
  }
}
