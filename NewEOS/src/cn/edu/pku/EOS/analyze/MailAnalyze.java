package cn.edu.pku.EOS.analyze;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.Path;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MailAnalyze {
	public static void analyzeApache(){
		File file = new File("E:\\CrawlData\\Apache\\cn.edu.pku.EOSCN.crawler.MboxCrawler");
		Arrays.stream(file.listFiles()).filter(project->project.isDirectory()).forEach(project->{
			Arrays.stream(project.listFiles()).forEach(mlist->{
				Arrays.stream(mlist.listFiles()).filter(mboxfile->mboxfile.getName().startsWith("http")).forEach(mboxfile->{
					String date = mboxfile.getName().substring(mboxfile.getName().length() - 11,mboxfile.getName().length() - 5);

				});
			});
		});
	}


	public static void setToFile(Set<String> set, String filename){
		File file = new File(filename);
		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileUtils.write(file,""+set.size()+"\n");
			FileUtils.writeLines(file,set,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String,Integer> countUser(String projectName, String query){
		File project = new File("E:\\CrawlData\\Apache\\cn.edu.pku.EOSCN.crawler.JiraIssueCrawler\\"+projectName);
		Map<String, Integer> cnt = new HashMap<>();
		//System.out.println(project.getName());
		Arrays.stream(project.listFiles()).filter(dateDir->dateDir.isDirectory() && dateDir.getName().matches("[0-9]{4}-[0-9]{2}")).forEach(dateDir->{
			Set<String> assigneeDateSet = new HashSet<>();
			Arrays.stream(dateDir.listFiles()).filter(f->f.getName().startsWith(query)).forEach(f->{
				try {
					cnt.put(dateDir.getName(), Integer.valueOf(FileUtils.readLines(dateDir).get(0)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		});
		return cnt;
	}


	public static String downloadPathJson(String projectPath){
		projectPath = "E"+projectPath.substring(1);
		System.out.println(projectPath);
		File project = new File(projectPath);
		List<Map<String, String>> cnt = new ArrayList<>();
		Arrays.stream(project.listFiles()).filter(mlist->mlist.isDirectory()).forEach(mlist->{
			Map<String,String> mm = new HashMap<>();
			mm.put("mlist",mlist.getName());
			mm.put("path",mlist.getAbsolutePath());
			cnt.add(mm);
		});
		return new Gson().toJson(cnt);
	}

	public static void main(String args[]){
		//analyzeApache();
		//analyzeUser();
	}
}
