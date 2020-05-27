package demo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
public class MovieClassify extends Configured implements Tool{
	public int run(String[] args) throws Exception {
		if(args.length!=5){
			System.err.println("demo.MovieClassify <testinput> <traininput> <output> <k> <splitter>");
			System.exit(-1);
		}
		Configuration conf=getMyConfiguration();
		conf.setInt("K", Integer.parseInt(args[3]));
		conf.set("SPLITTER",args[4]);
		conf.set("TESTPATH", args[0]);
		Job job=Job.getInstance(conf, "movie_knn");
		job.setJarByClass(MovieClassify.class);//��������
		job.setMapperClass(MovieClassifyMapper.class);//����Mapper��
		job.setReducerClass(MovieClassifyReducer.class);//����Reducer��
		job.setMapOutputKeyClass(Text.class);//����Mapper����ļ�����
		job.setMapOutputValueClass(DistanceAndLabel.class);//����Mapper�����ֵ����
		job.setOutputKeyClass(Text.class);//����Reducer����ļ�����
		job.setOutputValueClass(NullWritable.class);//����Reducer�����ֵ����
		FileInputFormat.addInputPath(job, new Path(args[1]));//��������·��
		FileSystem.get(conf).delete(new Path(args[2]), true);//ɾ�����·��
		FileOutputFormat.setOutputPath(job, new Path(args[2]));//�������·��
		return job.waitForCompletion(true)?-1:1;//�ύ����
	}
	public static void main(String[] args) {
		String[] myArgs={
				"/movie/testData",
				"/movie/trainData",
				"/movie/knnout",
				"3",
				","
		};
		try {
			ToolRunner.run(getMyConfiguration(), new MovieClassify(), myArgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ��������Hadoop��Ⱥ������
	 * @return
	 */
	public static Configuration getMyConfiguration(){
		Configuration conf = new Configuration();
		conf.setBoolean("mapreduce.app-submission.cross-platform",true);
		conf.set("fs.defaultFS", "hdfs://localhost:9000");// ָ��namenode
		conf.set("mapreduce.framework.name","yarn"); // ָ��ʹ��yarn���
		String resourcenode="localhost";
		conf.set("yarn.resourcemanager.address", resourcenode+":8032"); // ָ��resourcemanager
		conf.set("yarn.resourcemanager.scheduler.address",resourcenode+":8030");// ָ����Դ������
		conf.set("mapreduce.jobhistory.address",resourcenode+":10020");
		conf.set("mapreduce.job.jar",JarUtil.jar(MovieClassify.class));
		return conf;	
	}
}
