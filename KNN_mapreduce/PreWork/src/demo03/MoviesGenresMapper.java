package demo03;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MoviesGenresMapper extends Mapper<LongWritable, Text, UserAndGender, Text> {
	private UserAndGender user_gender=new UserAndGender();
	private String splitter="";
	private Text genres=new Text();
	@Override
	protected void setup(Mapper<LongWritable, Text, UserAndGender, Text>.Context context)
			throws IOException, InterruptedException {
		splitter=context.getConfiguration().get("SPLITTER");
	}
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, UserAndGender, Text>.Context context)
			throws IOException, InterruptedException {
		String[] val=value.toString().split(splitter);
		user_gender.setUserID(val[0]);
		if(val[1].equals("M")){
			//�Ա�ΪM����0���
			user_gender.setGender(0);
		}else{
			//�Ա�ΪF����1���
			user_gender.setGender(1);
		}
		user_gender.setAge(Integer.parseInt(val[2]));
		user_gender.setOccupation(val[3]);
		user_gender.setZip_code(val[4]);
		genres.set(val[6]);
		context.write(user_gender, genres);
	}
}
