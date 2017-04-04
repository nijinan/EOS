package cn.edu.pku.EOS.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import cn.edu.pku.EOS.DAO.CrawlerTaskDao;
import cn.edu.pku.EOS.DAO.ProjectDAO;
import cn.edu.pku.EOS.business.ProjectBusiness;
import cn.edu.pku.EOS.entity.CrawlerTask;
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
		Project project;
		try {
			project = ProjectDAO.getProjectByUuid(uuid);
			String jsonString = new Gson().toJson(project);
			response.setContentType("text/plain");
			List<CrawlerTask> list = CrawlerTaskDao.getTaskByProject(uuid);
			String s = "";
			for (CrawlerTask ct : list){
				s += (new Gson().toJson(ct)) + ",";
			}
			s = s.substring(0, s.length() - 1);
			s = "[" + s + "]";
		    response.getWriter().print(jsonString + "|" + s);
		    System.out.println(s);
		} catch (NullPointerException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
