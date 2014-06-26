package ca.bubbleman.motionsensor;

import java.util.Arrays;

import ca.bubbleman.motionsensor.R;
import ca.bubbleman.motionsensor.LineGraphView;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView show;
	TextView values;
	LineGraphView graph;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		show = new TextView(getApplicationContext());
		values = new TextView(getApplicationContext());

		LinearLayout l = (LinearLayout)findViewById(R.id.main_activity);
        l.setOrientation(LinearLayout.VERTICAL);
        
        l.addView(values);
        l.addView(show);
        
        graph = new LineGraphView(getApplicationContext(), 100, Arrays.asList("x", "y", "z"));
        graph.setVisibility(View.VISIBLE);
        l.addView(graph);

//		SensorManager sensorManager = (SensorManager) getSystemService (SENSOR_SERVICE);
//		Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//		SensorEventListener listen = new ProximitySensorEventListener();
//		sensorManager.registerListener(listen, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);

		SensorManager gyroManager = (SensorManager) getSystemService (SENSOR_SERVICE);
		Sensor gyroSensor = gyroManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		SensorEventListener gyroListen = new GyroSensorEventListener(show, values, graph);
		gyroManager.registerListener(gyroListen, gyroSensor, SensorManager.SENSOR_DELAY_GAME);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}