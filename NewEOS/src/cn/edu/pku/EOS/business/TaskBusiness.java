package cn.edu.pku.EOS.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import cn.edu.pku.EOS.DAO.CrawlerTaskDao;
import cn.edu.pku.EOS.entity.CrawlerTask;
import cn.edu.pku.EOS.exception.CrawlerNodeNotRespondingException;
import cn.edu.pku.EOS.nodedispatch.KVPair;
import cn.edu.pku.EOS.nodedispatch.MessageSender;
import cn.edu.pku.EOS.nodedispatch.NodeManager;
/**
 * 查看爬去任务状态的相关处理逻辑
 * @author 张灵箫
 *
 */
public class TaskBusiness {
	private static final Logger logger = Logger.getLogger(ThisReference.class.getName());
	
	
	/**
	 * 查看任务状态，从数据库读取状态
	 * 如果正在进行，那么要查看子节点是否有该正在运行
	 * 没有或者无法连接搜算任务异常终止
	 * @author 张灵箫
	 * @param taskuuid
	 * @return
	 */
	public static int checkTaskStatus(String taskuuid) {
//		CrawlerTask crawlerTask;
//		try {
//			crawlerTask = CrawlerTaskDao.getTaskByUuid(taskuuid);
//		} catch (SQLException e) {
//			logger.info("db error: retrieving CrawlerTask failed!");
//			e.printStackTrace();
//			return -1;
//		}
//		if (crawlerTask.getStatus() != 0) {
//			return crawlerTask.getStatus();
//		}
//		List<KVPair> params = new ArrayList<KVPair>();
//		params.add(new KVPair("taskuuid", taskuuid));
//		try {
//			int result = Integer.parseInt(MessageSender.sendCommand(crawlerTask.getCrawlerNode(), NodeManager.getCheckTaskCommand(), params));
//			if (result != CrawlerTask.ERROR) {
//				return result;
//			} else {
//				CrawlerTaskDao.updateTaskStatus(taskuuid, CrawlerTask.ERROR, null);
//			}
//		} catch (CrawlerNodeNotRespondingException e) {
//			e.printStackTrace();
//			try {
//				CrawlerTaskDao.updateTaskStatus(taskuuid, CrawlerTask.ERROR, null);
//			} catch (SQLException e1) {
//				logger.info("db error: cannot report error task!");
//				e1.printStackTrace();
//			}
//		} catch (SQLException e) {
//			logger.info("db error: cannot report error task!");
//			e.printStackTrace();
//		}
		return CrawlerTask.ERROR;
	}


	public static CrawlerTask getTaskByProjectAndType(String puuid, String type) {
		try {
			return CrawlerTaskDao.getTaskByProjectAndType(puuid, type);
		} catch (SQLException e) {
			logger.error("DataBase Error Getting Task!");
			return null;
		}
	}
}
