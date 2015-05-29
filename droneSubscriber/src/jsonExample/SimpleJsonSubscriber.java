package jsonExample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;

import javax.swing.*;

import org.jivesoftware.smack.XMPPException;

import de.yadrone.apps.controlcenter.plugins.keyboard.KeyboardCommandManagerAlternative;
import de.yadrone.apps.paperchase.controller.PaperChaseAbstractController;
import de.yadrone.apps.paperchase.controller.PaperChaseAutoController;
import de.yadrone.apps.paperchase.controller.PaperChaseKeyboardController;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.FlightAnimation;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.video.ImageListener;
import edu.bath.sensorframework.JsonReading;
import edu.bath.sensorframework.JsonReading.Value;
import edu.bath.sensorframework.client.ReadingHandler;
import edu.bath.sensorframework.client.SensorClient;


public class SimpleJsonSubscriber {
	private boolean flying = false;
	private final droneOutputs droneOut;
	private IARDrone drone = null;
	private KeyboardCommandManagerAlternative keyboardCommandManager;
	private PaperChaseAbstractController autoController;
	private boolean flippedLast = false;
	public SimpleJsonSubscriber(){
		SensorClient sc = null;
		try {
			sc = new SensorClient("paul-dell", "dronesub1", "dronepass1");
		} catch (XMPPException e1) {
			e1.printStackTrace();
			System.exit(2);
		}
		droneOut = new droneOutputs();
		droneOut.setVisible(true);
		
		
		
		try
		{
			drone = new ARDrone();
			drone.start();
			
			cmd = drone.getCommandManager();
			

			drone.getNavDataManager().addAttitudeListener(new AttitudeListener() {
				
			    public void attitudeUpdated(float pitch, float roll, float yaw)
			    {
			        pitch = Math.round(pitch/1000);
			        yaw = Math.round(yaw/1000);
			        roll = Math.round(roll/1000);
			        droneOut.setPitchSlider((int)pitch);
			        droneOut.setYawSlider((int)yaw);
			        droneOut.setRollSlider((int)roll);
			    }

			    public void attitudeUpdated(float pitch, float roll) { }
			    public void windCompensation(float pitch, float roll) { }
			});
			drone.getNavDataManager().addAltitudeListener(new AltitudeListener() {
				
				public void receivedAltitude(int altitude){
					droneOut.setAltitude(altitude);
				}

				public void receivedExtendedAltitude(Altitude d){
				}

			});
			
			drone.getNavDataManager().addBatteryListener(new BatteryListener() {
				
			    public void batteryLevelChanged(int percentage)
			    {
			    	if(percentage<30){
			    		droneOut.setBatteryColour(Color.RED);
			    	}
			    	droneOut.setBatteryLevel(percentage);
			    	
			    }

			    public void voltageChanged(int vbat_raw) { }
			});
			
			TutorialVideoListener tv = new TutorialVideoListener(drone);
			PaperChaseKeyboardController keyboardController = new PaperChaseKeyboardController(drone);
			keyboardController.start();
			autoController = new PaperChaseAutoController(drone);

		}
		catch(Exception e){
			e.printStackTrace();
		}

		sc.addHandler("camdroneinterface", new ReadingHandler() {
			public void handleIncomingReading (String node, String rdf) {
				try {
					JsonReading jr = new JsonReading();
					jr.fromJSON(rdf);
					Value val = jr.findValue("command");
					String command = val.m_object.toString();
					
					
					if (val != null)
					{
						if(command.contains("flip")){
							if(flippedLast == true){
								command = "";
							}
							else{ flippedLast = true;}
						}
						else{
							flippedLast = false;
						}
						
						if(command.equals("takeoff")){
							cmd.takeOff();
							flying = true;
						}
						if(command.equals("hover")&&flying)
							cmd.hover();
						if(command.equalsIgnoreCase("land")&&flying){
							cmd.landing();
							flying = false;
						}
						if(command.contains("move")&&flying){
							float[] moveSpeeds;
							moveSpeeds = getSpeeds(command);
							cmd.move(moveSpeeds[0], moveSpeeds[1],moveSpeeds[2],moveSpeeds[3]);
							System.out.println("move(" + moveSpeeds[0]+" , "+ moveSpeeds[1] + " , " +  moveSpeeds[2]+" , " + moveSpeeds[3]+ ")");
						}
						if(command.equals("flipleft")){
							System.out.println("flipping left");
							drone.getCommandManager().animate(FlightAnimation.FLIP_LEFT);
							//Thread.sleep(3000);
							
						}
						if(command.equals("flipright")){
							System.out.println("flipping right");
							drone.getCommandManager().animate(FlightAnimation.FLIP_RIGHT);
						}
						
						
						
					
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		try {
			sc.subscribe("camdroneinterface");
		} catch (XMPPException e) {
			System.out.println("camera control stream not found");
			System.exit(3);
		}
		
		
		
		
		
		
		
		while (true)
		{
		
		}
		
	}
	/**
	 * @param args
	 */
	private static CommandManager cmd = null;
	public static void main(String[] args) throws XMPPException 
	{
		SimpleJsonSubscriber sc = new SimpleJsonSubscriber();
		
	}
	
	private float[] getSpeeds(String command){
		String tmp = command.substring(5);
		String[] items = tmp.replaceAll("\\(", "").replaceAll("\\)", "").split(",");

		float[] results = new float[items.length];

		for (int i = 0; i < items.length; i++) {
		    try {
		        results[i] = Float.parseFloat(items[i]);
		    } catch (NumberFormatException nfe) {};
		}
		return results;

	}
	
	
	
	
	
	
	
}

class TutorialVideoListener extends JFrame implements ComponentListener
{
    private BufferedImage image = null;
    private int IMG_WIDTH = 640;
    private int IMG_HEIGHT = 360;
    
    public TutorialVideoListener(final IARDrone drone)
    {
        super("Drone Video Stream");
        
        setSize(640,360);
        setVisible(true);
        this.addComponentListener(this);
        //sleep allows main thread time to establish drone connection
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
		}
        drone.getVideoManager().addImageListener(new ImageListener() {
            public void imageUpdated(BufferedImage newImage)
            {
            	image = newImage;
            	int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
            	image = resizeImage(image,type);
        		SwingUtilities.invokeLater(new Runnable() {
        			public void run()
        			{
        				repaint();
        			}
        		});
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e)
            {
                drone.getCommandManager().setVideoChannel(VideoChannel.NEXT);
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) 
            {
                drone.stop();
                System.exit(0);
            }
        });
    }
    
    public void paint(Graphics g)
    {
        if (image != null)
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, int type){
    	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
    	Graphics2D g = resizedImage.createGraphics();
    	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
    	g.dispose();
 
	return resizedImage;
    }
    
    public void componentResized(ComponentEvent e) {
    	Rectangle r = getBounds();
    	IMG_HEIGHT = r.height;
    	IMG_WIDTH = r.width;
    	//System.out.println(IMG_WIDTH + "   " + IMG_HEIGHT);
    }

    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}
}

class droneOutputs extends JFrame {

    /**
     * Creates new form droneOutputs
     */
    public droneOutputs() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        rollSlider = new JSlider();
        pitchSlider = new JSlider();
        yawSlider = new JSlider();
        rollLabel = new JLabel();
        pitchLabel = new JLabel();
        yawLabel = new JLabel();
        batteryLabel = new JLabel();
        batteryStatus = new JTextField();
        altitudeLabel = new JLabel();
        altitudeValue = new JTextField();
        forceLandButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Drone Status Bar");
        setResizable(false);
        
        rollSlider.setMaximum(40);
        rollSlider.setMinimum(-40);
        rollSlider.setValue(0);

        pitchSlider.setMaximum(40);
        pitchSlider.setMinimum(-40);
        pitchSlider.setValue(0);

        yawSlider.setMaximum(180);
        yawSlider.setMinimum(-180);
        yawSlider.setValue(0);

        rollLabel.setLabelFor(rollSlider);
        rollLabel.setText("Roll");
        rollLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        pitchLabel.setLabelFor(pitchSlider);
        pitchLabel.setText("Pitch");
        pitchLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        yawLabel.setLabelFor(yawSlider);
        yawLabel.setText("Yaw");
        yawLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        batteryLabel.setText("Battery Level: ");
        batteryLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        batteryStatus.setBackground(new java.awt.Color(51, 255, 51));
        batteryStatus.setText("100%");
        batteryStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batteryStatusActionPerformed(evt);
            }
        });

