package com.xyshzh.dht.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * DHT的报文必须是B编码格式.<br/>
 * string类型:<br/>
 * string类型的编码格式为[length]:[string].以字符串的长度开头,加一个冒号,并以字符串内容结束.<br/>
 * eg:'abc' => '3:abc'<br/>
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-21<br/>
 *
 */
public class BString implements BEncode, Comparable<BString> {

  private final String value;

  public BString(String value) {
    this.value = value;
  }

  public BString(byte[] value) {
    this(value, Charset.DEFAULT);
  }

  public BString(byte[] value, String charsetName) {
    try {
      this.value = new String(value, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int length() {
    return this.value.length();
  }

  @Override
  public int totalLength() {
    return String.valueOf(length()).length() + 1 + length();
  }

  @Override
  public byte[] date() {
    return date(Charset.DEFAULT);
  }

  @Override
  public byte[] date(String charsetName) {
    try {
      return this.value.getBytes(charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] totalDate() {
    return totalDate(Charset.DEFAULT);
  }

  @Override
  public byte[] totalDate(String charsetName) {
    ByteArrayOutputStream c = new ByteArrayOutputStream();
    try {
      c.write(String.valueOf(length()).getBytes(Charset.DEFAULT));
      c.write(58); // c.write((byte) ':');
      c.write(date(charsetName));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return c.toByteArray();
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof BString) {
      BString o = (BString) obj;
      return this.value.equals(o.value);
    } else {
      return false;
    }
  }

  @Override
  public int compareTo(BString o) {
    return this.value.compareTo(o.value);
  }

  /**
   * DHT的报文必须是B编码格式.<br/>
   * string类型:<br/>
   * string类型的编码格式为[length]:[string].以字符串的长度开头,加一个冒号,并以字符串内容结束.<br/>
   * eg:'abc' => '3:abc'<br/>
   */
  @Override
  public BEncode element(String content, int index) {
    char c = content.charAt(index);
    if (c >= '0' && c <= '9') {
      String str = content.substring(index);
      int length = Integer.parseInt(str.substring(0, str.indexOf(":")));
      str = str.substring(str.indexOf(":") + 1);
      return new BString(str.substring(0, length + 1));
    }
    return null;
  }

}
