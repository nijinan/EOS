package cn.edu.pku.EOS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.dbutils.QueryRunner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.pku.EOS.DAO.JDBCPool;
import cn.edu.pku.EOS.business.ProjectBusiness;
import cn.edu.pku.EOS.entity.Project;
import cn.edu.pku.EOS.entity.ResourceMetaData;
import cn.edu.pku.EOS.profileGenerator.ProjectMetaDataParser;

/**
 * 测试类
 * @author 张灵箫
 *
 */
public class TestUtil {
	
	/**
	 * 本地用main方法测试时，可以用该方法得到一个lucene的project对象
	 * @author 张灵箫
	 * @throws ClassNotFoundException 
	 * @throws SQLException
	 * 
	 */
	public static Project getLuceneProject(){
		//插入lucene项目
		//Project luceneProject = new Project("Apache Lucene");
		//DAOUtils.update(connection, "INSERT INTO project (uuid, name, programmingLanguage, description, hostUrl) VALUES (?,?,?,?,?)",
        //                 luceneProject.getUuid(), luceneProject.getName(),Project.JAVA , "Apache LuceneTM is a high-performance, full-featured text search engine library written entirely in Java. It is a technology suitable for nearly any application that requires full-text search, especially cross-platform.", "http://lucene.apache.org/core/" );
		try {
			JDBCPool.initPool();
			ProjectBusiness pBusiness = new ProjectBusiness();
			return pBusiness.getProjectByUuid("b7914db3-caa7-4d70-96cd-bd4b5b4ed029");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JDBCPool.shutDown();
			return null;
		}
	}

	/**
	 * @author 张灵箫
	 * 打印项目全部信息
	 * @param project
	 */
	public static void printProjectInfo(Project project) {
		System.out.println("==================this is project info=================");
		System.out.println(project.getName());
		System.out.println(project.getHostUrl());
		System.out.println(project.getProgrammingLanguage());
		System.out.println(project.getDescription());
		List<ResourceMetaData> datas = project.getResources();
		System.out.println(datas.size());
		for (ResourceMetaData resourceMetaData : datas) {
			System.out.println(resourceMetaData.getType());
			System.out.println(resourceMetaData.getCrawler());
			List<String> urlList= resourceMetaData.getBaseUrls();
			for (String url : urlList) {
				System.out.println(url);
			}
		}
		System.out.println("==================this is project info=================");
	}
	
	public static void transferProjectData() throws IOException, SQLException, ClassNotFoundException {
		JDBCPool.initPool();
		List<Project> projects= ProjectBusiness.getAllProject();

		Class.forName("com.mysql.jdbc.Driver");
	    System.out.println("Connecting to database...");
	    Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.4.181:3306/tsrdb","root","woxnsk");
	    projects = projects.subList(85, projects.size()-1);
	    for (Project project : projects) {
	    	QueryRunner runner = new QueryRunner();
		    String htmlString = ProjectMetaDataParser.getHtmlStringFromUrl("https://www.ohloh.net/p.xml?api_key=aDoI7NXN7jpUU789Xlpn7g&sort=activity_level&query=" + project.getName().replace(" ", "+"));
		    String urlString = null;
		    try {
			    urlString = htmlString.substring(htmlString.indexOf("<html_url>") + 10, htmlString.indexOf("</html_url>"));
			} catch (Exception e) {
				continue;
			}
			int result = runner.update(conn, "update resource set evidenceLocation = ? where uuid = ?",
					urlString,
					project.getUuid());


			System.out.println(project.getName() + " \t" + urlString);
		}
	    conn.close();
	}
	
	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
//		String htmlString = ProjectMetaDataParser.getHtmlStringFromUrl("http://mail-archives.apache.org/mod_mbox/lucene-general/");
//		Document document = Jsoup.parse(htmlString);
//		Elements elements = document.select("td.msgcount");
//		int count = 0;
//		for (Element element : elements) {
//			count += Integer.parseInt(element.text());
//		}
//		System.out.println(count);
		transferProjectData();
	}
}
