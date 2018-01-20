package com.grandland.janusgraph.export.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.elasticsearch.search.sort.SortOrder;
import org.janusgraph.graphdb.relations.CacheEdge;
import org.janusgraph.graphdb.vertices.CacheVertex;

import com.google.bigtable.repackaged.com.google.gson.Gson;
import com.grandland.janusgraph.core.ESConnection;
import com.grandland.janusgraph.core.GraphFactory;
import com.grandland.janusgraph.core.LongEncoding;

/**
 * 概览大图, type=1
 * 
 * match p=(a:Company)-[r:INVEST_H|:INVEST_O]-(b) where a.state <> '注销企业' return a as startnode ,nodes(p) as nodes,rels(p) as links,length(p) as length limit 200
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-17<br/>
 *
 */
public class M_GaiLan_Type1 {
  public static AtomicInteger size = new AtomicInteger(0);
  
  public static void main(String[] args) {
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();
    for (Integer page = 0;; page++) {
      /** 保存ES中获取的Company的ID信息 */
      ArrayList<Long> ids = new ArrayList<Long>();
      /** 从ES中获取的Company的ID信息,放入{es_id_s}中 */
      esGet(ids, page);
      /** 如果从ES里面获取的Department的ID信息为0, 说明已经执行完成 */
      if (0 == ids.size()) {
        break;
      } else {
        System.out.println("第" + page + "页!");
        System.out.println("ids::::::" + ids.size());
      }
      // match p=(a:Company)-[r:INVEST_H|:INVEST_O]-(b) where a.state <> '注销企业' return a as startnode ,nodes(p) as nodes,rels(p) as links,length(p) as length limit 200
      ids.parallelStream().forEach((id) -> {
        // 关系
        List<Map<String, Object>> relations = new ArrayList<>();
        // 出发节点
        CacheVertex fromVertex = null;
        // 关联的点
        HashSet<Long> vertices = new HashSet<Long>();
        // Company被投资的总金额
        Double imoney = 0.0;
        // Company投资的总金额
        Double omoney = 0.0;
        // Company投资的总金额
        Double money = 0.0;
        // 查找关系开始
        GraphTraversal<Vertex, Map<String, Object>> result = g.V(id).has("type", "Company").has("state", P.neq("注销企业")).as("a").bothE().has("type", P.within("INVEST_H", "INVEST_O")).as("r").otherV().as("b").select("a", "b")
            .dedup();
        // 处理所有节点信息
        while (result.hasNext()) {
          Map<String, Object> r = result.next();
          if (null == fromVertex) {
            fromVertex = (CacheVertex) r.get("a");
            vertices.add((Long) fromVertex.id());
          }
          vertices.add((Long) ((CacheVertex) r.get("b")).id());
        }
        // 如果有点, 则进行关系处理
        if (null != fromVertex && 1 < vertices.size()) {
          // 处理被投资金额
          GraphTraversal<Vertex, Object> moneyTemps = g.V(id).has("type", "Company").has("state", P.neq("注销企业")).as("a").inE().has("type", P.within("INVEST_H", "INVEST_O")).as("r").outV().as("b").select("r").dedup()
              .values("real_amount");
          while (moneyTemps.hasNext()) {
            try {
              Object imoneyTemp = moneyTemps.next();
              if (imoneyTemp instanceof Long) {
                imoney += Double.valueOf((Long) imoneyTemp);
              } else if (imoneyTemp instanceof Integer) {
                imoney += Double.valueOf((Integer) imoneyTemp);
              } else if (imoneyTemp instanceof Double) {
                imoney += (Double) imoneyTemp;
              } else if (imoneyTemp instanceof Float) {
                imoney += (Float) imoneyTemp;
              } else if (imoneyTemp instanceof String) {
                imoney += Double.valueOf((String) imoneyTemp);
              } else {
                ;
              }
            } catch (Exception e) {
              ;
            }
          }
          // 处理投资金额
          moneyTemps = g.V(id).has("type", "Company").has("state", P.neq("注销企业")).as("a").outE().has("type", P.within("INVEST_H", "INVEST_O")).as("r").inV().as("b").select("r").dedup()
              .values("real_amount");
          while (moneyTemps.hasNext()) {
            try {
              Object omoneyTemp = moneyTemps.next();
              if (omoneyTemp instanceof Long) {
                omoney += Double.valueOf((Long) omoneyTemp);
              } else if (omoneyTemp instanceof Integer) {
                omoney += Double.valueOf((Integer) omoneyTemp);
              } else if (omoneyTemp instanceof Double) {
                omoney += (Double) omoneyTemp;
              } else if (omoneyTemp instanceof Float) {
                omoney += (Float) omoneyTemp;
              } else if (omoneyTemp instanceof String) {
                omoney += Double.valueOf((String) omoneyTemp);
              } else {
                ;
              }
            } catch (Exception e) {
              ;
            }
          }
          // 处理关系
          GraphTraversal<Vertex, Object> countTemp = g.V(id).has("type", "Company").has("state", P.neq("注销企业")).as("a").bothE().has("type", P.within("INVEST_H", "INVEST_O")).as("r").otherV().as("b").select("r");
          //  fvid      tvid      type    count
          Map<Long, Map<Long, Map<String, Long>> > count = new HashMap<>();
          while (countTemp.hasNext()) {
            CacheEdge r = (CacheEdge) countTemp.next();
            org.janusgraph.graphdb.relations.RelationIdentifier edgeid = r.id();
            String type = r.label();
            long fvid = edgeid.getOutVertexId();
            long tvid = edgeid.getInVertexId();
            //                    fvid
            if (count.containsKey(fvid)) {
              //  tvid      type    count                 fvid
              Map<Long, Map<String, Long>> _2 = count.get(fvid);
              //                 fvid
              if (_2.containsKey(tvid)) {
                //  type    count            fvid
                Map<String, Long> _3 = _2.get(tvid);
                //                 type
                if (_3.containsKey(type)) {
                  //     type          count
                  _3.put(type, _3.get(type) + 1);
                } else {
                  //     type count
                  _3.put(type, 1L);
                }
              } else {
                //  type    count
                Map<String, Long> map = new HashMap<String, Long>();
                //      type    count
                map.put(type, 1L);
                //     tvid  <type,count>
                _2.put(tvid, map);
              }
            } else {
              //  tvid      type    count
              Map<Long, Map<String, Long>> _2 = new HashMap<>();
              //   type    count
              Map<String, Long> _3 = new HashMap<>();
              //     type  count
              _3.put(type, 1L);
              //     tvid  <type,count>
              _2.put(tvid, _3);
              //        fvid  <tvid,<type,count>>
              count.put(fvid, _2);
            }
          }
          count.forEach((Long fvid, Map<Long, Map<String, Long>> _2)->{
            _2.forEach((Long tvid, Map<String, Long> _3)->{
              _3.forEach((String type, Long _4_count)->{
                Map<String, Object> link = new HashMap<>();
                link.put("fvid", fvid);
                link.put("tvid", tvid);
                link.put("type", type);
                link.put("count", _4_count);
                relations.add(link);
              });
            });
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
          money = imoney * 0.5 + omoney * 0.5;
          addES(id, uid, name, time, imoney, omoney, money, vertices, relations);
        }
        size.addAndGet(1);
        if(size.get() % 100 == 0){
          System.out.println("size::::" + size);
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
   * @param imoney
   *          被投资金额
   * @param omoney
   *          投资金额
   * @param money
   *          权衡后金额
   * @param nodes
   *          牵扯到节点信息
   * @param links
   *          牵扯到的关系信息
   */
  public static final void addES(Long id, String uid, String name, String time, Double imoney, Double omoney, Double money, HashSet<Long> nodes, List<Map<String, Object>> links) {
    TransportClient client = ESConnection.getClient();
    IndexRequestBuilder requestBuilder = client.prepareIndex("janusgraph_all_gailantype1c", "a", LongEncoding.encode(id));
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
    source.put("imoney", imoney);
    source.put("omoney", omoney);
    source.put("money", money);
    source.put("nodes", nodes);
    source.put("links", links);
    source.put("updatetime", System.currentTimeMillis());
    source.put("type", "GaiLanTypeIsOneForC");
    requestBuilder.setSource(source);
    requestBuilder.execute().actionGet();
    System.out.println("-----------------------------------------------");
    System.out.println(new Gson().toJson(source));
    System.out.println("-----------------------------------------------");
  }

  /**
   * 获取所有Department的ID
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
    boolQuery.mustNot(QueryBuilders.matchPhraseQuery("state__STRING", "注销企业"));
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