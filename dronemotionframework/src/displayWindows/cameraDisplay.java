package displayWindows;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import intelControl.intelCameraControl;

public class cameraDisplay extends JFrame{
	private intelCameraControl camfeed;
	private colourPicker colPic;
	public cameraDisplay(intelCameraControl camcon, colourPicker cp) {
		colPic = cp;
		System.out.println("CREATING GUI");    
		camfeed = camcon;
		initComponents();
	}
	
    private void initComponents() {

        jSeparator1 = new JSeparator();
        showRGB = new JRadioButton();
        commandsLabel = new JLabel();
        showNodes = new JRadioButton();
        palmCheckbox = new JCheckBox();
        fingerCheckbox = new JCheckBox();
        lastCommandLabel = new JLabel();
        lastcommandOutput = new JTextField();
        camFeedPanel = new JPanel();
        viewLabel = new JLabel();
        showDepth = new JRadioButton();
        jSeparator2 = new JSeparator();
        jSeparator3 = new JSeparator();
        jSeparator4 = new JSeparator();
        lastCommandLabel1 = new JLabel();
        leftHand = new JLabel();
        rightHand = new JLabel();
        speedLabel = new JLabel();
        speedSetter = new JSpinner();
        colourSchemeButton = new JButton();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jSeparator1.setOrientation(SwingConstants.VERTICAL);

        showRGB.setText("RGB");
        showRGB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showRGBVid){
                	camfeed.showRGBVid = false;
                }
                else{
                	camfeed.showRGBVid = true;
                }
            }
        });

        commandsLabel.setFont(new java.awt.Font("Tahoma", 0, 18));
        commandsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        commandsLabel.setText("  Options");

        showNodes.setText("Show Nodes");
        showNodes.setToolTipText("");
        showNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showGeoNodes){
                	camfeed.showGeoNodes = false;
                	palmCheckbox.setEnabled(false);
                	fingerCheckbox.setEnabled(false);
                }
                else{
                	camfeed.showGeoNodes = true;
                	palmCheckbox.setEnabled(true);
                	fingerCheckbox.setEnabled(true);
                }
            }
        });
        showNodes.setSelected(true);

        palmCheckbox.setText("Palms");
        palmCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showPalms){
                	camfeed.showPalms = false;
                }
                else{
                	camfeed.showPalms = true;
                }
            }
        });
        palmCheckbox.setSelected(true);

        

        fingerCheckbox.setText("Fingertips");
        fingerCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showFingers){
                	camfeed.showFingers = false;
                }
                else{
                	camfeed.showFingers = true;
                }
            }
        });
        fingerCheckbox.setSelected(true);

        lastCommandLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lastCommandLabel.setText("Last Command");

        lastcommandOutput.setHorizontalAlignment(JTextField.CENTER);
        lastcommandOutput.setText("Takeoff");


        GroupLayout camFeedPanelLayout = new GroupLayout(camFeedPanel);
        camFeedPanel.setLayout(camFeedPanelLayout);
        camFeedPanelLayout.setHorizontalGroup(
            camFeedPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(viewLabel, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        camFeedPanelLayout.setVerticalGroup(
            camFeedPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(viewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        showDepth.setText("Depth Map");
        showDepth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showDepth){
                	camfeed.showDepth = false;
                }
                else{
                	camfeed.showDepth = true;
                }
            }
        });
        showDepth.setSelected(true);

        lastCommandLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        lastCommandLabel1.setText("Hands Detected");

        leftHand.setBackground(new java.awt.Color(255, 255, 255));
        leftHand.setBorder(BorderFactory.createEtchedBorder());

        rightHand.setBackground(new java.awt.Color(255, 255, 255));
        rightHand.setBorder(BorderFactory.createEtchedBorder());

        speedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        speedLabel.setText("Max Speed (%)");

        speedSetter.setModel(new javax.swing.SpinnerNumberModel(30, 0, 100, 1));
        ChangeListener changeListener1 = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
            	camfeed.setSpeed((int)speedSetter.getValue());
            }
          };
          speedSetter.addChangeListener(changeListener1);
          ((DefaultEditor) speedSetter.getEditor()).getTextField().setEditable(false);

        colourSchemeButton.setText("Colour Scheme");
        colourSchemeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colPic.setVisible(true);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(camFeedPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(showNodes)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(fingerCheckbox)
                                    .addComponent(palmCheckbox))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(commandsLabel, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(showRGB)
                                    .addComponent(showDepth)
                                    .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator3, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lastCommandLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lastCommandLabel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                            .addComponent(speedLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator4, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastcommandOutput, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(leftHand, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rightHand, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
                            .addComponent(colourSchemeButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(speedSetter, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(commandsLabel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showRGB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showDepth)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showNodes)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(palmCheckbox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fingerCheckbox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lastCommandLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(leftHand)
                    .addComponent(rightHand))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(lastCommandLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastcommandOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(speedLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(speedSetter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(colourSchemeButton)
                .addGap(40, 40, 40))
            .addComponent(camFeedPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }                 
                                     
                                    

         
    public void setAction(String txt){
    	lastcommandOutput.setText(txt);
    }

           
	    
    private JPanel 				camFeedPanel;
    private JLabel 				commandsLabel,lastCommandLabel,lastCommandLabel1,speedLabel;
    private JCheckBox 			fingerCheckbox,palmCheckbox;
    private JButton 			forceLandButton, colourSchemeButton;
    private JSeparator 			jSeparator1,jSeparator2,jSeparator3,jSeparator4;
    private JTextField 			lastcommandOutput;
    private JRadioButton 		showDepth,showNodes,showRGB;
    public  JLabel				leftHand,rightHand,viewLabel;
    private JSpinner 			speedSetter;

}
