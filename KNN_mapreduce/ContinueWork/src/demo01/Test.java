package demo01;

import java.util.Arrays;

public class Test {
	public static void main(String[] args) {
		String[] a={"a","b","c","d"};
		String[] b=Arrays.copyOfRange(a,2,a.length);
		for(String str:b){
			System.out.println(str);
		}
	}

}
