import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * 
 * @author Tamer
 * AvciBayes v. 0.2
 * Supports multiple-labels, not just binary classification
 * Assuming tab as a delimiter and class label located in the first column
 */

public class AvciBayes {
	
	private String trainingFile;
	private String testFile;
	
	private ArrayList<String[]> records;
	private ArrayList<String[]> testRecords;
	private HashMap<String, Integer> classLabels; //key=classLabel value=frequency
	private HashSet<String> labels;
	
	private ArrayList<Attribute> featureList;
	
	private int correct;
	private int incorrect;
	private String delimiter;
	
	
	public static void main(String[] args) throws IOException {
		long time1 = System.currentTimeMillis();	
		
		AvciBayes naiveBayes = new AvciBayes();
		
		System.out.println("How is your attributes(columns, features) separated(e.g. tab, comma, space)? Enter the symbol");
//		Scanner scan = new Scanner(System.in);
//		naiveBayes.delimiter = scan.next();
//		
		naiveBayes.trainingFile = args[0]; naiveBayes.testFile = args[1]; 
		
		naiveBayes.loadData();
		naiveBayes.createAllAttributes();
		
		naiveBayes.testLoad();
		
		naiveBayes.classify();
			
		
		System.out.println("\nStatistics: \nAccuracy is: "+ (double)naiveBayes.correct/(double)(naiveBayes.correct+naiveBayes.incorrect));
		
		System.out.println("\nTotal time (including) file i/o: " + ((double) (System.currentTimeMillis() - time1)/1000) + " seconds");
		
	}

	private void createAllAttributes() throws IOException {

		featureList = new ArrayList<Attribute>();
		for(int i = 0; i<records.get(0).length-1; i++)
			featureList.add(new Attribute());
		
		for(int i = 0; i<records.size(); i++) {
			for(int j=0; j<records.get(0).length-1; j++)
				featureList.get(j).addAttribute(records.get(i)[j+1], records.get(i)[0]);
		}
		
	}
	
	
	private void loadData() throws IOException {
		
		records = new ArrayList<String[]>();
		classLabels = new HashMap<String, Integer>();
		labels = new HashSet<String>();
		Scanner scan = new Scanner(new File(trainingFile));
		
		while(scan.hasNextLine()) {
			String headerLine = scan.nextLine();
			String record[]  = headerLine.split("	");
			records.add(record);
			
			if(classLabels.containsKey(record[0]))
				classLabels.put(record[0], classLabels.get(record[0])+1);
			else
				classLabels.put(record[0], 1);
			
			labels.add(record[0]);
			
		}
		scan.close();
	}
	private void testLoad() throws IOException {
		
		testRecords = new ArrayList<String[]>();
		Scanner scan = new Scanner(new File(testFile));
		
		while(scan.hasNextLine()) {
			String headerLine = scan.nextLine();
			String record[]  = headerLine.split("	");
			testRecords.add(record);
		}
		scan.close();
	
	}

	private void classify() {
		
		String[] labelArray = new String[labels.size()]; 
		labels.toArray(labelArray);
		
		for(String[] record : testRecords) {
			
			
			Double[] prob = new Double[classLabels.size()];
			
			for(int i = 0; i<classLabels.size(); i++){
				prob[i] = 1.0;
			}
			
			
			
			for(int i = 0; i<record.length-1; i++) {
				
				for(int j = 0; j<labels.size(); j++) {
					String label = labelArray[j];
					prob[j] *= (double) featureList.get(i).getFrequency(record[i+1], label)/(double)classLabels.get(label);
				}
				
			}
			
			HashMap<Double, String> labelMap = new HashMap<Double, String>();
			
			for(int j = 0; j<labels.size(); j++) {
				String label = labelArray[j];
				prob[j] *= (double) classLabels.get(label) / (double) records.size();
				labelMap.put(prob[j], label);
			}
			
			Arrays.sort(prob);
						
			if(record[0].equals(labelMap.get(prob[prob.length-1]))) {
				correct++;
				System.out.println("Correct classification: " + Arrays.toString(record));
			}
			else {
				incorrect++;
				System.out.println("**Incorrect classification**: "+labelMap.get(prob[prob.length-1])+ " " +Arrays.toString(record));
			}
			
		}
		
	}
	
	
}
