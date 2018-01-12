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
 *       JanusGraph属性信息。
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyKeyEnum {
  /**
   * JanusGraph属性名称。
   * 
   * @return
   */
  public String name() default "";

  /**
   * JanusGraph属性类型。
   * 
   * @return
   */
  public DataType dataType() default PropertyKeyEnum.DataType.NULL;

  /**
   * @author Shengjun Liu</br>
   * @time 2017.11.21</br>
   *       JanusGraph属性类型枚举。
   */
  public enum DataType {
    Byte(java.lang.Byte.class), Short(java.lang.Short.class), Char(java.lang.Character.class),

    Integer(java.lang.Integer.class), Long(java.lang.Long.class), Float(java.lang.Float.class),

    Double(java.lang.Double.class), Boolean(java.lang.Boolean.class), String(java.lang.String.class),

    Enum(java.lang.Enum.class), Class(java.lang.Class.class), NULL(null);

    private Class<?> clazz;

    private DataType(Class<?> clazz) {
      this.clazz = clazz;
    }

    public Class<?> getClazz() {
      return clazz;
    }
  };
}