package cn.edu.pku.EOS;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.pku.EOS.DAO.JDBCPool;
import cn.edu.pku.EOS.business.ProjectBusiness;
import cn.edu.pku.EOS.entity.Project;

public class ApacheProjectsCrawler {

	private static final String APACHE_PROJECTS_LIST_URL = "http://projects.apache.org/indexes/alpha.html";
	private static final String APACHE_PROJECTS_BASE_URL = "http://projects.apache.org";
	
	public static List<Project> ReadProjectListFromApache() throws IOException {
		List<Project> apacheProjects = new ArrayList<Project>();
		ProjectBusiness pBusiness = new ProjectBusiness();
		Document doc = Jsoup.parse(getHtmlStringFromUrl(APACHE_PROJECTS_LIST_URL));
		Elements alphaElements = doc.select("div.section");
		int count = 0;
		//System.out.println(alphaElements.size());
		for (Element element : alphaElements) {
			Elements innerElements = element.select("li");
			for (Element innerElement : innerElements) {
				String projectUrlString = APACHE_PROJECTS_BASE_URL + innerElement.select("a").first().attr("href");
				System.out.println(projectUrlString);
				Project project = pBusiness.parseProjectInfoFromWebPage(projectUrlString);
				pBusiness.createNewProject(project);
				count++;
			}
		}
		System.out.println(count);
		return apacheProjects;
	}

	
	protected static String getHtmlStringFromUrl(String pageurl) throws IOException {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(pageurl);
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));

		int statusCode = client.executeMethod(get);
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + get.getStatusLine());
		}
		byte[] responseBody = get.getResponseBody();
		String htmlPageString = new String(responseBody);

//		System.out.println(htmlPageString);
		return htmlPageString;
	}
	
	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
		JDBCPool.initPool();
//		List<Project> projects = ReadProjectListFromApache();
//		Project project = new ProjectBusiness().parseProjectInfoFromWebPage("http://projects.apache.org/projects/bean_validation__incubating_.html");
//		TestUtil.printProjectInfo(project);
	}
}
