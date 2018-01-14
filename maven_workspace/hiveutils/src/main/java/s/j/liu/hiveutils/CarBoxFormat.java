package s.j.liu.hiveutils;

public class CarBoxFormat extends org.apache.hadoop.hive.ql.exec.UDF {
  public String evaluate(String value) {
    if (null != value) {
      value = value.replace("\"\"", "\"");
    } else {
      return null;
    }
    if (value.startsWith("\"") && value.endsWith("\"")) {
      value = value.substring(1, value.length() - 1);
    } else {
      if (value.startsWith("\"")) {
        value = value.substring(1);
      }
      if (value.endsWith("\"")) {
        value = value.substring(0, value.length() - 1);
      }
    }
    try {
      if (value.startsWith("{")) {
        return value;
      } else {
        String[] carBox = value.split(",");
        return "{\"y\":" + carBox[1] + ",\"x\":" + carBox[0] + ",\"w\":" + carBox[2] + ",\"h\":"
            + carBox[3] + "}";
      }
    } catch (Exception e2) {
      return null;
    }
  }

  public String evaluate(int value) {
    return null;
  }
}
