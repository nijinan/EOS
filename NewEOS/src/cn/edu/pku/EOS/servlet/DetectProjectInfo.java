package cn.edu.pku.EOS.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.pku.EOS.TestUtil;
import cn.edu.pku.EOS.business.ProjectBusiness;
import cn.edu.pku.EOS.entity.Project;

/**
 * Servlet implementation class Test
 * 用于测试的servlet
 * @author 张灵箫
 */
public class DetectProjectInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DetectProjectInfo() {
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
		ProjectBusiness pb = new ProjectBusiness();
		String url = request.getParameter("url");
		Project project = pb.parseProjectInfoFromWebPage(url);
		Project project2 = pb.getProjectByName(project.getName());
		if (project2 != null) {
			pb.updateProjectInfo(project, project2.getUuid());
			TestUtil.printProjectInfo(project);
			//System.out.println("Test Success!");
			response.setContentType("text/plain");  
		    response.getWriter().print(project2.getUuid());
			return;
		}
		TestUtil.printProjectInfo(project);
		pb.createNewProject(project);
		project = pb.getProjectByName(project.getName());
		response.setContentType("text/plain");  
	    response.getWriter().print(project.getUuid());
	}

}
