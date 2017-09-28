package cn.edu.pku.EOS.servlet;

import cn.edu.pku.EOS.util.ZipUtil;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class GetCrawlerTaskInfo
 */
public class DownloadAllZip extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadAllZip() {
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
		List<String> filelist = new ArrayList();
		String [] files = filepath.split(";");
		for (String s : files)if (s.length() > 1){
			filelist.add(ZipUtil.zip(s,null));
		}
		downloadZip(request, response, filelist);
	}
	private void downloadZip(HttpServletRequest request,
			HttpServletResponse response, List<String> filepath) throws FileNotFoundException, IOException{
		String tempdir = "E:/";
		String path = ZipUtil.zipAll(filepath,null);
		File file = new File(path);
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
