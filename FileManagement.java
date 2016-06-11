package napwork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class FileManagement {
	
	private String filepath;
	private String propsname;
	private String readString;
	
	private Properties props = null;
	private OutputStream os = null;
	private InputStream is = null;
	
	public static final int OPEN_OUTPUTSTREAM = 1;
	public static final int OPEN_INPUTSTREAM = 2;
	public static final int CLOSE_OUTPUTSTREAM = 3;
	public static final int CLOSE_INPUTSTREAM = 4;
	public static final int FILEPATH = 5;
	public static final int PROPERTIESNAME = 6;
	
	FileManagement(){
		
	}
	
	void open(int param){
		props = new Properties();

		if(param == OPEN_OUTPUTSTREAM){
			try{
				os = new FileOutputStream(filepath);
			} catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
            	e.printStackTrace();
            }
		} else if(param == OPEN_INPUTSTREAM){
			try{
				is = new FileInputStream(filepath);
				props.load(is);
			} catch (FileNotFoundException e) {
	             e.printStackTrace();
	        } catch (IOException e) {
	             e.printStackTrace();
	        }
		}
	}
	
	void close(int param){
		if(param == CLOSE_OUTPUTSTREAM){
			try{
				props.store(os, propsname);
	            os.close();
			} catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
            	e.printStackTrace();
            } 
		} else if(param == CLOSE_INPUTSTREAM){
			try{
				is.close();
			} catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
            	e.printStackTrace();
            } 
		}
	}
	
	String readProperties(String key){
		readString = props.getProperty(key);
		return readString;
	}
	
	void writeProperties(String key, String value){
		props.setProperty(key, value);
	}
	
	void setConfig(int param, Object value){
		switch(param){
		case FILEPATH: filepath = (String) value; break;
		case PROPERTIESNAME: propsname = (String) value; break;
		}
	}
}
