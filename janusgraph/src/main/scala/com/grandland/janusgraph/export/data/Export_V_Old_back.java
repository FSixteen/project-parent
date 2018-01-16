package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
public class Export_V_Old_back {
  @SuppressWarnings({ "resource" })
  public static void main(String[] args) {
    long offset = Long.valueOf(args[1]);
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(new File(args[0])));
      String tempString = null;
      long size = 0;
      GraphFactory.getInstance().builderConfig();
      while ((size < offset) && ((tempString = reader.readLine()) != null)) {
        System.out.println("size:::_" + (++size) + "_");
      }
      JanusGraphTransaction tx = GraphFactory.getInstance().getGraph().newTransaction();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 10) {
          JSONObject content = net.sf.json.JSONObject.fromObject(tempString);
          long targetID = content.getLong("id");
          String labels = content.getJSONArray("labels").getString(0);
          String property = content.getString("properties");
          JSONObject properties = net.sf.json.JSONObject.fromObject(property);

          JanusGraphVertex v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, labels);
          v.property("targetID", targetID);
          v.property("type", labels);
          for (Object key : properties.keySet()) {
            if (labels.trim().equals("Company") && ((String) key).trim().equals("type")) {
              v.property("ctype", properties.get((String) key));
            } else if (labels.trim().equals("Person") && ((String) key).trim().equals("type")) {
              v.property("ptype", properties.get((String) key));
            } else if (labels.trim().equals("Department") && ((String) key).trim().equals("type")) {
              v.property("dtype", properties.get((String) key));
            } else {
              v.property((String) key, properties.get((String) key));
            }
          }
          v.property("updatetime", System.currentTimeMillis());
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
}