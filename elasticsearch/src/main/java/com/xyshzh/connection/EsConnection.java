package com.xyshzh.connection;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.xyshzh.utils.Utils;

/**
 * ElasticSearch Transport Client Util.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-08<br/>
 *
 */
public class EsConnection {

  private static String ips = "127.0.0.1";
  private static Integer port = 9300;
  private static Builder builder = Settings.builder();
  private static Settings settings = null;
  private static Integer client_min_size = 5; // Client集合最小数量
  private static Integer client_max_size = client_min_size * 4; // Client集合最大数量
  private static Integer client_init_batch_size = client_min_size; // 批次大小,本次初始化Client数量
  private static Vector<TransportClient> clients = new Vector<TransportClient>();

  static {
    init();
  }

  public static void init() {
    try {
      builder.put("cluster.name", "es-app");
      builder.put("client.transport.sniff", true);
      settings = builder.build();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取Transport Client.
   * 
   * @return TransportClient客户端.
   */
  public static TransportClient getClient() {
    if (0 < clients.size()) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          getClients();
        }
      }).start();
      return clients.remove(0);
    } else {
      synchronized (String.class) {
        if (0 < clients.size()) {
          return clients.remove(0);
        } else {
          getClients();
          return clients.remove(0);
        }
      }
    }
  }

  /**
   * 释放Transport Client.
   * 
   * @param client
   *          TransportClient客户端.
   */
  public static void releaseClient(TransportClient client) {
    if (null == client) {
      return;
    } else if (client_max_size < clients.size()) {
      client.close();
    } else {
      clients.addElement(client);
    }
  }

  /**
   * 初始化Client集合.
   * 
   * @return TransportClient集合
   */
  @SuppressWarnings("resource")
  private static synchronized void getClients() {
    synchronized (String.class) {
      if (client_min_size > clients.size()) {
        for (int i = client_init_batch_size; i > 0; i--) {
          List<TransportAddress> addressList = getAllAddress();
          clients.addElement(new PreBuiltTransportClient(settings).addTransportAddresses(addressList.toArray(new TransportAddress[addressList.size()])));
        }
      }
    }
  }

  /**
   * 获得所有的地址.
   *
   * @return TransportAddress集合
   */
  private static synchronized List<TransportAddress> getAllAddress() {
    List<TransportAddress> addressList = new ArrayList<TransportAddress>();
    for (String ip : ips.split(",")) {
      if (!Utils.isStandardOfIPV4(ip)) {
        continue;
      } else {
        addressList.add(new TransportAddress(new InetSocketAddress(ip, port)));
      }
    }
    return addressList;
  }

}
