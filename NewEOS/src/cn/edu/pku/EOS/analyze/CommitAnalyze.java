package cn.edu.pku.EOS.analyze;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CommitAnalyze {
	public static void analyzeApache(){
		File file = new File("E:\\CrawlData\\Apache\\cn.edu.pku.EOSCN.crawler.GitCrawler");
		Arrays.stream(file.listFiles()).filter(project->project.isDirectory()).forEach(project->{
			Arrays.stream(project.listFiles()).forEach(mlist->{
				Arrays.stream(project.listFiles()).filter(mboxfile->mboxfile.getName().startsWith("http")).forEach(mboxfile->{
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

	public static String countUser(String projectName){
		File project = new File("E:\\CrawlData\\Apache\\cn.edu.pku.EOSCN.crawler.GitCrawler\\"+projectName);
		project = project.listFiles()[0];
		List<Map<String, Integer>> cnt = new ArrayList<>();
		//System.out.println(project.getName());
		JSONArray jsarr = new JSONArray();
		Arrays.stream(project.listFiles()).filter(dateDir->dateDir.isDirectory() && dateDir.getName().matches("[0-9]{4}-[0-9]{2}")).forEach(dateDir->{
			Set<String> assigneeDateSet = new HashSet<>();
			Arrays.stream(dateDir.listFiles()).filter(f->f.getName().startsWith("reporter")).forEach(f->{
				try {
					JSONObject jsobj = new JSONObject();
					jsobj.put("date",dateDir.getName());
					jsobj.put("userNum",Integer.valueOf(FileUtils.readLines(f).get(0)));
					jsobj.put("issueNum",dateDir.list().length);
					jsarr.put(jsobj);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		});
		return jsarr.toString();
	}


	public static String downloadPathJson(String projectName){
		File project = new File("E:\\CrawlData\\Apache\\cn.edu.pku.EOSCN.crawler.GitCrawler\\"+projectName);
		project = project.listFiles()[0];
		System.out.println(project.getName());
		List<Map<String, String>> cnt = new ArrayList<>();
		Arrays.stream(project.listFiles()).filter(dateDir->dateDir.isDirectory() && dateDir.getName().matches("[0-9]{4}-[0-9]{2}")).forEach(dateDir->{
			Map<String,String> mm = new HashMap<>();
			mm.put("date",dateDir.getName());
			mm.put("path",dateDir.getAbsolutePath());
			cnt.add(mm);
		});
		return new Gson().toJson(cnt);
	}

	public static void main(String args[]){
		//analyzeApache();
		//analyzeUser();
	}
}
