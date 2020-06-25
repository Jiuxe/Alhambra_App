package e.daniel.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.MotionEvent;

import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static maes.tech.intentanim.CustomIntent.customType;

public class seleccionableTorreVela extends Activity implements ZXingScannerView.ResultHandler{
    // VARIABLE PARA ANIMACION
    private String animacion = "fadein-to-fadeout";

    // VARIABLES MULTITOUCH
    private View dedos;
    private float lastPosition;
    private boolean hayDosDedos = false;


    // ************* ACELOREMETRO **************************//
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    boolean siguiente = false;

    // ************* CAMARA QR **************************//
    private String nombreColeccionable = "Torre de la Vela" ;
    private ZXingScannerView escanerView;
    private ImageView imagenBlo, imagenDes;
    // ***************************************************  //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seleccionable_torre_vela);

        // ****************** QR *************************//

        MenuAlcazaba.etiquetaTorreVela.setVisibility(View.INVISIBLE);
        imagenBlo = (ImageView) findViewById(R.id.bloqueadoTorreVela);
        imagenDes = (ImageView) findViewById(R.id.desbloqueadoTorreVela);
        if(!MenuAlcazaba.torreVelaBloqueada) {
            imagenBlo.setVisibility(View.INVISIBLE);
            imagenDes.setVisibility(View.VISIBLE);
        }

        // ************* ACELOREMETRO **************************//

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float z = sensorEvent.values[2];

                if (z <= -14 && !siguiente) {
                    startActivity(new Intent(getBaseContext(), seleccionableAlcazaba.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    siguiente = true;
                    finish();
                    customType(seleccionableTorreVela.this,animacion);
                } else if (z > 14 && !siguiente) {
                    startActivity(new Intent(getBaseContext(), seleccionableAlcazaba.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    siguiente = true;
                    finish();
                    customType(seleccionableTorreVela.this,animacion);
                }else if(z > -14 && z < 14 && siguiente){
                    siguiente = false;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        startAcelerometro();

        // ***************************************************  //

        if(!MenuAlcazaba.torreVelaBloqueada) {
            multiTouchParonamica();
        }else{
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

    /*************** ACELEROMETRO ******************/
    //Iniciamos el sensor del acelerometro
    private void startAcelerometro(){
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Paramos el sensor sel acelerometro
    private void stopAcelerometro(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    /**************** ESCANER QR *******************/

    //Iniciamos el sensor de QR
    public void EscanerQR(){
        escanerView = new ZXingScannerView(this);
        setContentView(escanerView);
        escanerView.setResultHandler(this);
        escanerView.startCamera();
    }

    //Realizamos el analisis del resultado del QR para saber si este es correcto o no
    //dependiendo en que coleccionable nos encontremos.

    //Los comentarios son los mismo que en seleccionableAlcazabar
    @Override
    public void handleResult(Result result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String nombre = result.getText();
        if(nombre.equals(nombreColeccionable)){
            builder.setTitle("Desbloqueado Coleccionable");
            builder.setMessage(result.getText());
            MenuAlcazaba.torreVelaBloqueada = false;
        }else{
            builder.setTitle("Error");
            builder.setMessage("Coleccionable Incorrecto");
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        escanerView.resumeCameraPreview(this);
        setContentView(R.layout.activity_seleccionable_torre_vela);

        if(!MenuAlcazaba.torreVelaBloqueada) {
            imagenBlo = (ImageView) findViewById(R.id.bloqueadoTorreVela);
            imagenDes = (ImageView) findViewById(R.id.desbloqueadoTorreVela);
            imagenBlo.setVisibility(View.INVISIBLE);
            imagenDes.setVisibility(View.VISIBLE);
        }
    }

    /****************** MULTITOUCH *****************/

    public void multiTouchParonamica(){
        dedos = (View) findViewById(R.id.viewAlcazaba);         //Capturamos la vista actual...

        dedos.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int action = event.getAction();

                switch(action & MotionEvent.ACTION_MASK){       //Aplicamos la máscara y
                                                                // vemos que acción estamos haciendo...

                    case MotionEvent.ACTION_POINTER_DOWN: {     //Si pulsamos la pantalla...

                        //A continuación comprobamos que haya dos dedos pulsando la pantalla...
                        if(event.getPointerCount() > 1 && event.getPointerCount() < 3){
                            hayDosDedos = true;
                            lastPosition = event.getY();       //Capturamos a que altura de la pantalla hemos pulsado...
                        }

                        if( event.getPointerCount() > 2){
                            hayDosDedos = false;
                        }
                        break;
                    }

                    case MotionEvent.ACTION_UP: {               //Si levantamos y dejamos de pulsar la pantalla...
                        if(hayDosDedos){                        //Solo lo tendremos en cuenta si se realiza con dos dedos...

                            //Vemos si se ha realizado un desplazamiento,
                            // capturando la posición de la pantalla  donde se ha levantado y restando a la posición donde se pulsó.
                            int diff = (int) (event.getY() - lastPosition);

                            hayDosDedos = false;                //Actualizamos que ya no hay dos dedos en la pantalla...

                            // Si el desplazamiento es de abajo a arriba (es negativo)
                            // Mediante experimentación hemos impuesto que el desplazamiento tiene que ser mayor a 400, para que sea un
                            // desplazamiento claro...
                            if(diff < -400){

                                // Vamos al activity vision camara...
                                Intent intent = new Intent(getApplicationContext(), VisionCamara.class);
                                startActivityForResult(intent, 0);
                                customType(seleccionableTorreVela.this,animacion);
                            }

                            break;
                        }

                    }
                }
                return true;
            }
        });
    }

    public void multiTouchQR(){
        dedos = (View) findViewById(R.id.viewAlcazaba);

        dedos.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int action = event.getAction();

                switch(action & MotionEvent.ACTION_MASK){

                    case MotionEvent.ACTION_POINTER_DOWN: {

                        if(event.getPointerCount() > 1 && event.getPointerCount() < 3){
                            hayDosDedos = true;
                            lastPosition = event.getY();
                        }

                        if( event.getPointerCount() > 2){
                            hayDosDedos = false;
                        }
                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        if(hayDosDedos){
                            int diffY = (int) (event.getY() - lastPosition);
                            hayDosDedos = false;
                            if(diffY < -400){
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
