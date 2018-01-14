package s.j.liu.utils;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.jexl2.JexlEngine;

/**
 * 
 * @author LiuShengJun
 * @since 2017-04-06 15:13:42
 */
public class Utils {

  /**
   * from properties 2 static class
   * 
   * @param _clazz
   *          Static class class
   * @param _file_path
   *          properties file's path
   * @param accessible
   * @param debug
   * @param type
   *          0:By_class;other:By_properties
   * @throws Exception
   */
  public static void initProperties(Class<?> _clazz, String _file_path, boolean accessible,
      boolean debug, int type) throws Exception {
    Properties properties = new Properties(); // 创建properties工具类实例
    properties.load(new FileInputStream(_file_path));// 加载properties文件
    Class<?> clazz = _clazz;// 获取类对象Class
    Object object = clazz.newInstance();// 实例化类对象
    Field[] fields = null;// 获取类中所有字段
    if (type == 0) {
      fields = clazz.getDeclaredFields();// 获取类中所有字段
    } else {
      fields = getDeclaredFieldsByProperties(properties, clazz);
    }
    setFieldValue(fields, properties, object, accessible);
    if (debug)
      getFieldValue(fields, object);
  }

  /**
   * from properties 2 standard class
   * 
   * @param _object
   *          Standard object class
   * @param _file_path
   *          properties file's path
   * @param accessible
   * @param debug
   * @throws Exception
   */
  public static void initProperties(Object _object, String _file_path, boolean accessible,
      boolean debug, int type) throws Exception {
    Properties properties = new Properties(); // 创建properties工具类实例
    properties.load(new FileInputStream(_file_path));// 加载properties文件
    Class<?> clazz = _object.getClass();// 获取类对象Class
    Object object = _object;// 实例化类对象
    Field[] fields = null;// 获取类中所有字段
    if (type == 0) {
      fields = clazz.getDeclaredFields();// 获取类中所有字段
    } else {
      fields = getDeclaredFieldsByProperties(properties, clazz);
    }
    setFieldValue(fields, properties, object, accessible);
    if (debug)
      getFieldValue(fields, object);
  }

  /**
   * Get Field in properties 2 Field[]
   * 
   * @param properties
   *          Properties
   * @param clazz
   *          Class<?>
   * @return
   */
  private static Field[] getDeclaredFieldsByProperties(Properties properties, Class<?> clazz) {
    Enumeration<?> element = properties.propertyNames();// 得到配置文件中的名字
    List<Field> fieldList = new ArrayList<Field>();
    while (element.hasMoreElements()) {
      Field field = null;
      try {
        field = clazz.getDeclaredField((String) element.nextElement());
      } catch (Exception e) {
      } // 获取类中字段
      if (field != null)
        fieldList.add(field);
    }
    Field[] fields = new Field[fieldList.size()];
    for (int i = 0; i < fieldList.size(); i++) {
      fields[i] = fieldList.get(i);
    }
    fieldList.clear();
    return fields;
  }

  /**
   * set Field Value from properties 2 object
   * 
   * @param fields
   * @param properties
   * @param object
   * @param accessible
   */
  private static void setFieldValue(Field[] fields, Properties properties, Object object,
      boolean accessible) {
    for (Field field : fields) {// 遍历所有类中字段
      field.setAccessible(accessible);
      if (field.getType().getName().indexOf("int") != -1) {// 如果字段类型为int
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, Integer.parseInt(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("java.lang.String") != -1) {// 如果字段类型为java.lang.String
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, properties.getProperty(field.getName()));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("Integer") != -1) {// 如果字段类型为Integer
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, new Integer(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("boolean") != -1) {// 如果字段类型为boolean
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, Boolean.parseBoolean(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("java.lang.Boolean") != -1) {// 如果字段类型为java.lang.Boolean
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, new Boolean(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("char") != -1) {// 如果字段类型为char
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, properties.getProperty(field.getName()).toCharArray()[0]);
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("java.lang.Character") != -1) {// 如果字段类型为java.lang.Character
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object,
                new Character(properties.getProperty(field.getName()).toCharArray()[0]));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("float") != -1) {// 如果字段类型为float
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, Float.parseFloat(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("java.lang.Float") != -1) {// 如果字段类型为java.lang.Float
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, new Float(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("double") != -1) {// 如果字段类型为double
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, Double.parseDouble(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("java.lang.Double") != -1) {// 如果字段类型为java.lang.Double
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, new Double(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("long") != -1) {// 如果字段类型为long
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, Long.parseLong(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else if (field.getType().getName().indexOf("java.lang.Long") != -1) {// 如果字段类型为java.lang.Long
        if (properties.getProperty(field.getName()) != null)
          try {
            field.set(object, new Long(properties.getProperty(field.getName())));
          } catch (Exception e) {
          }
      } else {
        ;
      }
    }
  }

  /**
   * output class's content(fields's value)
   * 
   * @param fields
   * @param object
   * @throws Exception
   */
  private static void getFieldValue(Field[] fields, Object object) throws Exception {
    getFieldValue(fields, object, 20);
  }

  /**
   * output class's content(fields's value)
   * 
   * @param fields
   * @param object
   * @throws Exception
   */
  private static void getFieldValue(Field[] fields, Object object, int label_width)
      throws Exception {
    for (Field field : fields) {
      System.out.printf("%-" + label_width + "s", field.getName());
      System.out.println(":" + field.get(object));
    }
  }

  /**
   * String 2 Object
   * 
   * @param argument
   *          String argument
   * @return Object
   */
  public static Object eval(String argument) {
    return new JexlEngine().createExpression(argument).evaluate(null);
  }
}