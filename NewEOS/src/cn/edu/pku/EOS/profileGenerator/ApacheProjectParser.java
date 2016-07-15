package cn.edu.pku.EOS.profileGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.pku.EOS.entity.ResourceMetaData;

public class ApacheProjectParser extends ProjectMetaDataParser {

	@Override
	protected String parseProgrammingLanguage(String htmlPageString) throws ParserException {
		Parser parser = new Parser();
		parser.setInputHTML(htmlPageString);
		NodeList list = parser.extractAllNodesThatMatch(new StringFilter("Programming Languages"));
		return list.elementAt(1).getParent().getNextSibling().getNextSibling().toPlainTextString().trim();
	}

	@Override
	protected String parseHostUrl(String htmlPageString) throws ParserException {
		Parser parser = new Parser();
		parser.setInputHTML(htmlPageString);
		NodeList list = parser.extractAllNodesThatMatch(new StringFilter("Project Website"));
		return list.elementAt(0).getParent().getNextSibling().getNextSibling().toPlainTextString().trim();
	}

	@Override
	protected String parseDescription(String htmlPageString) throws ParserException {
		Parser parser = new Parser();
		parser.setInputHTML(htmlPageString);
		NodeList list = parser.extractAllNodesThatMatch(new TagNameFilter("p"));
		return list.elementAt(1).toPlainTextString().trim();
	}

	@Override
	protected String parseName(String htmlPageString) throws ParserException {
		Parser parser = new Parser();
		parser.setInputHTML(htmlPageString);
		NodeList list = parser.extractAllNodesThatMatch(new TagNameFilter("h2"));
		return list.elementAt(0).toPlainTextString();
	}

	@Override
	protected ResourceMetaData parseCodeMetaData(String htmlPageString) throws ParserException {
		ResourceMetaData data = new ResourceMetaData();
		data.setType(ResourceMetaData.CODE_TYPE);

		List<String> urlStrings = new ArrayList<String>();
		Parser parser = new Parser();
		parser.setInputHTML(htmlPageString);
		NodeList list = parser.extractAllNodesThatMatch(new StringFilter("SVN Direct"));
		if (list.size() == 0) {
			return null;
		}
		urlStrings.add(list.elementAt(0).getParent().getNextSibling().getNextSibling().toPlainTextString().trim());
		data.setBaseUrls(urlStrings);
		
		data.setCrawler("SVNCrawler");
		
		return data;
	}

	@Override
	protected ResourceMetaData parseBugMetaData(String htmlPageString) throws ParserException {
		ResourceMetaData data = new ResourceMetaData();
		data.setType(ResourceMetaData.BUG_TYPE);

		List<String> urlStrings = new ArrayList<String>();
		Parser parser = new Parser();
		parser.setInputHTML(htmlPageString);
		NodeList list = parser.extractAllNodesThatMatch(new StringFilter("Bug/Issue Tracker"));
		if (list.size() == 0) {
			return null;
		}
		
		String url = list.elementAt(0).getParent().getNextSibling().getNextSibling().toPlainTextString().trim();
		
		if (url.contains("jira")) {
			if (!url.contains("https")) {
				url = "https" + url.substring(4, url.length());
			}
		}
		
		urlStrings.add(url);
		data.setBaseUrls(urlStrings);
		
		if(urlStrings.get(0).contains("jira") || urlStrings.get(0).contains("issues")) {
			data.setCrawler("JiraCrawler");
		} else if (urlStrings.get(0).contains("bugzilla")) {
			data.setCrawler("BugzillaCrawler");
		}
		
		return data;
	}

