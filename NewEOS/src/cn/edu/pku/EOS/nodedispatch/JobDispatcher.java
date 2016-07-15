package cn.edu.pku.EOS.nodedispatch;

import java.util.ArrayList;
import java.util.List;


import cn.edu.pku.EOS.entity.CrawlerTask;
import cn.edu.pku.EOS.exception.CrawlerNodeNotRespondingException;
import cn.edu.pku.EOS.exception.CrawlerNotInNodeListException;

/**
 * 把爬取任务分配给不同的节点
 * @author 张灵箫
 *
 */
public class JobDispatcher {

	List<CrawlerNode> nodes = null;
	
	public JobDispatcher(String crawlerClassName) throws CrawlerNotInNodeListException {
		nodes = new ArrayList<CrawlerNode>();
		List<String> ipList = NodeManager.getNodeList(crawlerClassName);
		if (ipList == null) {
			return;
		}
		for (String ip : ipList) {
			nodes.add(new CrawlerNode(ip, crawlerClassName));
		}
	}

	/**
	 * 以随机方式分配任务到某个节点
	 * @author 张灵箫
	 * @param resoucetype
	 * @param project
	 * @return
	 * @throws CrawlerNodeNotRespondingException 
	 * @throws CrawlerNotInNodeListException 
	 */
	public String dispatchCrawlerJob(CrawlerTask crawlerTask) throws CrawlerNodeNotRespondingException {
		int nodenum = nodes.size();
		int chosen = (int) (Math.random() * nodenum);
		String host = nodes.get(chosen).getHostAddress();
		crawlerTask.setCrawlerNode(host);
		String commandName = NodeManager.getCrawlCommand();
		List<KVPair> params = new ArrayList<KVPair>();
		params.add(new KVPair("type", crawlerTask.getResourceType()));
		params.add(new KVPair("projectuuid", crawlerTask.getProjectUuid()));
		params.add(new KVPair("taskuuid", crawlerTask.getUuid()));
		return MessageSender.sendCommand(host, commandName, params);
	}

}

