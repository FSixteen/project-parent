package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;

import com.grandland.janusgraph.core.GraphFactory;

import net.sf.json.JSONObject;

/**
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-16<br/>
 *
 */
public class Export_V_Old {
  private static Boolean c = false;  // 是否处理Company
  private static Boolean d = false;  // 是否处理Department
  private static Boolean p = false;  // 是否处理Person
  
  @SuppressWarnings({ "resource" })
  public static void main(String[] args) {
    FileWriter cwriter = null; // 记录Company的信息
    FileWriter dwriter = null; // 记录Department的信息
    FileWriter pwriter = null; // 记录Person的信息
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(new File(args[0]))); // 执行数据源
      cwriter = new FileWriter(args[0] + "_c");
      dwriter = new FileWriter(args[0] + "_d");
      pwriter = new FileWriter(args[0] + "_p");
      String tempString = null;
      long size = 0;
      GraphFactory.getInstance().builderConfig();
      JanusGraphTransaction tx = GraphFactory.getInstance().getGraph().newTransaction();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 5) {
          JSONObject content = net.sf.json.JSONObject.fromObject(tempString);
          long neo4jid = content.getLong("id");
          String label = content.getJSONArray("labels").getString(0);
          String property = content.getString("properties");
          JSONObject properties = net.sf.json.JSONObject.fromObject(property);
          if (d && "Department".equals(label)) {
            // Old: uid name type  time                                     reg_person money state
            // New: uid name dtype time timestamp state address phone type (regioncode money state)
            JanusGraphVertex v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, label);
            v.property("neo4jid", neo4jid);
            v.property("type", label);
            v.property("tag", 1);
            for (Object key : properties.keySet()) {
              if("uid".equals(((String) key))){
                v.property("uid", properties.get("uid"));
              }else if("name".equals(((String) key))){
                v.property("name", ((String)properties.get("name")).trim().replace(" ", ""));
              }else if("type".equals(((String) key))){
                v.property("dtype", properties.get("type"));
              }else if("time".equals(((String) key))){
                v.property("time", properties.get("time"));
                try {
                  v.property("timestamp", new SimpleDateFormat("yyyy-mm-dd HH:MM:ss").parse((String) properties.get("time")).getTime());
                } catch (Exception e) {
                }
              }else if("reg_person".equals(((String) key))){
                v.property("reg_person", properties.get("reg_person"));
              }else if("money".equals(((String) key))){
                v.property("money", properties.get("money"));
              }else if("state".equals(((String) key))){
                v.property("state", properties.get("state"));
              }else{
                ;
              }
            }
            v.property("updatetime", System.currentTimeMillis());
            dwriter.write(neo4jid + "," + properties.get("name") + "\r\n");
          } else if (c && "Company".equals(label)) {
            // Old: uid name type  reg_person money time           state
            // New: uid name ctype reg_person money time timestamp state scope phone address tag regioncode
            JanusGraphVertex v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, label);
            v.property("neo4jid", neo4jid);
            v.property("type", label);
            v.property("tag", 1);
            for (Object key : properties.keySet()) {
              if("uid".equals(((String) key))){
                v.property("uid", properties.get("uid"));
              }else if("name".equals(((String) key))){
                v.property("name", ((String)properties.get("name")).trim().replace(" ", ""));
              }else if("type".equals(((String) key))){
                v.property("ctype", properties.get("type"));
              }else if("reg_person".equals(((String) key))){
                v.property("reg_person", properties.get("reg_person"));
              }else if("money".equals(((String) key))){
                v.property("money", properties.get("money"));
              }else if("time".equals(((String) key))){
                v.property("time", properties.get("time"));
                try {
                  v.property("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) properties.get("time")).getTime());
                } catch (Exception e) {
                }
              }else if("state".equals(((String) key))){
                v.property("state", properties.get("state"));
              }else{
                ;
              }
            }
            v.property("updatetime", System.currentTimeMillis());
            cwriter.write(neo4jid + "," + properties.get("name") + "\r\n");
          } else if (p && "Person".equals(label)) {
            // Old: uid name born
            // New: uid name birthday address sex phone education cardno ptype
            JanusGraphVertex v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, label);
            v.property("neo4jid", neo4jid);
            v.property("type", label);
            v.property("tag", 1);
            for (Object key : properties.keySet()) {
              if("uid".equals(((String) key))){
                v.property("uid", properties.get("uid"));
                try {
                  String sex = uid2sex((String)properties.get("uid"));
                  if(null != sex){
                    v.property("sex", sex);
                  }
                } catch (Exception e) {
                }
                try {
                  String birthday = uid2birthday((String)properties.get("uid"));
                  if(null != birthday){
                    v.property("birthday", birthday);
                  }
                } catch (Exception e) {
                }
              }else if("name".equals(((String) key))){
                v.property("name", ((String)properties.get("name")).trim().replace(" ", ""));
              }else{
                ;
              }
            }
            v.property("updatetime", System.currentTimeMillis());
            String uid = "";
            String name = "";
            try {
              uid = (String) properties.get("uid");
            } catch (Exception e) {
            }
            try {
              name = (String) properties.get("name");
            } catch (Exception e) {
            }
            if(null != uid || null != name){
              pwriter.write(neo4jid + "," + properties.get("uid") + "," + properties.get("name") + "\r\n");
            }
          } else {
            ;
          }
          size++;
          if (size % 2000 == 0) {
            tx.commit();
            tx = GraphFactory.getInstance().getGraph().newTransaction();
            System.out.println(size);
          }
        } else {
          size++;
        }
      }
      tx.commit();
      tx.close();
      GraphFactory.getInstance().close();
      reader.close();
      cwriter.flush();
      dwriter.flush();
      pwriter.flush();
      cwriter.close();
      dwriter.close();
      pwriter.close();
      System.out.println(size);
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

  private static String uid2sex(String uid) {
    if (18 != uid.length()) {
      return null;
    } else {
      String temp = uid.substring(16, 17);
      return "1".equals(temp) ? "男" : "女";
    }
  }

  private static String uid2birthday(String uid) {
    if (18 != uid.length()) {
      return null;
    } else {
      String temp = uid.substring(6, 14);
      return temp.substring(0, 4) + "-" + temp.substring(4, 6) + "-" + temp.substring(6, 8);
    }
  }
}