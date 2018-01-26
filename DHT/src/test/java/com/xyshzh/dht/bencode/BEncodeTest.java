package com.xyshzh.dht.bencode;

import org.junit.Test;

public class BEncodeTest {
  @Test
  public void test() {
    BList list = new BList();
    list.add(new BInteger(123));
    list.add(new BString("haha"));
    assert new String(list.totalDate()).equals("li123e4:hahae");

    BMap map = new BMap();
    map.put(new BString("a"), new BString("b"));
    assert new String(map.totalDate()).equals("d1:a1:be");
  }
}
