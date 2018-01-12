package com.grandland.janusgraph.work;

import org.janusgraph.core.JanusGraph;

import com.grandland.janusgraph.core.BuildSchema;
import com.grandland.janusgraph.core.GraphFactory;

public class InitSchema {
  private JanusGraph graph = null;

  public InitSchema() {
    GraphFactory.getInstance().builderConfig();
    this.graph = GraphFactory.getInstance().getGraph();
  }

  public void init() {
    new BuildSchema().execut(this.graph.openManagement(), SchemaContentForES.class);
    this.graph.close();
  }

  public static void main(String[] args) {
    InitSchema initSchema = new InitSchema();
    initSchema.init();
  }
}
