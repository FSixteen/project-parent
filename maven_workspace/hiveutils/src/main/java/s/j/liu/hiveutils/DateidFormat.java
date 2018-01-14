package s.j.liu.hiveutils;

public class DateidFormat extends org.apache.hadoop.hive.ql.exec.UDF {
  public int evaluate(String value) {
    try {
      return Integer.valueOf(value.substring(0, 8));
    } catch (Exception e) {
      return 0;
    }
  }

  public int evaluate(int value) {
    try {
      return evaluate(String.valueOf(value));
    } catch (Exception e) {
      return 0;
    }
  }

  public int evaluate(long value) {
    try {
      return evaluate(String.valueOf(value));
    } catch (Exception e) {
      return 0;
    }
  }

  public int evaluate(Object value) {
    try {
      return evaluate(String.valueOf(value));
    } catch (Exception e) {
      return 0;
    }
  }

  public static void main(String[] args) {
    System.out.println(new DateidFormat().evaluate("10000002 00:00:00"));
  }
}
