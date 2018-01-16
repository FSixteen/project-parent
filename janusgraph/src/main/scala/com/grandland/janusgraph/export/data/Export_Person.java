package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandland.janusgraph.core.GraphFactory;

public class Export_Person {
  @SuppressWarnings({ "unchecked", "resource" })
  public static void main(String[] args) {
    BufferedReader reader = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    try {
      reader = new BufferedReader(new FileReader(new File(args[0])));
      String tempString = null;
      long size = 0;
      GraphFactory.getInstance().builderConfig();
      JanusGraphTransaction tx = GraphFactory.getInstance().getGraph().newTransaction();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 20) {
          Map<String, String> list = gson.fromJson(tempString, HashMap.class);
          JanusGraphVertex v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, "Person");
          v.property("type", "Person");
          v.property("uid", list.get("uid"));
          v.property("name", list.get("name"));
          String birthday = uid2birthday(list.get("uid"));
          if (null != birthday && 10 == birthday.trim().length()) {
            v.property("birthday", birthday);
          }
          if (null != list.get("address") && 0 < list.get("address").trim().length()) {
            v.property("address", list.get("address"));
          }
          String sex = uid2sex(list.get("uid"));
          if (null != sex && 0 < sex.trim().length()) {
            v.property("sex", sex);
          }
          if (null != list.get("phone") && 0 < list.get("phone").trim().length()) {
            v.property("phone", list.get("phone"));
          }
          if (null != list.get("education") && 0 < list.get("education").trim().length()) {
            v.property("education", list.get("education"));
          }
          if (null != list.get("cardno") && 0 < list.get("cardno").trim().length()) {
            v.property("cardno", list.get("cardno"));
          }
          if (null != list.get("ptype") && 0 < list.get("ptype").trim().length()) {
            v.property("ptype", list.get("ptype"));
          }
          v.property("updatetime", System.currentTimeMillis());
          size++;
          if (size % 200 == 0) {
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