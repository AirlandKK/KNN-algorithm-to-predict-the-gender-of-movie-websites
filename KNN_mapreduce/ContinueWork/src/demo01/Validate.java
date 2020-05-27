package demo01;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import demo.JarUtil;
public class Validate extends Configured implements Tool{
	public int run(String[] args) throws Exception {
		if(args.length!=3){
			System.err.println("demo01.Validate <input> <output> <splitter>");
			System.exit(-1);
		}
		Configuration conf=getMyConfiguration();
		conf.set("SPLITTER", args[2]);
		Job job=Job.getInstance(conf, "validate");
		job.setJarByClass(Validate.class);//��������
		job.setMapperClass(ValidateMapper.class);//����Mapper��
		job.setReducerClass(ValidateReducer.class);//����Reducer��
		job.setMapOutputKeyClass(NullWritable.class);//����Mapper����ļ���ʽ
		job.setMapOutputValueClass(Text.class);//����Mapper�����ֵ��ʽ
		job.setOutputKeyClass(DoubleWritable.class);//����Reducer����ļ���ʽ
		job.setOutputValueClass(NullWritable.class);//����Reducer�����ֵ��ʽ
		FileInputFormat.addInputPath(job, new Path(args[0]));//��������·��
		FileSystem.get(conf).delete(new Path(args[1]),true);//����ɾ�����·��
		FileOutputFormat.setOutputPath(job, new Path(args[1]));//�������·��
		return job.waitForCompletion(true)?-1:1;
	}
	public static void main(String[] args) {
		String[] myArgs={
				"/movie/knnout/part-r-00000",
				"/movie/validateout",
				","
		};
		try {
			ToolRunner.run(getMyConfiguration(), new Validate(), myArgs);
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
		conf.set("mapreduce.job.jar",JarUtil.jar(Validate.class));
		return conf;	
	}
}
