package napwork;

import org.apache.log4j.Logger;

public class Logging {
	
	public String filename;
	public String pattern;
	public String maxfilesize;
	public String appenderName; //not sure
	
	public static final int FILENAME = 1;
	public static final int PATTERN = 2;
	public static final int MAX_FILE_SIZE = 3;
	public static final int APPENDERNAME = 4;
	public static final int DEBUG = 6;
	public static final int ERROR = 7;
	public static final int FATAL = 8;
	public static final int INFO = 9;
	public static final int TRACE = 11;
	public static final int WARN = 13;
	
	Logging(){
		
	}
	
	public void open(){
		
	}
	
	public void close(){
		
	}
	
	public void read(){
		
	}
	
	public void write(int level, String msg){
		
	}
	
	public void setConfig(){
		
	}
	
	public Object getConfig(int param){
		
		return filename;
	}
}
