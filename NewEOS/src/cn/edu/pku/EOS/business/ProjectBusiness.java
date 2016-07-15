package cn.edu.pku.EOS.business;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

import jcifs.smb.SmbFile;

import org.apache.log4j.Logger;

import cn.edu.pku.EOS.TestUtil;
import cn.edu.pku.EOS.DAO.CrawlerTaskDao;
import cn.edu.pku.EOS.DAO.ProjectDAO;
import cn.edu.pku.EOS.DAO.ResourceMetaDataDAO;
import cn.edu.pku.EOS.config.Config;
import cn.edu.pku.EOS.entity.CrawlerTask;
import cn.edu.pku.EOS.entity.Project;
import cn.edu.pku.EOS.entity.ResourceMetaData;
import cn.edu.pku.EOS.exception.CrawlerNodeNotRespondingException;
import cn.edu.pku.EOS.exception.CrawlerNotInNodeListException;
import cn.edu.pku.EOS.nodedispatch.JobDispatcher;
import cn.edu.pku.EOS.nodedispatch.MessageSender;
import cn.edu.pku.EOS.profileGenerator.ProjectMetaDataParser;
import cn.edu.pku.EOS.profileGenerator.ProjectMetaDataParserFactory;
import cn.edu.pku.EOS.util.RemoteFileOperation;
/**
 * project相关业务逻辑
 * @author 张灵箫
 *
 */
public class ProjectBusiness {
	
	
	private static final Logger logger = Logger.getLogger(ProjectBusiness.class.getName());
	/**
	 * 根据uuid得到project对象，包含所有的resourceMetadata
	 * @author 张灵箫
	 * @param uuid
	 * @return
	 */
	public static List<Project> getAllProject() {
		List<Project> projects = null;
		try {
			projects = new ProjectDAO().getAllProject();
//			for (Project project : projects) {
//				project.getResources().addAll(new ResourceMetaDataDAO().getDataListByProjectUuid(project.getUuid()));
//			}
		} catch (SQLException e) {
			System.out.println("database error!");
		}
		return projects;
	}
	
	public Project getProjectByUuid(String uuid) {
		Project project = null;
		List<ResourceMetaData> rDatas = null;
		ProjectDAO projectDAO = new ProjectDAO();
		ResourceMetaDataDAO resourceMetaDataDAO = new ResourceMetaDataDAO();
		try {
			project = projectDAO.getProjectByUuid(uuid);
			rDatas = resourceMetaDataDAO.getDataListByProjectUuid(uuid);
			project.getResources().addAll(rDatas);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		return project;
	}
	/**
	 * 根据用户上传的XML文件更改已有项目的信息，包括资源信息
	 * XML上传到项目对应的文件夹中，以uuid命名
	 * @param uuid
	 * @return 1表示成功，0表示出错
	 */
	public int updateProjectInfo(Project project, String uuid) {
		ProjectDAO pDao = new ProjectDAO();
		ResourceMetaDataDAO rDao = new ResourceMetaDataDAO();
		try {
			//System.out.println(project.getDescription());
			pDao.updateProjectInfo(uuid, project);
			rDao.deleteAllMetaDataOfProject(uuid);
			for (ResourceMetaData data : project.getResources()) {
				rDao.insertResourceMetaData(uuid, data);
			}
		} catch (SQLException e) {
			System.out.println("Database error!");
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	
	/**
	 * @author 张灵箫
	 * 调用爬虫爬去某个项目的某个指定类型的资源
	 * @param string
	 * @param project
	 */
	public String startNewCrawlerTask(String resoucetype, Project project) {
		ResourceMetaData data = project.getResourceByType(resoucetype);
		JobDispatcher jd;
		try {
			jd = new JobDispatcher(data.getCrawler());
		} catch (CrawlerNotInNodeListException e) {
			logger.info(data.getCrawler() + " do not have a node!");
			e.printStackTrace();
			return data.getCrawler() + " do not have a node!";
		}
		String result;
		CrawlerTask crawlerTask = new CrawlerTask(project, resoucetype);
		try {
			result = jd.dispatchCrawlerJob(crawlerTask);
		} catch (CrawlerNodeNotRespondingException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
		if (result.equals(MessageSender.REMOTE_SUCCESS_RESPONCE)) {
			try {
				CrawlerTaskDao.insertCrawlerTask(crawlerTask);
				crawlerTask = CrawlerTaskDao.getTask(crawlerTask);
			} catch (SQLException e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			}
		}
		//System.out.println(data);
//		Crawler crawler = null;
//		try {
//			//System.out.println(Crawler.class.getPackage().getName() + "." + data.getCrawler());
//			crawler = (Crawler) Class.forName(Crawler.class.getPackage().getName() + "." + data.getCrawler()).newInstance();
//			crawler.setProject(project);
//			crawler.setUrlList(data.getBaseUrls());
//		} catch (Exception e) {
//			System.out.println("创建爬虫对象时错误!");
//			e.printStackTrace();
//			return;
//		} 
//		CrawlerPool.startCrawlerThread(crawler);
		return crawlerTask.getUuid();
	}
	
	/**
	 * @author 张灵箫
	 * project对象，存入数据库
	 * @param xmlFilePath xml文件路径
	 * @return
	 */
	public void createNewProject(Project project) {
		ProjectDAO projectDAO = new ProjectDAO();
		ResourceMetaDataDAO dataDAO = new ResourceMetaDataDAO();
		try {
			projectDAO.insertProject(project);
			List<ResourceMetaData> datas = project.getResources();
			for (ResourceMetaData resourceMetaData : datas) {
				dataDAO.insertResourceMetaData(project.getUuid(), resourceMetaData);
			}
		} catch (SQLException e) {
			logger.info("Database error!");
			e.printStackTrace();
		}
	}
	
	public Project parseProjectInfoFromWebPage(String pageurl) {
		Project project = null;
		String parsertype = ProjectMetaDataParserFactory.inferParserTypeFromUrl(pageurl);
		ProjectMetaDataParser parser = ProjectMetaDataParserFactory.getParser(parsertype);
		try {
			project = parser.parseProjectInfoFromWebPage(pageurl);
		} catch (IOException e) {
			logger.info("Unable to parse page to Project: network failure!");
			e.printStackTrace();
		} catch (NullPointerException e) {
			logger.info("Unable to parse page to Project: Unkown type!");
			e.printStackTrace();
		}
		return project;
	}
	
	public Project getProjectByName(String name) {
		ProjectDAO projectDAO = new ProjectDAO();
		try {
			return projectDAO.getProjectByName(name);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws SQLException {
		ProjectBusiness pb = new ProjectBusiness();
		Project project = pb.parseProjectInfoFromWebPage("http://projects.apache.org/projects/lucene_core.html");
		TestUtil.printProjectInfo(project);
		//pb.createNewProject(project);
	}

	public static int getDataNum(String puuid, String type) {
		ResourceMetaDataDAO dao = new ResourceMetaDataDAO();
		List<ResourceMetaData> datas = null;
		try {
			datas = dao.getDataListByProjectUuid(puuid);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		for (ResourceMetaData resourceMetaData : datas) {
			if (resourceMetaData.getType().equals(type)) {
				return resourceMetaData.getCount();
			}
		}
		return 0;
	}
	




}
