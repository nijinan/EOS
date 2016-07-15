package cn.edu.pku.EOS.profileGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.htmlparser.util.ParserException;

import cn.edu.pku.EOS.entity.Project;
import cn.edu.pku.EOS.entity.ResourceMetaData;

public abstract class ProjectMetaDataParser {
	
	String pageUrlString = null;

	public Project parseProjectInfoFromWebPage(String pageurl) throws IOException {
		pageUrlString = pageurl;
		Project project = new Project();
		String htmlPageString = getHtmlStringFromUrl(pageurl);
		
		try {
			project.setName(parseName(htmlPageString));
			project.setDescription(parseDescription(htmlPageString));
			project.setHostUrl(parseHostUrl(htmlPageString));
			project.setProgrammingLanguage(parseProgrammingLanguage(htmlPageString));
			
			List<ResourceMetaData> datas = new ArrayList<ResourceMetaData>();
			for (String resourceType : ResourceMetaData.RESOURCE_TYPE_LIST) {
				ResourceMetaData data = parseResourceMetaData(resourceType, htmlPageString);
				if (data != null) {
					datas.add(data);
				}
			}
			project.setResources(datas);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return project;
	}

	private ResourceMetaData parseResourceMetaData(String resourceType, String htmlPageString) throws ParserException {
		if (resourceType.equals(ResourceMetaData.DOC_TYPE)) {
			return parseDocumentationMetaData(htmlPageString);
		}
		if (resourceType.equals(ResourceMetaData.MAIL_TYPE)) {
			return parseMailingListMetaData(htmlPageString);
		}
		if (resourceType.equals(ResourceMetaData.BUG_TYPE)) {
			return parseBugMetaData(htmlPageString);
		}
		if (resourceType.equals(ResourceMetaData.CODE_TYPE)) {
			return parseCodeMetaData(htmlPageString);
		}
		if (resourceType.equals(ResourceMetaData.RELATIVEWEB_TYPE)) {
			ResourceMetaData data = new ResourceMetaData(resourceType, null, "RelativeWebCrawlerVer2");
			return data;
		}
		return null;
	}
	protected abstract ResourceMetaData parseCodeMetaData(String htmlPageString) throws ParserException;
	protected abstract ResourceMetaData parseBugMetaData(String htmlPageString) throws ParserException;
	protected abstract ResourceMetaData parseMailingListMetaData(String htmlPageString) throws ParserException;
	protected abstract ResourceMetaData parseDocumentationMetaData(String htmlPageString) throws ParserException;

	protected abstract String parseProgrammingLanguage(String htmlPageString)throws ParserException;
	protected abstract String parseHostUrl(String htmlPageString)throws ParserException;
	protected abstract String parseDescription(String htmlPageString)throws ParserException;
	protected abstract String parseName(String htmlPageString)throws ParserException;

	public static String getHtmlStringFromUrl(String pageurl) throws IOException,
			HttpException {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(pageurl);
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(0, false));
		
		int statusCode = client.executeMethod(get);
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + get.getStatusLine());
		}
		byte[] responseBody = get.getResponseBody();
		String htmlPageString = new String(responseBody);

		//System.out.println(htmlPageString);
		return htmlPageString;
	}

}
