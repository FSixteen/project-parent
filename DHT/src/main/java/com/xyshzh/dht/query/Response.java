package com.xyshzh.dht.query;

import java.nio.ByteBuffer;

import com.xyshzh.dht.bencode.BList;
import com.xyshzh.dht.bencode.BMap;

/**
 * 一个KRPC消息由一个独立的字典组成，其中有2个关键字是所有的消息都包含的，其余的附加关键字取决于消息类型。
 * 每一个消息都包含t关键字，它是一个代表了transactionID的字符串类型。transactionID由请求node
 * 产生，并且回复中要包含回显该字段，所以回复可能对应一个节点的多个请求。transactionID应当被编码为一个
 * 短的二进制字符串，比如2个字节，这样就可以对应2^16个请求。另一个每个KRPC消息都包含的关键字是y，它由一
 * 个字节组成，表明这个消息的类型。y对应的值有三种情况：q表示请求，r表示回复，e表示错误。
 * 
 * 回复，对应于KPRC消息字典中的“y”关键字的值是“r”，包含了一个附加的关键字r。关键字“r”是一个字典类型，
 * 包含了返回的值。发送回复消息是在正确解析了请求消息的基础上完成的。
 * 
 * @author Shengjun Liu
 * @version 2018-02-12
 *
 */
public class Response {
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
    request.put("y", "r");// 消息的类型
    request.put("r", new BMap("id", id)); // 请求参数
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
   * @param nodes
   * @return
   */
  public static ByteBuffer find_node(String t, String id, String nodes) {
    BMap request = new BMap();
    request.put("t", t); // transaction ID
    request.put("y", "r");// 消息的类型
    request.put("r", new BMap() {
      private static final long serialVersionUID = 1L;
      {
        this.put("id", id);
        this.put("nodes", nodes);
      }
    }); // 请求参数
    return ByteBuffer.wrap(request.totalDate());
  }

  public static ByteBuffer get_peers(String t, String id, String token, String nodes,
      String... values) {
    BMap request = new BMap();
    request.put("t", t); // transaction ID
    request.put("y", "r");// 消息的类型
    request.put("r", new BMap() {
      private static final long serialVersionUID = 1L;
      {
        this.put("id", id);
        this.put("token", token);
        if (null != nodes && null == values) {
          this.put("nodes", nodes);
        } else if (null == nodes && null != values) {
          this.put("values", new BList(values));
        } else {
          throw new IllegalArgumentException();
        }
      }
    }); // 请求参数
    return ByteBuffer.wrap(request.totalDate());
  }

  public static ByteBuffer announce_peer(String t, String id, String info_hash, int port,
      String token) {
    return null;
  }
}
