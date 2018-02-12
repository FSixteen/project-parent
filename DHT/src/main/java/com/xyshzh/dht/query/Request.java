package com.xyshzh.dht.query;

import java.nio.ByteBuffer;

import com.xyshzh.dht.bencode.BMap;

/**
 * 一个KRPC消息由一个独立的字典组成，其中有2个关键字是所有的消息都包含的，其余的附加关键字取决于消息类型。
 * 每一个消息都包含t关键字，它是一个代表了transactionID的字符串类型。transactionID由请求node
 * 产生，并且回复中要包含回显该字段，所以回复可能对应一个节点的多个请求。transactionID应当被编码为一个
 * 短的二进制字符串，比如2个字节，这样就可以对应2^16个请求。另一个每个KRPC消息都包含的关键字是y，它由一
 * 个字节组成，表明这个消息的类型。y对应的值有三种情况：q表示请求，r表示回复，e表示错误。
 * 
 * 请求，对应于KPRC消息字典中的“y”关键字的值是“q”，它包含2个附加的关键字“q”和“a”。关键字“q”是一个字
 * 符串类型，包含了请求的方法名字。关键字“a”一个字典类型包含了请求所附加的参数。
 * 
 * @author Shengjun Liu
 * @version 2018-02-12
 *
 */
public class Request {
  /**
   * 最基础的请求就是ping。这时KPRC协议中的“q”=“ping”。Ping请求包含一个参数id，它是一个20字节
   * 的字符串包含了发送者网络字节序的nodeID。对应的ping回复也包含一个参数id，包含了回复者的nodeID。
   * 
   * @param t
   * @param id
   * @return
   */
  public static ByteBuffer ping(String t, String id) {
    BMap request = new BMap();
    request.put("t", t); // transaction ID
    request.put("y", "q");// 消息的类型
    request.put("q", "ping"); // 请求的方法名字
    request.put("a", new BMap("id", id)); // 请求参数
    return ByteBuffer.wrap(request.totalDate());
  }

  /**
   * Findnode被用来查找给定ID的node的联系信息。这时KPRC协议中的q=“find_node”。find_node请求包
   * 含2个参数，第一个参数是id，包含了请求node的nodeID。第二个参数是target，包含了请求者正在查找的node
   * 的nodeID。当一个node接收到了find_node的请求，他应该给出对应的回复，回复中包含2个关键字id和nodes，
   * nodes是一个字符串类型，包含了被请求节点的路由表中最接近目标node的K(8)个最接近的nodes的联系信息。
   * 
   * @param t
   * @param id
   * @param target
   * @return
   */
  public static ByteBuffer find_node(String t, String id, String target) {
    BMap request = new BMap();
    request.put("t", t); // transaction ID
    request.put("y", "q");// 消息的类型
    request.put("q", "find_node"); // 请求的方法名字
    request.put("a", new BMap() {
      private static final long serialVersionUID = 1L;
      {
        this.put("id", id);
        this.put("target", target);
      }
    }); // 请求参数
    return ByteBuffer.wrap(request.totalDate());
  }

  /**
   * Getpeers与torrent文件的info_hash有关。这时KPRC协议中的”q”=”get_peers”。get_peers请求
   * 包含2个参数。第一个参数是id，包含了请求node的nodeID。第二个参数是info_hash，它代表torrent文件的
   * infohash。如果被请求的节点有对应info_hash的peers，他将返回一个关键字values,这是一个列表类型的字
   * 符串。每一个字符串包含了"CompactIP-address/portinfo"格式的peers信息。如果被请求的节点没有这个
   * infohash的peers，那么他将返回关键字nodes，这个关键字包含了被请求节点的路由表中离info_hash最近的
   * K个nodes，使用"Compactnodeinfo"格式回复。在这两种情况下，关键字token都将被返回。token关键字在
   * 今后的annouce_peer请求中必须要携带。Token是一个短的二进制字符串。
   * 
   * @param t
   * @param id
   * @param info_hash
   * @return
   */
  public static ByteBuffer get_peers(String t, String id, String info_hash) {
    BMap request = new BMap();
    request.put("t", t); // transaction ID
    request.put("y", "q");// 消息的类型
    request.put("q", "get_peers"); // 请求的方法名字
    request.put("a", new BMap() {
      private static final long serialVersionUID = 1L;
      {
        this.put("id", id);
        this.put("info_hash", info_hash);
      }
    }); // 请求参数
    return ByteBuffer.wrap(request.totalDate());
  }

  /**
   * 这个请求用来表明发出announce_peer请求的node，正在某个端口下载torrent文件。announce_peer包
   * 含4个参数。第一个参数是id，包含了请求node的nodeID；第二个参数是info_hash，包含了torrent文件的
   * infohash；第三个参数是port包含了整型的端口号，表明peer在哪个端口下载；第四个参数数是token，这是在
   * 之前的get_peers请求中收到的回复中包含的。收到announce_peer请求的node必须检查这个token与之前我
   * 们回复给这个节点get_peers的token是否相同。如果相同，那么被请求的节点将记录发送announce_peer节点
   * 的IP和请求中包含的port端口号在peer联系信息中对应的infohash下。
   * 
   * @param t
   * @param id
   * @param info_hash
   * @param port
   * @param token
   * @return
   */
  public static ByteBuffer announce_peer(String t, String id, String info_hash, int port,
      String token) {
    BMap request = new BMap();
    request.put("t", t); // transaction ID
    request.put("y", "q");// 消息的类型
    request.put("q", "announce_peer"); // 请求的方法名字
    request.put("a", new BMap() {
      private static final long serialVersionUID = 1L;
      {
        this.put("id", id);
        this.put("info_hash", info_hash);
        this.put("port", port);
        this.put("token", token);
      }
    }); // 请求参数
    return ByteBuffer.wrap(request.totalDate());
  }
}
