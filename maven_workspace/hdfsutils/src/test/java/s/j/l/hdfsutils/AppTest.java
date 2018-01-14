package s.j.l.hdfsutils;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
  FileSystem fs = null;

  @Before
  public void init() throws IOException {
    Configuration conf = new Configuration();
    fs = FileSystem.get(conf);
  }

  @Test
  public void main() throws IOException {
    Path path = new Path("/haha");
    fs.mkdirs(path);
  }

  @After
  public void end() throws IOException {
    fs.close();
  }
}
