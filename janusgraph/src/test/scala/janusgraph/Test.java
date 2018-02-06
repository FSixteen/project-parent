package janusgraph;

import org.janusgraph.util.encoding.LongEncoding;

public class Test {
  public static void main2(String[] args) {
    String str = "36.0";
    System.out.println(str.substring(0, str.indexOf(".")));
  }

  public static void main1(String[] args) {
    System.out.println(LongEncoding.decode("t33kao"));
    System.out.println(LongEncoding.encode(1305428120L));
  }
}
