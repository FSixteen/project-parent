package com.grandland.janusgraph.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Shengjun Liu</br>
 * @time 2017.11.21</br>
 * 
 *       JanusGraph顶点信息。
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VertexLabelEnum {
  /**
   * JanusGraph顶点名称。
   * 
   * @return
   */
  public String name() default "";
}