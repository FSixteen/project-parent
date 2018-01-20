package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandland.janusgraph.core.GraphFactory;

public class Export_V_New_Department {
  @SuppressWarnings({ "unchecked", "resource" })
  public static void main(String[] args) {
    BufferedReader reader = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    try {
      reader = new BufferedReader(new FileReader(new File("C:/Users/Administrator/Desktop/dtemp.txt")));
      String tempString = null;
      long size = 0;
      GraphFactory.getInstance().builderConfig();
      JanusGraphTransaction tx = GraphFactory.getInstance().getGraph().newTransaction();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 10) {
          Map<String, String> list = gson.fromJson(tempString, HashMap.class);
          JanusGraphVertex v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, "Department");
          v.property("type", "Department");
          
          try {
            String uid = list.get("uid");
            if (null != uid && !"".equals(uid.trim())) {
              v.property("uid", uid.trim().replace(" ", ""));
            }
          } catch (Exception e) {
          }
          try {
            String name = list.get("name");
            if (null != name && !"".equals(name.trim())) {
              v.property("name", name);
            }
          } catch (Exception e) {
          }
          try {
            String time = list.get("time");
            if (null != time && !"".equals(time.trim())) {
              v.property("time", time);
              try {
                v.property("timestamp", new SimpleDateFormat("yyyy-mm-dd HH:MM:ss").parse(time).getTime());
              } catch (Exception e) {
              }
            }
          } catch (Exception e) {
          }
          
          try {
            String address = list.get("address");
            if (null != address && !"".equals(address.trim())) {
              v.property("address", address.trim().replace(" ", ""));
            }
          } catch (Exception e) {
          }
          try {
            String regioncode = list.get("regioncode");
            if (null != regioncode && !"".equals(regioncode.trim())) {
              v.property("regioncode", regioncode);
            }
          } catch (Exception e) {
          }

          try {
            Double money = Double.valueOf(list.get("money"));
            v.property("money", money);
          } catch (Exception e) {
          }
          try {
            String dtype = list.get("dtype");
            if (null != dtype && !"".equals(dtype.trim())) {
              v.property("dtype", dtype);
            }
          } catch (Exception e) {
          }
          try {
            String phone = list.get("phone");
            if (null != phone && !"".equals(phone.trim())) {
              v.property("phone", phone);
            }
          } catch (Exception e) {
          }
          try {
            String scope = list.get("scope");
            if (null != scope && !"".equals(scope.trim())) {
              v.property("scope", scope);
            }
          } catch (Exception e) {
          }
          try {
            String state = list.get("state");
            if (null != state && !"".equals(state.trim())) {
              v.property("state", state);
            }
          } catch (Exception e) {
          }
          try {
            String neo4jid = list.get("neo4jid");
            if (null != neo4jid && !"".equals(neo4jid.trim())) {
              v.property("neo4jid", neo4jid);
            }
          } catch (Exception e) {
          }
          v.property("tag", 5);
          v.property("updatetime", System.currentTimeMillis());
          
          size++;
          if (size % 400 == 0) {
            tx.commit();
            tx = GraphFactory.getInstance().getGraph().newTransaction();
            System.out.println(size);
          }
        }
      }
      tx.commit();
      tx.close();
      GraphFactory.getInstance().close();
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