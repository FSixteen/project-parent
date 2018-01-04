package com.grandland.janusgraph.core;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.VertexLabel;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.EdgeLabelMaker;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;

import com.grandland.janusgraph.annotation.EdgeLabelEnum;
import com.grandland.janusgraph.annotation.IndexEnum;
import com.grandland.janusgraph.annotation.IndexEnum.Index;
import com.grandland.janusgraph.annotation.IndexEnum.Mapping;
import com.grandland.janusgraph.annotation.PropertyKeyEnum;
import com.grandland.janusgraph.annotation.VertexLabelEnum;

public final class BuildSchema {
  public void execut(JanusGraphManagement mgmt, Class<?> _clazz) {
    Field[] fields = _clazz.getDeclaredFields();
    System.out.println("\n\n");
    System.out.println("---开始处理PropertyKeyEnum注解---");
    for (Field field : fields) {
      String fieldName = field.getName();
      Class<?> fieldClazz = field.getType();
      PropertyKeyEnum propertyKeyEnum = field.getAnnotation(PropertyKeyEnum.class);
      if (null != propertyKeyEnum) {
        String name = propertyKeyEnum.name();
        Class<?> clazz = propertyKeyEnum.dataType().getClazz();
        name = (null == name || "".equals(name)) ? fieldName : name;
        clazz = (null == clazz) ? fieldClazz : clazz;
        System.out.print("开始处理字段:" + fieldName + ",name=" + name + ",dataType=" + clazz);
        createPropertyKey(mgmt, fieldName, clazz);
        System.out.println("......处理完成.");
      }
    }

    System.out.println("---开始处理VertexLabelEnum注解---");
    for (Field field : fields) {
      String fieldName = field.getName();
      VertexLabelEnum vertexLabelEnum = field.getAnnotation(VertexLabelEnum.class);
      if (null != vertexLabelEnum) {
        String name = vertexLabelEnum.name();
        name = (null == name || "".equals(name)) ? fieldName : name;
        System.out.print("开始处理字段:" + fieldName + ",name=" + name);
        makeVertexLabel(mgmt, name);
        System.out.println("......处理完成.");
      }
    }

    System.out.println("---开始处理EdgeLabelEnum注解---");
    for (Field field : fields) {
      String fieldName = field.getName();
      EdgeLabelEnum edgeLabelEnum = field.getAnnotation(EdgeLabelEnum.class);
      if (null != edgeLabelEnum) {
        String name = edgeLabelEnum.name();
        org.janusgraph.core.Multiplicity multiplicity = edgeLabelEnum.multiplicity();
        String signature = edgeLabelEnum.signature();
        name = (null == name || "".equals(name)) ? fieldName : name;
        signature = (null == signature || "".equals(signature)) ? null : signature;
        System.out.print("开始处理字段:" + fieldName + ",name=" + name + ",multiplicity=" + multiplicity + ",signature=" + signature);
        makeVertexLabel(mgmt, name);
        System.out.println("......处理完成.");
      }
    }

    /*
    System.out.println("---开始处理IndexEnum注解---");
    for (Field field : fields) {
      String fieldName = field.getName();
      IndexEnum indexEnum = field.getAnnotation(IndexEnum.class);
      if (null != indexEnum) {
        String name = indexEnum.name();
        boolean unique = indexEnum.unique();
        Index[] indexs = indexEnum.index();
        Mapping mapping = indexEnum.mapping();
        String[] indexList = indexEnum.indexList();
        boolean compositeIndex = indexEnum.compositeIndex();
        ConsistencyModifier consistencyModifier = indexEnum.consistencyModifier();
        boolean mixedIndex = indexEnum.mixedIndex();
        String mixedIndexName = indexEnum.mixedIndexName();
        name = (null == name || "".equals(name)) ? fieldName : name;
        System.out.print("开始处理字段:" + fieldName + ",");
        System.out.print(",Unique值:" + unique);
        System.out.print(",Index值:" + Arrays.toString(indexs));
        System.out.print(",Mapping值:" + mapping);
        System.out.print(",indexList值:" + Arrays.toString(indexList));
        System.out.print(",CompositeIndex值:" + compositeIndex);
        System.out.print(",ConsistencyModifier值:" + consistencyModifier);
        System.out.print(",mixedIndex值:" + mixedIndex);
        System.out.print(",MixedIndexName值:" + mixedIndexName);
        for (Index index : indexs) {
          createIndex(mgmt, name, index, unique, compositeIndex, consistencyModifier, mixedIndex, mixedIndexName, mapping.getMapping(),
              indexList);
          System.out.println("Index:" + index + "创建完成...");
        }
        System.out.println("......处理完成.");
      }
    }
    // */

    System.out.println("---开始处理完整的IndexEnum注解---");
    JanusGraphManagement.IndexBuilder all_vertex = null;
    JanusGraphManagement.IndexBuilder all_edge = null;
    boolean mixedIndex = false;
    String mixedIndexName = null;
    for (Field field : fields) {
      String fieldName = field.getName();
      IndexEnum indexEnum = field.getAnnotation(IndexEnum.class);
      PropertyKey key = mgmt.getPropertyKey(fieldName);
      if (null != indexEnum && null != key) {
        Index[] indexs = indexEnum.index();
        Mapping mapping = indexEnum.mapping();
        if (null == mixedIndexName) {
          mixedIndexName = indexEnum.mixedIndexName();
        }
        if (false == mixedIndex) {
          mixedIndex = indexEnum.mixedIndex();
        }
        for (Index index : indexs) {
          if (index == Index.Vertex) {
            if (null == all_vertex) {
              all_vertex = mgmt.buildIndex("all_vertex", org.apache.tinkerpop.gremlin.structure.Vertex.class);
            }
            if (Mapping.NULL != mapping)
              all_vertex.addKey(key, mapping.getMapping().asParameter());
            else
              all_vertex.addKey(key);
          }
          if (index == Index.Edge) {
            if (null == all_edge) {
              all_edge = mgmt.buildIndex("all_edge", org.apache.tinkerpop.gremlin.structure.Vertex.class);
            }
            if (Mapping.NULL != mapping)
              all_edge.addKey(key, mapping.getMapping().asParameter());
            else
              all_edge.addKey(key);
          }
        }
      }
    }
    if (!mixedIndex) {
      mgmt.setConsistency(all_vertex.buildCompositeIndex(), ConsistencyModifier.LOCK);
      mgmt.setConsistency(all_edge.buildCompositeIndex(), ConsistencyModifier.LOCK);
    } else {
      if (null != mixedIndexName) {
        all_edge.buildMixedIndex(mixedIndexName);
        all_vertex.buildMixedIndex(mixedIndexName);
      }
    }
    System.out.println("---处理完整的IndexEnum注解完成---");
    mgmt.commit();
    System.out.println("\n\n\\^o^/字段创建完成\\^o^/\n\n");
  }

