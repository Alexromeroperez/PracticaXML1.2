package com.arp.practicaxml12.actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arp.practicaxml12.R;

public class Ajustes extends AppCompatActivity {

     private SharedPreferences sp;
     private SharedPreferences.Editor ed;
     private RadioButton rbS1,rbS2,rbTotal,rbParcial,rbM1,rbM2;
     private RadioGroup rg1,rg2,rg3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        sp=getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        ed=sp.edit();

        ini();
    }

    public void guardar(View v){
        int seleccion1=rg1.getCheckedRadioButtonId();
        int seleccion2=rg2.getCheckedRadioButtonId();
        int seleccion3=rg3.getCheckedRadioButtonId();
        switch (seleccion1){
            case R.id.rbSin:
                ed.putBoolean("sincroniza",true);
                break;
            case R.id.rbSinNo:
                ed.putBoolean("sincroniza",false);
                break;
        }
        switch (seleccion2){
            case R.id.rbParcial:
                ed.putBoolean("tipo",true);
                break;
            case R.id.rbTotal:
                ed.putBoolean("tipo",false);
                break;
        }
        switch (seleccion3){
            case R.id.rbMostrar:
                ed.putBoolean("mostrar",true);
                break;
            case R.id.rbNoMostrar:
                ed.putBoolean("mostrar",false);
                break;
        }
        ed.commit();
        Toast.makeText(this,R.string.str_ajustes,Toast.LENGTH_SHORT).show();
        finish();
    }

    private void ini(){
        rbS1=(RadioButton)findViewById(R.id.rbSin);
        rbS2=(RadioButton)findViewById(R.id.rbSinNo);
        rbTotal=(RadioButton)findViewById(R.id.rbTotal);
        rbParcial=(RadioButton)findViewById(R.id.rbParcial);
        rbM1=(RadioButton)findViewById(R.id.rbMostrar);
        rbM2=(RadioButton)findViewById(R.id.rbNoMostrar);
        rg1=(RadioGroup)findViewById(R.id.rgInicio);
        rg2=(RadioGroup)findViewById(R.id.rgTipo);
        rg3=(RadioGroup)findViewById(R.id.rgMustra);

        if(sp.getBoolean("sincroniza",true)){
            rbS1.setChecked(true);
        }else {
            rbS2.setChecked(true);
        }
        if(sp.getBoolean("tipo",true)){
            rbParcial.setChecked(true);
        }else{
            rbTotal.setChecked(true);
        }
        if(sp.getBoolean("mostrar",true)){
            rbM1.setChecked(true);
        }else
        {
            rbM2.setChecked(true);
        }
    }
}
