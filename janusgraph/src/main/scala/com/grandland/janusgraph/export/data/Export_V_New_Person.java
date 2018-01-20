package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandland.janusgraph.core.GraphFactory;

public class Export_V_New_Person {
  @SuppressWarnings({ "unchecked", "resource" })
  public static void main(String[] args) {
    BufferedReader reader = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    try {
//      reader = new BufferedReader(new FileReader("C:/Users/Administrator/Desktop/2018.01.17.person.txt"));
      reader = new BufferedReader(new FileReader(new File(args[0])));
      String tempString = null;
      long size = 0;
      GraphFactory.getInstance().builderConfig();
      JanusGraphTransaction tx = GraphFactory.getInstance().getGraph().newTransaction();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 20) {
          HashMap<String, String> list = null;
          try {
            list = gson.fromJson(tempString, HashMap.class);
          } catch (Exception e) {
            System.out.println(size + ":::" + tempString);
            continue;
          }
          JanusGraphVertex v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, "Person");
          v.property("type", "Person");
          v.property("uid", list.get("uid"));
          String name = list.get("name");
          if (null != name) {
            name = name.trim();
            if (name.startsWith(",")) {
              name = name.substring(1);
            }
            if (name.endsWith(",")) {
              name = name.substring(0, name.length() - 1);
            }
          }else {
            name = "";
          }
          v.property("name", name);
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
          String ptype = list.get("ptype");
          if (null != ptype) {
            ptype = ptype.trim();
            if (ptype.startsWith(",")) {
              ptype = ptype.substring(1);
            }
            if (ptype.endsWith(",")) {
              ptype = ptype.substring(0, ptype.length() - 1);
            }
          }
          if (null != ptype && 0 < ptype.length()) {
            v.property("ptype", list.get("ptype"));
          }
          v.property("tag", 2);
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