  public PropertyKey createPropertyKey(JanusGraphManagement mgmt, String propertyKeyName, Class<?> dataType) {
    if (null == mgmt)
      throw new NullPointerException("JanusGraphManagement mgmt = null");
    PropertyKey propertyKey = mgmt.getPropertyKey(propertyKeyName);
    if (null == propertyKey) {
      propertyKey = mgmt.makePropertyKey(propertyKeyName).dataType(dataType).make();
    } else {
      System.out.print("PropertyKey:" + propertyKeyName + "已存在，本次不在创建.");
    }
    return propertyKey;
  }

  public VertexLabel makeVertexLabel(JanusGraphManagement mgmt, String name) {
    if (null == mgmt)
      throw new NullPointerException("JanusGraphManagement mgmt = null");
    VertexLabel vertexLabel = mgmt.getVertexLabel(name);
    if (null == vertexLabel) {
      vertexLabel = mgmt.makeVertexLabel(name).make();
    } else {
      System.out.print("VertexLabel:" + name + "已存在，本次不在创建.");
    }
    return vertexLabel;
  }

  public EdgeLabelMaker makeEdgeLabel(JanusGraphManagement mgmt, String name, Multiplicity multiplicity, String signature) {
    if (null == mgmt)
      throw new NullPointerException("JanusGraphManagement mgmt = null");
    EdgeLabelMaker edgeLabelMaker = null;
    if (null == mgmt.getEdgeLabel(name)) {
      edgeLabelMaker = mgmt.makeEdgeLabel(name);
    } else {
      System.out.print("EdgeLabel:" + name + "已存在，本次不在创建.");
    }
    PropertyKey propertyKey = ((null == signature || "".equals(signature)) ? null : mgmt.getPropertyKey(signature));
    edgeLabelMaker.multiplicity((null == multiplicity) ? Multiplicity.MULTI : multiplicity);
    if (null != signature && null != propertyKey)
      edgeLabelMaker.signature(propertyKey);
    edgeLabelMaker.make();
    return edgeLabelMaker;
  }

