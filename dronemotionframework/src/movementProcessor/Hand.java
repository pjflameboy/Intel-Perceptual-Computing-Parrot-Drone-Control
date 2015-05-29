package movementProcessor;

import movementProcessor.Coordinate;

//import positionData.Coordinate;
public class Hand{
	private Coordinate coordinate;
	private Coordinate unfiltered;	
	private boolean active;
	
	public Hand(Coordinate coordinate, Coordinate unfiltered, boolean active) {
	    this.coordinate = coordinate;
	    this.unfiltered = unfiltered;
	    this.active = active;
	}
	
	public Coordinate getUnfilteredCoordinate() {
	    return unfiltered;
	}
	
	public Coordinate getCoordinate() {
	    return coordinate;
	}

	public boolean isActive() {
	    return active;
	}

}