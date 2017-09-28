package cn.edu.pku.EOS.servlet;

import cn.edu.pku.EOS.DAO.CrawlerTaskDao;
import cn.edu.pku.EOS.analyze.BugAnalyze;
import cn.edu.pku.EOS.analyze.CommitAnalyze;
import cn.edu.pku.EOS.analyze.MailAnalyze;
import cn.edu.pku.EOS.entity.CrawlerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet implementation class GetCrawlerTaskInfo
 */
public class GetDownloadList extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDownloadList() {
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
		String projectName = request.getParameter("project");

		response.setContentType("text/plain");
		String json = "";

		if (type.equals("BUG")) {
			json = BugAnalyze.downloadPathJson(projectName);
		}
		if (type.equals("MAIL")){
			//String filepath = request.getParameter("filepath");
			CrawlerTask ct = null;
			try {
				ct = CrawlerTaskDao.getTaskByProjectAndType(puuid, "Mbox");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			json = MailAnalyze.downloadPathJson(ct.getDownload().split(";")[0]);
		}
		if (type.equals("COMMIT")){
			//String filepath = request.getParameter("filepath");
			json = CommitAnalyze.downloadPathJson(projectName);
		}
		json = json.replace("\\","\\\\");
		System.out.print(json);
		response.getWriter().print(json);
	}
	private void downloadZip(HttpServletRequest request,
			HttpServletResponse response, String filepath) throws FileNotFoundException, IOException{
		String tempdir = "E:/";
		
		File file = new File(filepath);

	}
}
