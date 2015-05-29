package mainControl;
import java.io.UnsupportedEncodingException;


import javax.swing.JOptionPane;

import org.jivesoftware.smack.XMPPException;

import intelControl.intelCameraControl;
import JsonPublisher.JsonSensor;

public class main {
	public static void main(String[] args) throws XMPPException, UnsupportedEncodingException {
		String messageDialog = "Place open hands over the circles (match palm to circle)\nThumbs up to take off\nOpen hands (high fives) to land\n";
		messageDialog+= "Move hands up and down separately as if using a steering wheel to turn left and right\n";
		messageDialog+= "Move both hands up or down to adjust altitude\nMove both hands forwards or backwards to move forwards or backwards\n";
		messageDialog+= "Move one hand forward or backwards to turn the drone left or right\n";
		messageDialog+= "Show the peace sign on either hand to flip the drone left or right";
		
//		if(JOptionPane.showConfirmDialog(null, "Would you like to see the instructions?", "Welcome", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
//			JOptionPane.showMessageDialog(null, messageDialog);
//		}
		JsonSensor js = new JsonSensor("paul-desktop", "camerapub", "camerapass", "camdroneinterface");
		//js.run();
		intelCameraControl icc = new intelCameraControl(js);
	}

}
