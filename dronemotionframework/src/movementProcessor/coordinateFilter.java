package movementProcessor;

import java.util.List;


import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class coordinateFilter {
	 private List<Coordinate> history;
	 public coordinateFilter(){
		 history = Lists.newLinkedList();
	 }
	
	 public Coordinate getFilteredCoord(Coordinate co){
		 if (co != null) {
			 addCoordinate(co);
		 }
	        return smoothedCoordinate();
	 }
	 
	 private void addCoordinate(Coordinate co) {
	     history.add(co);
	     while (history.size() > 25)
	         history.remove(0);
	 }
	 
	 private Coordinate smoothedCoordinate() {
        if (history.size() == 0)
            return null;

        float x,y,z;
        
        x = (float) getValue(Lists.transform(history,new listgetX()));
        y = (float) getValue(Lists.transform(history,new listgetY()));
        z = (float) getValue(Lists.transform(history,new listgetZ()));
        

        return new Coordinate(x, y, z);
    }
	    
	    
    private double getValue(List<Double> values) {
        double numerator = 0;
        double denominator = 0;
        int n = values.size();

        double currentValue = values.get(n - 1);
        for (int i = 0; i < n; i++) {
            double value = values.get(i);

            double exponent = (n - i) * (n - i) / (2.0 * 25) + Math.pow(currentValue - value, 2.0) / (2 * 0.03*0.03);
            double weight = Math.exp(-exponent);

            numerator += weight * value;
            denominator += weight;
        }
        return numerator / denominator;
    } 

	    
	 
	 
	
}

class listgetX implements Function<Coordinate, Double> {

    @Override
    public Double apply(Coordinate co) {
        return (double) co.getX();
    }
}
class listgetY implements Function<Coordinate, Double> {

    @Override
    public Double apply(Coordinate co) {
        return (double) co.getY();
    }
}

class listgetZ implements Function<Coordinate, Double> {

    @Override
    public Double apply(Coordinate co) {
        return (double) co.getZ();
    }
}

