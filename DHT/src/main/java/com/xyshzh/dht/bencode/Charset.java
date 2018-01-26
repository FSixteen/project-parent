package com.xyshzh.dht.bencode;

/**
 * 常见的编码格式.<br/>
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-21<br/>
 *
 */
public interface Charset {
  String US_ASCII = "US-ASCII";
  String ISO646_US = "ISO646-US";
  String ISO_8859_1 = "ISO-8859-1";
  String ISO_LATIN_1 = "ISO-LATIN-1";
  String UTF_8 = "UTF-8";
  String UTF_16BE = "UTF-16BE";
  String UTF_16LE = "UTF-16LE";
  String UTF_16 = "UTF-16";
  String GB2312 = "GB2312";
  String GB18030 = "GB18030";
  String GBK = "GBK";
  String DEFAULT = UTF_8;
}
