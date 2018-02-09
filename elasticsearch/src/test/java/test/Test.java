package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.xyshzh.elasticsearch.client.Connection;
import com.xyshzh.elasticsearch.execute.Execute;

public class Test {
  public static void main(String[] args) {
    Map<String, Object> config = new HashMap<>();
    config.put("hosts", "20.28.30.22,20.28.30.23,20.28.30.26,20.28.30.27,20.28.30.28,20.28.30.29");
    config.put("port", 9300);
    config.put("cluster.name", "my-es");
    config.put("client.transport.sniff", true);
    Execute execute = new Execute(new Connection.ClientConnection(config));

    List<Map<String, Object>> list = new ArrayList<>();
    execute.filter(list, "janusgraph_all_gltype2", "a", 0, 10, new String[] { "id", "uid", "name", "money", "time", "timestamp" }, null, null, null);
    System.out.println(list.size());
    System.out.println(new Gson().toJson(list));
  }
}
