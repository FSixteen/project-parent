package s.j.liu.hiveutils;

public class RowKeyFormat extends org.apache.hadoop.hive.ql.exec.UDF {
  public String evaluate(String value) {
    if (null != value) {
      if (value.length() >= 32) {
        return value;
      } else {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 32 - value.length(); i++) {
          sb.append("_");
        }
        return value + sb.toString();
      }
    } else {
      return "________________________________";
    }
  }

  public static void main(String[] args) {
    System.out.println(new RowKeyFormat().evaluate("123"));
    System.out.println(new RowKeyFormat().evaluate("123").length());
  }
}
