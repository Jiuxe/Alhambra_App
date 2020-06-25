package e.daniel.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static maes.tech.intentanim.CustomIntent.customType;

public class seleccionableAlcazaba extends Activity implements ZXingScannerView.ResultHandler {

    private String animacion = "fadein-to-fadeout";

    // MULTITOUCH
    private View dedos;
    private float lastPosition;
    private boolean hayDosDedos = false;

    // ************* ACELOREMETRO **************************//
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    boolean siguiente = false;

    // ************* CAMARA QR **************************//
    private String nombreColeccionable = "Alcazabar" ;
    private ZXingScannerView escanerView;
    private ImageView imagenBlo, imagenDes;

    // ***************************************************  //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seleccionable_alcazaba);

        // ****************** QR *************************//

        MenuAlcazaba.etiquetaAlcazaba.setVisibility(View.INVISIBLE);
        imagenBlo = (ImageView) findViewById(R.id.bloqueadoAlcazaba);
        imagenDes = (ImageView) findViewById(R.id.desbloqueadoAlcazaba);
        if(!MenuAlcazaba.alcazabaBloqueada) {
            imagenBlo.setVisibility(View.INVISIBLE);
            imagenDes.setVisibility(View.VISIBLE);
        }

        // ***************** ACELOREMETRO **************//

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float z = sensorEvent.values[2];

                // Se compueba que el valor es mayor que un valor arbitrario para reducir sensibilidad,
                // Si es asi se inicia una actividad de la siguiente tarjeta

                if (z <= -14 && !siguiente) {

                    startActivity(new Intent(getBaseContext(), seleccionableTorreVela.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    siguiente = true;
                    finish();
                    customType(seleccionableAlcazaba.this,animacion);
                } else if (z > 14 && !siguiente) {
                    startActivity(new Intent(getBaseContext(), seleccionableTorreVela.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    siguiente = true;
                    finish();
                    customType(seleccionableAlcazaba.this,animacion);
                }else if(z > -14 && z < 14 && siguiente){
                    siguiente = false;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        startAcelerometro();

        //  ***************************************************  //

        if(MenuAlcazaba.alcazabaBloqueada) {
            multiTouchQR();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAcelerometro();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAcelerometro();
    }


    /**************** ACELEROMETRO ***************/

    //Iniciamos el sensor del acelerometro
    private void startAcelerometro(){
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Paramos el sensor sel acelerometro
    private void stopAcelerometro(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    /*************** ESCANER QR ******************/

    //Iniciamos el sensor de QR
    public void EscanerQR(){
        escanerView = new ZXingScannerView(this);
        setContentView(escanerView);
        escanerView.setResultHandler(this);
        escanerView.startCamera();
    }

    //Realizamos el analisis del resultado del QR para saber si este es correcto o no
    //dependiendo en que coleccionable nos encontremos
    @Override
    public void handleResult(Result result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Extraemos el string proporcionado por el QR
        String nombre = result.getText();

        //Comparamos el string con el codigo de validacion
        if(nombre.equals(nombreColeccionable)){
            //Si es correcto desbloqueamos el coleccionable
            MenuAlcazaba.alcazabaBloqueada = false;
        }else{
            //Si es incorrecto mandamos un aviso de error
            builder.setTitle("Error");
            builder.setMessage("Coleccionable Incorrecto");
        }

        //Mostramos la alerta en caso de error
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        escanerView.resumeCameraPreview(this);
        //Establecemos el nuevo contexto del activity
        setContentView(R.layout.activity_seleccionable_alcazaba);

        //Si ha sido desbloqueado cambiamos la imagen presente en el coleccionable por
        //la imagen desbloqueada
        if(!MenuAlcazaba.alcazabaBloqueada) {
            imagenBlo = (ImageView) findViewById(R.id.bloqueadoAlcazaba);
            imagenDes = (ImageView) findViewById(R.id.desbloqueadoAlcazaba);
            imagenBlo.setVisibility(View.INVISIBLE);
            imagenDes.setVisibility(View.VISIBLE);

            finish();
            startActivity(getIntent());
            customType(seleccionableAlcazaba.this,animacion);
        }

    }

    /************** MULTITOUCH ***********/

    public void multiTouchQR(){
        dedos = (View) findViewById(R.id.viewAlcazaba);

        dedos.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int action = event.getAction();

                switch(action & MotionEvent.ACTION_MASK){
                    // Leemos mas de un dedo sobre la pantalla
                    case MotionEvent.ACTION_POINTER_DOWN: {
                        // Si tenemos justo dos dedos ponemos la variable indicativa a true
                        if(event.getPointerCount() > 1 && event.getPointerCount() < 3){
                            hayDosDedos = true;
                            lastPosition = event.getY();
                        }
                        // Si ponemos mas de dos dedos volvemos a ponerla a false
                        if( event.getPointerCount() > 2){
                            hayDosDedos = false;
                        }
                        break;
                    }
                    // Leemos que se levanta un dedo
                    case MotionEvent.ACTION_UP: {
                        // Si habia dos dedos en la pantalla
                        if(hayDosDedos){
                            int diffY = (int) (event.getY() - lastPosition);
                            hayDosDedos = false;
                            // Y si la posicion con la de antes era mayor a un valor arbitrario
                            if(diffY < -400){
                                // LLamamos al sensor de escanerQR
                                EscanerQR();
                            }

                            break;
                        }

                    }
                }
                return true;
            }
        });
    }

}