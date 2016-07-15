package cn.edu.pku.EOS.exception;

@SuppressWarnings("serial")
public class CrawlerNodeNotRespondingException extends Exception {

	private String host;
	public CrawlerNodeNotRespondingException(String host) {
		this.host = host;
	}
	
	@Override
	public String getMessage(){
		return "Can't not connect to host " + host;
	}

}
