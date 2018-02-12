package com.xyshzh.dht.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.xyshzh.dht.BencodeInteger;
import com.xyshzh.dht.BencodeList;
import com.xyshzh.dht.BencodeMap;
import com.xyshzh.dht.BencodeString;
import com.xyshzh.dht.BencodeType;

/**
 * DHT的报文必须是B编码格式.<br/>
 * Dictionary<string,object>类型:<br/>
 * Dictionary<string,object>类型的编码格式为d[Key-Value]e.以d开头,加上字典中每个键值对的编码,以e结尾.<br/>
 * eg:'{name:Shengjun_Liu,from:Shandong}'=>'d4:name12:Shengjun_Liu4:from8:Shandonge'<br/>
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-21<br/>
 *
 */
public class BMap extends HashMap<BEncode, BEncode> implements BEncode {
  private static final long serialVersionUID = -1516513658151651365L;

  public BMap() {
  }

  public BMap(String key, String value) {
    this.put(new BString(key), new BString(value));
  }

  public BEncode put(String key, String value) {
    return super.put(new BString(key), new BString(value));
  }

  public BEncode put(String key, int value) {
    return super.put(new BString(key), new BInteger(value));
  }

  public BEncode put(String key, BEncode value) {
    return super.put(new BString(key), value);
  }

  @Override
  public int length() {
    int length = 0;
    for (Map.Entry<BEncode, BEncode> entry : entrySet()) {
      length += entry.getKey().totalLength();
      length += entry.getValue().totalLength();
    }
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
    this.forEach((BEncode _1, BEncode _2) -> {
      try {
        c.write(_1.totalDate(charsetName));
        c.write(_2.totalDate(charsetName));
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
      c.write(100); // c.write((byte) 'd');
      c.write(date(charsetName));
      c.write(101); // c.write((byte) 'e');
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return c.toByteArray();
  }
  
  
  
  public static BMap getMap(String source, int index) {
    char c = source.charAt(index++);
    if(c == 'd') {
        BMap result = new BMap();
        BencodeString key = null;
        for(;;) {
          // 如果获取到任意一种类型的B编码数据
            BencodeType element;
            if(null != (element = BencodeString.getString(source, index)) || 
             null != (element = BencodeInteger.getInt(source, index)) || 
             null != (element = BencodeList.getList(source, index)) || 
             null != (element = BencodeMap.getMap(source, index))) {
              
              // key不为null, 则获取到的是value, 否则获取到的是key
                if(null != key) {
                    result.put(key, element);
                    key = null;
                } else {
                    key = (BencodeString) element;
                }
                index += element.getTotalLength();
                continue;
            }
            
            // 如果无任何类型数据可获取, 就是达到了字典类型的结尾, 如果结尾不是e则是非法的B编码格式
            if(source.charAt(index) == 'e') {
                break;
            } else {
              System.out.println(source);
              return null;
            }
        }
        return result;
    }
    return null;
}
}
