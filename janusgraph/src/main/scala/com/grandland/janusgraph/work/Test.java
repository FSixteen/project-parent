package com.grandland.janusgraph.work;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandland.janusgraph.core.LongEncoding;

public class Test {
  
  public static void main(String[] args) {
    List<Long> ls = Arrays.asList(123L,153L,1L);
    ls.sort((Long a, Long b)->{
      if(a>b){
        return -1;
      }else if(a<b){
        return 1;
      }else{
        return 0;
      }
    });
    System.out.println(new Gson().toJson(ls));
  }
  
  public static void main2(String[] args) {
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
