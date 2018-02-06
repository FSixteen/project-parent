package com.grandland.janusgraph.export.data;

import java.util.ArrayList;
import java.util.List;
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

import com.grandland.janusgraph.core.ESConnection;
import com.grandland.janusgraph.core.LongEncoding;
import com.xyshzh.gremlin.client.Connection;

/**
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-17<br/>
 *
 */
public class ES_UpdatePageRank {
  private static final Connection conn = new Connection.ClientConnection("src/main/scala/gremlin-driver.properties", false);
  private static final Client client = conn.getClient();

  public static void main(String[] args) throws InterruptedException {
    for (;;) {
      try {
        System.out.print(".");
        List<Long> ids = esGet();
        List<Long> temp = new ArrayList<>();
        ids.forEach((Long id) -> {
          temp.add(id);
          if (temp.size() > 200) {
            client.submit("g.V(" + StringUtils.join(temp, ",") + ").property('pageRank','0.0')");
            temp.clear();
          }
        });
        if (temp.size() > 0) {
          client.submit("g.V(" + StringUtils.join(temp, ",") + ").property('pageRank','0.0')");
          temp.clear();
        }
        Thread.sleep(3000);
      } catch (Exception e) {
        System.out.println();
        Thread.sleep(10000);
      }
    }
  }

  public static final List<Long> esGet() {
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_vertex").setTypes("all_vertex");
    prepareSearch.setFrom(0).setSize(50000);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().mustNot(QueryBuilders.rangeQuery("pageRank").gte(0).lte(10));
    prepareSearch.setQuery(boolQuery);
    SearchResponse searchResponse = prepareSearch.get();
    SearchHits searchHits = searchResponse.getHits();
    List<Long> list = new ArrayList<>();
    searchHits.forEach((SearchHit hits) -> {
      list.add(LongEncoding.decode(hits.getId()));
    });
    return list;
  }

}