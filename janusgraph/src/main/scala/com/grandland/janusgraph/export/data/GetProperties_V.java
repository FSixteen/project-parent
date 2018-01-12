package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
 * 
 * @author root
 *
 */
public class GetProperties_V {
  @SuppressWarnings({ "resource" })
  public static void main(String[] args) {
    long offset = -1L;
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(new File("D:/Program Files/eclipse/other_workSpace/nodes.csv")));
      String tempString = null;
      long size = 0;
      while ((size < offset) && ((tempString = reader.readLine()) != null)) {
        System.out.println("size:::_" + (++size) + "_");
      }
      HashSet<String> cpropertiesss = new HashSet<>();
      HashSet<String> dpropertiesss = new HashSet<>();
      HashSet<String> ppropertiesss = new HashSet<>();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 10) {
          JSONObject content = net.sf.json.JSONObject.fromObject(tempString);
          String property = content.getString("properties");
          String labels = content.getJSONArray("labels").getString(0);
          JSONObject properties = net.sf.json.JSONObject.fromObject(property);
          for (Object key : properties.keySet()) {
            if (labels.trim().equals("C")) {
              cpropertiesss.add((String) key);
            } else if (labels.trim().equals("")) {
              ppropertiesss.add((String) key);
            } else if (labels.trim().equals("")) {
              dpropertiesss.add((String) key);
            } else {
            }
          }
          size++;
          if (size % 2000 == 0) {
            System.out.println(size);
          }
        } else {
          size++;
        }
      }
      System.out.println(new Gson().toJson(cpropertiesss));
      System.out.println(new Gson().toJson(ppropertiesss));
      System.out.println(new Gson().toJson(dpropertiesss));
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