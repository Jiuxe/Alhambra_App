package e.daniel.myapplication;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.hardware.SensorManager;
import android.widget.HorizontalScrollView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;



public class VisionCamara extends Activity {

    // Inicializamos el sensor
    private SensorManager sensorManager;
    // Inicializamos la posicion de las coordenadas
    private int yBegin = 0;
    private HorizontalScrollView fondo;

    // Variables para mover la imagen con sensor

    private HorizontalScrollView view;
    private int xAnterior = 0;
    private int CentroGlobal = 70;
    private int proporcion;

    SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_camara);
        // Cargamos los objetos de la vista
        fondo = findViewById(R.id.h_scrollPanoramica);

        view = findViewById(R.id.h_scrollPanoramica);

        // proporcion = (int) view.getScaleX()/180;
        sensorEventListener = new SensorEventListener(){
            // Cuando el sensor detecta un cambio
            // Nos movemos a la posicion de la imagen
            // asignada a ese valor del sensor
            public void onSensorChanged(SensorEvent event) {
                float[] values = event.values;
                int x = (int) values[0];
                if(CentroGlobal > 0) {
                    view.scrollTo(x*10, yBegin);
                }else{
                    view.scrollTo(x*10, yBegin);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

}