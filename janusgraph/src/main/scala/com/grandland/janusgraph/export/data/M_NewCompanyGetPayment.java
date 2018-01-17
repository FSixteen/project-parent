package com.grandland.janusgraph.export.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.elasticsearch.search.sort.SortOrder;
import org.janusgraph.graphdb.relations.CacheEdge;
import org.janusgraph.graphdb.vertices.CacheVertex;

import com.google.bigtable.repackaged.com.google.gson.Gson;
import com.grandland.janusgraph.core.ESConnection;
import com.grandland.janusgraph.core.GraphFactory;
import com.grandland.janusgraph.core.LongEncoding;

/**
 * 寻找新办企业获得注资.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-17<br/>
 *
 */
public class M_NewCompanyGetPayment {
  public static void main(String[] args) {
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();
    for (Integer page = 0;; page++) {
      /** 保存ES中获取的Company的ID信息 */
      ArrayList<Long> ids = new ArrayList<Long>();
      /** 从ES中获取的Company的ID信息,放入{es_id_s}中 */
      esGet(ids, page);
      /** 如果从ES里面获取的Company的ID信息为0, 说明已经执行完成 */
      if (0 == ids.size()) {
        break;
      } else {
        System.out.println("第" + page + "页!");
        System.out.println("ids::::::" + ids.size());
      }
      ids.parallelStream().forEach((id) -> {
        List<Map<String, Object>> relations = new ArrayList<>();
        CacheVertex fromVertex = null;
        HashSet<Long> vertices = new HashSet<Long>();
        Double money = 0.0;
        GraphTraversal<Vertex, Map<String, Object>> result = g.V(id).has("type", "Company").as("a").inE().has("type", "PAYMENT").as("r").outV().has("type", "Department").as("b").select("a", "b")
            .dedup();
        while (result.hasNext()) {
          Map<String, Object> r = result.next();
          if (null == fromVertex) {
            fromVertex = (CacheVertex) r.get("a");
            vertices.add((Long) ((CacheVertex) r.get("a")).id());
          }
          vertices.add((Long) ((CacheVertex) r.get("b")).id());
        }
        if (null != fromVertex && 0 < vertices.size()) {
          // 处理金额
          GraphTraversal<Vertex, Object> moneyTemps = g.V(id).has("type", "Company").as("a").inE().has("type", "PAYMENT").as("r").outV().has("type", "Department").as("b").select("r").dedup()
              .values("money");
          while (moneyTemps.hasNext()) {
            try {
              Object moneyTemp = moneyTemps.next();
              if (moneyTemp instanceof Long) {
                money += Double.valueOf((Long) moneyTemp);
              } else if (moneyTemp instanceof Integer) {
                money += Double.valueOf((Integer) moneyTemp);
              } else if (moneyTemp instanceof Double) {
                money += (Double) moneyTemp;
              } else if (moneyTemp instanceof String) {
                money += Double.valueOf((String) moneyTemp);
              } else {
                ;
              }
            } catch (Exception e) {
              ;
            }
          }
          // 处理关系
          GraphTraversal<Vertex, Object> countTemp = g.V(id).has("type", "Company").as("a").inE().as("r").outV().has("type", "Department").as("b").select("r");
          Map<Long, Map<String, Long>> count = new HashMap<>();
          while (countTemp.hasNext()) {
            CacheEdge r = (CacheEdge) countTemp.next();
            org.janusgraph.graphdb.relations.RelationIdentifier edgeid = r.id();
            String type = r.label();
            long outVid = edgeid.getOutVertexId();
            if (count.containsKey(outVid)) {
              Map<String, Long> t = count.get(outVid);
              if (t.containsKey(type)) {
                t.put(type, t.get(type) + 1);
              } else {
                t.put(type, 1L);
              }
            } else {
              Map<String, Long> t = new HashMap<>();
              t.put(type, 1L);
              count.put(outVid, t);
            }
          }
          count.forEach((Long _1, Map<String, Long> _2) -> {
            Map<String, Object> link = new HashMap<>();
            _2.forEach((String __1, Long __2) -> {
              link.put("fvid", id);
              link.put("tvid", _1);
              link.put("type", __1);
              link.put("count", __2);
            });
            relations.add(link);
          });
          Iterator<VertexProperty<Object>> uidT = fromVertex.properties("uid");
          Iterator<VertexProperty<Object>> nameT = fromVertex.properties("name");
          Iterator<VertexProperty<Object>> timeT = fromVertex.properties("time");
          String uid = "";
          String name = "";
          String time = "";
          if (uidT.hasNext()) {
            uid = (String) uidT.next().value();
          }
          if (nameT.hasNext()) {
            name = (String) nameT.next().value();
          }
          if (timeT.hasNext()) {
            time = (String) timeT.next().value();
          }
          addES(id, uid, name, time, money, vertices, relations);
        }
      });
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
    TransportClient client = ESConnection.getClient();
    IndexRequestBuilder requestBuilder = client.prepareIndex("janusgraph_all_findnewcompany", "all_findnewcompany", LongEncoding.encode(id));
    Map<String, Object> source = new HashMap<>();
    source.put("id", id);
    source.put("uid", uid);
    source.put("name", name);
    source.put("time", time);
    try {
      source.put("time_stamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime());
    } catch (ParseException e) {
      return;
    }
    source.put("money", money);
    source.put("nodes", nodes);
    source.put("links", links);
    source.put("type", "NewCompanyGetPayment");
    requestBuilder.setSource(source);
    requestBuilder.execute().actionGet();
    System.out.println("-----------------------------------------------");
    System.out.println(new Gson().toJson(source));
    System.out.println("-----------------------------------------------");
  }

  /**
   * 获取所有Company的ID
   * 
   * @param ids
   */
  public static final void esGet(ArrayList<Long> ids, Integer page) {
    System.out.println("开始拉取ES数据");
    final Integer size = 1000000;
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_vertex").setTypes("all_vertex");
    prepareSearch.setFrom(page * size).setSize(size);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("type", "Company"));
    prepareSearch.setQuery(boolQuery);
    prepareSearch.addSort("updatetime", SortOrder.DESC);
    prepareSearch.storedFields();
    SearchResponse searchResponse = prepareSearch.get();
    SearchHits searchHits = searchResponse.getHits();
    searchHits.forEach((SearchHit hits) -> {
      ids.add(LongEncoding.decode(hits.getId()));
    });
    System.out.println("结束拉取ES数据");
  }

}