package demo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Test {
	public static Set<Integer> trainIndex(int count){
		Set<Integer> train_index=new HashSet<Integer>();
		int trainSplitNum=(int)(count*0.8);
		Random random=new Random();
		while(train_index.size()<trainSplitNum){
			train_index.add(random.nextInt(count));
		}
		return train_index;	
	}
	public static void main(String[] args) {
		int count=10;
		Set<Integer> train_index=trainIndex(count);
		System.out.println(train_index.size());
	}

}
