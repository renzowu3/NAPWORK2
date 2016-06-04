package napwork;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyboardMethods extends Device implements KeyListener{

	private ArrayList<Integer> keyCode = new ArrayList<Integer>();
	private boolean keyCodeChecker;

	public static final int SETKEYCODE = 1;
	public static final int GETKEYCODE = 2;
	public static final int READ_KEY = 3;

	@Override
	void open(int param) {
		// NO FUNCTION
	}

	@Override
	void close(int param) {
		// NO FUNCTION
	}

	@Override
	void write(int param) {
		// NO FUNCTION
	}

	@Override
	Object read(int param) {
		return keyCodeChecker;
	}

	@Override
	void setConfig(int param, Object value) {
		if(param == SETKEYCODE){
			keyCode.add((int)value);
		}

	}

	@Override
	Object getConfig(int param) {
		if(param == GETKEYCODE){
			return keyCode;
		}
		return null;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Not Used
	}
	//create Thread for buffer to store keycode of keystroke
	@Override
	public void keyPressed(KeyEvent e){
		for(int i = 0; i < keyCode.size(); i++){
			if(e.getKeyCode() == keyCode.get(i)) //replace 'a' with key set with setConfig
			{
				keyCodeChecker = true;			
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Not Used

	}

}