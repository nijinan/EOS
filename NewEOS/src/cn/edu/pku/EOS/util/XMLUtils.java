package cn.edu.pku.EOS.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import cn.edu.pku.EOS.TestUtil;
import cn.edu.pku.EOS.config.Config;
import cn.edu.pku.EOS.entity.Project;

public class XMLUtils {
	public static Project getProjectFromXmlFile(String remotefilepath) {
		if (remotefilepath.startsWith("smb")) {
			remotefilepath = RemoteFileOperation.smbGet(remotefilepath, Config.getTempDir());
		}
		
		
		Project project = null;
		File xmlFile = new File(remotefilepath);
		Digester digester = new Digester();
		digester.addObjectCreate("project", Project.class);
		digester.addCallMethod("project/project_info/name", "setName", 0);
		digester.addCallMethod("project/project_info/hostUrl", "setHostUrl", 0);
		digester.addCallMethod("project/project_info/programmingLanguage", "setProgrammingLanguage", 0);
		digester.addCallMethod("project/project_info/description", "setDescription", 0);
		digester.addCallMethod("project/resources/resource", "addResourceMetaData", 3);
		digester.addCallParam("project/resources/resource/type", 0);
		digester.addCallParam("project/resources/resource/urls", 1);
		digester.addCallParam("project/resources/resource/crawler", 2);
		try {
			project = digester.parse(xmlFile);
		} catch (IOException e) {
			System.out.println("XML file not exist!");
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			System.out.println("XML file schema incorrect!");
			e.printStackTrace();
			return null;
		}
		return project;
	}
	
	public static void main(String[] args) {
		Project project = null;
		project = getProjectFromXmlFile("D:/EOSdir/b7914db3-caa7-4d70-96cd-bd4b5b4ed029/b7914db3-caa7-4d70-96cd-bd4b5b4ed029.xml");
		TestUtil.printProjectInfo(project);
	}

	public static String getProjectXmlFile(String uuid) {
		return Config.getEOSDir() + "/" + uuid + "/" + uuid + ".xml";
	}
}