        altitudeLabel.setText("Altitude:");
        altitudeLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        altitudeValue.setText("0");
        altitudeValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                altitudeValueActionPerformed(evt);
            }
        });

        forceLandButton.setText("Force Land");
        forceLandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forceLandButtonActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(rollSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(batteryLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(batteryStatus, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pitchSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(yawSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(rollLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(altitudeLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pitchLabel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                .addGap(175, 175, 175)
                                .addComponent(yawLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(altitudeValue, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(forceLandButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(rollSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(pitchSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(yawSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(pitchLabel)
                    .addComponent(yawLabel)
                    .addComponent(rollLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(batteryLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                    .addComponent(batteryStatus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(altitudeLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(altitudeValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(forceLandButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void batteryStatusActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void altitudeValueActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void forceLandButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(droneOutputs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(droneOutputs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(droneOutputs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(droneOutputs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
    }

    // Variables declaration - do not modify                     
    private JLabel altitudeLabel;
    private JTextField altitudeValue;
    private JLabel batteryLabel;
    private JTextField batteryStatus;
    private JButton forceLandButton;
    private JLabel pitchLabel;
    private JSlider pitchSlider;
    private JLabel rollLabel;
    private JSlider rollSlider;
    private JLabel yawLabel;
    private JSlider yawSlider;
    // End of variables declaration                   
    
    
    public void setRollSlider(int value){
    	if(value>rollSlider.getMaximum())
    		rollSlider.setValue(rollSlider.getMaximum());
    	else if(value<rollSlider.getMinimum())
    		rollSlider.setValue(rollSlider.getMinimum());
    	else
    		rollSlider.setValue(value);
    }
    public void setYawSlider(int value){
    	if(value>yawSlider.getMaximum())
    		yawSlider.setValue(yawSlider.getMaximum());
    	else if(value<yawSlider.getMinimum())
    		yawSlider.setValue(yawSlider.getMinimum());
    	else
    		yawSlider.setValue(value);
    }
    public void setPitchSlider(int value){
    	if(value>pitchSlider.getMaximum())
    		pitchSlider.setValue(pitchSlider.getMaximum());
    	else if(value<pitchSlider.getMinimum())
    		pitchSlider.setValue(pitchSlider.getMinimum());
    	else
    		pitchSlider.setValue(value);
    }
    public void setBatteryLevel(int percentage){
    	batteryStatus.setText(percentage + "%");
    }
    public void setAltitude(int altitude){
    	altitudeValue.setText("" + altitude);
    }
    public void setBatteryColour(Color y){
    	batteryStatus.setBackground(y);
    	
    }
}
