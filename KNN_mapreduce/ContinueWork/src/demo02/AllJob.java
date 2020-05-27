package demo02;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
public class AllJob {
	public static void main(String[] args) throws IOException {
		Configuration conf=new Configuration();
		conf.set("fs.defaultFS", "hdfs://localhost:9000");
		FileSystem fs=FileSystem.get(conf);
		double maxAccuracy=0.0;
		int bestK=0;
		int[] k={2,3,5,9,15,30,55,70,80,100};
		for(int i=2;k[i]<=100;k[i]++){
			double accuracy=0.0;
			String[] classifyArgs={
					"/movie/validateData",
					"/movie/trainData",
					"/movie/knnout",
					String.valueOf(k[i]),
					","
			};
			try {
				ToolRunner.run(demo.MovieClassify.getMyConfiguration(), new demo.MovieClassify(), classifyArgs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] validateArgs={
					"/movie/knnout/part-r-00000",
					"/movie/validateout",
					","
			};
			try {
				ToolRunner.run(demo01.Validate.getMyConfiguration(),new demo01.Validate(),validateArgs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FSDataInputStream is=fs.open(new Path("/movie/validateout/part-r-00000"));
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			String line="";
			while((line=br.readLine())!=null){
				accuracy=Double.parseDouble(line);
			}
			br.close();
			is.close();
			if(accuracy>maxAccuracy){
				maxAccuracy=accuracy;
				bestK=k[i];
			}				
		}
		System.out.println("����Kֵ�ǣ�"+bestK+"\t"+"����Kֵ��Ӧ��׼ȷ�ʣ�"+maxAccuracy);
	}
}
