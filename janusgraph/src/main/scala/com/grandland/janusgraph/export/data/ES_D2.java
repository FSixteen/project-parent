package com.grandland.janusgraph.export.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import com.grandland.janusgraph.core.ESConnection;
import com.xyshzh.gremlin.client.Connection;

/**
 * 寻找亲属办理企业并获得注资的信息.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-17<br/>
 *
 */
public class ES_D2 {
  private static final Connection conn = new Connection.ClientConnection("./gremlin-driver.properties", false);
  private static final Client client = conn.getClient();
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    for (long page = 0; page < 3000; page++) {
      AtomicInteger size = new AtomicInteger(0);
      long haha = 25000L;
      long start = haha - (page + 1) * 5000L;
      long end = haha - page * 5000L;
       System.out.println(start + ":~~:" + end);
      List<Map<String, Object>> list = esGet(start, end);
      List<String> result = new ArrayList<>();
       System.out.println("数据大小::" + list.size());
      for (int i = 0; i < list.size(); i++) {
        // size.addAndGet(1);
        // if (size.get() % 100 == 0) {
        // System.out.println("size::::" + size);
        // }
        Map<String, Object> _1 = list.get(i);
        for (int j = i + 1; j < list.size(); j++) {
          Map<String, Object> _2 = list.get(j);
          String _1_fvid = String.valueOf(_1.get("fvid"));
          String target_fvid = String.valueOf(_2.get("fvid"));
          if (_1_fvid.trim().equals(target_fvid.trim())) {
            String _1_tvid = String.valueOf(_1.get("tvid"));
            String target_tvid = String.valueOf(_2.get("tvid"));
            if (_1_tvid.trim().equals(target_tvid.trim())) {
              String _1_type = (String) _1.get("type");
              String target_type = (String) _2.get("type");
              if (_1_type.trim().equals(target_type.trim())) {
                String _1_duty = (String) _1.getOrDefault("duty", "0");
                String _1_role = (String) _1.getOrDefault("role", "0");
                String _1_tips = (String) _1.getOrDefault("tips", "0");
                String _1_time = (String) _1.getOrDefault("time", "0");
                String _1__id = (String) _1.get("_id");
                // --------------
                String target_duty = (String) _2.getOrDefault("duty", "0");
                String target_role = (String) _2.getOrDefault("role", "0");
                String target_tips = (String) _2.getOrDefault("tips", "0");
                String target_time = (String) _2.getOrDefault("time", "0");
                String target__id = (String) _2.get("_id");
                // --------------
                double money = (double) _1.getOrDefault("money", 1111111111.0) - (double) _2.getOrDefault("money", 1111111111.0);
                double real_amount = (double) _1.getOrDefault("real_amount", 1111111111.0) - (double) _2.getOrDefault("real_amount", 1111111111.0);
                double percent = (double) _1.getOrDefault("percent", 1111111111.0) - (double) _2.getOrDefault("percent", 1111111111.0);
                double subscribed_amount = (double) _1.getOrDefault("subscribed_amount", 1111111111.0) - (double) _2.getOrDefault("subscribed_amount", 1111111111.0);
                if (real_amount == 0.0 && percent == 0.0 && subscribed_amount == 0.0) {
                  if (money == 0.0 && _1_role.trim().equals(target_role.trim()) && _1_duty.trim().equals(target_duty.trim()) && _1_tips.trim().equals(target_tips.trim())
                      && _1_time.trim().equals(target_time.trim())) {
                    result.add("'" + target__id + "'");
                    list.remove(j);
                  }
                }
              }
            }
          }
        }
      }
       System.out.println("重复数据大小::" + result.size());
      List<String> temp = new ArrayList<String>(105);
      for (String _1 : result) {
        temp.add(_1);
        if (temp.size() > 200) {
          String query = "g.E(" + StringUtils.join(temp, ',') + ").drop()";
          try {
            client.submit(query).all().get();
          } catch (Exception e) {
            e.printStackTrace();
          }
          System.out.println(query);
          temp.clear();
        }
      }
      if (temp.size() > 0) {
        String query = "g.E(" + StringUtils.join(temp, ',') + ").drop()";
        try {
          client.submit(query).all().get();
        } catch (Exception e) {
          e.printStackTrace();
        }
        System.out.println(query);
        temp.clear();
      }
      if (result.size() > 0) {
         break;
      }
    }
  }

  public static final List<Map<String, Object>> esGet(Long from, Long to) {
    // System.out.println("开始拉取ES数据");
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_edge").setTypes("all_edge");
    prepareSearch.setFrom(0).setSize(300000);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    boolQuery.must(QueryBuilders.rangeQuery("fvid").gte(from).lte(to));
    boolQuery.mustNot(QueryBuilders.termQuery("fvid", 1305428120L));
    boolQuery.mustNot(QueryBuilders.termQuery("fvid", 139416L));
    boolQuery.mustNot(QueryBuilders.termQuery("fvid", 12432L));
    boolQuery.mustNot(QueryBuilders.termQuery("fvid", 4248L));
    boolQuery.mustNot(QueryBuilders.termQuery("fvid", 119032L));
    boolQuery.mustNot(QueryBuilders.termQuery("fvid", 24648L));
    boolQuery.mustNot(QueryBuilders.termQuery("fvid", 188568L));
    prepareSearch.setQuery(boolQuery);
    prepareSearch.addSort("fvid", SortOrder.DESC).addSort("tvid", SortOrder.DESC).addSort("money", SortOrder.DESC);
    SearchResponse searchResponse = prepareSearch.get();
    SearchHits searchHits = searchResponse.getHits();
    List<Map<String, Object>> list = new ArrayList<>();
    searchHits.forEach((SearchHit hits) -> {
      Map<String, Object> map = hits.getSourceAsMap();
      map.put("_id", hits.getId());
      list.add(map);
    });
    // System.out.println("结束拉取ES数据");
    return list;
  }

}