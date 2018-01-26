package com.grandland.janusgraph.export.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class M_KongGuLian {
  public static AtomicInteger size = new AtomicInteger(0);

  public static void main(String[] args) {
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();
    for (Integer page = 40;; page++) {
      /** 保存ES中获取的Department的ID信息 */
      ArrayList<Long> ids = new ArrayList<Long>();
      /** 从ES中获取的Department的ID信息,放入{es_id_s}中 */
      esGet(ids, page);
      // ids.add(2936832L);
      /** 如果从ES里面获取的Department的ID信息为0, 说明已经执行完成 */
      if (0 == ids.size()) {
        break;
      } else {
        System.out.println("第" + page + "页!");
        System.out.println("ids::::::" + ids.size());
      }
      ids.parallelStream().forEach((Long id) -> {
        // 关系
        List<Map<String, Object>> relations = new ArrayList<>();
        // 出发节点
        CacheVertex fromVertex = null;
        // 关联的点
        HashSet<Long> vertices = new HashSet<Long>();
        // Department指出的总金额
        Double money = 0.0;
        // 查找路径
        GraphTraversal<Vertex, Path> result = g.V(id).repeat(__.bothE().has("type", P.within("INVEST_H", "INVEST_O")).order().by("real_amount", Order.decr).otherV()).times(3).emit().path().dedup();
        if (result.hasNext()) {
          // 取出所有路径
          List<Path> ps = new ArrayList<>();
          while (result.hasNext()) {
            ps.add(result.next());
          }
          // 计算每组路径中的总金额
          List<Object[]> pds = new ArrayList<>();
          for (Path _1 : ps) {
            double path_money = 0.0;
            for (Object _2 : _1) {
              if (_2 instanceof CacheEdge) {
                double _m = ((CacheEdge) _2).value("real_amount");
                path_money = path_money + _m;
              } else if (_2 instanceof CacheVertex) {
                Long _id = (Long) ((CacheVertex) _2).id();
                if (null == fromVertex) {
                  if (0 == (_id - id)) {
                    fromVertex = (CacheVertex) _2;
                  }
                }
              } else {
                System.out.println(_2.getClass());
              }
            }
            pds.add(new Object[] { path_money, _1 });
          }
          // 计算所有控股链总金额
          money = pds.parallelStream().mapToDouble((Object[] _1) -> {
            return (Double) _1[0];
          }).sum();
          // 筛选金额最大的前100条, 并取出其关系.
          Map<Long, HashSet<Long>> rms = new ConcurrentHashMap<>();
          pds.sort((Object[] _1, Object[] _2) -> {
            if (((Path) _1[1]).size() > ((Path) _2[1]).size()) {
              return -1;
            } else if (((Path) _1[1]).size() < ((Path) _2[1]).size()) {
              return 1;
            } else {
              if ((double) _1[0] > (double) _2[0])
                return -1;
              else if ((double) _1[0] < (double) _2[0])
                return 1;
              else
                return 0;
            }
          });
          pds.parallelStream().limit(100).map((Object[] _1) -> {
            return (Path) _1[1];
          }).forEach((Path _1) -> {
            for (Object _2 : _1) {
              if (_2 instanceof org.janusgraph.graphdb.relations.CacheEdge) {
                long fvid = ((org.janusgraph.graphdb.relations.CacheEdge) _2).id().getOutVertexId();
                long tvid = ((org.janusgraph.graphdb.relations.CacheEdge) _2).id().getInVertexId();
                vertices.add(fvid);
                vertices.add(tvid);
                long min = (fvid < tvid) ? fvid : tvid;
                long max = (fvid > tvid) ? fvid : tvid;
                if (rms.containsKey(min)) {
                  rms.get(min).add(max);
                } else {
                  rms.put(min, new HashSet<Long>());
                  synchronized (String.class) {
                    if (rms.containsKey(min)) {
                      rms.get(min).add(max);
                    } else {
                      rms.put(min, new HashSet<Long>());
                      rms.get(min).add(max);
                    }
                  }
                }
              }
            }
          });
          // _fvid______tvid______type____count
          Map<Long, Map<Long, Map<String, Long>>> count = new HashMap<>();
          // 开始生成关系.

          rms.forEach((Long _fvid, HashSet<Long> _tvids) -> {
            GraphTraversal<Vertex, CacheEdge> countTemp = g.V(_fvid).bothE().as("r").otherV().hasId(_tvids.toArray()).select("r");
            while (countTemp.hasNext()) {
              CacheEdge r = countTemp.next();
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
          });
          if (null != fromVertex) {
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
            if (/* money >= 0.0 && */vertices.size() > 0 && relations.size() > 0) {
              addES(id, uid, name, time, money, vertices, relations);
            }
          }
        }
        size.addAndGet(1);
        if (size.get() % 100 == 0) {
          System.out.println("size::::" + size);
        }
      });
      ids.clear();
      System.gc();
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

  /**
   * 获取所有Company的ID
   * 
   * @param ids
   */
  public static final void esGet(ArrayList<Long> ids, Integer page) {
    System.out.println("开始拉取ES数据");
    final Integer size = 10000;
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_vertex").setTypes("all_vertex");
    prepareSearch.setFrom(page * size).setSize(size);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("type", "Company"));
    // boolQuery.mustNot(QueryBuilders.matchPhraseQuery("state__STRING",
    // "注销企业"));
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