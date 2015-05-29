package movementProcessor;


public class changeCalculation {
	
	private Coordinate leftStartPoint;
	private Coordinate rightStartPoint;
	private float maxRoll = 0.28f;
	private float minRoll = 0.1f;
	private float roll = 0f;
	private float yaw = 0f;
	private float pitch=0f;
	private float aspeed = 0f;
    private float securityScaleBuffer = 0.8f;
    private float maxPitch = 0.2f * securityScaleBuffer;
    private static final float MIN_PITCH = 0.2f;

	
	
    public void setLeftHandStart(Coordinate leftStart) {
        leftStartPoint = leftStart;
    }

    public void setRightHandStart(Coordinate rightStart) {
        rightStartPoint = rightStart;
    }

    
    public void calculateMoves(Hand leftHand, Hand rightHand) {
        calculateYaw(leftHand, rightHand);
        calculateRoll(leftHand, rightHand);
        calculatePitch(leftHand, rightHand);
        calculateAltitudeSpeed(leftHand, rightHand);
    }
	
    private void calculateRoll(Hand leftHand, Hand rightHand) {
        float roll = leftHand.getCoordinate().getY() - rightHand.getCoordinate().getY();
        roll = roll/150;
        
        if (Math.abs(roll) <= minRoll) {
            roll = 0;
        }
        if (Math.abs(roll) >= maxRoll) {
        	roll = maxRoll*Math.signum(roll);
        }       

        //note roll here is set between 0.1 and 0.3, this is now scaled to between 0.1 and 1.0
        roll = (roll - (Math.signum(roll)*0.1f))*5 + (Math.signum(roll)*0.1f);
        this.roll = -roll;
    }
    private void calculateYaw(Hand leftHand, Hand rightHand) {
        float yaw = (leftHand.getCoordinate().getZ()-leftStartPoint.getZ()) + (rightStartPoint.getZ()-rightHand.getCoordinate().getZ());
//        
//        if(Math.abs(yaw)>0.05){
////        	if(Math.abs(yaw)>0.145){
////        		yaw = 1f*Math.signum(yaw);
////        	}
////        	else{
//        		//yaw= ((float)Math.round(((yaw-(Math.signum(yaw)*0.05))*10f)*1000))/1000 + 0.1f;
//        	
////        	}
//        }
//        else{
//        	yaw = 0f;
//        }
//        //yaw = ((yaw -(Math.signum(yaw)*0.1f))*2)+(Math.signum(yaw)*0.1f);
        
        if(Math.abs(yaw)>0.05){
        	float tmp = yaw -(Math.signum(yaw)*0.05f);
        	yaw = Math.signum(yaw)*(1-(1-(tmp*tmp)));
        	yaw = Math.signum(yaw)*0.2f + 20*yaw;
        	yaw = (float)Math.round(yaw*1000f)/1000;
        }
        else{
        	yaw = 0f;
        }
        if(Math.abs(yaw)>1f){
        	yaw = Math.signum(yaw)*1f;
        }
        
        
        this.yaw = -yaw;
    }
    
    private void calculatePitch(Hand leftHand, Hand rightHand) {
        float pitch = ((leftHand.getCoordinate().getZ() - leftStartPoint.getZ()) +
                (rightHand.getCoordinate().getZ() - rightStartPoint.getZ())) / 2;
//        if (Math.abs(pitch) >= Math.abs(maxPitch)) {
//            maxPitch = Math.abs(pitch);
//        }
//
//        //Scale pitch using maxPitch
//        pitch = pitch / maxPitch;
//
//
//        if (Math.abs(pitch) <= 0.1) {
//            pitch = 0;
//        }
//
//        pitch = pitch - Math.signum(pitch) * MIN_PITCH;
//        if(Math.abs(pitch)>0.3){
//        	pitch = Math.signum(pitch)*0.3f;
//        }
//        if(Math.abs(pitch)<0.1){
//        	pitch = 0f;
//        }       
//        if(Math.abs(leftHand.getCoordinate().getZ() - leftStartPoint.getZ())<0.01||Math.abs(rightHand.getCoordinate().getZ() - rightStartPoint.getZ())<0.01||Math.signum(leftHand.getCoordinate().getZ() - leftStartPoint.getZ())!=Math.signum(rightHand.getCoordinate().getZ() - rightStartPoint.getZ())){
//        	pitch = 0f;
//        }
        
        if(Math.abs(pitch)>0.035){
        	float tmp = Math.abs(pitch-(Math.signum(pitch)*0.035f));
        	pitch = Math.signum(pitch) * 6 * (1-(1-(10*(tmp*tmp))));
        	if(pitch<0f){
        		pitch = pitch*2;
        	}
        	pitch = pitch + (Math.signum(pitch)*0.1f);

        }
        else{
        	pitch = 0f;
        }
        if(Math.abs(pitch)>1f){
        	pitch = Math.signum(pitch)*1f;
        }
        
        
        
        
        this.pitch = pitch;
    }
    
    private void calculateAltitudeSpeed(Hand leftHand, Hand rightHand) {
    	float altitude = ((leftHand.getCoordinate().getY() - leftStartPoint.getY()) +
                (rightHand.getCoordinate().getY() - rightStartPoint.getY()));
	
    	if((leftHand.getCoordinate().getY() - leftStartPoint.getY()>5)&&(rightHand.getCoordinate().getY() - rightStartPoint.getY()>5)){
    		altitude = altitude/2;	
    	}
    	else if((leftHand.getCoordinate().getY() - leftStartPoint.getY()<-10)&&(rightHand.getCoordinate().getY() - rightStartPoint.getY()<-10)){
    		altitude = altitude/2;
    	}
    	else{
    		altitude = -0f;
    	}
    	altitude = altitude/100;
    	if(Math.abs(altitude)>0.4)
    		altitude = Math.signum(altitude) * 0.4f;
    	altitude = (altitude-Math.signum(altitude)*0.1f)*3 +Math.signum(altitude)*0.1f;
    	aspeed = -altitude;

    }
    
    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public float getPitch() {
        return pitch;
    }

    public float getASpeed() {
        return aspeed;
    }

	
	
	
	
    public boolean hasHandStarts() {
        return (rightStartPoint != null) && (leftStartPoint != null);
    }	
	
	
	
}
