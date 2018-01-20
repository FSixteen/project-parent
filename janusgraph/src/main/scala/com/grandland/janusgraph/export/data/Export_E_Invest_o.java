package com.grandland.janusgraph.export.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandland.janusgraph.core.GraphFactory;

/**
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-16<br/>
 *
 */
public class Export_E_Invest_o {
  public static AtomicInteger size = new AtomicInteger(0);
  public static BufferedReader reader = null;
  public static void main(String[] args) {
    if(2 != args.length){
      System.out.println("args[0]是文件, args[1]是偏移量.");
    }
//    Long minsize = Long.valueOf(args[1]);
    Long minsize = 0L;
    int thread = 10;
    final int tep = 10;
    CountDownLatch count = new CountDownLatch(thread);
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    try {
//      reader = new BufferedReader(new FileReader(new File(args[0])));
      reader = new BufferedReader(new FileReader(new File("C:\\Users\\Administrator\\Desktop\\kinship_4.txt")));
      for(;minsize > size.get();){
        reader.readLine();
        size.addAndGet(1);
        if (size.get() % 400 == 0) {
          System.out.println(size);
        }
      }
      System.out.println("跳出for, 初始化Graph");
//      System.exit(1);
      GraphFactory.getInstance().builderConfig();
      JanusGraph graph = GraphFactory.getInstance().getGraph();
      GraphTraversalSource g = graph.traversal();
      System.out.println("Graph初始化完成, 进入whille");
      System.out.println("Graph::isOpen:::" + graph.isOpen());
      for(int i = 0 ; i < thread ; i ++){
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              String tempString = null;
              while ((tempString = reader.readLine()) != null) {
                if (tempString.length() > 10) {
                  HashMap<String, String> content = null;
                  try {
                    content = gson.fromJson(tempString, HashMap.class);
                  } catch (Exception e) {
                    System.out.println(size + ":::" + tempString);
                    continue;
                  }

                  String startId = content.get("start");
                  String endId = content.get("end");
                  if (null == startId || null == endId || "".equals(startId.trim()) || "".equals(endId.trim())) {
                    size.addAndGet(1);
                    continue;
                  }
                  String type = content.get("type");
                  String tips = content.get("tips");
                  String uid = content.get("uid");
                  String name = content.get("name");
                  String money = content.get("money");
                  String real_amount = content.get("real_amount");
                  String role = content.get("role");
                  String subscribed_amount = content.get("subscribed_amount");
                  String percent = content.get("percent");
                  String time = content.get("time");
                  String timestamp = content.get("timestamp");
                  String status = content.get("status");
                  String registname = content.get("registname");
                  String registid = content.get("registid");
                  String duty = content.get("duty");
                  String membership = content.get("membership");
                  String category = content.get("category");
                  String jointime = content.get("jointime");
                  String jointimestamp = content.get("jointimestamp");
                  String regioncode = content.get("regioncode");
                  String regionname = content.get("regionname");
                  Long tag = Long.valueOf(content.get("tag"));

                  if ("PAYMENT".equals(type)) {
                    // Old: time value tips
                    // New: time timestamp money tips
                    GraphTraversal<Vertex, Vertex> starts = g.V().has("type", "Department").has("neo4jid", startId);
                    GraphTraversal<Vertex, Vertex> endVs = g.V().has("type", "Company").has("neo4jid", endId);
                    Vertex startV = null;
                    Vertex endV = null;
                    if (starts.hasNext()) {
                      startV = starts.next();
                    }
                    if (endVs.hasNext()) {
                      endV = endVs.next();
                    }
                    if (null != startV && null != endV) {
                      Edge e = startV.addEdge(type, endV);
                      deal(e, startV.id(), endV.id(), type /* type */, tips /* tips */, null /* uid */, null /* name */, money /* money */, null /* real_amount */, null /* role */,
                          null /* subscribed_amount */, null /* percent */, time /* time */, null /* timestamp */, null /* status */, null /* registname */, null /* registid */, null /* duty */,
                          null /* membership */, null /* category */, null /* jointime */, null /* jointimestamp */, null /* regioncode */, null /* regionname */, tag);
                    } else {
                      System.out.println("NULL:::PAYMENT:::" + tempString);
                    }
                  } else if ("Project".equals(type)) {
                    // Old: uid time_stamp name time value
                    // New: uid timestamp name time money
                    GraphTraversal<Vertex, Vertex> starts = g.V().has("type", "Department").has("neo4jid", startId);
                    GraphTraversal<Vertex, Vertex> endVs = g.V().has("type", "Company").has("neo4jid", endId);
                    Vertex startV = null;
                    Vertex endV = null;
                    if (starts.hasNext()) {
                      startV = starts.next();
                    }
                    if (endVs.hasNext()) {
                      endV = endVs.next();
                    }
                    if (null != startV && null != endV) {
                      Edge e = startV.addEdge(type, endV);
                      deal(e, startV.id(), endV.id(), type /* type */, null /* tips */, uid /* uid */, name /* name */, money /* money */, null /* real_amount */, null /* role */,
                          null /* subscribed_amount */, null /* percent */, time /* time */, null /* timestamp */, null /* status */, null /* registname */, null /* registid */, null /* duty */,
                          null /* membership */, null /* category */, null /* jointime */, null /* jointimestamp */, null /* regioncode */, null /* regionname */, tag);
                    } else {
                      System.out.println("NULL:::Project:::" + tempString);
                    }
                  } else if ("INVEST_H".equals(type)) {
                    // Old: real_amount role subscribed_amount percent
                    // New: real_amount role subscribed_amount percent
                    GraphTraversal<Vertex, Vertex> starts = g.V().has("type", "Person").has("uid", startId);
                    GraphTraversal<Vertex, Vertex> endVs = g.V().has("neo4jid", endId);
                    Vertex startV = null;
                    Vertex endV = null;
                    if (starts.hasNext()) {
                      startV = starts.next();
                    }
                    if (endVs.hasNext()) {
                      endV = endVs.next();
                    }
                    if (null != startV && null != endV) {
                      Edge e = startV.addEdge(type, endV);
                      deal(e, startV.id(), endV.id(), type /* type */, null /* tips */, null /* uid */, null /* name */, null /* money */, real_amount /* real_amount */, role /* role */,
                          subscribed_amount /* subscribed_amount */, percent /* percent */, null /* time */, null /* timestamp */, null /* status */, null /* registname */, null /* registid */,
                          null /* duty */, null /* membership */, null /* category */, null /* jointime */, null /* jointimestamp */, null /* regioncode */, null /* regionname */, tag);
                    } else {
                      System.out.println("NULL:::" + tempString);
                    }
                  } else if ("INVEST_O".equals(type)) {
                    // Old: real_amount role subscribed_amount percent
                    // New: real_amount role subscribed_amount percent
                    GraphTraversal<Vertex, Vertex> starts = g.V().has("tag", 4).has("type", "Company").has("uid", startId);
                    GraphTraversal<Vertex, Vertex> endVs = g.V().has("neo4jid", endId);
                    Vertex startV = null;
                    Vertex endV = null;
                    if (starts.hasNext()) {
                      startV = starts.next();
                    }
                    if (endVs.hasNext()) {
                      endV = endVs.next();
                    }
                    if (null != startV && null != endV) {
                      Edge e = startV.addEdge(type, endV);
                      deal(e, startV.id(), endV.id(), type /* type */, null /* tips */, null /* uid */, null /* name */, null /* money */, real_amount /* real_amount */, role /* role */,
                          subscribed_amount /* subscribed_amount */, percent /* percent */, null /* time */, null /* timestamp */, null /* status */, null /* registname */, null /* registid */,
                          null /* duty */, null /* membership */, null /* category */, null /* jointime */, null /* jointimestamp */, null /* regioncode */, null /* regionname */, tag);
                    } else {
                      System.out.println("NULL:::INVEST_O:::" + tempString);
                    }
                  } else if ("OWN".equals(type)) {
                    // Old: role
                    // New: role
                    GraphTraversal<Vertex, Vertex> starts = g.V().has("neo4jid", startId);
                    GraphTraversal<Vertex, Vertex> endVs = g.V().has("neo4jid", endId);
                    Vertex startV = null;
                    Vertex endV = null;
                    if (starts.hasNext()) {
                      startV = starts.next();
                    }
                    if (endVs.hasNext()) {
                      endV = endVs.next();
                    }
                    if (null != startV && null != endV) {
                      Edge e = startV.addEdge(type, endV);
                      deal(e, startV.id(), endV.id(), type /* type */, null /* tips */, null /* uid */, null /* name */, null /* money */, null /* real_amount */, role /* role */,
                          null /* subscribed_amount */, null /* percent */, null /* time */, null /* timestamp */, null /* status */, null /* registname */, null /* registid */, null /* duty */,
                          null /* membership */, null /* category */, null /* jointime */, null /* jointimestamp */, null /* regioncode */, null /* regionname */, tag);
                    } else {
                      System.out.println("NULL:::OWN:::" + tempString);
                    }
                  } else if ("KINSHIP".equals(type)) {
                    // Old: date status
                    // New: time timestamp status registname registid
                    GraphTraversal<Vertex, Vertex> starts = g.V().has("type", "Person").has("uid", startId);
                    GraphTraversal<Vertex, Vertex> endVs = g.V().has("type", "Person").has("uid", endId);
                    Vertex startV = null;
                    Vertex endV = null;
                    if (starts.hasNext()) {
                      startV = starts.next();
                    }
                    if (endVs.hasNext()) {
                      endV = endVs.next();
                    }
                    if (null != startV && null != endV) {
                      GraphTraversal<Edge, Edge> olde = g.E().has("tag", 12).has("fvid", startV.id()).has("tvid", endV.id()).has("time", time);
                      if(olde.hasNext()){
                        System.out.println(size.get() + ":::::::" + "已有.");
                      }else{
                        System.out.println(size.get() + ":::::::" + "么有,执行插入.");
                      Edge e = startV.addEdge(type, endV);
                      deal(e, startV.id(), endV.id(), type /* type */, null /* tips */, null /* uid */, null /* name */, null /* money */, null /* real_amount */, null /* role */,
                          null /* subscribed_amount */, null /* percent */, time /* time */, null /* timestamp */, status /* status */, registname /* registname */, registid /* registid */,
                          null /* duty */, null /* membership */, null /* category */, null /* jointime */, null /* jointimestamp */, null /* regioncode */, null /* regionname */, tag);
                      }
                    } else {
                      System.out.println("NULL:::KINSHIP:::" + tempString);
                    }
                  } else if ("SERVE".equals(type)) {
                    // Old: role duty membership
                    // New: role duty membership category jointime jointimestamp
                    GraphTraversal<Vertex, Vertex> starts = g.V().has("type", "Person").has("uid", startId);
                    GraphTraversal<Vertex, Vertex> endVs = g.V().has("type", "Department").has("neo4jid", endId);
                    Vertex startV = null;
                    Vertex endV = null;
                    if (starts.hasNext()) {
                      startV = starts.next();
                    }
                    if (endVs.hasNext()) {
                      endV = endVs.next();
                    }
                    if (null != startV && null != endV) {
                      GraphTraversal<Edge, Edge> olde = g.E().has("tag", 11).has("fvid", startV.id()).has("tvid", endV.id()).has("jointime", jointime);
                      if(olde.hasNext()){
                        System.out.println(size.get() + ":::::::" + "已有.");
                      }else{
                        System.out.println(size.get() + ":::::::" + "么有,执行插入.");
                        Edge e = startV.addEdge(type, endV);
                        deal(e, startV.id(), endV.id(), type /* type */, null /* tips */, null /* uid */, null /* name */, null /* money */, null /* real_amount */, role /* role */,
                            null /* subscribed_amount */, null /* percent */, null /* time */, null /* timestamp */, null /* status */, null /* registname */, null /* registid */, duty /* duty */,
                            membership /* membership */, category /* category */, jointime /* jointime */, null /* jointimestamp */, null /* regioncode */, null /* regionname */, tag);
                      }
                    } else {
                      System.out.println("NULL:::SERVE:::" + tempString);
                    }
                  } else {
                    ;
                  }
                  size.addAndGet(1);
                  if (10 >= size.get() % tep) {
                    g.tx().commit();
                    g.tx().open();
                    System.out.println(size);
                  }
                } else {
                  size.addAndGet(1);
                }
              }
              g.tx().commit();
              g.tx().open();
            } catch (Exception e) {
              e.printStackTrace();
            }
            count.countDown();
          }
        }).start();
      }
     
      count.await();
      g.tx().commit();
      g.tx().open();
      reader.close();
      GraphFactory.getInstance().close();
    } catch (IOException | InterruptedException e) {
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
  
  public static void deal(Edge e, Object startId, Object endId, String type, String tips, String uid, String name, String money, String real_amount, String role, String subscribed_amount, String percent,
      String time, String timestamp, String status, String registname, String registid, String duty, String membership, String category, String jointime, String jointimestamp, String regioncode,
      String regionname, Long tag) {
    e.property("fvid", (Long) startId);
    e.property("tvid", (Long) endId);
    e.property("type", type);
    e.property("tag", tag);
    if (null != tips && 0 < tips.trim().length()) {
      e.property("tips", tips);
    }
    if (null != uid && 0 < uid.trim().length()) {
      e.property("uid", uid);
    }
    if (null != name && 0 < name.trim().length()) {
      e.property("name", name);
    }
    if (null != money && 0 < money.trim().length()) {
      e.property("money", Double.valueOf(money));
    }
    if (null != real_amount && 0 < real_amount.trim().length()) {
      e.property("real_amount", Double.valueOf(real_amount));
    }
    if (null != role && 0 < role.trim().length()) {
      e.property("role", role);
    }
    if (null != subscribed_amount && 0 < subscribed_amount.trim().length()) {
      e.property("subscribed_amount", Double.valueOf(subscribed_amount));
    }
    if (null != percent && 0 < percent.trim().length()) {
      e.property("percent", Double.valueOf(percent));
    }
    if (null != time && 0 < time.trim().length()) {
      e.property("time", time);
    }
    if (null != timestamp && 0 < timestamp.trim().length()) {
      e.property("timestamp", Long.valueOf(timestamp));
    } else {
      try {
        e.property("timestamp", new SimpleDateFormat("yyyy-mm-dd HH:MM:ss").parse(time).getTime());
      } catch (Exception ex) {
      }
    }
    if (null != status && 0 < status.trim().length()) {
      e.property("status", status);
    }
    if (null != registname && 0 < registname.trim().length()) {
      e.property("registname", registname);
    }
    if (null != registid && 0 < registid.trim().length()) {
      e.property("registid", registid);
    }
    if (null != duty && 0 < duty.trim().length()) {
      e.property("duty", duty);
    }
    if (null != membership && 0 < membership.trim().length()) {
      e.property("membership", membership);
    }
    if (null != category && 0 < category.trim().length()) {
      e.property("category", category);
    }
    if (null != jointime && 0 < jointime.trim().length()) {
      e.property("jointime", jointime);
    } else {
      try {
        e.property("jointimestamp", new SimpleDateFormat("yyyy-mm-dd" /*yyyy-mm-dd HH:MM:ss*/).parse(jointime).getTime());
      } catch (Exception ex) {
      }
    }
    if (null != regioncode && 0 < regioncode.trim().length()) {
      e.property("regioncode", regioncode);
    }
    if (null != regionname && 0 < regionname.trim().length()) {
      e.property("regionname", regionname);
    }
    e.property("updatetime", System.currentTimeMillis());
  }
  
}