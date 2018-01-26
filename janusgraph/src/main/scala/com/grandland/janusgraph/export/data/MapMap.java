package com.grandland.janusgraph.export.data;

import java.util.HashMap;

public class MapMap extends HashMap<String, Object> implements Comparable<MapMap> {

  @Override
  public int compareTo(MapMap o) {

    Number this_fvid = (Number) this.get("fvid");
    Number this_tvid = (Number) this.get("tvid");
    Number this_money = (Number) this.get("money");
    String this_tips = (String) this.get("tips");
    String this_time = (String) this.get("time");
    String this__id = (String) this.get("_id");

    Number target_fvid = (Number) o.get("fvid");
    Number target_tvid = (Number) o.get("tvid");
    Number target_money = (Number) o.get("money");
    String target_tips = (String) o.get("tips");
    String target_time = (String) o.get("time");
    String target__id = (String) o.get("_id");

    if (this_fvid == target_fvid && this_tvid == target_tvid && this_money == target_money && this_tips.trim().equals(target_tips.trim()) && this_time.trim().equals(target_time.trim())) {
      System.out.println(this__id + ":::" + target__id + "::: same");
      return 0;
    } else {
      System.out.println(this__id + ":::" + target__id + "::: not same");
      return 1;
    }
  }
}
