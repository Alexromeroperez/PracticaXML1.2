package com.arp.practicaxml12.soporte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alex on 24/10/2015.
 */
public class  Actividades extends AppCompatActivity{

    public void lanzarActividad(Class clase){
        Intent i=new Intent(this,clase);
        startActivity(i);

    }

    public void lanzarActividadDatos(Class clase,Bundle b){
        Intent i=new Intent(this,clase);
        i.putExtras(b);
        startActivity(i);
    }

    public void lanzarResultado(Class clase,int constante){
        Intent i=new Intent(this,clase);
        startActivityForResult(i, constante);
    }
    public void lanzarResultadoDatos(Class clase,int constante,Bundle b){
        Intent i=new Intent(this,clase);
        i.putExtras(b);
        startActivityForResult(i,constante);
    }

}
