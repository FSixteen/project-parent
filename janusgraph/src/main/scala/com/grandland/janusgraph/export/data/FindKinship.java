package com.grandland.janusgraph.export.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class FindKinship {
  public static void main(String[] args) {
    ArrayList<Long> ids = new ArrayList<Long>();
    esGet(ids);
    ArrayList<Long> rids = new ArrayList<Long>();
    jgGet(ids, rids);
    System.out.println("rids::::::" + rids);
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();
    rids.forEach((Long id) -> {
      List<Map<String, Object>> relation = new ArrayList<>();
      ArrayList<Long> relationIds = new ArrayList<Long>();
      Double[] payment = new Double[] { 0.0 };
      GraphTraversal<Vertex, Map<String, Object>> result = g.V(id).has("type", "Department").as("a").aggregate("aa")
          .inE().has("type", "SERVE").as("x").outV().has("type", "Person").as("b").bothE().has("type", "KINSHIP")
          .as("y").otherV().has("type", "Person").as("c").outE().has("type", P.within("OWN", "INVEST_H", "SERVE"))
          .as("z").inV().has("type", "Company").as("d").inE().has("type", "PAYMENT").as("r").outV()
          .where(P.within("aa")).select("a", "b", "c", "d").dedup();
      CacheVertex a = null;
      while (result.hasNext()) {
        Map<String, Object> r = result.next();
        a = (CacheVertex) r.get("a");
        CacheVertex b = (CacheVertex) r.get("b");
        CacheVertex c = (CacheVertex) r.get("c");
        CacheVertex d = (CacheVertex) r.get("d");
        relationIds.add((Long) a.id());
        relationIds.add((Long) b.id());
        relationIds.add((Long) c.id());
        relationIds.add((Long) d.id());
        System.out.println(a.id() + "--" + b.id() + "--" + c.id() + "--" + d.id());
        // a --> b
        dealRelation(a, b, relation, false, payment);
        // a <-- b
        dealRelation(b, a, relation, false, payment);
        // b --> c
        dealRelation(b, c, relation, false, payment);
        // b <-- c
        dealRelation(c, b, relation, false, payment);
        // c --> d
        dealRelation(c, d, relation, false, payment);
        // c <-- d
        dealRelation(d, c, relation, false, payment);
        // d --> a
        dealRelation(d, a, relation, true, payment);
        // d <-- a
        dealRelation(a, d, relation, true, payment);
      }
      Iterator<VertexProperty<Object>> uidT = a.properties("uid");
      Iterator<VertexProperty<Object>> nameT = a.properties("name");
      String uid = "";
      String name = "";
      if (uidT.hasNext()) {
        uid = (String) uidT.next().value();
      }
      if (nameT.hasNext()) {
        name = (String) nameT.next().value();
      }
      addES((Long) a.id(), uid, name, relationIds, relation, payment[0]);
    });
    GraphFactory.getInstance().close();
  }

  public static final void addES(Long id, String uid, String name, List<Long> nodes, List<Map<String, Object>> size,
      Double money) {
    System.out.println("-----------------------------------------------");
    System.out.println("\"id\":" + new Gson().toJson(id));
    System.out.println("\"uid\":" + new Gson().toJson(uid));
    System.out.println("\"name\":" + new Gson().toJson(name));
    System.out.println("\"nodes\":" + new Gson().toJson(nodes));
    System.out.println("\"size\":" + new Gson().toJson(size));
    System.out.println("\"money\":" + money);
    System.out.println("-----------------------------------------------");
    TransportClient client = ESConnection.getClient();
    IndexRequestBuilder requestBuilder = client.prepareIndex("janusgraph_all_findkinship", "all_findkinship",
        LongEncoding.encode(id));
    Map<String, Object> source = new HashMap<>();
    source.put("id", id);
    source.put("uid", uid);
    source.put("name", name);
    source.put("nodes", nodes);
    source.put("size", size);
    source.put("money", money);
    requestBuilder.setSource(source);
    requestBuilder.execute().actionGet();
  }

  public static final void esGet(ArrayList<Long> ids) {
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_vertex")
        .setTypes("all_vertex");
    prepareSearch.setFrom(0).setSize(100000000);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("type", "Department"));
    prepareSearch.setQuery(boolQuery);
    prepareSearch.storedFields();
    SearchResponse searchResponse = prepareSearch.get();
    SearchHits searchHits = searchResponse.getHits();
    searchHits.forEach((SearchHit hits) -> {
      ids.add(LongEncoding.decode(hits.getId()));
    });
    ids.sort((Long s, Long e) -> {
      if (s > e) {
        return 1;
      } else if (s < e) {
        return -1;
      } else {
        return 0;
      }
    });
  }

  public static final void jgGet(ArrayList<Long> ids, ArrayList<Long> rids) {
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();
    ids.parallelStream().forEach((Long id) -> {
      GraphTraversal<Vertex, Vertex> result = g.V(id).has("type", "Department").aggregate("a").inE()
          .has("type", "SERVE").as("x").outV().has("type", "Person").as("b").bothE().has("type", "KINSHIP").as("y")
          .otherV().has("type", "Person").as("c").outE().has("type", P.within("OWN", "INVEST_H", "SERVE")).as("z").inV()
          .has("type", "Company").as("d").inE().has("type", "PAYMENT").as("r").outV().where(P.within("a"));
      if (result.hasNext()) {
        System.out.println(":::::::::::::::::::" + result.next().id());
        rids.add((Long) result.next().id());
      }
    });
  }

  public static final void dealRelation(CacheVertex fvid, CacheVertex tvid, List<Map<String, Object>> results,
      boolean status, Double[] payment) {
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_edge").setTypes("all_edge");
    prepareSearch.setFrom(0).setSize(100000000);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("fvid", fvid.id()))
        .must(QueryBuilders.matchPhraseQuery("tvid", tvid.id()));
    prepareSearch.setQuery(boolQuery);
    SearchResponse searchResponse = prepareSearch.get();
    SearchHits searchHits = searchResponse.getHits();
    Map<String, Integer> map = new HashMap<>();
    searchHits.forEach((SearchHit hits) -> {
      Map<String, Object> getSourceAsMap = hits.getSourceAsMap();
      String type = (String) getSourceAsMap.get("type");
      if (status) {
        if ("PAYMENT".equals(type)) {
          Object value = getSourceAsMap.getOrDefault("value", "0");
          if (null != value) {
            if (value instanceof String) {
              payment[0] = payment[0] + Double.valueOf((String) value);
            } else if (value instanceof Long) {
              payment[0] = payment[0] + Double.valueOf((Long) value);
            } else if (value instanceof Integer) {
              payment[0] = payment[0] + Double.valueOf((Integer) value);
            } else {

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
      m.put("size", value);
      m.put("fvid", fvid.id());
      m.put("tvid", tvid.id());
      results.add(m);
      // results.put(fvid.id() + "--" + key + "--" + tvid.id(), m);
    });
  }
}