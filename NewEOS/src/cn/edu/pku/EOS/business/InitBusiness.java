package cn.edu.pku.EOS.business;

import java.io.File;

import cn.edu.pku.EOS.DAO.JDBCPool;
import cn.edu.pku.EOS.config.Config;

/**
 * 服务器启动时处理初始化事务的类，包括初始化数据库连接池等
 * @author 张灵箫
 *
 */

public class InitBusiness {
	public static void initEOS(){
		try {
			JDBCPool.initPool();
			File file = new File(Config.getTempDir());
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			System.out.println("initiation failed!");
			e.printStackTrace();
		} 
	}
}
