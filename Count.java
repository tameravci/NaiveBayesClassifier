import java.util.HashMap;

public class Count {

	private HashMap<String, Integer> labelFrequency;
	
	public Count(String label) {
		labelFrequency = new HashMap<String, Integer>();
		labelFrequency.put(label, 1);
	}
	
	public void increment(String label) {
		if(labelFrequency.containsKey(label))
			labelFrequency.put(label, labelFrequency.get(label)+1);
		else
			labelFrequency.put(label, 1);
	}
	
	public int getFrequency(String label) {
		if(labelFrequency.containsKey(label))
			return labelFrequency.get(label);
		else
			return 1;
	}
	
}
