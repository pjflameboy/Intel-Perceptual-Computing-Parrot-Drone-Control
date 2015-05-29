package droneKeyboardControl;


import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jivesoftware.smack.XMPPException;

import edu.bath.sensorframework.sensor.Sensor;
import edu.bath.sensorframework.JsonReading;

public class keyboardControl extends Sensor {
	private float rollSpeed = 0f;
	private float yawSpeed = 0f;
	private float pitchSpeed = 0f;
	private float altitudeSpeed = 0f;
	private boolean flying = false;
	private boolean flipleft = false;
	private boolean flipright = false;
	private boolean takeoff = false;
	private boolean land = false;
	private ArrayList<String>typedKeys = new ArrayList<String>();
	
	public keyboardControl(String server, String user, String pwd, String node) throws XMPPException
	{
		super(server, user, pwd, node);
		init();
	}

	public void run() throws UnsupportedEncodingException
	{

		double i = 0.0f;
		int cnt = 0;
		
//		while(true){
//		}
	}
	private void init(){
		JFrame jf = new JFrame();
		jf.setTitle("My Empty Frame");
		jf.setSize(300,200); // default size is 0,0
		jf.setLocation(10,200); // default is 0,0 (top left corner)
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		System.out.println("grab the whole keyboard input from now on ...");
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(keyEventDispatcher);
	    while(true){
	    	long startTime = System.currentTimeMillis();
	    	while(System.currentTimeMillis()-startTime<20){
	    		//System.out.println(startTime);
	    	}
	    	String nextCommand = processCommand();
	    	if(!nextCommand.equals("")){
	    		try {
					sendCommand(nextCommand);
					System.out.println(nextCommand);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
	    	}
	    }
		
	}
	private KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
		
		public boolean dispatchKeyEvent(KeyEvent e)
		{
			if (e.getID() == KeyEvent.KEY_PRESSED) 
			{

				int key = e.getKeyCode();
				if(key==KeyEvent.VK_Q){
					flipleft = true;
				}
				if(key == KeyEvent.VK_E)
					flipright = true;
				
				if(key==KeyEvent.VK_ENTER)
					if(flying==false){
						takeoff=true;
					}
				if(key==KeyEvent.VK_SPACE)
					if(flying==true)
						land=true;
				
				
				if(!typedKeys.contains("" + e.getKeyCode()))
					typedKeys.add("" + e.getKeyCode());
				
				
				
				if(typedKeys.contains("" + KeyEvent.VK_UP)){
	            	if(altitudeSpeed ==0)
	            		altitudeSpeed = 0.1f;
	            	else if(altitudeSpeed >=0.99f){
	            		altitudeSpeed = 1f;
	            	}
	            	else
	            		altitudeSpeed+=0.02;
				}
				if(typedKeys.contains("" + KeyEvent.VK_DOWN)){
	            	if(altitudeSpeed ==0)
	            		altitudeSpeed = -0.1f;
	            	else if(altitudeSpeed <=-0.99f){
	            		altitudeSpeed = -1f;
	            	}
	            	else
	            		altitudeSpeed+=-0.02;
				}
				if(typedKeys.contains("" + KeyEvent.VK_RIGHT)){
	            	if(yawSpeed ==0)
	            		yawSpeed = 0.1f;
	            	else if(yawSpeed >=0.99f){
	            		yawSpeed = 1f;
	            	}
	            	else
	            		yawSpeed+=0.02;
				}
				if(typedKeys.contains("" + KeyEvent.VK_LEFT)){
	            	if(yawSpeed ==0)
	            		yawSpeed = -0.1f;
	            	else if(yawSpeed <=-0.99f){
	            		yawSpeed = -1f;
	            	}
	            	else
	            		yawSpeed+=-0.02;
				}
				if(typedKeys.contains("" + KeyEvent.VK_S)){
	            	if(pitchSpeed ==0)
	            		pitchSpeed = 0.1f;
	            	else if(pitchSpeed >=0.99f){
	            		pitchSpeed = 1f;
	            	}
	            	else
	            		pitchSpeed+=0.02;
				}
				if(typedKeys.contains("" + KeyEvent.VK_W)){
	            	if(pitchSpeed ==0)
	            		pitchSpeed = -0.1f;
	            	else if(pitchSpeed <=-0.99f){
	            		pitchSpeed = -1f;
	            	}
	            	else
	            		pitchSpeed+=-0.02;
				}
				if(typedKeys.contains("" + KeyEvent.VK_D)){
	            	if(rollSpeed ==0)
	            		rollSpeed = 0.1f;
	            	else if(rollSpeed >=0.99f){
	            		rollSpeed = 1f;
	            	}
	            	else
	            		rollSpeed+=0.02;
				}
				if(typedKeys.contains("" + KeyEvent.VK_A)){
	            	if(rollSpeed ==0)
	            		rollSpeed = -0.1f;
	            	else if(rollSpeed <=-0.99f){
	            		rollSpeed = -1f;
	            	}
	            	else
	            		rollSpeed+=-0.02;
				}
				
				

				
	        } 
			else if (e.getID() == KeyEvent.KEY_RELEASED) 
	        {
				
				int key = e.getKeyCode();
				if(typedKeys.contains("" + e.getKeyCode()))
					typedKeys.remove("" + e.getKeyCode());
				if(!typedKeys.contains("" + KeyEvent.VK_UP)&&!typedKeys.contains("" + KeyEvent.VK_UP)){
					altitudeSpeed = 0;
				}
				if(!typedKeys.contains("" + KeyEvent.VK_RIGHT)&&!typedKeys.contains("" + KeyEvent.VK_LEFT)){
					yawSpeed = 0;
				}
				if(!typedKeys.contains("" + KeyEvent.VK_D)&&!typedKeys.contains("" + KeyEvent.VK_A)){
					rollSpeed = 0;
				}
				if(!typedKeys.contains("" + KeyEvent.VK_W)&&!typedKeys.contains("" + KeyEvent.VK_S)){
					pitchSpeed = 0;
				}
	        }
	        return false;
		}
	};
	
	private String processCommand(){
		String command = "";
		if(flying == false){
			if(takeoff==true){
				command="takeoff";
				takeoff = false;
				flying = true;
			}
		}
		else{

			if(yawSpeed!=0||altitudeSpeed!=0||rollSpeed!=0||pitchSpeed!=0){
				command = "move("+rollSpeed + ","+pitchSpeed + "," + altitudeSpeed + "," + yawSpeed + ")";
			}
			else if(land == true){
				command = "land";
				land = false;
				flying = false;
			}
			else if(flipleft ==true){
				command = "flipleft";
				flipleft = false;
				flipright = false;
			}
			else if(flipright == true){
				command = "flipright";
				flipright = false;
			}
			else{
				command = "hover";
			}
			
		}
		
		return command;
	}
	
	public void sendCommand(String command) throws UnsupportedEncodingException{
		JsonReading jr = new JsonReading();
		jr.addValue("command",command);
		publish(jr);
	}
	
	public static void main(String[] args){
		try {
			keyboardControl kb = new keyboardControl("paul-dell", "camerapub", "camerapass", "camdroneinterface");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

	}
	
}


