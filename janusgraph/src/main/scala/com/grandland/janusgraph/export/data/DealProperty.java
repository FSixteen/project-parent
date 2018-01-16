package com.grandland.janusgraph.export.data;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;

import com.grandland.janusgraph.core.GraphFactory;

/**
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-16<br/>
 *
 */
public class DealProperty {

  public static void main(String[] args) throws Exception {
    for (int i = 20; i > 0; i--) {
      GraphFactory.getInstance().builderConfig();
      GraphTraversalSource g = GraphFactory.getInstance().getG();
      g.V().hasNot("tag").limit(1000).drop();
      System.out.println(i);
      g.tx().commit();
      g.tx().open();
    }
    GraphFactory.getInstance().close();
  }
}