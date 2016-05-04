package napwork;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SpeakerMethods extends Device{

	//Local Attributes
	private boolean formatBigEndian;
	private int formatChannels;
	private float formatFrameRate;
	private int formatFrameSize;
	private float formatSampleRate;
	private int formatSampleSizeInBits;

	private SourceDataLine sourceLine = null;
	private AudioFormat SDLAudioFormat = null;
	private DataLine.Info infoSDL = null;
	private byte[] sourceBytes = null;
	private int byteSize = 0;
	private AudioInputStream Clip_AudioInputStreamFile;
	private AudioInputStream Clip_AudioInputStreamURL;
	private AudioFormat Clip_AudioFormatFile;
	private AudioFormat Clip_AudioFormatURL;
	private DataLine.Info infoClipFile;
	private DataLine.Info infoClipURL;
	private Clip audioClipFile = null;
	private Clip audioClipURL = null;
	private File audiofile;
	private File audioURL;
	private String audioFilepath;
	private String audioURLpath;

	//Attributes for identification
	public static final int AUDIO_CHANNELS = 1;
	public static final int AUDIO_FRAMESIZE = 2;
	public static final int AUDIO_SAMPLESIZEINBITS = 3;
	public static final int AUDIO_BIGENDIAN= 4;
	public static final int AUDIO_FRAMERATE = 5;
	public static final int AUDIO_SAMPLERATE = 6;
	public static final int SET_SOURCELINE_BYTES = 7;
	public static final int OPEN_SOURCELINE_AUDIO = 8;
	public static final int WRITE_SOURCELINE_BYTES = 9;
	public static final int CLOSE_SOURCELINE_AUDIO = 10;
	public static final int CLOSE_SOURCELINE_WRITE = 11;
	public static final int AUDIO_URL = 12;
	public static final int AUDIO_FILEPATH = 13;
	public static final int OPEN_CLIP_AUDIOFILE = 14;
	public static final int OPEN_CLIP_AUDIOURL = 15;
	public static final int WRITE_CLIP_AUDIOFILE = 16;
	public static final int WRITE_CLIP_AUDIOURL = 17;
	public static final int CLOSE_CLIP_AUDIOFILE = 18;
	public static final int CLOSE_CLIP_AUDIOURL = 19;
	public static final int OPEN_SOURCEDATALINE = 20;
	public static final int PAUSE_CLIP_AUDIOFILE = 21;
	public static final int PAUSE_CLIP_AUDIOURL = 22;
	public static final int AUDIO_SOURCEBYTES = 23;
	public static final int AUDIO_WRITEBYTESIZE = 24;
	public static final int OPEN_PLAYER = 25;

	@Override
	void open(int param) {
		// TODO Auto-generated method stub
		if(param == OPEN_SOURCEDATALINE){
			SDLAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, formatSampleRate, formatSampleSizeInBits, formatChannels, formatFrameSize, formatFrameRate, formatBigEndian);
			infoSDL = new DataLine.Info(SourceDataLine.class,  SDLAudioFormat);
			try {
				sourceLine = (SourceDataLine) AudioSystem.getLine(infoSDL);
				sourceLine.open();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(param == OPEN_CLIP_AUDIOFILE){
			Clip_AudioFormatFile =  Clip_AudioInputStreamFile.getFormat();
			infoClipFile= new DataLine.Info(Clip.class, Clip_AudioFormatFile);
			try {
				audioClipFile = (Clip) AudioSystem.getLine(infoClipFile);
				audioClipFile.open(Clip_AudioInputStreamFile);
			} catch (LineUnavailableException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(param == OPEN_CLIP_AUDIOURL){
			Clip_AudioFormatURL =  Clip_AudioInputStreamURL.getFormat();
			infoClipURL =  new DataLine.Info(Clip.class, Clip_AudioFormatURL);
			try {
				audioClipURL = (Clip) AudioSystem.getLine(infoClipURL);
				audioClipURL.open(Clip_AudioInputStreamURL);
			} catch (LineUnavailableException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(param == OPEN_PLAYER){
			sourceLine.start();
			System.out.println("Sourceline start");
		}

	}

	@Override
	void close(int param){
		if(param == CLOSE_SOURCELINE_WRITE){
			sourceLine.stop();
		} else if(param == CLOSE_SOURCELINE_AUDIO){
			sourceLine.close();
		}
		else if(param == CLOSE_CLIP_AUDIOFILE){
			audioClipFile.close();
			try {
				Clip_AudioInputStreamFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(param == CLOSE_CLIP_AUDIOURL){
			audioClipURL.close();
			try {
				Clip_AudioInputStreamURL.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(param == PAUSE_CLIP_AUDIOFILE){
			audioClipFile.stop();
		} else if(param == PAUSE_CLIP_AUDIOURL){
			audioClipURL.stop();
		}
	}


	@Override
	void write(int param){
		if(param == WRITE_SOURCELINE_BYTES){
				sourceLine.write(sourceBytes, 0,  sourceBytes.length);
		}
		else if(param == WRITE_CLIP_AUDIOFILE){
			audioClipFile.start();
			do{
				try{
					Thread.sleep(50);
				}catch(InterruptedException ie){
					ie.printStackTrace();
				}
			}while(audioClipFile.isActive());
		} else if(param == WRITE_CLIP_AUDIOURL){
			audioClipURL.start();
		}
	}

	@Override
	Object read(int param) {
		// TODO Auto-generated method stub
		System.out.println("No function available!");
		return null;
	}

	@Override
	void setConfig(int param, Object value) {
		// TODO Auto-generated method stub
		switch(param){
		case AUDIO_FILEPATH: 
			audioFilepath = (String)value;
			audiofile = new File(audioFilepath);
			try {
				Clip_AudioInputStreamFile = AudioSystem.getAudioInputStream(audiofile);
			} catch (UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} break;
		case AUDIO_URL:
			audioURLpath = (String)value;
			audioURL = new File(audioURLpath);
			try{
				Clip_AudioInputStreamURL = AudioSystem.getAudioInputStream(audioURL);
			} catch (UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			} break;
		case AUDIO_BIGENDIAN: formatBigEndian = (boolean)value; break;
		case AUDIO_CHANNELS: formatChannels = (int)value; break;
		case AUDIO_FRAMERATE: formatFrameRate = (float)value; break;
		case AUDIO_FRAMESIZE: formatFrameSize = (int)value; break;
		case AUDIO_SAMPLERATE: formatSampleRate = (float)value; break;
		case AUDIO_SAMPLESIZEINBITS: formatSampleSizeInBits = (int)value; break;
		case AUDIO_WRITEBYTESIZE: byteSize = (int)value; break;
		case AUDIO_SOURCEBYTES: sourceBytes = new byte[sourceLine.getBufferSize()]; break; //updated
		} 
	}

	@Override
	Object getConfig(int param) {
		// TODO Auto-generated method stub
		switch(param){
		case AUDIO_BIGENDIAN: return formatBigEndian;
		case AUDIO_CHANNELS: return formatChannels;
		case AUDIO_FRAMERATE: return formatFrameRate;
		case AUDIO_FRAMESIZE: return formatFrameSize;
		case AUDIO_SAMPLERATE: return formatSampleRate;
		case AUDIO_SAMPLESIZEINBITS: return formatSampleSizeInBits;
		case AUDIO_FILEPATH: return audiofile;
		case AUDIO_URL: return audioURL;
		case AUDIO_WRITEBYTESIZE: return byteSize;
		case AUDIO_SOURCEBYTES: return sourceBytes;
		}
		return null;
	}

}
