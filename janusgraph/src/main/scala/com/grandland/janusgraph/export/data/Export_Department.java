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

public class Export_Department {
  @SuppressWarnings({ "unchecked", "resource" })
  public static void main(String[] args) {
    BufferedReader reader = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    try {
      reader = new BufferedReader(new FileReader(new File("C:/Users/Administrator/Desktop/Department_All.json")));
      String tempString = null;
      long size = 0;
      GraphFactory.getInstance().builderConfig();
      JanusGraphTransaction tx = GraphFactory.getInstance().getGraph().newTransaction();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 10) {
          Map<String, Object> list = gson.fromJson(tempString, HashMap.class);
          JanusGraphVertex v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, "Department");
          v.property("type", "Department");
          v.property("region", list.get("region"));
          v.property("uid", list.get("uid"));
          v.property("uid_short", list.get("uid_short"));
          v.property("name", list.get("name"));
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
}