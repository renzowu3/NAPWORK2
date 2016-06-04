package napwork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ajwcc.pduUtils.test.integration.AbstractTester.InboundNotification;
import org.smslib.AGateway.Protocols;
import org.smslib.GatewayException;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;
import org.smslib.notify.GatewayStatusNotification;

import com.harshadura.gsm.smsdura.GsmModem.OutboundNotification;

public class BroadbandMethods extends Device {


	private List<InboundMessage> msgList;
	private SerialModemGateway gateway = null;
	private InboundNotification inboundNotification = null;
	private OutboundNotification outboundNotification = null;
	//private GatewayStatusNotification statusNotification = null;
	//private OrphanedMessageNotification orphanedMessageNotification = null;
	private String Read_modemID;
	private String Read_commPort;
	private int Read_baudRate;
	private String Read_manufacturer;
	private String Read_model;
	private String Send_modemID;
	private String Send_commPort;
	private int Send_baudRate;
	private String Send_manufacturer = "";
	private String Send_model = "";
	private String textMessage;
	private String contactNumber;

	public final static int RECEIVE_MODEMID = 1;
	public final static int RECEIVE_COMMPORT = 2;
	public final static int RECEIVE_MANUFACTURER = 3;
	public final static int RECEIVE_MODEL = 4;
	public final static int RECEIVE_BAUDRATE = 5;
	public final static int SEND_MODEMID = 6;
	public final static int SEND_COMMPORT = 7;
	public final static int SEND_MANUFACTURER = 8;
	public final static int SEND_MODEL = 9;
	public final static int SEND_BAUDRATE = 10;
	public final static int SENDSMSGATEWAY = 11;
	public final static int READSMSGATEWAY = 12;
	public final static int READ = 13;
	public final static int READ_ALL = 14;
	public final static int UNREAD = 15;
	public final static int TEXTMESSAGE = 15;
	public final static int NUMBER = 16;
	public final static int CLOSEGATEWAY = 17;
	public final static int SENDSMS = 18;

	@Override
	void open(int param) {
		if(param == SENDSMSGATEWAY){
			//outboundNotification = new OutboundNotification();
			gateway = new SerialModemGateway(Send_modemID, Send_commPort, Send_baudRate, Send_manufacturer, Send_model);
			//SerialModemGateway gateway = new SerialModemGateway("modem.com4", "/dev/ttyUSB9", 9600, "", "");
			gateway.setInbound(true);
			gateway.setOutbound(true);    
		}
		else if(param == READSMSGATEWAY){
			//inboundNotification = new InboundNotification();
			gateway = new SerialModemGateway(Read_modemID, Read_commPort, Read_baudRate, Read_manufacturer, Read_model);
			//statusNotification = new GatewayStatusNotification();
			//orphanedMessageNotification = new OrphanedMessageNotification();
			gateway.setProtocol(Protocols.PDU);
			gateway.setInbound(true);
			gateway.setOutbound(true);
		}
	}

	@Override
	void close(int param) {
		if(param == CLOSEGATEWAY){
			try {
				Service.getInstance().stopService();
			} catch (SMSLibException | IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	void write(int param) {
		if(param == SENDSMS){
			Service.getInstance().setOutboundMessageNotification(outboundNotification);

			try {
				Service.getInstance().addGateway(gateway);
				Service.getInstance().startService();
				OutboundMessage msg = new OutboundMessage(contactNumber, textMessage);
				Service.getInstance().sendMessage(msg);
			} catch (SMSLibException | IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

	@Override
	Object read(int param) {
		//Service.getInstance().setInboundMessageNotification(inboundNotification);
		//Service.getInstance().setGatewayStatusNotification(statusNotification);
		//Service.getInstance().setOrphanedMessageNotification(orphanedMessageNotification);
		msgList = new ArrayList<InboundMessage>();

		try {
			Service.getInstance().addGateway(gateway);
			Service.getInstance().startService();
			if(param == READ){
				Service.getInstance().readMessages(msgList, MessageClasses.READ);
			}
			else if(param == READ_ALL){
				Service.getInstance().readMessages(msgList, MessageClasses.ALL);
			}
			else if(param == UNREAD){
				Service.getInstance().readMessages(msgList, MessageClasses.UNREAD);
			}
		} catch (SMSLibException | IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return msgList.toString();
	}

	@Override
	void setConfig(int param, Object value) {
		switch(param){
		case RECEIVE_MODEMID: Read_modemID = (String)value; break;
		case RECEIVE_MODEL: Read_model = (String)value;break;
		case RECEIVE_MANUFACTURER: Read_manufacturer = (String)value;break;
		case RECEIVE_COMMPORT: Read_commPort = (String)value;break;
		case RECEIVE_BAUDRATE: Read_baudRate = (int)value;break;
		case SEND_MODEMID: Send_modemID = (String)value;break;
		case SEND_MODEL: Send_model = (String)value;break;
		case SEND_MANUFACTURER: Send_manufacturer = (String)value;break;
		case SEND_COMMPORT: Send_commPort = (String)value;break;
		case SEND_BAUDRATE: Send_baudRate = (int)value;break;
		case TEXTMESSAGE: textMessage = (String)value;break;
		case NUMBER: contactNumber = (String)value;break;
		}
	}

	@Override
	Object getConfig(int param) {
		switch(param){
		case RECEIVE_MODEMID: return Read_modemID;
		case RECEIVE_MODEL: return Read_model;
		case RECEIVE_MANUFACTURER: return Read_manufacturer;
		case RECEIVE_COMMPORT: return Read_commPort;
		case RECEIVE_BAUDRATE: return Read_baudRate;
		case SEND_MODEMID: return Send_modemID;
		case SEND_MODEL: return Send_model;
		case SEND_MANUFACTURER: return Send_manufacturer;
		case SEND_COMMPORT: return Send_commPort;
		case SEND_BAUDRATE: return Send_baudRate;
		case TEXTMESSAGE: return textMessage;
		case NUMBER: return contactNumber;
		}
		return null;
	}

}
