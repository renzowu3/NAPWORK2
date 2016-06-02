package napwork;

import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialMethods extends Device {

	private String portName;
	private int baudRate;
	private int dataBits;
	private int stopBits;
	private int parity;
	private byte[] buffer;
	private SerialPort serialPort = null;

	public final static int SERIAL_PORTNAME = 1;
	public final static int SERIAL_BAUDRATE = 2;
	public final static int SERIAL_DATABITS = 3;
	public final static int SERIAL_STOPBITS = 4;
	public final static int SERIAL_PARITY = 5;


	@Override
	void open(int param) {
		try {
			serialPort.openPort();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	@Override
	void close(int param) {
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	@Override
	void write(int param) {
		//serialPort.writeBytes();
	}

	@Override
	Object read(int param) {
		//buffer = serialPort.readBytes();
		return null;
	}

	@Override
	void setConfig(int param, Object value) {
		switch(param){
		case SERIAL_PORTNAME: portName = (String)value; break;
		case SERIAL_BAUDRATE: baudRate = (int)value; break;
		case SERIAL_DATABITS: dataBits = (int)value; break;
		case SERIAL_STOPBITS: stopBits = (int)value; break;
		case SERIAL_PARITY: parity = (int)value; break;
		}

	}

	@Override
	Object getConfig(int param) {
		switch(param){
		case SERIAL_PORTNAME: return portName;
		case SERIAL_BAUDRATE: return baudRate;
		case SERIAL_DATABITS: return dataBits;
		case SERIAL_STOPBITS: return stopBits;
		case SERIAL_PARITY: return parity;
		}
		return null;
	}
}
