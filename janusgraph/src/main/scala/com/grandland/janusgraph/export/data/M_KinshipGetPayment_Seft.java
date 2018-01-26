package com.grandland.janusgraph.export.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
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
public class M_KinshipGetPayment_Seft {
  public static AtomicInteger size = new AtomicInteger(0);

  @SuppressWarnings("resource")
  public static void main(String[] args) {
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();
    for (Integer page = 0;; page++) {
      System.out.print("请输入公司.亲属.ID::eg:123::");
      Scanner scan = new Scanner(System.in);
      if (scan.equals("q")) {
        break;
      }
      String[] arg = scan.nextLine().split(" ");
      long id = 0L;
      try {
        id = Long.valueOf(arg[0]);
      } catch (Exception e) {
        System.out.println("ERROR");
        continue;
      }

      try {
        // 关系信息
        List<Map<String, Object>> relations = new ArrayList<>();
        // 牵扯到的定点ID
        ArrayList<Long> vertices = new ArrayList<Long>();
        // 注资金额
        Double[] payment = new Double[] { 0.0 };
        // 查询路径
        GraphTraversal<Vertex, Map<String, Object>> result = g.V(id).has("type", "Department").as("a").aggregate("aa").inE().has("type", "SERVE").as("x").outV().has("type", "Person").as("b").bothE()
            .has("type", "KINSHIP").as("y").otherV().has("type", "Person").as("c").outE().has("type", P.within("OWN", "INVEST_H", "SERVE")).as("z").inV().has("type", "Company").as("d").inE()
            .has("type", "PAYMENT").as("r").outV().where(P.within("aa")).select("a", "b", "c", "d").dedup();
        // 保留顶点信息
        CacheVertex a = null;
        while (result.hasNext()) {
          Map<String, Object> r = result.next();
          // 多条路的顶点信息是一样的, 只处理一次即可.
          if (null == a) {
            a = (CacheVertex) r.get("a");
            vertices.add((Long) a.id());
          }
          CacheVertex b = (CacheVertex) r.get("b");
          CacheVertex c = (CacheVertex) r.get("c");
          CacheVertex d = (CacheVertex) r.get("d");
          // 将路径中的id写入到vertices, 自动去重.
          vertices.add((Long) b.id());
          vertices.add((Long) c.id());
          vertices.add((Long) d.id());
          // 打印路径
          System.out.println(a.id() + "--" + b.id() + "--" + c.id() + "--" + d.id());
          // 以下是处理各路径中, 各组临近点的关系.
          // a --> b
          dealRelation(a, b, relations, false, payment);
          // a <-- b
          dealRelation(b, a, relations, false, payment);
          // b --> c
          dealRelation(b, c, relations, false, payment);
          // b <-- c
          dealRelation(c, b, relations, false, payment);
          // c --> d
          dealRelation(c, d, relations, false, payment);
          // c <-- d
          dealRelation(d, c, relations, false, payment);
          // d --> a
          dealRelation(d, a, relations, true, payment);
          // d <-- a
          dealRelation(a, d, relations, true, payment);
        }
        // 处理顶点信息, 获取顶点的uid, name
        String uid = "";
        String name = "";
        try {
          uid = (String) a.properties("uid").next().value();
        } catch (Exception e) {
        }
        try {
          name = (String) a.properties("name").next().value();
        } catch (Exception e) {
        }
        // 写入ES中
        addES((Long) a.id(), uid, name, vertices, relations, payment[0]);
      } catch (Exception e) {
      }
    }
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
   * @param nodes
   *          牵扯到节点信息
   * @param links
   *          牵扯到的关系信息
   * @param money
   *          注资金额
   */
  public static final void addES(Long id, String uid, String name, List<Long> nodes, List<Map<String, Object>> links, Double money) {
    TransportClient client = ESConnection.getClient();
    IndexRequestBuilder requestBuilder = client.prepareIndex("janusgraph_all_kinshipgetpyment", "a", LongEncoding.encode(id));
    Map<String, Object> source = new HashMap<>();
    source.put("id", id);
    source.put("uid", uid);
    source.put("name", name);
    source.put("nodes", nodes);
    source.put("links", links);
    source.put("money", money);
    source.put("updatetime", System.currentTimeMillis());
    source.put("type", "KinshipGetPayment");
    requestBuilder.setSource(source);
    requestBuilder.execute().actionGet();
    System.out.println("-----------------------------------------------");
    System.out.println(new Gson().toJson(source));
    System.out.println("-----------------------------------------------");
  }

  public static final void jgGet(ArrayList<Long> ids, ArrayList<Long> rids) {
    AtomicInteger size = new AtomicInteger(0);
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();
    ids.parallelStream().forEach((Long id) -> {
      GraphTraversal<Vertex, Vertex> result = g.V(id).has("type", "Department").aggregate("a").inE().has("type", "SERVE").as("x").outV().has("type", "Person").as("b").bothE().has("type", "KINSHIP")
          .as("y").otherV().has("type", "Person").as("c").outE().has("type", P.within("OWN", "INVEST_H", "SERVE")).as("z").inV().has("type", "Company").as("d").inE().has("type", "PAYMENT").as("r")
          .outV().where(P.within("a"));
      if (result.hasNext()) {
        System.out.println(":::::::::::::::::::" + result.next().id());
        rids.add((Long) result.next().id());
      }
      size.addAndGet(1);
      if (size.get() % 100 == 0) {
        System.out.println("size::::" + size);
      }
    });
  }

  public static final void dealRelation(CacheVertex fvid, CacheVertex tvid, List<Map<String, Object>> results, boolean status, Double[] payment) {
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_edge").setTypes("all_edge");
    prepareSearch.setFrom(0).setSize(Integer.MAX_VALUE);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("fvid", fvid.id())).must(QueryBuilders.matchPhraseQuery("tvid", tvid.id()));
    prepareSearch.setQuery(boolQuery);
    SearchResponse searchResponse = prepareSearch.get();
    SearchHits searchHits = searchResponse.getHits();
    Map<String, Integer> map = new HashMap<>();
    searchHits.forEach((SearchHit hits) -> {
      Map<String, Object> getSourceAsMap = hits.getSourceAsMap();
      String type = (String) getSourceAsMap.get("type");
      if (status) {
        if ("PAYMENT".equals(type)) {
          Object value = getSourceAsMap.getOrDefault("money", "0");
          if (null != value) {
            if (value instanceof Long) {
              payment[0] = payment[0] + Double.valueOf((Long) value);
            } else if (value instanceof Integer) {
              payment[0] = payment[0] + Double.valueOf((Integer) value);
            } else if (value instanceof Short) {
              payment[0] = payment[0] + Double.valueOf((Short) value);
            } else if (value instanceof Double) {
              payment[0] = payment[0] + ((Double) value);
            } else if (value instanceof Float) {
              payment[0] = payment[0] + ((Float) value);
            } else if (value instanceof String) {
              payment[0] = payment[0] + Double.valueOf((String) value);
            } else {
              ;
            }
          }
        }
      }
      if (map.get(type) != null) {
        map.put(type, map.get(type) + 1);
      } else {
        map.put(type, 1);
      }
    });
    map.forEach((String key, Integer value) -> {
      Map<String, Object> m = new HashMap<>();
      m.put("type", key);
      m.put("count", value);
      m.put("fvid", fvid.id());
      m.put("tvid", tvid.id());
      results.add(m);
    });
  }
}