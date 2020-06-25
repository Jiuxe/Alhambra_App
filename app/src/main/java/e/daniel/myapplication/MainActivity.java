package e.daniel.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;
import static maes.tech.intentanim.CustomIntent.customType;

public class MainActivity extends Activity {


    // *********************** GPS ********************//
    // Objeto de tipo Location Manager para las peticiones GPS

    // Objetos y variables para el GPS

    LocationManager locationManager;
    // Variables que indican la posicion actual del usuario
    double longitude, latitude;
    // Lista de posiciones, una por cada zona seleccionable del mapa
    ArrayList<Pair<Double, Double>> posiciones = new ArrayList<Pair<Double, Double>>();
    // Variable que indica la zona actual en la que nos encontramos del mapa
    int indice;
    // Fin Variables y objetos para GPS

    // Objeto de tipo Scale Gesture Detectos usado para la fncionalidad de zoom
    private ScaleGestureDetector detector;
    private ScrollView fondo;
    // Objetos image View para indicar que las zonas no estan seleccionadas
    private ImageView im, alcazabarOscuro, palaciosOscuro, medinaOscuro, carlosOscuro, jardinesOscuro, generalifeOscuro, gpsAlcazaba;
    // Botones de cada zona seleccionable del mapa
    private Button botonAlcazaba, botonPalacios, botonCarlos, botonJardines, botonMedina, botonGeneralife, rosaDeLosVientos;
    // ANIMACIONES
    private String animacion_5 = "fadein-to-fadeout";

    // Array de zonas oscuras que utilizaremos para oscureces las no seleccionadas

    private ArrayList<ImageView> zonasOscuras = new ArrayList<ImageView>();

