package com.xyshzh.dht.bencode;

/**
 * DHT的报文必须是B编码格式.<br/>
 * BEncoding是BitTorrent用在传输数据结构的编码方式,我们最熟悉的'种子'文件,它里面的元数据就是BEncoding过的字典表.<br/>
 * 虽然比用纯二进制编码效率低,但由于结构简单而且不受字节存储顺序影响,这对于跨平台性非常重要,而且具有较好的灵活性,<br/>
 * 即使存在故障的字典键,只要将其忽略并更换新的就能兼容补充.<br/>
 * 这种编码方式支持四种类型的数据:string,int,Dictionary<string,object>,List<object>,各自的编码规则如下:<br/>
 * 1.string类型:<br/>
 * string类型的编码格式为[length]:[string].以字符串的长度开头,加一个冒号,并以字符串内容结束.<br/>
 * eg:'abc' => '3:abc'<br/>
 * 2.int类型:<br/>
 * int类型的编码格式为i[int]e.以i开头,加上数字,以e结尾.<br/>
 * eg:'123' => 'i123e'<br/>
 * 3.List<object>类型:<br/>
 * List<object>类型的编码格式为l[object]e.以l开头,加上列表中各个元素的编码(元素的类型同样为BEncoding支持的类型),以e结尾.<br/>
 * eg:'abc,123' => 'l3:abci123ee'<br/>
 * 4.Dictionary<string,object>类型:<br/>
 * Dictionary<string,object>类型的编码格式为d[Key-Value]e.以d开头,加上字典中每个键值对的编码,以e结尾.<br/>
 * eg:'{name:Shengjun_Liu,from:Shandong}'=>'d4:name12:Shengjun_Liu4:from8:Shandonge'<br/>
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-21<br/>
 *
 */
public interface BEncode {

  /**
   * 获取数据长度.
   * 
   * @return
   */
  int length();

  /**
   * 获取总数据长度.
   * 
   * @return
   */
  int totalLength();

  /**
   * 获取数据.
   * 
   * @return
   */
  byte[] date();

  /**
   * 获取数据.
   * 
   * @return
   */
  byte[] date(String charsetName);

  /**
   * 获取总数据.
   * 
   * @return
   */
  byte[] totalDate();

  /**
   * 获取总数据.
   * 
   * @return
   */
  byte[] totalDate(String charsetName);

  /**
   * 生成对象.
   * 
   * @param content
   * @param index
   * @return
   */
  BEncode element(String content, int index);
}
