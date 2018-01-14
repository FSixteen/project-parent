package s.j.liu.hbaseutils;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

/**
 * @version v0.0.1
 * @since 2017-07-10 09:45:01
 * @author Shengjun Liu
 *
 */
public class BaseConnectionO {
	private static Configuration conf;
	private static Connection connection;

	static {
		reConnection();
	}

	private BaseConnectionO() {
	}

	public static Table getTable(String table_name) {
		return getTable(TableName.valueOf(table_name));
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

	public static void reConnection() {
		try {
			conf = new Configuration();
			conf.addResource(new Path("hbase-site.xml"));
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
