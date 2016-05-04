package napwork;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class MicrophoneMethods extends Device {

	private float formatSampleRate;
	private int formatSampleSizeInBits;
	private int formatChannels;
	private int formatFrameSize;
	private float formatFrameRate;
	private boolean formatBigEndian;

	private TargetDataLine targetLine;
	private AudioFormat TDLAudioFormat = null;
	private DataLine.Info infoTDL;
	private byte[] targetBytes;
	private int readByteSize;
	private String filepath;
	private File audioFile;
	private AudioInputStream aStream;

	//Attributes for Identification
	public final static int AUDIO_SAMPLESIZEINBITS =1;
	public final static int AUDIO_CHANNELS = 2;
	public final static int AUDIO_FRAMESIZE = 3;
	public final static int AUDIO_SAMPLERATE = 4;
	public final static int AUDIO_FRAMERATE = 5;
	public final static int AUDIO_BIGENDIAN = 6;
	public final static int AUDIO_READBYTESIZE = 13;
	public final static int AUDIO_TARGETBYTES = 14;
	public final static int AUDIO_FILEPATH = 15;

	public static final int OPEN_TARGETLINE_AUDIO = 7;
	public static final int OPEN_RECORDER = 13;
	public static final int WRITE_TARGETLINE_AUDIO = 8;
	public static final int CLOSE_TARGETLINE_AUDIO = 9;
	public static final int CLOSE_TARGETLINE_READ = 10;
	public static final int READ_TARGETLINE_BYTES = 11;
	public static final int READ_TARGETLINE_FILE = 12;
	

	@Override
	void open(int param){
		if(param == OPEN_TARGETLINE_AUDIO){
			TDLAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, formatSampleRate, formatSampleSizeInBits, formatChannels, formatFrameSize, formatFrameRate, formatBigEndian);
			infoTDL = new DataLine.Info(TargetDataLine.class,  TDLAudioFormat);
			try {
				targetLine = (TargetDataLine) AudioSystem.getLine(infoTDL);
				targetLine.open();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		} else if(param == OPEN_RECORDER){
			targetLine.start();
		}
	}

	void close(int param){
		if(param == CLOSE_TARGETLINE_READ){
			targetLine.stop();
		} else if(param == CLOSE_TARGETLINE_AUDIO){
			targetLine.close();
		}
	}

	Object read(int param){
		if(param == READ_TARGETLINE_BYTES){
			targetBytes = new byte[32000];
			readByteSize = targetLine.read( targetBytes, 0,  targetBytes.length);
			return targetBytes;
		} else if(param == READ_TARGETLINE_FILE){
			aStream = new AudioInputStream(targetLine);
			audioFile = new File(filepath + ".mp3");
			try {
				AudioSystem.write(aStream, AudioFileFormat.Type.WAVE, audioFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	void write(int param) {
		System.out.println("NO FUNCTION");
		// TODO Auto-generated method stub

	}

	void setConfig(int param, Object value){
		switch(param){
		case AUDIO_SAMPLESIZEINBITS: formatSampleSizeInBits = (int)value; break;
		case AUDIO_CHANNELS: formatChannels = (int)value; break;
		case AUDIO_FRAMERATE: formatFrameRate = (float)value; break;
		case AUDIO_FRAMESIZE: formatFrameSize = (int)value; break;
		case AUDIO_SAMPLERATE: formatSampleRate = (float)value; break;
		case AUDIO_BIGENDIAN: formatBigEndian = (boolean)value; break;
		case AUDIO_READBYTESIZE: readByteSize = (int)value; break;
		case AUDIO_TARGETBYTES: targetBytes = (byte[])value; break;
		case AUDIO_FILEPATH: filepath = (String)value; break;
		}
	}

	@Override
	Object getConfig(int param) {
		// TODO Auto-generated method stub
		switch(param){
		case AUDIO_SAMPLESIZEINBITS: return formatSampleSizeInBits;
		case AUDIO_CHANNELS: return formatChannels;
		case AUDIO_FRAMERATE: return formatFrameRate;
		case AUDIO_FRAMESIZE: return formatFrameSize;
		case AUDIO_SAMPLERATE: return formatSampleRate; 
		case AUDIO_BIGENDIAN:  return formatBigEndian;
		case AUDIO_READBYTESIZE: return readByteSize;
		case AUDIO_TARGETBYTES: return targetBytes;
		case AUDIO_FILEPATH: return filepath;
		}
		return null;
	}


}
