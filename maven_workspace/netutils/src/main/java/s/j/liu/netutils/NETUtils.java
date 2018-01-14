package s.j.liu.netutils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class NETUtils {
	private CloseableHttpClient httpClient;
	private RequestConfig defaultRequestConfig = null;
	private PoolingHttpClientConnectionManager connManager = null;

	public NETUtils(int setConnectionRequestTimeout, int setSocketTimeout, int setConnectTimeout) {
		connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(1000);
		connManager.setDefaultMaxPerRoute(200);
		defaultRequestConfig = RequestConfig.custom().setConnectionRequestTimeout(setConnectionRequestTimeout)
				.setSocketTimeout(setSocketTimeout) // 数据传输超时时间
				.setConnectTimeout(setConnectTimeout).build(); // 连接超时时间
		httpClient = HttpClients.custom().setConnectionManager(connManager)
				.setDefaultRequestConfig(defaultRequestConfig).build();
	}
	
	public static  void main(String [] args) {
	  NETUtils n = new NETUtils(3000, 3000, 3000);
	  byte [] s = n.getGetBytes("http://mat1.gtimg.com/fashion/images/index/2017/09/07/tpz1.jpg");
	  System.out.println(s.length);
  }

	/**
	 * 执行请求
	 * 
	 * @param url
	 * @return
	 */
	public byte[] getGetBytes(String url) {
		byte[] result = null;
		if (url.startsWith("ftp")) {
			try {
				result = ftpDownload(url);
			} catch (Exception e) {
			}
		} else if (url.startsWith("http") || url.startsWith("https")) {
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httpGet);
				int status = response.getStatusLine().getStatusCode();
				if (status == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					result = EntityUtils.toByteArray(entity);
					EntityUtils.consume(entity);
				} else {
					httpGet.abort();
				}
			} catch (ConnectTimeoutException e) {
				System.out.println("连接超时...");
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			} finally {
				if (response != null) {
					try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			result = null;
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

	/**
	 * 下载文件(调用之前需要先调用set方法设置FTP服务器地址等信息)
	 *
	 * @param remoteUrl
	 * @return
	 * @throws Exception
	 */
	public byte[] ftpDownload(String remoteUrl) throws Exception {
		final Pattern FTP_PATTERN = Pattern.compile("^ftp://(\\S*):(\\S*)@(\\d+.\\d+.\\d+.\\d+):(\\d+)");
		byte[] result = null;
		String username = "";
		String password = "";
		String hostname = "";
		String hostport = "";
		String imagePath = "";
		FTPClient ftp = new FTPClient();
		try {
			Matcher matcher = null;
			if (remoteUrl.indexOf("@") != -1) { // 带有用户名密码的地址
				matcher = FTP_PATTERN.matcher(remoteUrl);
				if (matcher.find()) {
					username = matcher.group(1);
					password = matcher.group(2);
					hostname = matcher.group(3);
					hostport = matcher.group(4);
					imagePath = remoteUrl.replace(matcher.group(0), "");
					int pos = imagePath.indexOf("/");
					if (pos != 0) {
						imagePath = imagePath.substring(pos);
					}
				}
				try {
					Integer.valueOf(hostport);
				} catch (Exception e) {
					hostport="21";
				}
				ftp.connect(hostname, Integer.valueOf(hostport));
				ftp.enterLocalPassiveMode(); // 设置为被动模式传输
				ftp.setControlEncoding("UTF-8"); // 设置编码格式
				ftp.setFileType(FTP.BINARY_FILE_TYPE); // 设置以二进制方式传输
				ftp.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE); // 设置为文件流传输模式
				ftp.setBufferSize(10240);
				ftp.setKeepAlive(true);
				ftp.setDefaultTimeout(3000);
				ftp.setConnectTimeout(3000);
				ftp.setDataTimeout(3000);
				ftp.login(username, password);
				ftp.changeWorkingDirectory("/");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ftp.retrieveFile(imagePath, out);
				result = out.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftp != null && ftp.isConnected()) {
				ftp.logout();
				ftp.disconnect();
			}
		}
		return result;
	}

}
