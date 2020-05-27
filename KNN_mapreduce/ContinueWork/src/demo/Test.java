package demo;

public class Test {
	public static void main(String[] args) {
		String test="3,38,4,0,3,17,6,1,11,0,0,0,0,3,1,22,27,0";
		String[] testData=test.split(",");
		String train="0,2,6,18,0,14,0,0,5,5,2,3,20,3,14,3,21,0";
		String[] trainData=train.split(",");
		System.out.println(Distance(trainData,testData));
		
	}
	private static double Distance(String[] singleTrainData, String[] singleTestData) {
		double sum=0.0;
		for(int i=0;i<singleTrainData.length;i++){
			sum+=Math.pow(Double.parseDouble(singleTrainData[i]), Double.parseDouble(singleTestData[i]));
		}
		return Math.sqrt(sum);
	}
}
