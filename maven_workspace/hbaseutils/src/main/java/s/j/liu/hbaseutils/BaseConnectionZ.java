package s.j.liu.hbaseutils;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

/**
 * @version v0.0.1
 * @since 2017-07-10 09:45:01
 * @author Shengjun Liu
 */
public class BaseConnectionZ {
	private static Configuration conf;
	private static Connection connection;

	private BaseConnectionZ() {
	}

	public static Table getTable(String table_name) {
		return getTable(TableName.valueOf(table_name));
	}

	public static Connection getConnection() {
		return connection;
	}

	public static Table getTable(TableName table_name) {
		try {
			return connection.getTable(table_name);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Admin getAdmin() {
		try {
			return connection.getAdmin();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void reConnection(String zk, String port) {
		try {
			conf = new Configuration();
			conf.set("hbase.zookeeper.quorum", zk);
//			conf.set("hbase.zookeeper.property.clientPort", port);
			connection = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close() {
		try {
			if (null != connection && !connection.isClosed()) {
				connection.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
