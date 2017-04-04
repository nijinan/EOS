package cn.edu.pku.EOS.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cn.edu.pku.EOS.DAO.CrawlerTaskDao;
import cn.edu.pku.EOS.DAO.DAOUtils;
import cn.edu.pku.EOS.business.ProjectBusiness;
import cn.edu.pku.EOS.business.TaskBusiness;
import cn.edu.pku.EOS.entity.CrawlerTask;
import cn.edu.pku.EOS.entity.FileNum;

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
		CrawlerTask ct;
		try {
			ct = CrawlerTaskDao.getTaskByProjectAndType(puuid, type);
			String jsonString = new Gson().toJson(ct);
			int dataNum = 0;
			if (ct != null) {
				dataNum = ProjectBusiness.getDataNum(puuid, type);
				FileNum fn = DAOUtils.getResult(FileNum.class, "select * from "+ ct.getResourceType() + " where uuid = ?",ct.getUuid()).get(0);
				jsonString += "|" + fn.getFileNum();
				jsonString += "|" + fn.getFileSize();
			}
			response.setContentType("text/plain");  
		    response.getWriter().print(jsonString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
