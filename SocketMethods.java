package napwork;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SocketMethods extends Device{

	private String filepath;
	private int serverPortNum;
	private int clientPortNum;
	private String clientHostname;
	private int TCPbuffersize; // recommended default 4096
	private int UDPbuffersize;
	private byte[] bytearray;
	private byte[] filearray;
	private int udpPortNum;
	private int writeMode;
	private int readMode;
	private InetAddress receiverAddress;
	private byte[] udparray;
	private int read; //add
	private int size; //add
	
	private ServerSocket sc;
	private Socket soc;
	private InputStream is;
	private OutputStream os;
	private FileInputStream fis;
	private FileOutputStream fos;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	private DataInputStream dis;
	private DataOutputStream dos;
	private DatagramSocket udpSoc;
	private DatagramSocket udpServerSoc;
	private InetAddress IPAddress;
	private DatagramPacket receivePacket;
	private DatagramPacket sendPacket;

	public static final int OPEN_TCP_CLIENTSOCKET = 1;
	public static final int OPEN_TCP_SERVERSOCKET = 2;
	public static final int OPEN_UDP_CLIENTSOCKET = 3;
	public static final int OPEN_UDP_SERVERSOCKET = 4;
	public static final int TCP_SERVERPORT = 5;
	public static final int TCP_CLIENTPORT = 6;
	public static final int TCP_CLIENTHOSTNAME = 7;
	public static final int UDP_PORT = 8;
	public static final int TCP_BUFFERSIZE = 9;
	public static final int UDP_BUFFERSIZE = 10;
	public static final int FILEPATH = 11;
	public static final int ADDRESS = 12;
	public static final int READ_FILETRANSFER = 13;
	public static final int READ_BYTES = 14;
	public static final int READ_DATAGRAM = 15;
	public static final int WRITE_FILETRANSFER = 16;
	public static final int WRITE_BYTES = 17;
	public static final int WRITE_DATAGRAM = 18;
	public static final int CLOSE_SERVERSOCKET = 19;
	public static final int CLOSE_CLIENTSOCKET = 20;
	public static final int CLOSE_INPUTSTREAM = 21;
	public static final int CLOSE_OUTPUTSTREAM = 22;
	public static final int CLOSE_FILEINPUTSTREAM = 23;
	public static final int CLOSE_FILEOUTPUTSTREAM = 24;
	public static final int CLOSE_DATAINPUTSTREAM = 25;
	public static final int CLOSE_DATAOUTPUTSTREAM = 26;
	public static final int READY_WRITE_TRANSFERFILE = 27;
	public static final int READY_WRITE_BYTES = 28;
	public static final int READY_READ_TRANSFERFILE = 29;
	public static final int READY_READ_BYTES = 30;
	public static final int BYTEARRAY = 31;
	public static final int FILEARRAY = 32;
	public static final int CLOSE_BUFFEREDINPUTSTREAM = 33;
	public static final int CLOSE_BUFFEREDOUTPUTSTREAM = 34;
	public static final int SIZE = 35; //add

	@Override
	void open(int param) {
		if(param == OPEN_TCP_CLIENTSOCKET){
			try {
				soc = new Socket(clientHostname, clientPortNum);
				System.out.println("Connected to server!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(param == OPEN_TCP_SERVERSOCKET){
			try {
				sc = new ServerSocket(serverPortNum);
				soc =  sc.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(param == OPEN_UDP_CLIENTSOCKET){
			try {
				udpSoc = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}else if(param == OPEN_UDP_SERVERSOCKET){
			try {
				udpServerSoc = new DatagramSocket(udpPortNum);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	void close(int param) {
		try{
			if(param == CLOSE_SERVERSOCKET){
				sc.close();
			}else if(param == CLOSE_CLIENTSOCKET){
				soc.close();
			}else if(param == CLOSE_INPUTSTREAM){
				is.close();
			}else if(param == CLOSE_OUTPUTSTREAM){
				os.close();
			}else if(param == CLOSE_FILEINPUTSTREAM){
				fis.close();
			}else if(param == CLOSE_FILEOUTPUTSTREAM){
				fos.flush();
				fos.close();
			}else if(param == CLOSE_DATAOUTPUTSTREAM){
				dos.close();
			}else if(param == CLOSE_DATAINPUTSTREAM){
				dis.close();
			}else if(param == CLOSE_BUFFEREDINPUTSTREAM){
				bis.close();
			}else if(param == CLOSE_BUFFEREDOUTPUTSTREAM){
				bos.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	void write(int param) {
		if(param == WRITE_BYTES && writeMode == 0){
			try {	
				bos.write(bytearray); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(param == WRITE_FILETRANSFER && writeMode == 1){
			try {
				System.out.println("WRITEsss");
				while ((read = bis.read(filearray, 0, filearray.length)) != -1) { //updated
					dos.write(filearray, 0, read);
					System.out.println("read:" + read);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(param == WRITE_DATAGRAM){
			
			try {
				sendPacket = new DatagramPacket(udparray, UDPbuffersize, receiverAddress, udpPortNum);
				udpSoc.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	Object read(int param) {
		if(param == READ_BYTES && readMode == 0){
			try {	
				dis.read(bytearray); //edited
				System.out.println("read bytes");
				return bytearray;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(param == READ_FILETRANSFER && readMode == 1){
			try {
				while((read = bis.read(filearray, 0, filearray.length)) != -1){ //edited
					bos.write(filearray, 0, read);
				}
				System.out.println("Saved file to:" +filepath);
				return filepath;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(param == READ_DATAGRAM){
			try {
				receivePacket = new DatagramPacket(udparray, UDPbuffersize);
				udpSoc.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	void setConfig(int param, Object value) {
		switch(param){
		case READY_WRITE_BYTES: try {
			dos = new DataOutputStream(soc.getOutputStream());
			bos = new BufferedOutputStream(dos);
			writeMode = 0;
		} catch (IOException e) {
			e.printStackTrace();
		} break;
		case READY_WRITE_TRANSFERFILE: try {
			dos = new DataOutputStream(soc.getOutputStream());
			bos = new BufferedOutputStream(dos);
			fis = new FileInputStream(filepath);
			bis = new BufferedInputStream(fis);
			writeMode = 1;
		} catch (IOException e) {
			e.printStackTrace();
		} break;
		case READY_READ_BYTES: try { //updated
			dis = new DataInputStream(soc.getInputStream());
			//bis = new BufferedInputStream(dis);
			size = dis.readInt();
			System.out.println("read Int:" + size);
			readMode = 0;
		} catch (IOException e) {
			e.printStackTrace();
		} break;
		case READY_READ_TRANSFERFILE: try { //updated
			dis = new DataInputStream(soc.getInputStream());
			bis = new BufferedInputStream(dis);
			fos = new FileOutputStream(filepath);
			bos = new BufferedOutputStream(bos);
			readMode = 1;
		} catch (IOException e) {
			e.printStackTrace();
		} break;
		case TCP_CLIENTPORT: clientPortNum = (int)value; break;
		case TCP_SERVERPORT: serverPortNum = (int)value; break;
		case UDP_PORT: udpPortNum = (int)value; break;
		case TCP_BUFFERSIZE: TCPbuffersize = (int)value;  //updated
							 filearray = new byte[TCPbuffersize];
						     break;
		case UDP_BUFFERSIZE: UDPbuffersize = (int)value; break;
		case FILEPATH: filepath = (String)value; break;
		case TCP_CLIENTHOSTNAME: clientHostname = (String)value; break;
		case BYTEARRAY: bytearray = (byte[])value; break;
		case FILEARRAY: filearray = (byte[])value; break;
		}

	}

	@Override
	Object getConfig(int param) {
		switch(param){
		case TCP_SERVERPORT: return serverPortNum;
		case TCP_CLIENTPORT: return clientPortNum;
		case TCP_CLIENTHOSTNAME: return clientHostname;
		case TCP_BUFFERSIZE: return TCPbuffersize;
		case UDP_BUFFERSIZE: return UDPbuffersize;
		case ADDRESS: return IPAddress;
		case FILEPATH: return filepath;
		case BYTEARRAY: return bytearray;
		case FILEARRAY: return filearray;
		case SIZE: return size; //add
		}
		return null;
	}

}
