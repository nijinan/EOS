package cn.edu.pku.EOS.servlet;

import cn.edu.pku.EOS.analyze.BugAnalyze;
import cn.edu.pku.EOS.analyze.CommitAnalyze;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Servlet implementation class GetCrawlerTaskInfo
 */
public class GetStaticInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetStaticInfo() {
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
		//String puuid = request.getParameter("puuid");
		String type = request.getParameter("type");
		String projectName = request.getParameter("project");
		response.setContentType("text/plain");
		String json = "";
		if (type.equals("BUG"))
		json = BugAnalyze.countUser(projectName);
		if (type.equals("COMMIT"))
			json = CommitAnalyze.countUser(projectName);
		System.out.print("static : "+projectName + json);
		response.getWriter().print(json);
	}
}
