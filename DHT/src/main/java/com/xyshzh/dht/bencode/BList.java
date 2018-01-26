package com.xyshzh.dht.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;

/**
 * DHT的报文必须是B编码格式.<br/>
 * List<object>类型:<br/>
 * List<object>类型的编码格式为l[object]e.以l开头,加上列表中各个元素的编码(元素的类型同样为BEncoding支持的类型),以e结尾.<br/>
 * eg:'abc,123' => 'l3:abci123ee'<br/>
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-21<br/>
 *
 */
public class BList extends HashSet<BEncode> implements BEncode {
  private static final long serialVersionUID = -1516513658151651365L;

  @Override
  public int length() {
    int length = this.parallelStream().mapToInt((BEncode b) -> b.totalLength()).sum();
    return length;
  }

  @Override
  public int totalLength() {
    return length() + 2;
  }

  @Override
  public byte[] date() {
    return date(Charset.DEFAULT);
  }

  @Override
  public byte[] date(String charsetName) {
    ByteArrayOutputStream c = new ByteArrayOutputStream();
    this.forEach((BEncode b) -> {
      try {
        c.write(b.totalDate(charsetName));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    return c.toByteArray();
  }

  @Override
  public byte[] totalDate() {
    return totalDate(Charset.DEFAULT);
  }

  @Override
  public byte[] totalDate(String charsetName) {
    ByteArrayOutputStream c = new ByteArrayOutputStream();
    try {
      c.write(108); // c.write((byte) 'l');
      c.write(date(charsetName));
      c.write(101); // c.write((byte) 'e');
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return c.toByteArray();
  }
}
