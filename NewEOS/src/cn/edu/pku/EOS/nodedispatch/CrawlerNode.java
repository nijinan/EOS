package cn.edu.pku.EOS.nodedispatch;

/**
 * 表示一个爬虫节点的类，包括节点IP地址和节点的爬虫类型
 * 爬虫和IP地址应该是多对多的关系
 * @author 张灵箫
 *
 */
public class CrawlerNode {
	private String crawlerType;
	private String hostAddress;
	public CrawlerNode(String ip, String crawlerClassName) {
		crawlerType = crawlerClassName;
		hostAddress = ip;
	}
	public String getCrawlerType() {
		return crawlerType;
	}
	public void setCrawlerType(String crawlerType) {
		this.crawlerType = crawlerType;
	}
	public String getHostAddress() {
		return hostAddress;
	}
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}
	
	
	
}
