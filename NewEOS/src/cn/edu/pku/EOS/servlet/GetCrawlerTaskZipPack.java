package cn.edu.pku.EOS.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
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
import cn.edu.pku.EOS.util.ZipUtil;

/**
 * Servlet implementation class GetCrawlerTaskInfo
 */
public class GetCrawlerTaskZipPack extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCrawlerTaskZipPack() {
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
		String filepath = request.getParameter("filepath");
		downloadZip(request, response, filepath);
	}
	private void downloadZip(HttpServletRequest request,
			HttpServletResponse response, String filepath) throws FileNotFoundException, IOException{
		String tempdir = "E:/";
		
		//File sourcefile = new File(filepath);
		String projectName = "";
//		if (!sourcefile.exists()){
//			System.out.println("project "+ projectName +" does not exit!"); 
//			//return;
//		}
		projectName = "zoo";
		String tempfile = tempdir+projectName+".zip";
		ZipUtil.zip("E:/zookeeper/", tempfile);
		
		File file=new File(tempfile);  
		if(file.exists()){  
			response.setContentType("text/plain");  
		    response.getWriter().print(tempfile);
		}else{  
			System.out.println("project "+ projectName +" does not exit!");  
		}  
	}
}
