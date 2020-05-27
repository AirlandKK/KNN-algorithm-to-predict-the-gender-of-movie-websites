package demo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
public class SplitData {
	public static void main(String[] args) throws Exception {
		Configuration conf=new Configuration();
		conf.set("fs.defaultFS", "master:8020");
		FileSystem fs=FileSystem.get(conf);
		//��ȡԤ����֮��ĵ�Ӱ����·��
		Path moviedata=new Path("/movie/processing_out/part-m-00000");
		//�õ���Ӱ���ݴ�С
		int datasize=getSize(fs, moviedata);
		//�õ�train���ݶ�Ӧԭʼ�±�
		Set<Integer> train_index=trainIndex(datasize);
		
		System.out.println(train_index.size());
		//�õ�validate���ݶ�Ӧԭʼ���ݵ��±�
		Set<Integer> validate_index=validateIndex(datasize,train_index);
		System.out.println(validate_index.size());
		//ѵ�����ݴ�ŵ�·��
		Path train=new Path("hdfs://master:8020/movie/trainData");
		fs.delete(train,true);
		FSDataOutputStream os1=fs.create(train);
		BufferedWriter bw1=new BufferedWriter(new OutputStreamWriter(os1));
		//�������ݴ�ŵ�·��
		Path test=new Path("hdfs://master:8020/movie/testData");
		fs.delete(test,true);
		FSDataOutputStream os2=fs.create(test);
		BufferedWriter bw2=new BufferedWriter(new OutputStreamWriter(os2));
		//��֤���ݴ�ŵ�·��
		Path validate=new Path("hdfs://master:8020/movie/validateData");
		fs.delete(validate,true);
		FSDataOutputStream os3=fs.create(validate);
		BufferedWriter bw3=new BufferedWriter(new OutputStreamWriter(os3));
		//��ȡ���ݲ������ݷ�Ϊѵ�����ݡ����������Լ���֤����д�뵽HDFS
		FSDataInputStream is=fs.open(moviedata);
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String line="";
		int sum=0;
		int trainsize=0;
		int testsize=0;
		int validatesize=0;
		while((line=br.readLine())!=null){
			sum+=1;
			if(train_index.contains(sum)){
				trainsize+=1;
				bw1.write(line.toString());
				bw1.newLine();
			}else if(validate_index.contains(sum)){
				validatesize+=1;
				bw3.write(line.toString());
				bw3.newLine();
			}else{
				testsize+=1;
				bw2.write(line.toString());
				bw2.newLine();
			}
		}
		bw1.close();
		os1.close();
		bw2.close();
		os2.close();
		bw3.close();
		os3.close();
		br.close();
		is.close();
		fs.close();
	}
	/**
	 * ��ȡԭʼ���ݲ�ͳ�����ݵļ�¼��
	 * @param fs
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static int getSize(FileSystem fs,Path path) throws Exception{
		int count=0;
		FSDataInputStream is=fs.open(path);
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String line="";
		while((line=br.readLine())!=null){
			count++;
		}
		br.close();
		is.close();
		return count;		
	}
	/**
	 *�����ȡ 80%ԭʼ���ݵĶ�Ӧ�±�
	 * @param count
	 * @return
	 */
	public static Set<Integer> trainIndex(int count){
		Set<Integer> train_index=new HashSet<Integer>();
		int trainSplitNum=(int)(count*0.8);
		Random random=new Random();
		while(train_index.size()<trainSplitNum){
			int a=random.nextInt(count);
			train_index.add(a);
		}
		return train_index;	
	}
	/**
	 * �����ȡ10%ԭʼ���ݶ�Ӧ���±�
	 * @param count
	 * @param train_index
	 * @return
	 */
	public static Set<Integer> validateIndex(int count,Set<Integer> train_index){
		Set<Integer> validate_index=new HashSet<Integer>();
		int validateSplitNum=count-(int)(count*0.9);
		Random random=new Random();
		while(validate_index.size()<validateSplitNum){
			int a=random.nextInt(count);
			if(!train_index.contains(a)){
				validate_index.add(a);	
			}
		}
		return validate_index;	
	}
	
}
