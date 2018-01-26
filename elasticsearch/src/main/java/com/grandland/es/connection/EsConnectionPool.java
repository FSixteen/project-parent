package com.grandland.es.connection;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.grandland.utils.Utils;

/**
 * ElasticSearch Transport Client Util.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-08<br/>
 *
 */
public class EsConnectionPool {

  private Map<String, ?> config = null;
  private Builder builder = Settings.builder();
  private Settings settings = null;
  private Integer client_min_size = 5; // Client集合最小数量
  private Integer client_max_size = client_min_size * 4; // Client集合最大数量
  private Integer client_init_batch_size = client_min_size; // 批次大小,本次初始化Client数量
  private Vector<TransportClient> clients = new Vector<TransportClient>();

  public EsConnectionPool(Map<String, ?> config) {
    if (null == config || config.isEmpty())
      throw new RuntimeException(this.getClass().getName() + " [config] is null or empty");
    this.config = config;
    for (final Entry<String, ?> entry : this.config.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

    }
  }

  public static void main(String[] args) {
    new EsConnectionPool(null);
  }

  /**
   * 获取Transport Client.
   * 
   * @return TransportClient客户端.
   */
  public TransportClient getClient() {
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
  public void releaseClient(TransportClient client) {
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
  private synchronized void getClients() {
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
  private synchronized List<TransportAddress> getAllAddress() {
    List<TransportAddress> addressList = new ArrayList<TransportAddress>();
    for (String ip : "".split(",")) {
      if (!Utils.isStandardOfIPV4(ip)) {
        continue;
      } else {
        addressList.add(new TransportAddress(new InetSocketAddress(ip, 9300)));
      }
    }
    return addressList;
  }

}
