package s.j.liu.netutils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class NETPostFileUtils {
	private CloseableHttpClient httpClient;
	private RequestConfig defaultRequestConfig = null;
	private PoolingHttpClientConnectionManager connManager = null;

	public NETPostFileUtils(int setConnectionRequestTimeout, int setSocketTimeout, int setConnectTimeout) {
		connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(1000);
		connManager.setDefaultMaxPerRoute(200);
		defaultRequestConfig = RequestConfig.custom().setConnectionRequestTimeout(setConnectionRequestTimeout)
				.setSocketTimeout(setSocketTimeout) // 数据传输超时时间
				.setConnectTimeout(setConnectTimeout).build(); // 连接超时时间
		httpClient = HttpClients.custom().setConnectionManager(connManager)
				.setDefaultRequestConfig(defaultRequestConfig).build();
	}

	/**
	 * 
	 * @param url
	 * @param file
	 * @return
	 */
	public byte[] getPostBytes(String url, byte[] file) {
		byte[] result = null;
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			// 装填参数
			MultipartEntityBuilder body = MultipartEntityBuilder.create();
			if (file != null && file.length > 100) {
				body.addBinaryBody("image_files", file, ContentType.DEFAULT_BINARY, "image");
			}
			HttpEntity entity = body.build();
			httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				result = EntityUtils.toByteArray(responseEntity);
				EntityUtils.consume(responseEntity);
			} else {
				httpPost.abort();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * closeResources
	 * 
	 * @return
	 */
	public boolean closeResources() {
		try {
			if (httpClient != null)
				httpClient.close();
			if (connManager != null)
				connManager.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
