package ca.bubbleman.motionsensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class GyroSensorEventListener implements SensorEventListener {

	float[] outprevious = new float[3];
	float[] inprevious = new float[3];
	float[] array = new float[3];
	TextView show, output;
	LineGraphView graph;
	double startTime = System.currentTimeMillis(), endTime;
	double theta = 0, deltaTime = 0;
	
	static boolean rooted = false;
	float max = 0;
	float min = 0;
	
	boolean positive = true;

	public GyroSensorEventListener (TextView show1, TextView values,LineGraphView graph1){
		show = show1;
		graph = graph1;
		output = values;
	}

	@Override
	public void onAccuracyChanged(Sensor s, int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent se) {

		if (se.sensor.getType() == Sensor.TYPE_GYROSCOPE){

			array = this.lowpass(se.values.clone(), outprevious);

			graph.addPoint(array);

			output.setText(String.format("X:%f\nY:%f\nZ:%f", array[0], array[1], array[2]));

			endTime = System.currentTimeMillis();
			
			if (xzCalm(array[0], array[2]) || Math.abs(array[1]) > 3)  {				
				// update max and min
				if (array[1] > max) {
					max = array[1];
				} else if (array[1] < min){
					min = array[1];
				}
				
				//show.setText(min + "      " + max);
				
				// if max is really large and the last min was relatively smaller or v-v, then detect
				if (max > 5 && min > max*-0.8)  {
					show.setText(String.format("right"));
					max = 0;
					min = 0;
				} else if(min < -5 && max < min*-0.8) {
					show.setText(String.format("left"));
					max = 0;
					min = 0;
				}
	//				if (validDeltaTime(startTime, endTime) && Math.abs(array[1]) > 0.2 && xzCalm(array[0], array[2])){
	//					if (signDeltaValue(array[1], outprevious[1])){								
	//						if (validAngularSpeed(Math.abs(array[1]))){
	//							show.setText(String.format("Right"));
	//							startTime = System.currentTimeMillis();
	//						}
	//						else {
	//							show.setText(String.format(""));
	//							startTime = System.currentTimeMillis();
	//						}
	//					}
	//					else {						
	//						if (validAngularSpeed(Math.abs(array[1]))){
	//							show.setText(String.format("Left"));
	//							startTime = System.currentTimeMillis();
	//						}
	//						else {
	//							show.setText(String.format(""));
	//							startTime = System.currentTimeMillis();
	//						}
	//					}
	//				}	

			}

			outprevious = array;
		}
	}

	public boolean xzCalm(float x, float z) {
		return (Math.abs(x) < 0.07 && Math.abs(z) <= Math.abs(x));
	}
	
	public boolean signDeltaValue (float a, float b){
		if (a - b > 0){ 
			return true; //going clockwise
		}
		else {
			return false; //going counterclockwise
		}
	}

	public boolean validDeltaTime(double start, double end){
		double valid = 500; //time constant to check validity of turn
		if (end - start >= valid){
			return true;
		}
		else {
			return false;
		}
	}

	public boolean validAngularSpeed(double angular){
		double valid = 0.5; //angular speed constant to check validity of turn		
		if (angular > valid){
			return true;
		}
		else {
			return false;
		}
	}

	public boolean validMovement (){
		return true;
	}

	public float[] lowpass (float[] in, float[] outprevious){
		float[] out = new float[in.length];
		float a = 0.15f;
			for (int i = 0; i < in.length; i++){
				out[i] = a*in[i] + (1-a)*outprevious[i];
			}
		return out;
	}
}