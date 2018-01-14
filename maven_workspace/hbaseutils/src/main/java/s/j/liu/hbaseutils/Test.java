package s.j.liu.hbaseutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class Test {
	public static void main(String[] args) throws IOException {
		BaseConnectionZ.reConnection("139.199.18.29", "2181");
		System.out.println("1111111");
		get();
	}

	public static void get() {
		Table table = BaseConnectionZ.getTable("phone_pass");
		Scan scan = new Scan();
		scan.setMaxVersions(1);
		try {
			scan.setStartRow(Bytes.toBytes("bcde,a,0000"));
			scan.setStopRow(Bytes.toBytes("bcde,a,ZZZZ"));
			scan.setTimeRange(151348271801L, 151348396139L);
			ResultScanner rs = table.getScanner(scan);
			rs.forEach(row -> {
				System.out.println(Bytes.toString(row.getRow()));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void add() {
		String[] row = new String[] { "abc,wag,", "bcd,abc,", "abcd,wag,", "abc,wagg,", "abcd,wag,", "bcde,a," };
		try {
			HTable table = new HTable(TableName.valueOf("phone_pass"), BaseConnectionZ.getConnection());
			List<Put> puts = new ArrayList<Put>();
			for (long i = 151348270900L; i < 151348400900L; i += 901) {
				System.out.println(row[(int) (i % 6)] + i);
				Put put = new Put(Bytes.toBytes(row[(int) (i % 6)] + i), i);
				put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("tr"), Bytes.toBytes(""));
				puts.add(put);
			}
			try {
				System.out.println("------------------------");
				table.put(puts);
				System.out.println("------------------------");
				table.close();
				System.out.println("------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
