package demo;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
public class DistanceAndLabel implements Writable{
	private double distance;
	private String label;
	public DistanceAndLabel() {
	}
	public DistanceAndLabel(double distance,String label) {
		this.distance=distance;
		this.label=label;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 *�ȶ�ȡ���룬�ٶ�ȡ���
	 */
	public void readFields(DataInput in) throws IOException {
		this.distance=in.readDouble();
		this.label=in.readUTF();
		
	}
	/**
	 * �Ȱ�distnceд��out�����
	 * �ٰ�labelд��out�����
	 */
	public void write(DataOutput out) throws IOException {
		out.writeDouble(distance);
		out.writeUTF(label);
		
	}
	/**
	 * ʹ�ÿո񽫾����������ӳ��ַ���
	 */
	@Override
	public String toString() {
		return this.distance+","+this.label;
	}
}