	@Override
	protected ResourceMetaData parseMailingListMetaData(String htmlPageString) throws ParserException {
		ResourceMetaData data = new ResourceMetaData();
		data.setType(ResourceMetaData.MAIL_TYPE);

		List<String> urlStrings = new ArrayList<String>();
		Parser parser = new Parser();
		parser.setInputHTML(htmlPageString);
		NodeList list = parser.extractAllNodesThatMatch(new StringFilter("Mailing Lists"));
		if (list.size() == 0) {
			return null;
		}
		String currentUrlString = list.elementAt(0).getParent().getNextSibling().getNextSibling().toPlainTextString().trim();
		if (currentUrlString.contains("http://mail-archives.apache.org/mod_mbox/")) {
			urlStrings.add(currentUrlString);
		} else {
			String newHtmlString;
			try {
				newHtmlString = getHtmlStringFromUrl(list.elementAt(0).getParent().getNextSibling().getNextSibling().toPlainTextString().trim());
			} catch (HttpException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			urlStrings = parseArchiveUrlFromHTML(newHtmlString);
			if (urlStrings == null) {
				urlStrings = findMailArchiveFromApacheList();
			}
		}
		
		if (urlStrings == null) {
			return null;
		}
		data.setBaseUrls(urlStrings);
		
		data.setCrawler("ApacheMboxCrawler");
		
		return data;
	}

	private List<String> findMailArchiveFromApacheList() throws ParserException {
		List<String> urlStrings = new ArrayList<String>();
		String projectName = pageUrlString.substring(36);
		projectName = projectName.substring(0, projectName.length() - 5);
		//System.out.println(projectName);
		Document doc = null;
		try {
			doc = Jsoup.parse(getHtmlStringFromUrl("http://mail-archives.apache.org/mod_mbox/"));
		} catch (HttpException e) {
			return urlStrings;
		} catch (IOException e) {
			return urlStrings;
		}
		Elements elements = doc.select("a");
		for (Element element : elements) {
			String tmpUrlString = element.attr("href");
			if (tmpUrlString.contains(projectName) && tmpUrlString.contains("users")) {
				urlStrings.add("http://mail-archives.apache.org/mod_mbox/" + tmpUrlString);
				return urlStrings;
			}
		}
//		Parser parser = new Parser();
//		try {
//			parser.setInputHTML(getHtmlStringFromUrl("http://mail-archives.apache.org/mod_mbox/"));
//		} catch (HttpException e) {
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//		NodeList list = parser.extractAllNodesThatMatch(new StringFilter(projectName+"."));
//		Node[] nodes = list.elementAt(0).getParent().getParent().getNextSibling().getNextSibling().getChildren().toNodeArray();
//		for (Node node : nodes) {
//			if (node.toHtml().contains("user")) {
//				TagNode tagNode = (TagNode) node.getFirstChild();
//				urlStrings.add("http://mail-archives.apache.org/mod_mbox/" + tagNode.getAttribute("href"));
//			}
//		}
		return null;
	}

	private List<String> parseArchiveUrlFromHTML(String newHtmlString) {
		List<String> urlStrings = new ArrayList<String>();
		
		Parser parser = new Parser();
		try {
			parser.setInputHTML(newHtmlString);
			Node[] nodes = parser.extractAllNodesThatMatch(new TagNameFilter("a")).toNodeArray();
			for (Node node : nodes) {
				String urlString = ((TagNode) node).getAttribute("href");
				if (urlString != null) {
					if (urlString.contains("http://mail-archives.apache.org/mod_mbox/")
							&& urlString.contains("user")) {
						urlStrings.add(urlString);
						return urlStrings;
					}
				}
			}
		} catch (ParserException e) {
			return null;
		}
		
		return null;
	}

	@Override
	protected ResourceMetaData parseDocumentationMetaData(String htmlPageString) throws ParserException {
		ResourceMetaData data = new ResourceMetaData();
		data.setType(ResourceMetaData.DOC_TYPE);

		List<String> urlStrings = new ArrayList<String>();
		Parser parser = new Parser();
		parser.setInputHTML(htmlPageString);
		NodeList list = parser.extractAllNodesThatMatch(new StringFilter("Project Website"));
		urlStrings.add(list.elementAt(0).getParent().getNextSibling().getNextSibling().toPlainTextString().trim());
		data.setBaseUrls(urlStrings);
		
		data.setCrawler("HtmlDocCrawler");
		
		return data;
	}

}
