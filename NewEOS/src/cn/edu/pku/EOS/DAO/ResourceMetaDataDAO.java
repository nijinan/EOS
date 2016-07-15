package cn.edu.pku.EOS.DAO;

import java.sql.SQLException;
import java.util.List;

import cn.edu.pku.EOS.entity.ResourceMetaData;

public class ResourceMetaDataDAO {

	public List<ResourceMetaData> getDataListByProjectUuid(String uuid) throws SQLException {
		List<ResourceMetaData> datas = DAOUtils.getResult(ResourceMetaData.class, "select * from resourceMetaData where projectuuid = ?", uuid);
		for (ResourceMetaData resourceMetaData : datas) {
			//System.out.println(resourceMetaData.getType());
			resourceMetaData.getUrlsFromUrlString(resourceMetaData.getUrlListString());
		}
		return datas;
	}
	
	public int insertResourceMetaData(String projectuuid, ResourceMetaData data) throws SQLException {
		int result = DAOUtils.update("INSERT INTO resourceMetaData(projectuuid, type, crawler, urlListString) " +
				"VALUES(?, ?, ?, ?)", projectuuid, data.getType(), data.getCrawler(), data.getUrlListString());
		return result;
	}

	public int deleteAllMetaDataOfProject(String uuid) throws SQLException {
		int result = DAOUtils.update("DELETE FROM resourceMetaData WHERE projectuuid = ?", uuid);
		return result;
	}
}
