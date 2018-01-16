package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandland.janusgraph.core.GraphFactory;

public class Export_Company {
  public static void main(String[] args) {
    BufferedReader reader = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    try {
      reader = new BufferedReader(new FileReader(new File("C:/Users/Administrator/Desktop/company_all.txt")));
      String tempString = null;
      long size = 0;
//      GraphFactory.getInstance().builderConfig();
//      JanusGraphTransaction tx = GraphFactory.getInstance().getGraph().newTransaction();
      HashSet<String> s = new HashSet<>();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 20) {
          Map<String, String> list = gson.fromJson(tempString, HashMap.class);
          String reg_person=     list.get("reg_person"    );
          String uid=            list.get("uid"           );
          String address=        list.get("address"       );
          String regioncode=     list.get("regioncode"    );
          Double money=          Double.valueOf(list.get("money"         ));
          String ctype=          list.get("ctype"         );
          String phone=          list.get("phone"         );
          String scope=          list.get("scope"         );
          String name=           list.get("name"          );
          String time=          list.get("time"           );
          String state=          list.get("state"          );
          Long tag=            Long.valueOf(list.get("tag"            ));
//          JanusGraphVertex v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, "Company");
//          v.property("type", "Person");
//          v.property("uid", list.get("uid"));
//          v.property("name", list.get("name"));

          size++;
          if (size % 200 == 0) {
//            tx.commit();
//            tx = GraphFactory.getInstance().getGraph().newTransaction();
            System.out.println(gson.toJson(s));
            System.out.println(size);
          }
        }
      }
//      tx.commit();
//      tx.close();
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
  
  private static String dealTime(final String time){
//    String 
    return null;
  }
}