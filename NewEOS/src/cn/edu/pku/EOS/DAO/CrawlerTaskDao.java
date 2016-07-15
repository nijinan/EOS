package cn.edu.pku.EOS.DAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import cn.edu.pku.EOS.entity.CrawlerTask;

public class CrawlerTaskDao {
	
	
	public static int insertCrawlerTask(CrawlerTask crawlerTask) throws SQLException {
		int result = DAOUtils.update("INSERT INTO crawlerTask (uuid, projectUuid, crawlerNode, resourceType, startTime) VALUES (?,?,?,?,?)",
		                         crawlerTask.getUuid(), crawlerTask.getProjectUuid(), crawlerTask.getCrawlerNode(), crawlerTask.getResourceType(), crawlerTask.getStartTime());
		return result;
	}
	
	public static List<CrawlerTask> getUnfinishedCrawlerTask() throws SQLException{
		List<CrawlerTask> crawlerTasks = DAOUtils.getResult(CrawlerTask.class, "select * from crawlerTask where status = ?", 0);
		return crawlerTasks;
	}
	
	public static int updateTaskStatus(String uuid, int status, Date finishTime) throws SQLException {
		int result = DAOUtils.update("UPDATE crawlerTask SET status = ?, finishTime = ? WHERE uuid = ?", 
				status, finishTime, uuid);
		return result;
	}
	

	public static CrawlerTask getTaskByUuid(String taskuuid) throws SQLException{
		List<CrawlerTask> crawlerTasks = DAOUtils.getResult(CrawlerTask.class, "select * from crawlerTask where uuid = ?", taskuuid);
		return crawlerTasks.get(0);
	}
	
	public static CrawlerTask getTaskByProjectAndType(String puuid, String type) throws SQLException{
		List<CrawlerTask> crawlerTasks = DAOUtils.getResult(CrawlerTask.class, "select * from crawlerTask where projectUuid = ? and resourceType = ? ORDER BY startTime DESC", puuid, type);
		if (crawlerTasks.size() == 0) {
			return null;
		}
		return crawlerTasks.get(0);
	}
	
	public static void main(String[] args) {
		try {
			JDBCPool.initPool();
			CrawlerTask c1 = new CrawlerTask();
			c1.setCrawlerNode("1");
			c1.setResourceType("hehe");
			c1.setProjectUuid("haha-gaga");
			CrawlerTask c2 = new CrawlerTask();
			c2.setCrawlerNode("2");
			c2.setResourceType("gege");
			c2.setProjectUuid("gaga-gaga");
			
			insertCrawlerTask(c1);
			insertCrawlerTask(c2);
			
			List<CrawlerTask> crawlerTasks = getUnfinishedCrawlerTask();
			for (CrawlerTask crawlerTask : crawlerTasks) {
				System.out.println(crawlerTask.getStartTime());
			}
			
			JDBCPool.shutDown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static CrawlerTask getTask(CrawlerTask crawlerTask) throws SQLException {
		List<CrawlerTask> crawlerTasks = DAOUtils.getResult(CrawlerTask.class, "select * from crawlerTask where projectUuid = ? AND resourceType = ? AND startTime = ?",
				crawlerTask.getProjectUuid(), crawlerTask.getResourceType(), crawlerTask.getStartTime());
		return crawlerTasks.get(0);
	}

}
