package cn.edu.pku.EOS.entity;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import cn.edu.pku.EOS.DAO.ProjectDAO;

public class CrawlerTask {

	public static final int WAITING = 0;
	public static final int SUCCESS = 1;
	public static final int ERROR = -1;
	public static final int IN_PROGRESS = 2;
	
	private String uuid;
	private String projectUuid;
	private String crawlerNode = "127.0.0.1";
	private String resourceType;
	private Date startTime = new Date();
	private Date finishTime = null;
	private String entrys = "";
	private String download = "";
	private int status = WAITING;
	
	public CrawlerTask(){}
	
	public CrawlerTask(Project project, String resoucetype) {
		this.projectUuid = project.getUuid();
		this.resourceType = resoucetype;
		uuid = UUID.randomUUID().toString();
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getProjectUuid() {
		return projectUuid;
	}
	public void setProjectUuid(String projectUuid) {
		this.projectUuid = projectUuid;
	}
	public String getCrawlerNode() {
		return crawlerNode;
	}
	public void setCrawlerNode(String crawlerNode) {
		this.crawlerNode = crawlerNode;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}


	public String getEntrys() {
		return entrys;
	}

	public void setEntrys(String entrys) {
		this.entrys = entrys;
	}
	public String getDownload(){
		return download;
	}
	public void setDownload(String download){
		this.download = download;
	}
}
