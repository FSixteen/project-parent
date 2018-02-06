package com.grandland.janusgraph.export.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import com.google.gson.Gson;
import com.grandland.janusgraph.core.ESConnection;

/**
 * 寻找亲属办理企业并获得注资的信息.
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-17<br/>
 *
 */
public class ES_Hahahahaha {

  public static void main(String[] args) {
    esGet();
  }

  public static final void esGet() {
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_vertex").setTypes("all_vertex");

    prepareSearch.addAggregation(AggregationBuilders.terms("uid__STRING").field("uid__STRING").size(100000));

    SearchResponse searchResponse = prepareSearch.get();

    Aggregations aggregations = searchResponse.getAggregations();
    Terms _2 = aggregations.get("uid__STRING");

    _2.getBuckets().forEach((_1) -> {
      System.out.println(_1.getKeyAsString() + "," + _1.getDocCount());
    });

  }

}