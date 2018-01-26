package com.xyshzh.connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gremlin Connection Client Util.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-02<br/>
 * @since JDK1.8
 */
public final class JanusGraphConnection {

  private final Logger log = LoggerFactory.getLogger(JanusGraphConnection.class);

  private Cluster.Builder builder;
  private Integer clientMinSize = 10; // Client集合最小数量
  private Integer clientMaxSize = clientMinSize * 4; // Client集合最大数量
  private Integer clientInitBatchSize = clientMinSize; // 批次大小,本次初始化Client数量
  private Cluster cluster;
  private Vector<Client> clients = new Vector<Client>();

  public JanusGraphConnection(String filePath) {
    this(new File(JanusGraphConnection.class.getResource(filePath).getPath()));
  }

  public JanusGraphConnection(File configurationFile) {
    try {
      builder = Cluster.build(configurationFile);
      cluster = builder.create();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取Gremlin Client.
   * 
   * @return Gremlin客户端.
   */
  public Client getClient() {
    if (1 < clients.size()) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          getClients();
        }
      }).start();
      return clients.remove(0);
    } else {
      synchronized (this.getClass()) {
        if (1 < clients.size()) {
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
  public void releaseClient(Client client) {
    if (null == client || client.isClosing()) {
      return;
    } else if (clientMaxSize < clients.size() && !client.isClosing()) {
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
  private synchronized void getClients() {
    synchronized (this.getClass()) {
      if (clientMinSize > clients.size()) {
        for (int i = clientInitBatchSize; i > 0; i--) {
          clients.addElement(cluster.connect());
        }
      }
    }
  }

}