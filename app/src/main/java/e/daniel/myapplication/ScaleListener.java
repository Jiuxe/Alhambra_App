package e.daniel.myapplication;

import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


    ScrollView imageView;
    ImageView im;
    private float scale = 1f;
    private float xbegin = 0;
    private float ybegin = 0;
    //private float max_Y = imageView.
    //private float max_X = imageView.getScaleX();

    public ScaleListener(ScrollView imageView, ImageView im){
        // Variable de la vista de la imagen
        this.imageView = imageView;
        this.im = im;
        // Variables para el escalado de la vista
        this.xbegin = im.getScaleX();
        this.ybegin = im.getScaleY();
    }
    public boolean onScale(ScaleGestureDetector detector) {

        scale *= detector.getScaleFactor();
        // Limitamos el escalado
        if(scale > 1.3525){
            scale = (float) 1.3525;
        }
        if(scale >= xbegin || scale >= ybegin) {
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
        }else{
           scale = xbegin;
        }

        return true;
    }
}
