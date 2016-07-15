package cn.edu.pku.EOS.nodedispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import cn.edu.pku.EOS.exception.CrawlerNotInNodeListException;
/**
 * 
 * @author 灵箫
 * 从配置文件爬虫节点
 */
public class NodeManager {
		// 属性文件标识符
		private static String NODELIST_FILE_NAME = "cn.edu.pku.EOS.nodedispatch.crawlerNodes";

		// 所使用的ResourceBundle
		private static ResourceBundle bundle;

		// 静态私有方法，用于从属性文件中取得属性值
		static {
			try {
				//System.out.println("init config file!");
				bundle = ResourceBundle.getBundle(NODELIST_FILE_NAME);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		private static String getValue(String key) {
			try {
				return bundle.getString(key);
			} catch (Exception e) {
				return null;
			}
		}
		
		public static List<String> getNodeList(String crawlerClass) throws CrawlerNotInNodeListException {
			List<String> nodeList = new ArrayList<String>();
			if (getValue(crawlerClass) == null) {
				throw new CrawlerNotInNodeListException();
			}
			String[] nodes = getValue(crawlerClass).split(";");
			for (String string : nodes) {
				nodeList.add(string);
			}
			return nodeList;
		}
		
		public static String getNodeServiceName() {
			return getValue("servicename");
		}
		
		public static String getCheckTaskCommand() {
			return getValue("checktaskcommand");
		}
		
		public static String getCrawlCommand() {
			return getValue("crawlcommand");
		}
		
		public static void main(String[] args) {
//			//List<String> node = getNodeList("TestCrawler");
//			for (String string : node) {
//				System.out.println(string);
//			}
		}
		
}
