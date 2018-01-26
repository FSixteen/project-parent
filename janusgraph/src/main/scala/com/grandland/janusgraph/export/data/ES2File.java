package com.grandland.janusgraph.export.data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import com.grandland.janusgraph.core.ESConnection;
import com.grandland.janusgraph.core.LongEncoding;

/**
 * 寻找亲属办理企业并获得注资的信息.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-17<br/>
 *
 */
public class ES2File {
  public static AtomicInteger size = new AtomicInteger(0);

  public static void main(String[] args) {
    try {
      FileWriter writer = new FileWriter(args[0]);
      List<Long> ids = new ArrayList<>();
      for (Integer page = 0;; page++) {
        List<Long> _temp = esGet2(page, args[0]);
        if (0 == _temp.size()) {
          break;
        } else {
          ids.addAll(_temp);
          System.out.println("第" + page + "页!");
        }
      }
      System.out.println("ids::" + ids.size());
      List<Long> _ids = new ArrayList<>();
//      for (Integer page = 0;; page++) {
//        List<Long> _temp = esGet1(page);
//        if (0 == _temp.size()) {
//          break;
//        } else {
//          _ids.addAll(_temp);
//          System.out.println("第" + page + "页!");
//        }
//      }
//      System.out.println("_ids::" + _ids.size());
//      for (Long id : _ids) {
//        ids.remove(id);
//      }
      System.out.println("ids::" + ids.size());
      for (Long id : ids) {
        writer.append(id + "\r\n");
      }
      System.out.println("写入完成.");
      writer.flush();
      ids.clear();
      _ids.clear();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static final List<Long> esGet2(Integer page, String type) {
    List<Long> ids = new ArrayList<Long>();
    System.out.println("开始拉取ES数据");
    final Integer size = 4000000;
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_vertex").setTypes("all_vertex");
    prepareSearch.setFrom(page * size).setSize(size);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("type", type));
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
    return ids;
  }

  public static final List<Long> esGet1(Integer page) {
    List<Long> ids = new ArrayList<Long>();
    System.out.println("开始拉取ES数据");
    final Integer size = 100000;
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_kgl").setTypes("a");
    prepareSearch.setFrom(page * size).setSize(size);
    prepareSearch.addSort("updatetime", SortOrder.DESC);
    prepareSearch.storedFields();
    SearchResponse searchResponse = prepareSearch.get();
    SearchHits searchHits = searchResponse.getHits();
    searchHits.forEach((SearchHit hits) -> {
      ids.add(LongEncoding.decode(hits.getId()));
    });
    System.out.println("结束拉取ES数据");
    return ids;
  }

}