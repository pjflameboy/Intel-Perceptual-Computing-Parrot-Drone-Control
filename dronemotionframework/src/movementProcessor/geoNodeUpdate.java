package movementProcessor;

import java.io.UnsupportedEncodingException;

import JsonPublisher.JsonSensor;
import intel.pcsdk.PXCMGesture;
import movementProcessor.coordinateFilter;
import movementProcessor.changeCalculation;
import movementProcessor.Hand;

public class geoNodeUpdate {
	
    private coordinateFilter leftFilter;
    private coordinateFilter rightFilter;
    public changeCalculation change;
    public boolean ready = false;
    public boolean flying = false;
    private JsonSensor js;

	public geoNodeUpdate(JsonSensor js){
		leftFilter = new coordinateFilter();
		rightFilter = new coordinateFilter();
		change = new changeCalculation();
		this.js = js;
	}
	
	public void updateFeatures(PXCMGesture.GeoNode leftGeoNode, PXCMGesture.GeoNode rightGeoNode,boolean thumbsup,boolean thumbsdown){
		Hand rightHand = new Hand(getSmoothedCoordinate(rightFilter, rightGeoNode), getCoordinate(rightGeoNode), isActive(rightGeoNode));
        Hand leftHand = new Hand(getSmoothedCoordinate(leftFilter, leftGeoNode), getCoordinate(leftGeoNode), isActive(leftGeoNode));       
        
        if (!rightHand.isActive() || !leftHand.isActive()) {
        	if(flying==true){
        		try {
					js.sendCommand("land");
				} catch (UnsupportedEncodingException e) {
					System.out.println("COMMAND NOT SENT... EXITING");
					
					e.printStackTrace();
					System.exit(5);
				}
        	}
        	ready = false;
        	flying = false;
        }
        
        if (rightHand.isActive() && leftHand.isActive()) {
            if (rightHand.getCoordinate().getX() > leftHand.getCoordinate().getX()) {
                Hand temporaryRightHand = rightHand;
                rightHand = leftHand;
                leftHand = temporaryRightHand;
            }

            if(thumbsup==true)
            	ready = true;
            if (ready && !flying) {
            	System.out.println("Setting start coordinates");
                change.setRightHandStart(rightHand.getCoordinate());
                change.setLeftHandStart(leftHand.getCoordinate());
                flying = true;
        		try {
					js.sendCommand("takeoff");
				} catch (UnsupportedEncodingException e) {
					System.out.println("COMMAND NOT SENT... EXITING");
					
					e.printStackTrace();
					System.exit(5);
				}
            }

            if (ready && flying) {
                if (!change.hasHandStarts()) {
                    change.setRightHandStart(rightHand.getCoordinate());
                    change.setLeftHandStart(leftHand.getCoordinate());
                    System.out.println("LOST HAND");
                    System.exit(10);
                }
                else if(thumbsdown ==true){
                	flying =false;
                	ready = false;
            		try {
    					js.sendCommand("land");
    				} catch (UnsupportedEncodingException e) {
    					System.out.println("COMMAND NOT SENT... EXITING");
    					
    					e.printStackTrace();
    					System.exit(5);
    				}
                }
                else{
                	change.calculateMoves(leftHand, rightHand);
                }

                
            }
            
           
            
            
            
            
            
            
            
        }
		
	}
	
	
	
    private Coordinate getCoordinate(PXCMGesture.GeoNode handGeoNode) {
        if (handGeoNode.positionWorld != null) {
            return new Coordinate(handGeoNode.positionWorld.x, handGeoNode.positionImage.y, handGeoNode.positionWorld.y);
        } else {
            return null;
        }
    }
    
    private boolean isActive(PXCMGesture.GeoNode handGeoNode) {
        return handGeoNode.positionWorld != null;
    }

    private Coordinate getSmoothedCoordinate(coordinateFilter filter, PXCMGesture.GeoNode handGeoNode) {
        return filter.getFilteredCoord(getCoordinate(handGeoNode));
    }
	
	
	
}