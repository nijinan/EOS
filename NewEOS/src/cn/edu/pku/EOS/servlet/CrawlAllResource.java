package cn.edu.pku.EOS.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.pku.EOS.business.ProjectBusiness;
import cn.edu.pku.EOS.entity.Project;
import cn.edu.pku.EOS.entity.ResourceMetaData;

/**
 * Servlet implementation class CrawlAllResource
 */
public class CrawlAllResource extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrawlAllResource() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String projectUuid = request.getParameter("projectuuid");
		ProjectBusiness pb = new ProjectBusiness();
		Project project = pb.getProjectByUuid(projectUuid);
		List<ResourceMetaData> datas = project.getResources();
		for (ResourceMetaData data : datas) {
			pb.startNewCrawlerTask(data.getType(), project);
		}
		
	}

}
