package demo;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class DataProcessingMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
	private String splitter="";
	enum DataProcessingCounter{
		NullData,
		AbnormalData
	}
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		splitter=context.getConfiguration().get("SPLITTER");
	}
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		String[] val = value.toString().split(splitter);
		for(int i=5;i<val.length;i++){
			//�ж�ÿ���ֶε�ֵ�Ƿ��ǿ�ֵ����������0�滻
			if(val[i].equals("") || val[i].equals("null") || val[i].equals("NULL") || val[i].equals("NAN")){	
				context.getCounter(DataProcessingCounter.NullData).increment(1);
				val[i]="0";
			}else{
				context.getCounter(DataProcessingCounter.NullData).increment(0);
			}
			//�ж�ÿ���ֶε�ֵ�Ƿ����쳣ֵ����������0�滻
			if(Integer.parseInt(val[i])<0){
				context.getCounter(DataProcessingCounter.AbnormalData).increment(1);
				val[i]="0";
			}else{
				context.getCounter(DataProcessingCounter.AbnormalData).increment(0);
			}
		}
		
		String result="";
		//���½��ַ�������valƴ�ӳ��ַ���
		for(int i=0;i<val.length;i++){
			if(i==0){
				result=val[i];
			}else{
				result=result+splitter+val[i];
			}
		}
		context.write(new Text(result), NullWritable.get());
	}
}
