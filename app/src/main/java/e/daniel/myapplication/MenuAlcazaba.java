package e.daniel.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.util.ArrayList;

import static maes.tech.intentanim.CustomIntent.customType;

public class MenuAlcazaba extends Activity {

    // Declaramos el detector de los gestos en la vista, la imagen y el scroll
    private ScaleGestureDetector detector;
    private ScrollView fondo;
    private ImageView im;

    // Variables que indican el estado de las zonas bloqueadas o desbloqueaadas
    public static boolean alcazabaBloqueada = true;
    public static boolean torreVelaBloqueada = true;

    // ************* ACELOREMETRO **************************//
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    ArrayList<Integer> imagenesAlcazabar = new ArrayList<Integer>();
    boolean siguiente = false;
    int indice = 0;

     // *************************************************//
    //ANIMACIONES
    private String animacion_5 = "fadein-to-fadeout";

    // ETIQUETAS
    public static boolean activadoEtiquetaTorreVela = false;
    public static boolean activadoEtiquetaAlcazabar = false;

    public static ImageButton etiquetaAlcazaba, etiquetaTorreVela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_alcazaba);

        // Cargamos los objetos de la vista e iniciamos el sensor
        fondo = findViewById(R.id.scrollAlcazaba);
        im = findViewById(R.id.fondoAlcazaba);
        detector = new ScaleGestureDetector(this,new ScaleListener(fondo, im));

        // Obtenemos la escala
        detector.getScaleFactor();

        // Obtenemos cada boton y ponemos la visibilidad de estos a transparente
        // Haciendo la experiencia con el mapa mas interactiva
        final Button seleccionableAlcazaba = (Button) findViewById(R.id.seleccionableAlcazaba);

        seleccionableAlcazaba.setBackgroundColor(Color.TRANSPARENT);
        seleccionableAlcazaba.setTextColor(Color.TRANSPARENT);

        final Button seleccionableTorreDeLaVela = (Button) findViewById(R.id.seleccionableTorreDeLaVela);
        seleccionableTorreDeLaVela.setBackgroundColor(Color.TRANSPARENT);
        seleccionableTorreDeLaVela.setTextColor(Color.TRANSPARENT);

        // Cargamos los Image button de las zonas accesibles de nuestro Menu
        // Poniendo su visibilidad a INVISIBLE
        etiquetaAlcazaba = (ImageButton) findViewById(R.id.etiquetaAlcazaba);
        etiquetaAlcazaba.setBackgroundColor(Color.TRANSPARENT);
        etiquetaAlcazaba.setVisibility(View.INVISIBLE);

        etiquetaTorreVela = (ImageButton) findViewById(R.id.etiquetaTorreVela);
        etiquetaTorreVela.setBackgroundColor(Color.TRANSPARENT);
        etiquetaTorreVela.setVisibility(View.INVISIBLE);

        // Funcion del boton al pulsar, dependiendo de la visibilidad de este antes de clicar
        // cambiaremos su estado a activado y desactivado y la visibilidad a VISIBLE O INVISBLE
        seleccionableAlcazaba.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!activadoEtiquetaAlcazabar) {
                    etiquetaAlcazaba.setVisibility(View.VISIBLE);
                    activadoEtiquetaAlcazabar = true;
                } else {
                    activadoEtiquetaAlcazabar = false;
                    etiquetaAlcazaba.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Si pulsamos en el ImageButton desplegado nos redirecciona a la activity del seleccionable
        etiquetaAlcazaba.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), seleccionableAlcazaba.class);
                startActivityForResult(intent, 0);
                customType(MenuAlcazaba.this,animacion_5);
            }
        });

        seleccionableTorreDeLaVela.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!activadoEtiquetaTorreVela) {
                    if(!torreVelaBloqueada){
                        etiquetaTorreVela.setImageResource(R.drawable.etiquetatorreveladesbloqueado);
                    }
                    etiquetaTorreVela.setVisibility(View.VISIBLE);
                    activadoEtiquetaTorreVela = true;
                } else {
                    activadoEtiquetaTorreVela = false;
                    etiquetaTorreVela.setVisibility(View.INVISIBLE);
                }
            }
        });

        etiquetaTorreVela.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), seleccionableTorreVela.class);
                startActivityForResult(intent, 0);
                customType(MenuAlcazaba.this,animacion_5);
            }
        });

        // ***************** ACELOREMETRO **************//

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        imagenesAlcazabar.add(R.drawable.alcazabarelieve);
        imagenesAlcazabar.add(R.drawable.alcazabarelieve1);
        imagenesAlcazabar.add(R.drawable.alcazabarelieve2);
        imagenesAlcazabar.add(R.drawable.alcazabarelieve3);

        sensorEventListener = new SensorEventListener() {
            @Override
            // Configuramos el comportamiento del acelerometro
            // GIrando en el eje y, desactivamos los botones
            // y cambiamos la vista para simular efecto 3d
            public void onSensorChanged(SensorEvent sensorEvent) {
                float y = sensorEvent.values[1];

                if (y <= -10 && !siguiente) {
                    indice = (indice+1)%4;
                    siguiente = true;
                    if(indice != 0){

                        seleccionableAlcazaba.setEnabled(false);
                        seleccionableTorreDeLaVela.setEnabled(false);
                        etiquetaTorreVela.setVisibility(View.INVISIBLE);
                        etiquetaAlcazaba.setVisibility(View.INVISIBLE);

                    }else{
                        seleccionableAlcazaba.setEnabled(true);
                        seleccionableTorreDeLaVela.setEnabled(true);
                    }

                    im.setImageResource(imagenesAlcazabar.get(indice));

                }else if (y > 10 && !siguiente) {
                    indice = (((indice-1 % 4) + 4) % 4);
                    siguiente = true;
                    if(indice != 0){
                        seleccionableAlcazaba.setEnabled(false);
                        seleccionableTorreDeLaVela.setEnabled(false);
                        etiquetaTorreVela.setVisibility(View.INVISIBLE);
                        etiquetaAlcazaba.setVisibility(View.INVISIBLE);
                    }else{
                        seleccionableAlcazaba.setEnabled(true);
                        seleccionableTorreDeLaVela.setEnabled(true);
                    }
                    im.setImageResource(imagenesAlcazabar.get(indice));
                } else if (y < 10 && y > -10 && siguiente){
                    siguiente = false;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        startAcelerometro();

    }

    // **************** ACELEROMETRO *************** //

    private void startAcelerometro(){
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override

    public boolean dispatchTouchEvent(MotionEvent event){
        detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

}
