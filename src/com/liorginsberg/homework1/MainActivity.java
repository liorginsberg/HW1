package com.liorginsberg.homework1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button btnScan;
	private IntentIntegrator integrator;
	private TextView etRes;
	private Button btnGen;
	private EditText etShare;
	private SensorManager sensorManager;
	private TextView tvLinearX;
	private TextView tvLinearY;
	private TextView tvLinearZ;
	private TextView tvAzimuth;
	private Sensor orientationSensor;
	private Sensor linearSensor;
	private Sensor lightSensor;
	private TextView tvLight;
	private Sensor pressureSensor;
	private TextView tvPressure;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		integrator = new IntentIntegrator(this);

		etRes = (TextView)findViewById(R.id.etRes);
		etShare = (EditText)findViewById(R.id.etShare);
		
		btnScan = (Button) findViewById(R.id.btnScan);
		btnScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				integrator.initiateScan();
			}
		});
		
		btnGen = (Button)findViewById(R.id.btnGen);
		btnGen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String shareString = etShare.getText().toString();
				if(shareString.isEmpty()) {
					shareString = "Empty Message!";
				}
				integrator.shareText(shareString);
				
			}
		});
		
		tvLinearX = (TextView) findViewById(R.id.tvLinearX);
		tvLinearY = (TextView) findViewById(R.id.tvLinearY);
		tvLinearZ = (TextView) findViewById(R.id.tvLinearZ);
		tvAzimuth = (TextView) findViewById(R.id.tvAzimuth);
		tvLight = (TextView) findViewById(R.id.tvLight);
		tvPressure = (TextView) findViewById(R.id.tvPressure);
		
		
		//get sensor manager
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	
		
		//Get default sensor. return null if none
		linearSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		
		sensorManager.unregisterListener(linearAccelerarionListener);
		sensorManager.unregisterListener(orientationListener);
		sensorManager.unregisterListener(lightListener);
		sensorManager.unregisterListener(pressureListener);
		
	}


	@Override
	protected void onResume() {
		super.onResume();
		if(linearSensor != null) {
			sensorManager.registerListener(linearAccelerarionListener, linearSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}else {
			tvLinearX.setText("Not available");
			tvLinearY.setVisibility(View.GONE);
			tvLinearZ.setVisibility(View.GONE);
		}
		if(orientationSensor != null) {
			sensorManager.registerListener(orientationListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
			tvAzimuth.setText("Not available");
		}
		if(lightSensor != null) {
			sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}else {
			tvLight.setText("Not available");
		}
		if(pressureSensor != null) {
			sensorManager.registerListener(pressureListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}else {
			tvPressure.setText("Not available");
		}
	}


	SensorEventListener linearAccelerarionListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			tvLinearX.setText("x: " + String.valueOf(values[0]));
			tvLinearY.setText("y: " + String.valueOf(values[1]));
			tvLinearZ.setText("z: " + String.valueOf(values[2]));
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			//SensorManager.SENSOR_STATUS_ACCURACY_LOW/MEDIUM/HIGH
		}
	};
	
	SensorEventListener orientationListener = new SensorEventListener() {	

		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			tvAzimuth.setText(String.valueOf(values[0]));
		
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			//SensorManager.SENSOR_STATUS_ACCURACY_LOW/MEDIUM/HIGH
		}
	};
	
	SensorEventListener lightListener = new SensorEventListener() {	

		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			tvLight.setText(String.valueOf(values[0]));
		
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			//SensorManager.SENSOR_STATUS_ACCURACY_LOW/MEDIUM/HIGH
		}
	};
	
	SensorEventListener pressureListener = new SensorEventListener() {	

		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			tvPressure.setText(String.valueOf(values[0]));
		
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			//SensorManager.SENSOR_STATUS_ACCURACY_LOW/MEDIUM/HIGH
		}
	};
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			if (scanResult != null) {
				String result = scanResult.getContents();
				etRes.setText(result);
			}
		}
}
