package com.grandland.janusgraph.export.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.janusgraph.graphdb.relations.CacheEdge;
import org.janusgraph.graphdb.vertices.CacheVertex;

import com.google.bigtable.repackaged.com.google.gson.Gson;
import com.grandland.janusgraph.core.ESConnection;
import com.grandland.janusgraph.core.GraphFactory;
import com.grandland.janusgraph.core.LongEncoding;

/**
 * 寻找亲属办理企业并获得注资的信息.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-17<br/>
 *
 */
public class Temp {
  public static AtomicInteger size = new AtomicInteger(0);

  public static void main1(String[] args) {
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();
    List<Long> ids = Arrays.asList(4144L, 8240L, 4160L, 4232L, 4344L);
    ids.forEach(id -> {
      Double money = g.V(id).bothE().values("money").sum().next().doubleValue();
      System.out.println(id + ":::" + money);
    });
    GraphFactory.getInstance().close();
  }

  public static void main(String[] args) {
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();

    // 关系
    List<Map<String, Object>> relations = new ArrayList<>();
    // 出发节点
    CacheVertex fromVertex = null;
    // 关联的点
    HashSet<Long> vertices = new HashSet<Long>();
    // Department指出的总金额
    Double money = 0.0;
    // _fvid______tvid______type____count
    Map<Long, Map<Long, Map<String, Long>>> count = new HashMap<>();
    // 开始生成关系.
    List<Long> ids = Arrays.asList(4144L, 8240L, 4160L, 4232L, 4344L);
    Map<Long , Double > moneys = new HashMap<>();
    moneys.put(4144L, 172029119.05);
    moneys.put(8240L, 327480088.42);
    moneys.put(4160L, 127542464.52);
    moneys.put(4232L, 113702254.42);
    moneys.put(4344L, 102896723.91);
    ids.forEach(id -> {
      Vertex v = g.V(id).next();
      GraphTraversal<Vertex, Object> countTemp = g.V(id).bothE().as("r").select("r").limit(100);
      while (countTemp.hasNext()) {
        CacheEdge r = (CacheEdge) countTemp.next();
        org.janusgraph.graphdb.relations.RelationIdentifier edgeid = r.id();
        String type = r.label();
        long fvid = edgeid.getOutVertexId();
        long tvid = edgeid.getInVertexId();
        // ___________________fvid
        if (count.containsKey(fvid)) {
          // _tvid______type____count_________________fvid
          Map<Long, Map<String, Long>> _2 = count.get(fvid);
          // fvid
          if (_2.containsKey(tvid)) {
            // _type____count_____________fvid
            Map<String, Long> _3 = _2.get(tvid);
            // _type
            if (_3.containsKey(type)) {
              // ____type_______count
              _3.put(type, _3.get(type) + 1);
            } else {
              // _type___count
              _3.put(type, 1L);
            }
          } else {
            // _type____count
            Map<String, Long> map = new HashMap<String, Long>();
            // _____type_count
            map.put(type, 1L);
            // ____tvid_<type,count>
            _2.put(tvid, map);
          }
        } else {
          // tvid type count
          Map<Long, Map<String, Long>> _2 = new HashMap<>();
          // type count
          Map<String, Long> _3 = new HashMap<>();
          // type count
          _3.put(type, 1L);
          // tvid <type,count>
          _2.put(tvid, _3);
          // fvid <tvid,<type,count>>
          count.put(fvid, _2);
        }
      }
      count.forEach((Long fvid, Map<Long, Map<String, Long>> _2) -> {
        _2.forEach((Long tvid, Map<String, Long> _3) -> {
          _3.forEach((String type, Long _4_count) -> {
            Map<String, Object> link = new HashMap<>();
            link.put("fvid", fvid);
            link.put("tvid", tvid);
            link.put("type", type);
            link.put("count", _4_count);
            relations.add(link);
          });
        });
      });

      vertices.clear();
      relations.parallelStream().map(_1 -> {
        return Arrays.asList(_1.get("fvid"), _1.get("tvid"));
      }).flatMap(_1 -> _1.parallelStream()).forEach(_1 -> vertices.add((Long) _1));

      Map<String, Object> map = new HashMap<>();
      map.put("nodes", vertices);
      map.put("links", relations);
      map.put("updatetime", System.currentTimeMillis());
      map.put("id", id);
      map.put("type", "GaiLanTypeIsTwoForD");
      map.put("time", "");
      map.put("uid", "");
      map.put("name", v.value("name"));
      map.put("money", moneys.get(id));
      System.out.println(LongEncoding.encode(id) + "" +  new Gson().toJson(map));
    });
    GraphFactory.getInstance().close();
  }

  /**
   * 
   * @param id
   *          顶点id
   * @param uid
   *          顶点uid
   * @param name
   *          顶点name
   * @param time
   *          注册时间
   * @param money
   *          注资金额
   * @param nodes
   *          牵扯到节点信息
   * @param links
   *          牵扯到的关系信息
   */
  public static final void addES(Long id, String uid, String name, String time, Double money, HashSet<Long> nodes, List<Map<String, Object>> links) {
    System.out.println("------------------------");
    TransportClient client = ESConnection.getClient();
    IndexRequestBuilder requestBuilder = client.prepareIndex("janusgraph_all_kgl", "a", LongEncoding.encode(id));
    Map<String, Object> source = new HashMap<>();
    source.put("id", id);
    source.put("uid", uid);
    source.put("name", name);
    source.put("time", time);
    try {
      source.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime());
    } catch (ParseException e) {
      // return;
    }
    source.put("money", money);
    source.put("nodes", nodes);
    source.put("links", links);
    source.put("updatetime", System.currentTimeMillis());
    source.put("type", "KGL");
    requestBuilder.setSource(source);
    requestBuilder.execute().actionGet();
    System.out.println("-----------------------------------------------");
    System.out.println(new Gson().toJson(source));
    System.out.println("-----------------------------------------------");
  }

}