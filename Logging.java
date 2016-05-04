package napwork;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class Logging {
	
	public String filepath;
	public String pattern; //default
	public String maxfilesize; //example: 1MB
	public String appenderName; //not sure
	public Logger log = null;
	 
	public static final int FILEPATH = 1;
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
		try {
		      PatternLayout layout = new PatternLayout("%-5p %d{yyyyMMdd@HH:mm:ss,SSS} :%t - %m%n");
		      RollingFileAppender appender = new RollingFileAppender(layout, filepath);
		      appender.setName(appenderName);
		      appender.setMaxFileSize(maxfilesize);
		      appender.activateOptions();
		      Logger.getRootLogger().addAppender(appender);
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		
		log = Logger.getLogger(appenderName);
	}
	
	public void close(){
		
	}
	
	public void read(){
		
	}
	
	public void write(int level, String msg){
		if(level == DEBUG){
			log.debug(msg);
		}
		else if(level == ERROR){
			log.error(msg);
		}
		else if(level == FATAL){
			log.fatal(msg);
		}
		else if(level == INFO){
			log.info(msg);
		}
		else if(level == TRACE){
			log.trace(msg);
		}
		else if(level == WARN){
			log.warn(msg);
		}
	}
	
	public void setConfig(int param, Object value){
		if(param == FILEPATH){
			filepath = (String)value;
		}
		else if(param == MAX_FILE_SIZE){
			maxfilesize = (String)value;
		}
		else if(param == APPENDERNAME){
			appenderName = (String)value;
		}
	}
	
	public Object getConfig(int param){
		if(param == FILEPATH){
			return filepath;
		}
		else if(param == MAX_FILE_SIZE){
			return maxfilesize;
		}
		else if(param == APPENDERNAME){
			return appenderName;
		}
		return null;
	}
}
