package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;

import com.google.gson.Gson;
import com.grandland.janusgraph.core.GraphFactory;

import net.sf.json.JSONObject;

/**
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-16<br/>
 *
 */
public class Export_R_Old {
  public static void main(String[] args) {
    BufferedReader reader = null;
    FileWriter writer, PAYMENT, Project, INVEST_H, INVEST_O, OWN, KINSHIP, SERVE = null;
    try {
      reader = new BufferedReader(new FileReader(new File(args[0])));
      writer = new FileWriter(args[0] + "_");
      PAYMENT = new FileWriter(args[0] + "_PAYMENT");
      Project = new FileWriter(args[0] + "_Project");
      INVEST_H = new FileWriter(args[0] + "_INVEST_H");
      INVEST_O = new FileWriter(args[0] + "_INVEST_O");
      OWN = new FileWriter(args[0] + "_OWN");
      KINSHIP = new FileWriter(args[0] + "_KINSHIP");
      SERVE = new FileWriter(args[0] + "_SERVE");
      String tempString = null;
      long size = 0;
      GraphFactory.getInstance().builderConfig();
      JanusGraph graph = GraphFactory.getInstance().getGraph();
      GraphTraversalSource g = graph.traversal();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 10) {
          JSONObject content = net.sf.json.JSONObject.fromObject(tempString);
          long neo4jid = content.getLong("id");
          String type = content.getString("type").trim();
          if (type.equals("ACTED_IN") || type.equals("DIRECTED") || type.equals("PRODUCED") || type.equals("FOLLOWS") || type.equals("REVIEWED") || type.equals("WROTE")) {
            size++;
            continue;
          }
          long start = content.getLong("start");
          long end = content.getLong("end");
          String property = content.getString("properties");
          JSONObject properties = net.sf.json.JSONObject.fromObject(property);
          GraphTraversal<Vertex, Vertex> starts = g.V().has("neo4jid", start);
          GraphTraversal<Vertex, Vertex> endVs = g.V().has("neo4jid", end);
          if (starts.hasNext() && endVs.hasNext()) {
            Vertex startV = starts.next();
            Vertex endV = endVs.next();
            if("PAYMENT".equals(type)){
              // Old: time           value tips
              // New: time timestamp money tips
              Edge e = startV.addEdge(type, endV);
              e.property("type", type);
              e.property("fvid", startV.id());
              e.property("tvid", endV.id());
              e.property("updatetime", System.currentTimeMillis());
              for (Object key : properties.keySet()) {
                if("time".equals((String) key)){
                  e.property("time", properties.get("time"));
                  try {
                    e.property("timestamp", new SimpleDateFormat("yyyy-mm-dd HH:MM:ss").parse((String) properties.get("time")).getTime());
                  } catch (Exception e2) {
                  }
                }else if("value".equals((String) key)){
                  e.property("money", properties.get("value"));
                }else if("tips".equals((String) key)){
                  e.property("tips", properties.get("tips"));
                }else{
                  ;
                }
              }
              PAYMENT.write(startV.id() + "," + endV.id() + "\r\n");
            }else if("Project".equals(type)){
              // Old: uid time_stamp name time value 
              // New: uid timestamp  name time money
              Edge e = startV.addEdge(type, endV);
              e.property("type", type);
              e.property("fvid", startV.id());
              e.property("tvid", endV.id());
              e.property("updatetime", System.currentTimeMillis());
              for (Object key : properties.keySet()) {
                if("uid".equals((String) key)){
                  e.property("uid", properties.get("uid"));
                }else if("name".equals((String) key)){
                  e.property("name", properties.get("name"));
                }else if("time".equals((String) key)){
                  e.property("time", properties.get("time"));
                  try {
                    e.property("timestamp", new SimpleDateFormat("yyyy-mm-dd HH:MM:ss").parse((String) properties.get("time")).getTime());
                  } catch (Exception e2) {
                  }
                }else if("value".equals((String) key)){
                  e.property("money", properties.get("value"));
                }else if("tips".equals((String) key)){
                  e.property("tips", properties.get("tips"));
                }else{
                  ;
                }
              }
              Project.write(startV.id() + "," + endV.id() + "\r\n");
            }else if("INVEST_H".equals(type)){
              // Old: real_amount role subscribed_amount percent 
              // New: real_amount role subscribed_amount percent
              Edge e = startV.addEdge(type, endV);
              e.property("type", type);
              e.property("fvid", startV.id());
              e.property("tvid", endV.id());
              e.property("updatetime", System.currentTimeMillis());
              for (Object key : properties.keySet()) {
                if("real_amount".equals((String) key)){
                  e.property("real_amount", properties.get("real_amount"));
                }else if("role".equals((String) key)){
                  e.property("role", properties.get("role"));
                }else if("subscribed_amount".equals((String) key)){
                  e.property("subscribed_amount", properties.get("subscribed_amount"));
                }else if("percent".equals((String) key)){
                  try {
                    e.property("percent", properties.get("percent"));                    
                  } catch (Exception e2) {
                  }
                }else if("tips".equals((String) key)){
                  e.property("tips", properties.get("tips"));
                }else{
                  ;
                }
              }
              INVEST_H.write(startV.id() + "," + endV.id() + "\r\n");
            }else if("INVEST_O".equals(type)){
              // Old: real_amount role subscribed_amount percent 
              // New: real_amount role subscribed_amount percent
              Edge e = startV.addEdge(type, endV);
              e.property("type", type);
              e.property("fvid", startV.id());
              e.property("tvid", endV.id());
              e.property("updatetime", System.currentTimeMillis());
              for (Object key : properties.keySet()) {
                if("real_amount".equals((String) key)){
                  e.property("real_amount", properties.get("real_amount"));
                }else if("role".equals((String) key)){
                  e.property("role", properties.get("role"));
                }else if("subscribed_amount".equals((String) key)){
                  e.property("subscribed_amount", properties.get("subscribed_amount"));
                }else if("percent".equals((String) key)){
                  try {
                    e.property("percent", properties.get("percent"));                    
                  } catch (Exception e2) {
                  }
                }else if("tips".equals((String) key)){
                  e.property("tips", properties.get("tips"));
                }else{
                  ;
                }
              }
              INVEST_O.write(startV.id() + "," + endV.id() + "\r\n");
            }else if("OWN".equals(type)){
              // Old: role
              // New: role
              Edge e = startV.addEdge(type, endV);
              e.property("type", type);
              e.property("fvid", startV.id());
              e.property("tvid", endV.id());
              e.property("updatetime", System.currentTimeMillis());
              for (Object key : properties.keySet()) {
                if("role".equals((String) key)){
                  e.property("role", properties.get("role"));
                }else{
                  ;
                }
              }
              OWN.write(startV.id() + "," + endV.id() + "\r\n");
            }else if("KINSHIP".equals(type)){
              // Old: date            status 
              // New: time timestamp  status   registname  registid
              Edge e = startV.addEdge(type, endV);
              e.property("type", type);
              e.property("fvid", startV.id());
              e.property("tvid", endV.id());
              e.property("updatetime", System.currentTimeMillis());
              for (Object key : properties.keySet()) {
                if("date".equals((String) key)){
                  try {
                    e.property("time", new SimpleDateFormat("yyyy-mm-dd HH:MM:ss").format(new SimpleDateFormat("yyyymmdd").parse((String) properties.get("date"))));
                  } catch (ParseException e1) {
                    e.property("time",  properties.get("date"));
                  }
                  try {
                    e.property("timestamp", new SimpleDateFormat("yyyymmdd").parse((String) properties.get("date")).getTime());
                  } catch (Exception e2) {
                  }
                }else if("status".equals((String) key)){
                  e.property("status", properties.get("status"));
                }else if("tips".equals((String) key)){
                  e.property("tips", properties.get("tips"));
                }else{
                  ;
                }
              }
              KINSHIP.write(startV.id() + "," + endV.id() + "\r\n");
            }else if("SERVE".equals(type)){
              // Old: role duty membership 
              // New: role duty membership category jointime jointimestamp
              Edge e = startV.addEdge(type, endV);
              e.property("type", type);
              e.property("fvid", startV.id());
              e.property("tvid", endV.id());
              e.property("updatetime", System.currentTimeMillis());
              for (Object key : properties.keySet()) {
                if("role".equals((String) key)){
                  e.property("role", properties.get("role"));
                }else if("duty".equals((String) key)){
                  e.property("duty", properties.get("duty"));
                }else if("membership".equals((String) key)){
                  e.property("membership", properties.get("membership"));
                }else{
                  ;
                }
              }
              SERVE.write(startV.id() + "," + endV.id() + "\r\n");
            }else{
              ;
            }
          } else {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> v = new HashMap<>();
            for (Object key : properties.keySet()) {
              v.put((String) key, properties.get((String) key));
            }
            map.put("id", neo4jid);
            map.put("type", type);
            map.put("start", start);
            map.put("end", end);
            map.put("properties", v);
            writer.write(new Gson().toJson(map));
            writer.write("\r\n");
          }
          size++;
          if (size % 200 == 0) {
            g.tx().commit();
            g.tx().open();
            System.out.println(size);
            writer.flush();
          }
        } else {
          size++;
        }
      }
      g.tx().commit();
      g.tx().open();
      writer.flush();
      PAYMENT.flush();
      Project.flush();
      INVEST_H.flush();
      INVEST_O.flush();
      OWN.flush();
      KINSHIP.flush();
      SERVE.flush();
      PAYMENT.close();
      Project.close();
      INVEST_H.close();
      INVEST_O.close();
      OWN.close();
      KINSHIP.close();
      SERVE.close();
      writer.close();
      reader.close();
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