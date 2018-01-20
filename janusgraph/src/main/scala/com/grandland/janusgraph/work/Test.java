package com.grandland.janusgraph.work;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandland.janusgraph.core.LongEncoding;

public class Test {
  public static void main(String[] args) {
    System.out.println(LongEncoding.encode(1305804968));
  }
  
  @SuppressWarnings({ "unchecked", "resource" })
  public static void main1(String[] args) throws Exception {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    BufferedReader reader = null;
    reader = new BufferedReader(new FileReader("C:/Users/Administrator/Desktop/kinship.txt"));
    String tempString = null;
    Long size = 0L;
    while ((tempString = reader.readLine()) != null) {
      Map<String, String> list = null;
      try {
        list = gson.fromJson(tempString, Map.class);
      } catch (Exception e) {
        System.out.println(size + "::" + tempString);
      }
      size++;
    }
  }
}
