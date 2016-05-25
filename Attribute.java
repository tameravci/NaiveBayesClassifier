
import java.util.HashMap;
import java.util.HashSet;

public class Attribute {
	
	private HashMap<String, Count> featureList;
	
	public Attribute() {
		featureList = new HashMap<String, Count>();
	}
	
	public void addAttribute(String attr, String label) {
		if(featureList.containsKey(attr))
			featureList.get(attr).increment(label);
		else
			featureList.put(attr, new Count(label));
	}
	
	public int getFrequency(String attr, String label) {
		if(featureList.containsKey(attr))
			return featureList.get(attr).getFrequency(label);
		else
			return 1;
	}
	
	
}