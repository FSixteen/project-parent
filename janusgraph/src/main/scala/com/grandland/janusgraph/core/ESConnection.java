package com.grandland.janusgraph.core;

import java.net.InetSocketAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-08<br/>
 * @since JDK1.8
 */
public final class ESConnection {
  private static final Logger log = LoggerFactory.getLogger(ESConnection.class);

  private static Builder builder = null;
  private static Settings settings = null;
  private static TransportClient client = null;

  static {
    init();

  }

  public static void init() {
    try {
      builder = Settings.builder();
      builder.put("cluster.name", "my-es");
      builder.put("client.transport.sniff", true);
      settings = builder.build();
      client = new PreBuiltTransportClient(settings);
      for (String ip : "20.28.30.22,20.28.30.23,20.28.30.26".split(",")) {
        client.addTransportAddress(new TransportAddress(new InetSocketAddress(ip, 9300)));
      }
    } catch (Exception e) {
      log.error("Elasticsearch 初始化失败!", e);
    }
  }

  public static TransportClient getClient() {
    if (null == client) {
      init();
    }
    return client;
  }
}