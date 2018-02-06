package janusgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.tinkerpop.gremlin.driver.Client;

import com.xyshzh.gremlin.client.Connection;

import net.sf.json.JSONObject;

public class GetPageRank {
  public static void main(String[] args) {
    try {
      final Connection conn = new Connection.ClientConnection("src/main/scala/gremlin-driver.properties", false);
      final Client client = conn.getClient();
      BufferedReader reader = new BufferedReader(new FileReader(new File("C:/Users/Administrator/Desktop/data/xah")));
      String tempString = null;
      long size = 0;
      while ((tempString = reader.readLine()) != null) {
        client.submit(tempString.substring(3));
        if ((++size) % 120 == 0) {
          System.out.println(size);
          Thread.sleep(1000);
        }
      }
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main1(String[] args) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
    FileWriter pageW = new FileWriter(args[0] + "_pageW");
    String tempString = null;
    long size = 0;
    while ((tempString = reader.readLine()) != null) {
      size++;
      if (tempString.length() > 10) {
        JSONObject content = net.sf.json.JSONObject.fromObject(tempString);
        long id = content.getLong("id");
        JSONObject properties = content.getJSONObject("properties");
        if (properties.containsKey("pageRank")) {
          Double pageRank = properties.getDouble("pageRank");
          if (pageRank > 0.0) {
            pageW.write(":> g.V().has('neo4jid','" + id + "').property('pageRank','" + pageRank + "')\r\n");
          }
        }
      }
      if (size % 400 == 0) {
        System.out.println(size);
      }
    }
    reader.close();
    pageW.close();
  }
}
