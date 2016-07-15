package cn.edu.pku.EOS.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cn.edu.pku.EOS.business.ProjectBusiness;
import cn.edu.pku.EOS.entity.Project;

/**
 * Servlet implementation class GetProjectInfo
 */
public class GetProjectInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetProjectInfo() {
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
		String uuid = request.getParameter("uuid");
		ProjectBusiness projectBusiness = new ProjectBusiness();
		Project project = projectBusiness.getProjectByUuid(uuid);
		String jsonString = new Gson().toJson(project);
		response.setContentType("text/plain");  
	    response.getWriter().print(jsonString);
	}
	

}
