package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;

import com.google.gson.Gson;
import com.grandland.janusgraph.core.GraphFactory;

import net.sf.json.JSONObject;

/**
 * Hello world!
 *
 */
public class Export_R {
  public static void main(String[] args) {
    long offset = Long.valueOf(args[1]);
    BufferedReader reader = null;
    FileWriter writer = null;
    try {
      reader = new BufferedReader(new FileReader(new File(args[0])));
      writer = new FileWriter(args[0] + "_");
      String tempString = null;
      long size = 0;
      GraphFactory.getInstance().builderConfig();
      while ((size < offset) && ((tempString = reader.readLine()) != null)) {
        System.out.println("size:::_" + (++size) + "_");
      }
      JanusGraph graph = GraphFactory.getInstance().getGraph();
      GraphTraversalSource g = graph.traversal();
      while ((tempString = reader.readLine()) != null) {
        if (tempString.length() > 10) {
          JSONObject content = net.sf.json.JSONObject.fromObject(tempString);
          long targetID = content.getLong("id");
          String type = content.getString("type");
          if (type.equals("ACTED_IN") || type.equals("DIRECTED") || type.equals("PRODUCED") || type.equals("FOLLOWS")
              || type.equals("REVIEWED") || type.equals("WROTE")) {
            size++;
            continue;
          }
          long start = content.getLong("start");
          long end = content.getLong("end");
          String property = content.getString("properties");
          JSONObject properties = net.sf.json.JSONObject.fromObject(property);
          GraphTraversal<Vertex, Vertex> starts = g.V().has("targetID", start);
          GraphTraversal<Vertex, Vertex> endVs = g.V().has("targetID", end);
          if (starts.hasNext() && endVs.hasNext()) {
            Vertex startV = starts.next();
            Vertex endV = endVs.next();
            Edge e = startV.addEdge(type, endV);
            e.property("targetID", targetID);
            for (Object key : properties.keySet()) {
              e.property((String) key, properties.get((String) key));
            }
            e.property("type", type);
            e.property("updatetime", System.currentTimeMillis());
          } else {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> v = new HashMap<>();
            for (Object key : properties.keySet()) {
              v.put((String) key, properties.get((String) key));
            }
            map.put("id", targetID);
            map.put("type", type);
            map.put("start", start);
            map.put("end", end);
            map.put("properties", v);
            writer.write(new Gson().toJson(map));
            writer.write("\r\n");
          }
          size++;
          if (size % 50 == 0) {
            System.out.println(size);
            g.tx().commit();
            g.tx().open();
            writer.flush();
          }
        } else {
          size++;
        }
      }
      g.tx().commit();
      g.tx().open();
      writer.flush();
      writer.close();
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("I'm File Thread, I'm Over!");
  }
}