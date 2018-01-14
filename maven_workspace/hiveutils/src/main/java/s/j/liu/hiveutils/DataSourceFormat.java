package s.j.liu.hiveutils;

public class DataSourceFormat extends org.apache.hadoop.hive.ql.exec.UDF {
  public int evaluate(int value) {
    return ((value == 0) ? 1 : 0);
  }
  
  public int evaluate(String value) {
    return 0;
  }

  public static void main(String[] args) {
    System.out.println(new DataSourceFormat().evaluate(0));
  }
}
