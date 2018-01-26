package com.xyshzh.dht.bucket;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

import com.xyshzh.dht.NodeInfo;

public class Bucket {
  /**
   * 桶的覆盖范围最大为0到2的160次方,因为节点id为20字节
   */
  public static final BigInteger MAX_VALUE = new BigInteger("2").pow(160);
  /**
   * 当前桶的开始位置,其他节点的id值在开始位置(包含)和结束位置(不包含)之间则会被放入到当前桶中
   */
  private BigInteger start;
  /**
   * 当前桶的结束位置,其他节点的id值在开始位置(包含)和结束位置(不包含)之间则会被放入到当前桶中
   */
  private BigInteger end;
  /**
   * 当前桶中保存的节点,每个桶最多保存8个节点,超过8个节点后自动移除最旧节点,同时同一节点多次重复添加不会影响其他节点,这里使用LinkedHashMap作为LRU算法的简单实现
   */
  private final Map<BigInteger, NodeInfo> nodes = new LinkedHashMap<BigInteger, NodeInfo>();

}
