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
 * 
 *       JanusGraph边标签信息。
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EdgeLabelEnum {
  /**
   * JanusGraph边标签名称.
   * 
   * @return
   */
  public String name() default "";

  /**
   * JanusGraph边对应关系(1:1,1:N,N:1,N:N).
   * 
   * @return
   */
  public org.janusgraph.core.Multiplicity multiplicity() default org.janusgraph.core.Multiplicity.MULTI;

  /**
   * JanusGraph边签名信息.
   * 
   * @return
   */
  public String signature() default "";
}