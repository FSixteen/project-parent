package com.grandland.janusgraph.export.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

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
import com.xyshzh.connection.JanusGraphConnection;

/**
 * 寻找亲属办理企业并获得注资的信息.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-17<br/>
 *
 */
public class ES_D {
  private static final JanusGraphConnection conn = new JanusGraphConnection(new File("./gremlin-driver.properties"));
  private static final Client client = conn.getClient();

  // 2646982656 292712568
  /**
   * @param args
   */
  @SuppressWarnings("resource")
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    boolean status = true;
    for (int page = 0; page < 1000;) {
      if (status) {
        page++;
      }
      System.out.print("请输入ID:eg:12:q退出:");
      String line = "-1 -1"; // scan.nextLine();
      if (line.equals("q")) {
        break;
      }
      long fvid = 0L;
      long tvid = 0L;
      try {
        fvid = Long.valueOf(line.split(" ")[0]);
        tvid = Long.valueOf(line.split(" ")[1]);
      } catch (Exception e) {
        System.out.println("ERROR");
        continue;
      }

      List<Map<String, Object>> list = esGet(page, fvid, tvid);
      List<String> result = new ArrayList<>();
      System.out.println("数据大小::" + list.size());
      for (int i = 0; i < list.size(); i++) {
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

      // list.sort((Map<String, Object> _1, Map<String, Object> _2) -> {
      // String _1_fvid = String.valueOf(_1.get("fvid"));
      // String _1_tvid = String.valueOf(_1.get("tvid"));
      // String _1_type = (String) _1.get("type");
      // String _1_duty = (String) _1.getOrDefault("duty", "0");
      // String _1_role = (String) _1.getOrDefault("role", "0");
      // String _1_tips = (String) _1.getOrDefault("tips", "0");
      // String _1_time = (String) _1.getOrDefault("time", "0");
      // String _1__id = (String) _1.get("_id");
      //
      // String target_fvid = String.valueOf(_2.get("fvid"));
      // String target_tvid = String.valueOf(_2.get("tvid"));
      // String target_type = (String) _2.get("type");
      // String target_duty = (String) _2.getOrDefault("duty", "0");
      // String target_role = (String) _2.getOrDefault("role", "0");
      // String target_tips = (String) _2.getOrDefault("tips", "0");
      // String target_time = (String) _2.getOrDefault("time", "0");
      // String target__id = (String) _2.get("_id");
      //
      // double money = (double) _1.getOrDefault("money", 1111111111.0) -
      // (double) _2.getOrDefault("money", 1111111111.0);
      //
      // // System.out.println("--------");
      // // System.out.println(_1_fvid + "::" + _1_tvid + "::" + _1_money + "::"
      // // +
      // // _1_tips + "::" + _1_time + "::" + _1__id);
      // // System.out.println(target_fvid + "::" + target_tvid + "::" +
      // // target_money + "::" + target_tips + "::" + target_time + "::" +
      // // target__id);
      //
      // if (_1_fvid.trim().equals(target_fvid.trim()) &&
      // _1_tvid.trim().equals(target_tvid.trim()) && money == 0.0 &&
      // _1_type.trim().equals(target_type.trim())
      // && _1_role.trim().equals(target_role.trim()) &&
      // _1_duty.trim().equals(target_duty.trim()) &&
      // _1_tips.trim().equals(target_tips.trim()) &&
      // _1_time.trim().equals(target_time.trim())) {
      // // System.out.println(_1__id + ":::" + target__id + "::: same");
      //
      // System.out.println("--------");
      // System.out.println(_1_fvid + "::" + _1_tvid + "::" + (double)
      // _1.getOrDefault("money", 0.0) + "::" + _1_tips + "::" + _1_time + "::"
      // + _1__id);
      // System.out.println(target_fvid + "::" + target_tvid + "::" + (double)
      // _2.getOrDefault("money", 0.0) + "::" + target_tips + "::" + target_time
      // + "::" + target__id);
      // result.add("'" + target__id + "'");
      // return 0;
      // } else {
      // if (_1_type.trim().equals(target_type.trim())) {
      // return _1__id.compareTo(target__id);
      // } else {
      // return _1_type.compareTo(target_type);
      // }
      // }
      // });

      System.out.println("重复数据大小::" + result.size());
      List<String> temp = new ArrayList<String>(105);
      if (result.size() > 0) {
        status = false;
      }
      for (String _1 : result) {
        temp.add(_1);
        if (temp.size() > 100) {
          String query = "g.E(" + StringUtils.join(temp, ',') + ").drop()";
          try {
            client.submit(query).all().get();
          } catch (InterruptedException e) {
            e.printStackTrace();
          } catch (ExecutionException e) {
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
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (ExecutionException e) {
          e.printStackTrace();
        }
        System.out.println(query);
        temp.clear();
      }
      System.out.println("page::::::" + page);
    }
  }

  public static final List<Map<String, Object>> esGet(Integer page, Long fvid, Long tvid) {
    System.out.println("开始拉取ES数据");
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_edge").setTypes("all_edge");
    prepareSearch.setFrom((page * 5000 - 500) > 0 ? page * 5000 - 500 : 0).setSize(5000);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    if (fvid > 0) {
      boolQuery.must(QueryBuilders.matchPhraseQuery("fvid", fvid));
    }
    if (tvid > 0) {
      boolQuery.must(QueryBuilders.matchPhraseQuery("tvid", tvid));
    }
    if (fvid > 0 && tvid > 0) {
      prepareSearch.setQuery(boolQuery);
    }
    prepareSearch.addSort("fvid", SortOrder.DESC).addSort("tvid", SortOrder.DESC).addSort("money", SortOrder.DESC);
    SearchResponse searchResponse = prepareSearch.get();
    SearchHits searchHits = searchResponse.getHits();
    List<Map<String, Object>> list = new ArrayList<>();
    searchHits.forEach((SearchHit hits) -> {
      Map<String, Object> map = hits.getSourceAsMap();
      map.put("_id", hits.getId());
      list.add(map);
    });
    System.out.println("结束拉取ES数据");
    return list;
  }

}