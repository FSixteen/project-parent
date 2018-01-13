package s.l;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 */
public class Main {

	/**
	 * 程序入口
	 * 
	 * @param args
	 *            参数组
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		DHTClient dhtClient = new DHTClient("87.98.162.88", 6881);
		// DHTClient dhtClient = new DHTClient("87.98.162.88", 6881);
		// DHTClient dhtClient = new DHTClient("189.239.191.189", 3823);
		for (int i = 0; i < 20; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<Node> nodeList = SpiderUtils.getNodesInfo(dhtClient.findNodeOnDHT(SpiderUtils.buildNodeId()));
			for (Node node : nodeList) {
				Table.appendNode(node);
			}
		}
		for (Bucket bucket : Table.getBuckets()) {
			for (Node node : bucket.getNodes()) {
				new Thread(new NodeFinder(node.getIp(), node.getPort(), node.getId())).start();
			}
		}
	}
}