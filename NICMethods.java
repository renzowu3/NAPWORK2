package napwork;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class NICMethods extends Device{
	//JpcapCaptor Attributes
	private int capDeviceIndex = 0;
	private int capSnaplen = 65535;
	private boolean capPromisc = false;
	private int capTimeoutms = 20;
	//JpcapSender Attributes
	private int sendDeviceIndex = 0;
	//TCP Packet Attributes
	private int tcpsourcePort = 12;
	private int tcpdestinationPort = 34;
	private int tcp_seq = 56;
	private int tcp_ack_num = 78;
	private boolean tcp_urg = false;
	private boolean tcp_ack = false;
	private boolean tcp_psh = false;
	private boolean tcp_rst = false;
	private boolean tcp_syn = true;
	private boolean tcp_fin = true;
	private boolean tcp_rsv1 = true;
	private boolean tcp_rsv2 = true;
	private int tcp_window = 10;
	private int tcp_urgent = 10;
	//UDP Packet Attributes
	private int udpsourcePort = 12345;
	private int udpdestinationPort = 54321;
	//IMCP Packet Attributes
	private int icmpID = 999;
	private int icmp_seq = 1000;
	private int icmp_origtimestamp = 123;
	private int icmp_recvtimestamp = 456;
	private int icmp_transtimestamp = 789;
	private int icmp_subnetmask;
	//IPv4 Parameters Attributes
	private int ipv4_priority = 0;
	private boolean d_flag = false;
	private boolean t_flag = false;
	private boolean r_flag = false;
	private int rsv_tos = 0;
	private boolean rsv_frag = false;
	private boolean dont_frag = false;
	private boolean more_frag = false;
	private int offset = 0;
	private int ident = 1010101;
	private int ttl = 100;
	//defined depending on what type of packet will be sent
	private int protocol;
	private String src;
	private String dst;
	private byte[] srcMac;
	private byte[] dstMac;

	//Object Attributes
	private NetworkInterface[] devices;
	private JpcapSender sender = null;
	private JpcapCaptor captor = null;
	private TCPPacket tcp_packet = null;
	private UDPPacket udp_packet = null;
	private ICMPPacket icmp_packet = null;
	private EthernetPacket e_packet = null;

	//Conditional value for setConfig() and getConfig()
	public static final int CAPTOR_INDEX = 1;
	public static final int CAPTOR_SNAPLEN = 2;
	public static final int CAPTOR_TIMEOUT = 3;
	public static final int CAPTOR_PROMISCMODE = 4;
	public static final int SENDER_INDEX = 5;
	public static final int TCP_SRCPORT= 6;
	public static final int TCP_DSTPORT= 7;
	public static final int TCP_SEQ= 8;
	public static final int TCP_ACK_NUM= 9;
	public static final int TCP_WINDOW = 10;
	public static final int TCP_URGENT=11; //for int urgent
	public static final int TCP_URG = 12; //for boolean urg
	public static final int TCP_ACK = 13;
	public static final int TCP_PSH = 14;
	public static final int TCP_RST = 15;
	public static final int TCP_SYN = 16;
	public static final int TCP_FIN = 17;
	public static final int TCP_RSV1 = 18;
	public static final int TCP_RSV2 = 19;
	public static final int UDP_SRCPORT=20;
	public static final int UDP_DSTPORT=21;
	public static final int ICMP_ID =22;
	public static final int ICMP_SEQUENCE=23;
	public static final int ICMP_ORIGINAL_TIMESTAMP =24;
	public static final int ICMP_RECEIVE_TIMESTAMP =25;
	public static final int ICMP_TRANSMIT_TIMESTAMP =26;
	public static final int ICMP_SUBNETMASK =27;
	public static final int ICMP_TYPE = 28;
	public static final int ICMP_ECHO = 29;
	public static final int ICMP_ECHOREPLY=30;
	public static final int IPV4_PRIORITY=31;
	public static final int IPV4_RSV_TOS=32;
	public static final int IPV4_OFFSET=33;
	public static final int IPV4_IDENT=34;
	public static final int IPV4_TTL=35;
	public static final int IPV4_PROTOCOL=36;
	public static final int D_FLAG=37;
	public static final int T_FLAG=38;
	public static final int R_FLAG=39;
	public static final int RSV_FRAG=40;
	public static final int DONT_FRAG=41;
	public static final int MORE_FRAG=42;
	public static final int IPV4_SRC=43;
	public static final int IPV4_DST=44;
	public static final int IPV4_MAC_SRC = 52;
	public static final int IPV4_MAC_DST = 53;
	//Conditional value for open()
	public static final int OPEN_CAPTUREDEVICE = 45;
	public static final int OPEN_SENDERDEVICE = 46;
	//Conditional value for write()
	public static final int WRITE_TCPPACKET =47;
	public static final int WRITE_UDPPACKET=48;
	public static final int WRITE_ICMPPACKET=49;
	//Conditional value for close()
	public static final int CLOSE_CAPTUREDEVICE=50;
	public static final int CLOSE_SENDERDEVICE=51;
	//Conditional value for read()
	public static final int READ_PACKET=54;


	//Constructor Needed?
	NICMethods(){
		//What to put here...?
	}
	//Methods
	void open(int param){
		//Instantiate either JpcapCaptor or JpcapSender
		devices = JpcapCaptor.getDeviceList();
		if(param == OPEN_CAPTUREDEVICE){
			try {
				captor = JpcapCaptor.openDevice(devices[capDeviceIndex], capSnaplen, capPromisc, capTimeoutms);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(param == OPEN_SENDERDEVICE){
			try {
				sender=JpcapSender.openDevice(devices[capDeviceIndex]);
				System.out.println(devices[capDeviceIndex]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	void close(int param){
		//Close either JpcapCaptor or JpcapSender
		if(param == CLOSE_CAPTUREDEVICE){
			captor.close();
		} else if(param == CLOSE_SENDERDEVICE){
			sender.close();
		}
	}

	Object read(int param){
		if(param == READ_PACKET){
			Packet packet = captor.getPacket();    
			return packet;
		}
		return null;
	}

	void write(int param){ //updated
		if(param == WRITE_TCPPACKET){
			tcp_packet = new TCPPacket(tcpsourcePort, tcpdestinationPort, tcp_seq, tcp_ack_num, tcp_urg, tcp_ack, tcp_psh, tcp_rst, tcp_syn, tcp_fin, tcp_rsv1, tcp_rsv2, tcp_window, tcp_urgent);
			e_packet = new EthernetPacket();
			try {
				tcp_packet.setIPv4Parameter(ipv4_priority, d_flag, t_flag, r_flag, rsv_tos, rsv_frag, dont_frag, more_frag, offset, ident, ttl, protocol, InetAddress.getByName(src), InetAddress.getByName(dst));
				tcp_packet.data = ("data").getBytes();
				e_packet.frametype = EthernetPacket.ETHERTYPE_IP;
				e_packet.src_mac = srcMac;
				e_packet.dst_mac = dstMac;
				tcp_packet.datalink = e_packet;
				sender.sendPacket(tcp_packet);
				
				System.out.println(tcp_packet);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("test");
		}
		else if(param == WRITE_UDPPACKET){
			udp_packet = new UDPPacket(udpsourcePort, udpdestinationPort);
			e_packet = new EthernetPacket();
			try {
				udp_packet.setIPv4Parameter(ipv4_priority, d_flag, t_flag, r_flag, rsv_tos, rsv_frag, dont_frag, more_frag, offset, ident, ttl, protocol, InetAddress.getByName(src), InetAddress.getByName(dst));
				udp_packet.data = ("data").getBytes();
				e_packet.frametype = EthernetPacket.ETHERTYPE_IP;
				e_packet.src_mac = srcMac;
				e_packet.dst_mac = dstMac;
				udp_packet.datalink = e_packet;
				sender.sendPacket(udp_packet);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(param == WRITE_ICMPPACKET){
			icmp_packet = new ICMPPacket();
			icmp_packet.type = ICMPPacket.ICMP_ECHO;
			icmp_packet.seq = (short) icmp_seq;
			icmp_packet.id = (short) icmpID;
			icmp_packet.orig_timestamp = icmp_origtimestamp;
			icmp_packet.trans_timestamp = icmp_transtimestamp;
			icmp_packet.recv_timestamp = icmp_recvtimestamp;
			e_packet = new EthernetPacket();
			try {
				icmp_packet.setIPv4Parameter(ipv4_priority, d_flag, t_flag, r_flag, rsv_tos, rsv_frag, dont_frag, more_frag, offset, ident, ttl, protocol, InetAddress.getByName(src), InetAddress.getByName(dst));
				icmp_packet.data = ("data").getBytes();
				e_packet.frametype = EthernetPacket.ETHERTYPE_IP;
				e_packet.src_mac = srcMac;
				e_packet.dst_mac = dstMac;
				icmp_packet.datalink = e_packet;
				sender.sendPacket(icmp_packet);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void setConfig(int param, Object value){
		//insert conditional statements here and passing of value to corresponding variable
		switch(param){
		case CAPTOR_INDEX: capDeviceIndex = (int)value; break;
		case CAPTOR_SNAPLEN: capSnaplen = (int)value; break;
		case CAPTOR_TIMEOUT: capTimeoutms = (int)value; break;
		case TCP_SRCPORT: tcpsourcePort = (int)value; break;
		case TCP_DSTPORT: tcpdestinationPort = (int)value; break;
		case TCP_SEQ: tcp_seq = (int)value; break;
		case TCP_ACK_NUM: tcp_ack_num = (int)value; break;
		case TCP_WINDOW: tcp_window = (int)value; break;
		case TCP_URGENT: tcp_urgent = (int)value; break;
		case UDP_SRCPORT: udpsourcePort = (int)value; break;
		case UDP_DSTPORT: udpdestinationPort = (int)value; break;
		case ICMP_ID: icmpID = (int)value; break;
		case ICMP_SEQUENCE: icmp_seq = (int)value; break;
		case ICMP_ORIGINAL_TIMESTAMP: icmp_origtimestamp = (int)value; break;
		case ICMP_RECEIVE_TIMESTAMP: icmp_recvtimestamp = (int)value; break;
		case ICMP_TRANSMIT_TIMESTAMP: icmp_transtimestamp = (int)value; break;
		case ICMP_SUBNETMASK: icmp_subnetmask = (int)value; break;
		case IPV4_PRIORITY: ipv4_priority = (int)value; break;
		case IPV4_RSV_TOS: rsv_tos = (int)value; break;
		case IPV4_OFFSET: offset = (int)value; break;
		case IPV4_IDENT: ident = (int)value; break;
		case IPV4_TTL: ttl = (int)value; break;
		case IPV4_PROTOCOL: protocol = (int)value; break;

		case CAPTOR_PROMISCMODE: capPromisc = (boolean)value; break;
		case TCP_ACK: tcp_ack = (boolean)value; break;
		case TCP_PSH: tcp_psh = (boolean)value; break;
		case TCP_RST: tcp_rst = (boolean)value; break;
		case TCP_SYN: tcp_syn = (boolean)value; break;
		case TCP_FIN: tcp_fin = (boolean)value; break;
		case TCP_RSV1: tcp_rsv1 = (boolean)value; break;
		case TCP_RSV2: tcp_rsv2 = (boolean)value; break;
		case D_FLAG: d_flag = (boolean)value; break;
		case T_FLAG: t_flag = (boolean)value; break;
		case R_FLAG: r_flag = (boolean)value; break;
		case RSV_FRAG: rsv_frag = (boolean)value; break;
		case DONT_FRAG: dont_frag = (boolean)value; break;
		case MORE_FRAG: more_frag = (boolean)value; break;

		case IPV4_SRC: src = (String)value; break;
		case IPV4_DST: dst = (String)value; break;

		case IPV4_MAC_SRC: srcMac = (byte[])value; break;
		case IPV4_MAC_DST: dstMac = (byte[])value; break;
		}
	}


	Object getConfig(int param){
		//insert conditional statements here and returning of corresponding Object
		switch(param){
		case CAPTOR_INDEX: return capDeviceIndex;
		case CAPTOR_PROMISCMODE: return capPromisc;
		case CAPTOR_SNAPLEN: return capSnaplen;
		case CAPTOR_TIMEOUT: return capTimeoutms;
		case SENDER_INDEX: return sendDeviceIndex;
		case TCP_ACK: return tcp_ack;
		case TCP_ACK_NUM: return tcp_ack_num;
		case TCP_DSTPORT: return tcpdestinationPort;
		case TCP_SRCPORT: return tcpsourcePort;
		case TCP_FIN: return tcp_fin;
		case TCP_PSH: return tcp_psh;
		case TCP_RST: return tcp_rst;
		case TCP_RSV1: return tcp_rsv1;
		case TCP_RSV2: return tcp_rsv2;
		case TCP_SEQ: return tcp_seq;
		case TCP_SYN: return tcp_syn;
		case TCP_URG: return tcp_urg;
		case TCP_URGENT: return tcp_urgent;
		case TCP_WINDOW: return tcp_window;
		case UDP_SRCPORT: return udpsourcePort;
		case UDP_DSTPORT: return udpdestinationPort;
		case ICMP_ID: return icmpID;
		case ICMP_SEQUENCE: return icmp_seq;
		case ICMP_ORIGINAL_TIMESTAMP: return icmp_origtimestamp;
		case ICMP_RECEIVE_TIMESTAMP: return icmp_recvtimestamp;
		case ICMP_TRANSMIT_TIMESTAMP: return icmp_transtimestamp;
		case ICMP_SUBNETMASK: return icmp_subnetmask;
		case IPV4_PRIORITY: return ipv4_priority;
		case IPV4_RSV_TOS: return rsv_tos;
		case IPV4_OFFSET: return offset;
		case IPV4_IDENT: return ident;
		case IPV4_TTL: return ttl;
		case IPV4_PROTOCOL: return protocol;
		case D_FLAG: return d_flag;
		case T_FLAG: return t_flag;
		case R_FLAG: return r_flag;
		case DONT_FRAG: return dont_frag;
		case MORE_FRAG: return more_frag;
		case IPV4_SRC: return src;
		case IPV4_DST: return dst;
		case IPV4_MAC_SRC: return srcMac;
		case IPV4_MAC_DST: return dstMac;
		}
		return null;

	}



}



