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
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.eclipse.core.runtime.Path;

/**
 * Servlet implementation class GetCrawlerTaskInfo
 */
public class DownloadZip extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadZip() {
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
		System.out.println(filepath);
		downloadZip(request, response, filepath);
	}
	private void downloadZip(HttpServletRequest request,
			HttpServletResponse response, String filepath) throws FileNotFoundException, IOException{
		String tempdir = "E:/";
		filepath = ZipUtil.zip(filepath,null);
		File file = new File(filepath);
		if(file.exists()){  
			response.setContentType("application/zip");  
			response.addHeader("Content-Disposition", "attachment;filename="+file.getName());
			byte[] buffer=new byte[1024];  
			FileInputStream fis=null;  
			BufferedInputStream bis=null;  
			try{  
				fis=new FileInputStream(file);  
				bis=new BufferedInputStream(fis);  
				OutputStream os=response.getOutputStream();  
				int i=bis.read(buffer);  
				while(i!=-1){  
					os.write(buffer,0,i);  
					i=bis.read(buffer);  
				}  
			}catch(IOException e){  
				e.printStackTrace();
			}finally{  
				if(bis!=null){  
					bis.close();  
				}  
				if(fis!=null){  
					fis.close();  
				} 
			}  
		}else{  
			System.out.println("project "+ filepath +" does not exit!");  
		}  
	}
}
