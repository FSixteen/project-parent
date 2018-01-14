package s.j.l.cassandrautils;

import java.util.Arrays;
import java.util.List;

public class Test {
  CassandraUtils cu = null;

  public void start() {
    List<String> ips = Arrays.asList("10.103.6.23", "10.103.6.25", "10.103.6.26");
    cu = new CassandraUtils((String[]) ips.toArray());
    cu.getList("select * from yisa_oe.d_20170703 limit 1");
  }
  
  public static void main(String[] args) {
    new Test().start();
  }
}
