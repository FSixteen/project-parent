package janusgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.janusgraph.util.encoding.LongEncoding;

public class Test {
  public static void main(String[] args) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(new File("F:/relations.csv_")));
    FileWriter INVEST_H = new FileWriter("F:/INVEST_H.relations.csv_");
    FileWriter PAYMENT = new FileWriter("F:/PAYMENT.relations.csv_");
    FileWriter INVEST_O = new FileWriter("F:/INVEST_O.relations.csv_");
    FileWriter KINSHIP = new FileWriter("F:/KINSHIP.relations.csv_");
    FileWriter OWN = new FileWriter("F:/OWN.relations.csv_");
    FileWriter Project = new FileWriter("F:/Project.relations.csv_");
    FileWriter SERVE = new FileWriter("F:/SERVE.relations.csv_");
    FileWriter Other = new FileWriter("F:/Other.relations.csv_");
    String tempString = null;
    while ((tempString = reader.readLine()) != null) {
      if (null != tempString) {
        if (tempString.contains("\"type\":\"PAYMENT\""))
          PAYMENT.write(tempString + "\r\n");
        else if (tempString.contains("\"type\":\"INVEST_H\""))
          INVEST_H.write(tempString + "\r\n");
        else if (tempString.contains("\"type\":\"INVEST_O\""))
          INVEST_O.write(tempString + "\r\n");
        else if (tempString.contains("\"type\":\"KINSHIP\""))
          KINSHIP.write(tempString + "\r\n");
        else if (tempString.contains("\"type\":\"OWN\""))
          OWN.write(tempString + "\r\n");
        else if (tempString.contains("\"type\":\"Project\""))
          Project.write(tempString + "\r\n");
        else if (tempString.contains("\"type\":\"SERVE\""))
          SERVE.write(tempString + "\r\n");
        else
          Other.write(tempString + "\r\n");
      }
    }
    INVEST_O.flush();
    INVEST_O.close();
    PAYMENT.flush();
    PAYMENT.close();
    INVEST_H.flush();
    INVEST_H.close();
    KINSHIP.flush();
    KINSHIP.close();
    OWN.flush();
    OWN.close();
    SERVE.flush();
    SERVE.close();
    Project.flush();
    Project.close();
    Other.flush();
    Other.flush();
    reader.close();
  }

  public static void main3(String[] args) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(new File("F:/nodes.csv_")));
    FileWriter Company = new FileWriter("F:/Company.nodes.csv_");
    FileWriter Department = new FileWriter("F:/Department.nodes.csv_");
    FileWriter Person = new FileWriter("F:/Person.nodes.csv_");
    FileWriter Other = new FileWriter("F:/Other.nodes.csv_");
    String tempString = null;
    while ((tempString = reader.readLine()) != null) {
      if (null != tempString) {
        if (tempString.contains("\"labels\":\"Department\""))
          Department.write(tempString + "\r\n");
        else if (tempString.contains("\"labels\":\"Company\""))
          Company.write(tempString + "\r\n");
        else if (tempString.contains("\"labels\":\"Person\""))
          Person.write(tempString + "\r\n");
        else
          Other.write(tempString + "\r\n");
      }
    }
    Other.flush();
    Other.flush();
    Person.flush();
    Person.close();
    Department.flush();
    Department.close();
    Company.flush();
    Company.close();
    reader.close();
  }

  public static void main2(String[] args) {
    String str = "36.0";
    System.out.println(str.substring(0, str.indexOf(".")));
  }

  public static void main1(String[] args) {
    System.out.println(LongEncoding.decode("t33kao"));
    System.out.println(LongEncoding.encode(1305428120L));
  }
}
