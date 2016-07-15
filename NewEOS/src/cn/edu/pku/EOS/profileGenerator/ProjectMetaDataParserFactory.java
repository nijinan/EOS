package cn.edu.pku.EOS.profileGenerator;

public class ProjectMetaDataParserFactory {
	public static final String APACHE_TYPE = "apache";
	public static final String UNKNOWN_TYPE = "unknown";
	
	
	public static String inferParserTypeFromUrl(String pageurl) {
		if (pageurl.contains("apache")) {
			return APACHE_TYPE;
		}
		return UNKNOWN_TYPE;
	}

	public static ProjectMetaDataParser getParser(String type) {
		if (type.equals(APACHE_TYPE)) {
			return new ApacheProjectParser();
		}
		return null;
	}

}
