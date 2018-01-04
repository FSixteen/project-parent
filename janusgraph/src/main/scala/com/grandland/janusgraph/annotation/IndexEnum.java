package com.grandland.janusgraph.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Shengjun Liu<br/>
 * @time 2017.11.21<br/>
 *       JanusGraph索引信息。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IndexEnum {
  /**
   * @author Shengjun Liu<br/>
   * @time 2017.11.21<br/>
   *       JanusGraph索引类型枚举
   */
  public enum Index {
    Vertex(org.apache.tinkerpop.gremlin.structure.Vertex.class),

    Edge(org.apache.tinkerpop.gremlin.structure.Edge.class);

    private Class<? extends org.apache.tinkerpop.gremlin.structure.Element> clazz;

    private Index(Class<? extends org.apache.tinkerpop.gremlin.structure.Element> clazz) {
      this.clazz = clazz;
    }

    public Class<? extends org.apache.tinkerpop.gremlin.structure.Element> getClazz() {
      return clazz;
    }
  }

  /**
   * @author Shengjun Liu<br/>
   * @time 2017.11.21<br/>
   *       JanusGraph索引映射类型.
   */
  public enum Mapping {
    DEFAULT(org.janusgraph.core.schema.Mapping.DEFAULT), TEXT(org.janusgraph.core.schema.Mapping.TEXT),

    STRING(org.janusgraph.core.schema.Mapping.STRING), TEXTSTRING(org.janusgraph.core.schema.Mapping.TEXTSTRING),

    PREFIX_TREE(org.janusgraph.core.schema.Mapping.PREFIX_TREE), NULL(null);
    private org.janusgraph.core.schema.Mapping mapping;

    private Mapping(org.janusgraph.core.schema.Mapping mapping) {
      this.mapping = mapping;
    }

    public org.janusgraph.core.schema.Mapping getMapping() {
      return mapping;
    }
  }

  /**
   * JanusGraph索引名称。
   * 
   * @return
   */
  public String name() default "";

  /**
   * JanusGraph索引唯一性约束(true or false).
   * 
   * @return
   */
  public boolean unique() default false;

  /**
   * JanusGraph索引类型(Vertex or Edge)。
   * 
   * @return
   */
  public Index[] index();

  /**
   * JanusGraph索引映射类型.
   * 
   * @return
   */
  public Mapping mapping() default IndexEnum.Mapping.NULL;

  /**
   * JanusGraph索引属性列表.
   * 
   * @return
   */
  public String[] indexList() default {};

  /**
   * JanusGraph组合索引类型开关(true or false).
   * 
   * @return
   */
  public boolean compositeIndex() default false;

  /**
   * JanusGraph组合索引创建事务类型.
   * 
   * @return
   */
  public org.janusgraph.core.schema.ConsistencyModifier consistencyModifier() default org.janusgraph.core.schema.ConsistencyModifier.DEFAULT;

  /**
   * JanusGraph混合索引类型开关(true or false).
   * 
   * @return
   */
  public boolean mixedIndex() default false;

  /**
   * JanusGraph混合索引名称.
   * 
   * @return
   */
  public String mixedIndexName() default "search";

}
