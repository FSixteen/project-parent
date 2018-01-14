package allproject;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import s.j.liu.dbutils.MysqlUtils;
import s.j.liu.dbutils.OracleUtils;

public class Test {
  public static void main(String[] args) throws IOException {
     MysqlUtils mysql = new MysqlUtils("ip", "3306", "db", "user", "password");
     Connection con = mysql.getMysqlConnection();

     con = MysqlUtils.getMysqlConnection("ip", "3306", "db", "user", "password");

    Connection con1 = OracleUtils.getOracleServerConnection("ip", "3306", "db", "user", "password");
    
    Connection con2 = OracleUtils.getOracleSidConnection("ip", "3306", "db", "user", "password");
    

    /**
     * id     name       age
     * 1      name2      12
     * 2      name1      21
     */
    String sql = "select * from test where id = ? and name = ?";
    List<Map<String, String>> list = MysqlUtils.getResultListMap(con, sql, "id", "name");
    list.forEach(x->{
      x.get("id");
      x.get("name");
      x.get("age");
    });

    /**
     *  message_id
     *     1
     *     2
     */
    sql = "select message_id as id from test where id = ? and name = ?";
    List<String> list1 = MysqlUtils.getResultList(con, sql, "id", "id", "name");
    list1.forEach(x->{
      x = x;
    });
    
    /**
     *    id      name
     *     1     liusj
     */
    sql = "select id , name from test where id = ? and name = ?";
    Map<String, String> map = MysqlUtils.getResultMap(con, sql, "id" , "name", "id", "name");
    for(String key : map.keySet()){
      System.out.println("key:"+ key + "    value:"+ map.get(key));
    }
    
    /**
     *  message_id
     *     1
     */
    sql = "select message_id from test where id = ? and name = ?";
    String id = MysqlUtils.getResultString(con, sql, "message_id", "id", "name");
    

    sql = "insert into test(col,col2,col3)value(?,?,?)";
    long i = MysqlUtils.getResultLong(con, sql, "id", "name", "age");

  }
}
