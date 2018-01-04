package s.j.l.jg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
 * Hello world!
 *
 */
public class Go3 {
  public static void main(String[] args) {
    long offset = Long.valueOf("-1");
    BufferedReader reader = null;
    FileWriter writer = null;
    try {
      reader = new BufferedReader(new FileReader(new File("D:/Program Files/eclipse/other_workSpace/jg/relations.csv")));
      writer = new FileWriter("D:/Program Files/eclipse/other_workSpace/jg/relations.csv_");
      String tempString = null;
      long size = 0;
      while ((size < offset) && ((tempString = reader.readLine()) != null)) {
        System.out.println("size:::_" + (++size) + "_");
      }
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 10) {
          JSONObject content = net.sf.json.JSONObject.fromObject(tempString);
          long targetID = content.getLong("id");
          String type = content.getString("type");
          long start = content.getLong("start");
          long end = content.getLong("end");
          String property = content.getString("properties");
          JSONObject properties = net.sf.json.JSONObject.fromObject(property);
          Map<String, Object> map = new HashMap<>();
          Map<String, Object> v = new HashMap<>();
          for (Object key : properties.keySet()) {
            if (((String) key).equals("tips")) {
              v.put("tips", targetID + "的内容");
            } else if (((String) key).equals("person")) {
              v.put("person", targetID + "的person");
            } else {
              v.put((String) key, properties.get((String) key));
            }
          }
          size++;
          map.put("id", targetID);
          map.put("type", type);
          map.put("start", start);
          map.put("end", end);
          map.put("properties", v);
          writer.write(new Gson().toJson(map));
          writer.write("\r\n");
          if (size % 1000 == 0) {
            System.out.println("size:::" + size);
          }
        } else {
          size++;
        }
      }
      System.out.println("size:::" + size);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("I'm File Thread, I'm Over!");
  }
}
