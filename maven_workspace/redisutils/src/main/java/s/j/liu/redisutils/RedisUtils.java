package s.j.liu.redisutils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;

/**
 * @version v0.0.1
 * @since 2017-07-02 11:23:00
 * @author Shengjun Liu
 *
 */
public class RedisUtils {
  private String sentinelHost = null;
  private String sentinelName = null;
  private int sentinelPort = 0;
  private Set<String> set = new HashSet<String>();
  private JedisSentinelPool pool = null;
  private Jedis localJedis = null;
  private Pipeline localPipeline = null;
  private int localJedisIndex = 0;

  /**
   * Construction Method.
   */
  public RedisUtils() {
  }

  /**
   * Construction Method.
   * 
   * @param sentinelHost
   *          Redis Host
   * @param sentinelName
   *          Redis Name
   * @param sentinelPort
   *          Redis Port
   */
  public RedisUtils(String sentinelHost, String sentinelName, int sentinelPort) {
    super();
    this.sentinelHost = sentinelHost;
    this.sentinelName = sentinelName;
    this.sentinelPort = sentinelPort;
    String[] hosts = this.sentinelHost.split(",");
    for (String host : hosts) {
      this.set.add(String.valueOf(new HostAndPort(host, this.sentinelPort)));
    }
    this.pool = new JedisSentinelPool(this.sentinelName, this.set);
  }

  /**
   * Create Local Jedis.
   * 
   * @param index
   *          Index
   */
  public Jedis createLocalJedis(int index) {
    if (null == this.localJedis) {
      this.localJedisIndex = index;
      this.localJedis = getJedis();
      this.localJedis.select(this.localJedisIndex);
    } else {
      if (this.localJedisIndex != index) {
        releaseLocalJedis();
        createLocalJedis(index);
      }
    }
    return this.localJedis;
  }

  /**
   * Release Local Jedis.
   */
  public void releaseLocalJedis() {
    if (null != this.localPipeline) {
      commitLocalJedis2Server();
    }
    releaseJedis(this.localJedis);
    this.localJedisIndex = 0;
    this.localJedis = null;
  }

  /**
   * Get Value.
   * 
   * @param key
   *          Key
   * @param index
   *          Index
   * @return String
   */
  public String getValue(String key, int index) {
    if (null == this.localJedis || this.localJedisIndex != index) {
      createLocalJedis(index);
    }
    return this.localJedis.get(key);
  }

  /**
   * Set Key Value 2 Local Jedis.
   * 
   * @param key
   *          Key
   * @param value
   *          Value
   * @param index
   *          Index
   * @throws Exception
   *           Exception
   */
  public void setKeyValue2LocalJedis(String key, String value, int index) throws Exception {
    if (null == this.localJedis || this.localJedisIndex != index) {
      createLocalJedis(index);
    }
    if (null == this.localPipeline && null != this.localJedis) {
      this.localPipeline = this.localJedis.pipelined();
      this.localPipeline.multi();
    }
    if (null != this.localPipeline) {
      this.localPipeline.set(key, value);
    }
  }

  /**
   * Commit Local Jedis 2 Server.
   */
  public void commitLocalJedis2Server() {
    if (null != this.localPipeline) {
      this.localPipeline.exec();
      this.localPipeline.sync();
      try {
        this.localPipeline.close();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        this.localPipeline = null;
      }
    }
  }

  /**
   * Get Jedis.
   *
   * @return Jedis
   */
  public Jedis getJedis() {
    return pool.getResource();
  }

  /**
   * Release Jedis.
   * 
   * @param jedis
   *          Jedis Object
   */
  public void releaseJedis(Jedis jedis) {
    jedis.close();
  }

  /**
   * Close Pool.
   */
  public void closePool() {
    pool.close();
  }
}
