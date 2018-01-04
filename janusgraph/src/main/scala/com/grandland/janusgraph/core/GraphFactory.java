package com.grandland.janusgraph.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;

public final class GraphFactory implements Serializable {
  private static final long serialVersionUID = 1L;
  private JanusGraphFactory.Builder config = null;
  private JanusGraph graph = null;
  private GraphTraversalSource g = null;
  private JanusGraphTransaction tx = null;
  private static GraphFactory graphFactory = null;

  private GraphFactory() {
  }

  public static GraphFactory getInstance() {
    if (null == graphFactory) {
      graphFactory = new GraphFactory();
    }
    return graphFactory;
  }

  public JanusGraphFactory.Builder getConfig() {
    return config;
  }

  public JanusGraph getGraph() {
    return graph;
  }

  public GraphTraversalSource getG() {
    return g;
  }

  public JanusGraphTransaction getTx() {
    return tx;
  }

  public HashMap<JanusGraph, GraphTraversalSource> getGraphAndG() {
    return new HashMap<JanusGraph, GraphTraversalSource>() {
      private static final long serialVersionUID = 1L;
      {
        put(graph, g);
      }
    };
  }

  public void close() {
    if (null != tx) {
      tx.close();
    }
    if (null != g) {
      try {
        g.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (null != graph) {
      graph.close();
    }
  }

  private GraphFactory initialization() {
    graph = config.open();
    g = graph.traversal();
    tx = graph.newTransaction();
    return graphFactory;
  }

  public GraphFactory builderConfig() {
    if (null == this.config) {
      return builderConfigByFile(null);
    } else {
      return graphFactory;
    }
  }

  public GraphFactory builderConfigByFile(String filePath) {
    InputStream file = null;
    try {
      file = (null == filePath) ? this.getClass().getResourceAsStream("/janusgarph.property") : new FileInputStream(filePath);
    } catch (FileNotFoundException e1) {
    }
    Map<String, Object> properties = new HashMap<String, Object>();
    try {
      Properties p = new Properties();
      p.load(file);
      p.forEach((key, value) -> {
        properties.put((String) key, value);
        System.out.println(key + ":::" + value);
      });
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return builderConfigByProperties(properties);
  }

  public GraphFactory builderConfigByProperties(Map<String, Object> properties) {
    config = JanusGraphFactory.build();
    if (null != properties) {
      properties.forEach((key, value) -> config.set(key, value));
    }
    initialization();
    return graphFactory;
  }
}