    // Funcion que pone las zonas no seleccionadas en oscuro
    private void cambiaVisibilidad(ArrayList<ImageView> zonasOscuras, ImageView zona){
        for( int i = 0; i < zonasOscuras.size(); i ++){
            if(zonasOscuras.get(i) != zona){
                // Hacemos visible una imagen que muestra la misma zona pero un poco oscurecida
                zonasOscuras.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    // Usamos on Create para cargar la imagen antes de que se inicialice la vista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        fondo = findViewById(R.id.scroll);
        im = findViewById(R.id.fondo);
        detector = new ScaleGestureDetector(this,new ScaleListener(fondo, im));
        detector.getScaleFactor();

        // Obtenemos todos los views de la vista
        alcazabarOscuro = findViewById(R.id.alcazabarOscuro);
        palaciosOscuro = findViewById(R.id.palacionazariesOscuro);
        medinaOscuro = findViewById(R.id.medinaOscuro);
        carlosOscuro = findViewById(R.id.carlosOscuro);
        jardinesOscuro = findViewById(R.id.jardinesOscuro);
        generalifeOscuro = findViewById(R.id.generalifeOscuro);
        // Los añadimos a la lista de zonas oscuras
        zonasOscuras.add(alcazabarOscuro);
        zonasOscuras.add(palaciosOscuro);
        zonasOscuras.add(medinaOscuro);
        zonasOscuras.add(carlosOscuro);
        zonasOscuras.add(jardinesOscuro);
        zonasOscuras.add(generalifeOscuro);

        // Obtenemos los botones de la vista
        botonAlcazaba = (Button) findViewById(R.id.botonAlcazaba);
        botonPalacios = (Button) findViewById(R.id.botonPalaciosNazaries);
        botonCarlos = (Button) findViewById(R.id.botonPalacioCarlosV);
        botonJardines = (Button) findViewById(R.id.botonJardines);
        botonMedina = (Button) findViewById(R.id.botonMedina);
        botonGeneralife = (Button) findViewById(R.id.botonGeneralife);
        rosaDeLosVientos = (Button) findViewById(R.id.rosaDeLosVientos);
        gpsAlcazaba = (ImageView) findViewById(R.id.gpsAlcazaba);

        gpsAlcazaba.setVisibility(View.INVISIBLE);

        // Para cada boton ponemos su color a transparente, simulando asi que la zona de la imagen es el boton
        botonAlcazaba.setBackgroundColor(Color.TRANSPARENT);
        botonAlcazaba.setTextColor(Color.TRANSPARENT);


        // Para cada zona, su boton pone el resto de las zonas oscuras y esta con la imagen original
        // si mantenemos pulsado una de ellas se nos redirecciona a la zona seleccionada
        botonAlcazaba.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                if(alcazabarOscuro.getVisibility() == View.VISIBLE) {
                    alcazabarOscuro.setVisibility(View.INVISIBLE);
                    cambiaVisibilidad(zonasOscuras, alcazabarOscuro);
                    botonAlcazaba.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), MenuAlcazaba.class);
                            startActivityForResult(intent, 0);
                            customType(MainActivity.this, animacion_5);
                            return true;
                        }

                    });
                }else{
                    alcazabarOscuro.setVisibility(View.VISIBLE);
                }
            }
        });


        botonPalacios.setBackgroundColor(Color.TRANSPARENT);
        botonPalacios.setTextColor(Color.TRANSPARENT);

        botonPalacios.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                if(palaciosOscuro.getVisibility() == View.VISIBLE) {
                    palaciosOscuro.setVisibility(View.INVISIBLE);
                    cambiaVisibilidad(zonasOscuras, palaciosOscuro);
                }else{
                    palaciosOscuro.setVisibility(View.VISIBLE);
                }
            }
        });


        botonCarlos.setBackgroundColor(Color.TRANSPARENT);
        botonCarlos.setTextColor(Color.TRANSPARENT);

        botonCarlos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                if(carlosOscuro.getVisibility() == View.VISIBLE) {
                    carlosOscuro.setVisibility(View.INVISIBLE);
                    cambiaVisibilidad(zonasOscuras, carlosOscuro);
                }else{
                    carlosOscuro.setVisibility(View.VISIBLE);
                }
            }
        });


        botonJardines.setBackgroundColor(Color.TRANSPARENT);
        botonJardines.setTextColor(Color.TRANSPARENT);

        botonJardines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(jardinesOscuro.getVisibility() == View.VISIBLE) {
                    jardinesOscuro.setVisibility(View.INVISIBLE);
                    cambiaVisibilidad(zonasOscuras, jardinesOscuro);
                }else{
                    jardinesOscuro.setVisibility(View.VISIBLE);
                }
            }
        });


        botonMedina.setBackgroundColor(Color.TRANSPARENT);
        botonMedina.setTextColor(Color.TRANSPARENT);

        botonMedina.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                if(medinaOscuro.getVisibility() == View.VISIBLE) {
                    medinaOscuro.setVisibility(View.INVISIBLE);
                    cambiaVisibilidad(zonasOscuras, medinaOscuro);
                }else{
                    medinaOscuro.setVisibility(View.VISIBLE);
                }
            }
        });


        botonGeneralife.setBackgroundColor(Color.TRANSPARENT);
        botonGeneralife.setTextColor(Color.TRANSPARENT);

        botonGeneralife.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                if(generalifeOscuro.getVisibility() == View.VISIBLE) {
                    generalifeOscuro.setVisibility(View.INVISIBLE);
                    cambiaVisibilidad(zonasOscuras, generalifeOscuro);
                }else{
                    generalifeOscuro.setVisibility(View.VISIBLE);
                }
            }
        });

        // Inicializacion de las posiciones


        posiciones.add(new Pair<>(-3.624385,37.197358));
        posiciones.add(new Pair<>(70.0, 70.0));
        posiciones.add(new Pair<>(90.0, 90.0));
        posiciones.add(new Pair<>(100.0, 100.0));
        posiciones.add(new Pair<>(110.0, 110.0));
        posiciones.add(new Pair<>(120.0, 120.0));


        // Sensor longitud y latitud
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Boton para llamar al gps
        rosaDeLosVientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                bottonGPS(v);
            }
        });
    }
    // Funcion que lee nuestra posicion
    // Voy a hacer uso de un array de posiciones para cada zona de la alhambra
    // Y si esta cerca del centro de alguna de ellas que se active
    private final LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            longitude = location.getLongitude();
            latitude = location.getLatitude();

            for(int i=0; i<posiciones.size(); i++){
                if(posiciones.get(i).first > longitude-10 && posiciones.get(i).first < longitude+10 ){
                    if(posiciones.get(i).second > latitude-10 && posiciones.get(i).second < latitude+10){
                        gpsAlcazaba.setVisibility(View.VISIBLE);
                        zonasOscuras.get(i).setVisibility(View.INVISIBLE);
                        indice = i;
                    }
                }
            }
            for (int i = 0; i < zonasOscuras.size(); i++){
                if (i != indice){
                    zonasOscuras.get(i).setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    // Boton para activar el gps
    public void bottonGPS(View view) {

        if (!conexionEnabled()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Active el GPS", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                "Generando Ubicacion. Espere", Toast.LENGTH_SHORT);
        toast.show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListenerGPS);

    }
    // Comprobamos que tenemos GPS y conexión
    private boolean conexionEnabled(){
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }


}