  public void createIndex(JanusGraphManagement mgmt, String name, Index index, boolean unique, boolean compositeIndex,
      ConsistencyModifier consistencyModifier, boolean mixedIndex, String mixedIndexName, org.janusgraph.core.schema.Mapping mapping,
      PropertyKey... propertyKeys) {
    if (null == mgmt)
      throw new NullPointerException("JanusGraphManagement mgmt = null");
    if (mgmt.getGraphIndex(name) != null) {
      System.out.println("Index:" + name + "已存在，本次不在创建.");
      return;
    }
    if (null != index.getClazz()) {
      JanusGraphManagement.IndexBuilder indexBuilder = mgmt.buildIndex(name, index.getClazz());
      for (PropertyKey propertyKey : propertyKeys) {
        if (null != mapping)
          indexBuilder.addKey(propertyKey, mapping.asParameter());
        else
          indexBuilder.addKey(propertyKey);
      }
      if (unique)
        indexBuilder.unique();
      if (compositeIndex) {
        JanusGraphIndex janusGraphIndex = indexBuilder.buildCompositeIndex();
        mgmt.setConsistency(janusGraphIndex, consistencyModifier);
      } else if (mixedIndex) {
        indexBuilder.buildMixedIndex(mixedIndexName);
      } else {
        ;
      }
    }
  }

  public void createIndex(JanusGraphManagement mgmt, String name, Index index, boolean unique, boolean compositeIndex,
      ConsistencyModifier consistencyModifier, boolean mixedIndex, String mixedIndexName, org.janusgraph.core.schema.Mapping mapping,
      String... propertyKeys) {
    if (null == mgmt)
      throw new NullPointerException("JanusGraphManagement mgmt = null");
    if (mgmt.getGraphIndex((index == Index.Vertex) ? name : name + "_") != null) {
      System.out.println("Index:" + ((index == Index.Vertex) ? name : name + "_") + "已存在，本次不在创建.");
      return;
    }
    if (null != index.getClazz()) {
      JanusGraphManagement.IndexBuilder indexBuilder = mgmt.buildIndex((index == Index.Vertex) ? name : name + "_", index.getClazz());
      for (String propertyKey : propertyKeys) {
        PropertyKey key = mgmt.getPropertyKey(propertyKey);
        if (null != mapping)
          indexBuilder.addKey(key, mapping.asParameter());
        else
          indexBuilder.addKey(key);
      }
      if (unique)
        indexBuilder.unique();
      if (compositeIndex) {
        JanusGraphIndex janusGraphIndex = indexBuilder.buildCompositeIndex();
        mgmt.setConsistency(janusGraphIndex, consistencyModifier);
      } else if (mixedIndex) {
        indexBuilder.buildMixedIndex(mixedIndexName);
      } else {
        ;
      }
    }
  }

}
