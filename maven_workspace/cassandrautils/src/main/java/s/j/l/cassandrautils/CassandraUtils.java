package s.j.l.cassandrautils;

import java.util.Iterator;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;

public class CassandraUtils {
  Cluster cluster = null;
  Session session = null;

  /**
   * 
   * @param ips
   */
  public CassandraUtils(String... ips) {
    cluster = Cluster.builder().addContactPoints(ips)
        .withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
        .withReconnectionPolicy(new ConstantReconnectionPolicy(100L)).build();
    // cluster.getConfiguration().getProtocolOptions().setCompression(ProtocolOptions.Compression.LZ4);
    session = cluster.connect();
  }

  /**
   * 
   * @param cql
   */
  public void getList(String cql) {
    ResultSet rs = session.execute(cql);
    List<Row> dataList = rs.all();
    for (Row row : dataList) {
      System.out.println("==>name: " + row.getString("name"));
      System.out.println("==>age: " + row.getInt("age"));
    }
    rs = null;
  }

  /**
   * Close Resource.
   * 
   * @param obj
   *          as Cluster or as Session
   */
  public void closeResource(Object... obj) {
    for (Object object : obj) {
      if (object instanceof Cluster) {
        ((Cluster) object).close();
      }
      if (object instanceof Session) {
        ((Session) object).close();
      }
    }
  }

}
