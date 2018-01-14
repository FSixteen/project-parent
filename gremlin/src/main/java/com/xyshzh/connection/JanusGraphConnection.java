package com.xyshzh.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper.Builder;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyshzh.utils.Utils;

/**
 * Gremlin Connection Client Util.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-02<br/>
 * @since JDK1.8
 */
public final class JanusGraphConnection {

  private static final Logger log = LoggerFactory.getLogger(JanusGraphConnection.class);

  private static Builder mapper = GryoMapper.build().addRegistry(JanusGraphIoRegistry.getInstance());
  private static Cluster.Builder builder = Cluster.build().serializer(new GryoMessageSerializerV1d0(mapper));
  private static String ips = "127.0.0.1";
  private static Integer port = 8182;
  private static Integer client_min_size = 5; // Client集合最小数量
  private static Integer client_max_size = client_min_size * 4; // Client集合最大数量
  private static Integer client_init_batch_size = client_min_size; // 批次大小,本次初始化Client数量
  private static Cluster cluster;
  private static Vector<Client> clients = new Vector<Client>();

  /**
   * 初始化Gremlin配置信息并初始化Gremlin连接.
   */
  static {
    init();
  }

  /**
   * 初始化Gremlin配置信息并初始化Gremlin连接.
   */
  public static void init() {
    try {
      List<String> addressList = getAllAddress();
      builder.addContactPoints(addressList.toArray(new String[addressList.size()]));
      builder.port(port);
      builder.maxConnectionPoolSize(20);
      builder.reconnectInterval(20);
      builder.resultIterationBatchSize(10240);
      cluster = builder.create();
    } catch (Exception e) {
      log.error("Gremlin Connection 初始化失败!", e);
    }
  }

  /**
   * 获取Gremlin Client.
   * 
   * @return Gremlin客户端.
   */
  public static Client getClient() {
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
   * 释放Gremlin Client.
   * 
   * @param client
   *          Gremlin客户端.
   */
  public static void releaseClient(Client client) {
    if (null == client) {
      return;
    } else if (client_max_size < clients.size() && !client.isClosing()) {
      client.close();
    } else {
      clients.addElement(client);
    }
  }

  /**
   * 初始化Gremlin Client集合.
   * 
   * @return Gremlin Client集合
   */
  private static synchronized void getClients() {
    synchronized (String.class) {
      if (client_min_size > clients.size()) {
        for (int i = client_init_batch_size; i > 0; i--) {
          clients.addElement(cluster.connect());
        }
      }
    }
  }

  /**
   * 获得所有的IP地址集合.
   *
   * @return IP集合
   */
  private static synchronized List<String> getAllAddress() {
    List<String> addressList = new ArrayList<String>();
    for (String ip : ips.split(",")) {
      if (!Utils.isStandardOfIPV4(ip)) {
        continue;
      } else {
        addressList.add(ip);
      }
    }
    return addressList;
  }

}