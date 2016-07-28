package org.opendroidphp.app.util;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

/**
 * Created by Harold Montenegro on 28/07/16.
 */
public class AnimationUtilities {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static ObjectAnimator movimientoY(View view, int duracion, float desdePos, float hastaPos) {
        //Devuelve una animación para mover un view en "Y" o verticalmente, no la reproduce, sólo la devuelve lista para
        //reproducirse.
        ObjectAnimator animacion = new ObjectAnimator();
        animacion.setTarget(view);
        animacion.setPropertyName("translationY");
        animacion.setFloatValues(desdePos, hastaPos);
        animacion.setDuration(duracion);
        return animacion;
    }

}
