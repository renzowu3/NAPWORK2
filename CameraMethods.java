package napwork;

//import com.googlecode.javacpp.*;
//import com.googlecode.javacv.cpp.*;
import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;

//import java.util.logging.Level;  
//import java.util.logging.Logger;
//import java.io.IOException;

public class CameraMethods extends Device{

	//Local Attributes
	private int deviceNum;
	private int deviceFrameRate;
	private int recorderFrameRate;
	private int recorderBitRate;
	private int canvasWidth;
	private int canvasHeight;
	private String saveFilepathvid;
	private String saveFilepathimage;
	private String canvasTitle;
	private OpenCVFrameGrabber grabber;
	private FFmpegFrameRecorder recorder;
	private CanvasFrame canvasFrame;
	private IplImage deviceCapture;
	private int cameraMode = 0;

	//Attributes for identification
	public static final int DEVICE_NUM = 1;
	public static final int DEVICE_FRAMERATE = 2;
	public static final int CANVAS_WIDTH = 3;
	public static final int CANVAS_HEIGHT = 4;
	public static final int RECORDER_FRAMERATE = 5;
	public static final int RECORDER_BITRATE = 6;
	public static final int SAVE_FILEPATH_VIDEO =7;
	public static final int SAVE_FILEPATH_IMAGE = 8;
	public static final int CANVAS_TITLE = 9;
	public static final int READ_IMAGE = 10;
	public static final int READ_VIDEO = 11;
	public static final int OPEN_CAMERA = 12;
	public static final int CLOSE_CAMERA = 13;
	public static final int CLOSE_RECORDER = 14;
	public static final int OPEN_CANVAS = 15;
	public static final int CANVAS_FRAME = 16;
	public static final int CAMERA_MODE = 17;

	CameraMethods(){

	}

	public void open(int param){
		if(param == OPEN_CAMERA){
			try{
				//instantiates the capture device 
				grabber = new OpenCVFrameGrabber(deviceNum);
				grabber.start();
				//passes the image captured by the camera
				deviceCapture = grabber.grab();
				//creates a jframe for the camera image
				canvasFrame = new CanvasFrame(canvasTitle);  
				grabber.setFrameRate(grabber.getFrameRate());
				//creates a recorder where to save the file and its other parameters
				recorder = new FFmpegFrameRecorder(saveFilepathvid, canvasWidth, canvasHeight);
				//other configurations
				recorder.setFormat("mp4"); 
				recorder.setFrameRate(recorderFrameRate);
				recorder.setVideoBitrate(recorderBitRate);
				//recorder.start(); //transferred from open();
				//cameraMode = 1;

			} catch(Exception e){
				e.printStackTrace();
			}
		}else if(param == OPEN_CANVAS){
			canvasFrame.setCanvasSize(canvasWidth, canvasHeight);  
			canvasFrame.isVisible();
		}

	}


	public void close(int param){
		try{
			switch(param){
			case CLOSE_CAMERA: canvasFrame.dispose();
			cameraMode = 0;
			grabber.stop();
			break;
			case CLOSE_RECORDER: recorder.stop();
			break;
			}
		} 
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public Object read(int param){
		try{
			if(param == READ_IMAGE){
				while ( canvasFrame.isVisible() && ( deviceCapture =  grabber.grab()) != null) { 
					canvasFrame.showImage(deviceCapture); 
				} 
				cvSaveImage( saveFilepathimage,  deviceCapture);     
				return saveFilepathimage;
			}
			else if(param == READ_VIDEO){
				System.out.println("Recording...");
				recorder.start();
				while ( canvasFrame.isVisible() && ( deviceCapture =  grabber.grab()) != null) { 
					canvasFrame.showImage(deviceCapture); 
					recorder.record( deviceCapture);  
				}  
				return saveFilepathvid;
			}
		}

		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void setConfig(int param, Object value){
		if(param == DEVICE_NUM){
			deviceNum = (Integer)value;
		}
		else if(param == DEVICE_FRAMERATE){
			deviceFrameRate = (Integer)value;
		}
		else if(param == CANVAS_WIDTH){
			canvasWidth = (Integer)value;
		}
		else if(param == CANVAS_HEIGHT){
			canvasHeight = (Integer)value;
		}
		else if(param == RECORDER_FRAMERATE){
			recorderFrameRate = (Integer)value;
		}
		else if(param == RECORDER_BITRATE){
			recorderBitRate = (Integer)value;
		}
		else if(param == SAVE_FILEPATH_VIDEO){
			saveFilepathvid = (String)value;
		}
		else if(param == SAVE_FILEPATH_IMAGE){
			saveFilepathimage = (String)value;
		}
		else if(param == CANVAS_TITLE){
			canvasTitle = (String)value;
		}
	}

	public Object getConfig(int param){
		if(param == DEVICE_NUM){
			return  deviceNum;
		}
		else if(param == DEVICE_FRAMERATE){
			return  deviceFrameRate;
		}
		else if(param == CANVAS_WIDTH){
			return  canvasWidth;
		}
		else if(param == CANVAS_HEIGHT){
			return  canvasHeight;
		}
		else if(param == RECORDER_FRAMERATE){
			return  recorderFrameRate;
		}
		else if(param == RECORDER_BITRATE){
			return  recorderBitRate;
		}
		else if(param == SAVE_FILEPATH_VIDEO){
			return  saveFilepathvid;
		}
		else if(param == SAVE_FILEPATH_IMAGE){
			return  saveFilepathimage;
		}
		else if(param == CANVAS_TITLE){
			return  canvasTitle;
		}
		else if(param == CANVAS_FRAME){
			return canvasFrame;
		}
		else if(param == CAMERA_MODE){
			return cameraMode;
		}
		return null;
	}

	@Override
	void write(int param) {
		// TODO Auto-generated method stub
		System.out.println("NO FEATURE");
	}
}