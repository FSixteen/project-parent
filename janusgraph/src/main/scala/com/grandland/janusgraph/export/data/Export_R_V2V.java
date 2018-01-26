package com.grandland.janusgraph.export.data;

import java.util.HashMap;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;

import com.grandland.janusgraph.core.GraphFactory;

/**
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-16<br/>
 *
 */
public class Export_R_V2V {
  public static void main(String[] args) {
    try {
      GraphFactory.getInstance().builderConfig();
      JanusGraph graph = GraphFactory.getInstance().getGraph();
      GraphTraversalSource g = graph.traversal();

      long start = 163844272L;
      long end = 704524424L;
      String type = "INVEST_O";
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("tag", 6);
      map.put("percent", 100.0);
      map.put("role", "其他投资者");
      map.put("real_amount", 1000.0);
      map.put("subscribed_amount", 1000.0);

      GraphTraversal<Vertex, Vertex> starts = g.V(start);
      GraphTraversal<Vertex, Vertex> endVs = g.V(end);
      if (starts.hasNext() && endVs.hasNext()) {
        Vertex startV = starts.next();
        Vertex endV = endVs.next();
        Edge e = startV.addEdge(type, endV);
        e.property("type", type);
        e.property("fvid", startV.id());
        e.property("tvid", endV.id());
        e.property("updatetime", System.currentTimeMillis());
        for (HashMap.Entry<String, Object> entry : map.entrySet()) {
          e.property(entry.getKey(), entry.getValue());
        }
      }
      g.tx().commit();
      g.tx().open();
    } finally {
    }
    GraphFactory.getInstance().close();
    System.out.println("I'm File Thread, I'm Over!");
  }
}