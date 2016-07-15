package cn.edu.pku.EOS.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cn.edu.pku.EOS.business.ProjectBusiness;
import cn.edu.pku.EOS.business.TaskBusiness;
import cn.edu.pku.EOS.entity.CrawlerTask;

/**
 * Servlet implementation class GetCrawlerTaskInfo
 */
public class GetCrawlerTaskInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCrawlerTaskInfo() {
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
		String puuid = request.getParameter("puuid");
		String type = request.getParameter("type");
		CrawlerTask task = TaskBusiness.getTaskByProjectAndType(puuid, type);
		String jsonString = new Gson().toJson(task);
		int dataNum = 0;
		if (task != null) {
			dataNum = ProjectBusiness.getDataNum(puuid, type);
			jsonString += "|" + dataNum;
		}
		response.setContentType("text/plain");  
	    response.getWriter().print(jsonString);
	}

}
