package com.grandland.janusgraph.work;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Test {
  public static void main(String[] args) {
    try {
      System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-01-16 11:30:42").getTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
