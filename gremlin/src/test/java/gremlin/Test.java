package gremlin;

import com.xyshzh.gremlin.client.Connection;
import com.xyshzh.gremlin.execute.Execute;

public class Test {
  public static void main(String[] args) {
    Connection connection = new Connection.ClientConnection("src/resource/gremlin-driver.yaml", false);
    Execute execute = new Execute(connection);
    execute.getAllClass("g.V(3084432).inE().as('r').outV().as('z')", null);
    connection.close();
  }
}
