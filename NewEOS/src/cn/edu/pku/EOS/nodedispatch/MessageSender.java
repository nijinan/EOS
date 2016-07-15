package cn.edu.pku.EOS.nodedispatch;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import cn.edu.pku.EOS.exception.CrawlerNodeNotRespondingException;


/**
 * 实现节点间的底层通信协议
 * @author 张灵箫
 *
 */
public class MessageSender {

	public static final String REMOTE_SUCCESS_RESPONCE = "success";
	private static final Logger logger = Logger.getLogger(ThisReference.class.getName());
	/**
	 * 发送http请求到host
	 * @author 张灵箫
	 * @param host
	 * @param commandName
	 * @param params
	 * @return
	 * @throws CrawlerNodeNotRespondingException 
	 */
	public static String sendCommand(String host, String commandName,
			List<KVPair> params) throws CrawlerNodeNotRespondingException {
		String result = null;
		
		//得到http请求的url格式，含参数
		String httpRequestUrl = "http://" + host + "/" + NodeManager.getNodeServiceName() + "/" + commandName + "?";
		for (KVPair kvPair : params) {
			httpRequestUrl += kvPair.getKey() + "=" +kvPair.getValue() + "&";
		}
		httpRequestUrl = httpRequestUrl.substring(0, httpRequestUrl.length()-1);
		logger.info("Requesting url: " +httpRequestUrl);
		
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(httpRequestUrl);
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(0, false));

		try {
			// Execute the method.
			int statusCode = client.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + post.getStatusLine());
			}
			byte[] responseBody = post.getResponseBody();
			result = new String(responseBody);
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
			throw new CrawlerNodeNotRespondingException(host);
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
			throw new CrawlerNodeNotRespondingException(host);
		} finally {
			// Release the connection.
			post.releaseConnection();
		}
		
		return result;
	}

}
