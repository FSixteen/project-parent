package com.xyshzh.dht.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * DHT的报文必须是B编码格式.<br/>
 * int类型:<br/>
 * int类型的编码格式为i[int]e.以i开头,加上数字,以e结尾.<br/>
 * eg:'123' => 'i123e'<br/>
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-21<br/>
 *
 */
public class BInteger implements BEncode, Comparable<BInteger> {
  private final String value;

  public BInteger(Integer value) {
    this.value = String.valueOf(value);
  }

  public BInteger(String value) {
    this.value = value;
  }

  public BInteger(byte[] value) {
    this(value, Charset.DEFAULT);
  }

  public BInteger(byte[] value, String charsetName) {
    try {
      this.value = new String(value, Charset.DEFAULT);
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
    return length() + 2;
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
      c.write(105); // c.write((byte) 'i');
      c.write(date(charsetName));
      c.write(101); // c.write((byte) 'e');
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
    if (obj != null && obj instanceof BInteger) {
      BInteger o = (BInteger) obj;
      return this.value.equals(o.value);
    } else {
      return false;
    }
  }

  @Override
  public int compareTo(BInteger o) {
    return this.value.compareTo(o.value);
  }
}
