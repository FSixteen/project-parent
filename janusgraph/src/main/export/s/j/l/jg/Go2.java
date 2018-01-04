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
public class Go2 {
  public static String getUid(String uuid) {
    String uid = "";
    if (null != uuid && uuid.length() == 18) {
      uid += uuid.substring(3, 4);
      uid += uuid.substring(4, 5);
      uid += uuid.substring(1, 2);
      uid += uuid.substring(2, 3);
      uid += uuid.substring(5, 6);
      uid += uuid.substring(0, 1);
      uid += uuid.substring(6, 14);
      uid += uuid.substring(17, 18);
      uid += uuid.substring(14, 15);
      uid += uuid.substring(16, 17);
      uid += uuid.substring(15, 16);
    } else if (null != uuid && uuid.length() == 15) {
      uid += uuid.substring(3, 4);
      uid += uuid.substring(4, 5);
      uid += uuid.substring(1, 2);
      uid += uuid.substring(2, 3);
      uid += uuid.substring(5, 6);
      uid += uuid.substring(0, 1);
      uid += uuid.substring(13, 14);
      uid += uuid.substring(6, 13);
      uid += uuid.substring(14, 15);
    } else if (null != uuid && uuid.length() > 5) {
      uid += uuid.substring(1, 2);
      uid += uuid.substring(0, 1);
      uid += uuid.substring(2);
    } else {
      uid = uuid;
    }
    return uid;
  }

  public static void main(String[] args) {
    long offset = Long.valueOf("-1");
    BufferedReader reader = null;
    FileWriter writer = null;
    try {
      reader = new BufferedReader(new FileReader(new File("D:/Program Files/eclipse/other_workSpace/jg/nodes.csv")));
      writer = new FileWriter("D:/Program Files/eclipse/other_workSpace/jg/nodes.csv_");
      String tempString = null;
      long size = 0;
      while ((size < offset) && ((tempString = reader.readLine()) != null)) {
        System.out.println("size:::_" + (++size) + "_");
      }
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 10) {
          JSONObject content = net.sf.json.JSONObject.fromObject(tempString);
          long targetID = content.getLong("id");
          String labels = content.getJSONArray("labels").getString(0);
          String property = content.getString("properties");
          JSONObject properties = net.sf.json.JSONObject.fromObject(property);
          Map<String, Object> map = new HashMap<>();
          Map<String, Object> v = new HashMap<>();
          for (Object key : properties.keySet()) {
            if (labels.equals("Person")) {
              if (((String) key).equals("name")) {
                v.put("name", "Person-" + targetID);
              } else if (((String) key).equals("reg_person")) {
                v.put("reg_person", "Reg_Person-" + targetID);
              } else if (((String) key).equals("uid")) {
                String uuid = properties.getString("uid");
                v.put("uid", getUid(uuid));
              } else {
                v.put((String) key, properties.get((String) key));
              }
            } else if (labels.equals("Department")) {
              if (((String) key).equals("name")) {
                v.put("name", "Department-" + targetID);
              } else if (((String) key).equals("reg_person")) {
                v.put("reg_person", "Reg_Person-" + targetID);
              } else if (((String) key).equals("uid")) {
                String uuid = properties.getString("uid");
                v.put("uid", getUid(uuid));
              } else {
                v.put((String) key, properties.get((String) key));
              }
            } else if (labels.equals("Company")) {
              if (((String) key).equals("name")) {
                v.put("name", "Company-" + targetID);
              } else if (((String) key).equals("reg_person")) {
                v.put("reg_person", "Reg_Person-" + targetID);
              } else if (((String) key).equals("uid")) {
                String uuid = properties.getString("uid");
                v.put("uid", getUid(uuid));
              } else {
                v.put((String) key, properties.get((String) key));
              }
            } else {
              v.put((String) key, properties.get((String) key));
            }
          }
          size++;
          map.put("id", targetID);
          map.put("labels", labels);
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
    } catch (

    IOException e) {
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
