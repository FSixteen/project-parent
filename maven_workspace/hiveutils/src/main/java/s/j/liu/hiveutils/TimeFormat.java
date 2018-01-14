package s.j.liu.hiveutils;

public class TimeFormat extends org.apache.hadoop.hive.ql.exec.UDF {
  public String evaluate(String value) {
    try {
      return value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8) + " "
          + value.substring(8, 10) + ":" + value.substring(10, 12) + ":" + value.substring(12, 14);
    } catch (Exception e) {
      return "0000-00-00 00:00:00";
    }
  }

  public String evaluate(int value) {
    try {
      return evaluate(String.valueOf(value));
    } catch (Exception e) {
      return "0000-00-00 00:00:00";
    }
  }

  public String evaluate(long value) {
    try {
      return evaluate(String.valueOf(value));
    } catch (Exception e) {
      return "0000-00-00 00:00:00";
    }
  }

  public String evaluate(Object value) {
    try {
      return evaluate(String.valueOf(value));
    } catch (Exception e) {
      return "0000-00-00 00:00:00";
    }
  }
}
