package gremlin;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Result;

import com.xyshzh.connection.JanusGraphConnection;

public class Test {
  public static void main(String[] args) {
    JanusGraphConnection c = new JanusGraphConnection("src/resource/gremlin-driver.properties");
    Client client = c.getClient();
    List<Result> result = null;
    try {
      result = client.submit("g.V().limit(1)").all().get();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ExecutionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println(result.size());
  }
}
