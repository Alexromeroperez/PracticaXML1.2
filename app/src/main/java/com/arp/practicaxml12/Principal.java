package com.arp.practicaxml12;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arp.practicaxml12.actividades.Ajustes;
import com.arp.practicaxml12.actividades.Modifica;
import com.arp.practicaxml12.adaptador.Adaptador;
import com.arp.practicaxml12.pojo.Contacto;
import com.arp.practicaxml12.soporte.Actividades;
import com.arp.practicaxml12.soporte.Telefonos;
import com.arp.practicaxml12.soporte.XmlIO;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Principal extends Actividades {
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private ListView lv;
    private TextView tv;
    private int posicion;
    private Adaptador ad;
    private ArrayList<Contacto> listaC;
    private final static int AÑADIR=1,MODIFICAR=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        sp=getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        ini();
    }


    /****************************************************************/
    /****************************************************************/
    /****************************************************************/
    /*****************         Menus              *******************/
    /****************************************************************/
    /****************************************************************/
    /****************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ajustes) {
           lanzarActividad(Ajustes.class);
            return true;
        }else if (id == R.id.sincronizar) {
            if(sp.getBoolean("tipo",false)){
                ArrayList<Contacto> soporte;
                soporte= (ArrayList<Contacto>) Telefonos.getListaContactos(this);
                for (int i=0;i<soporte.size();i++){
                    if(soporte.get(i).compareTo(listaC.get(i))!=0){
                        listaC.add(soporte.get(i));
                    }
                }
                Collections.sort(listaC);
                escribir();
            }else if(sp.getBoolean("tipo",false)==false){
                listaC= (ArrayList<Contacto>) Telefonos.getListaContactos(this);
                añadirTlf(listaC);
                escribir();
            }
            actualizar();
            fecha();
            muestra();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_emergente, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        posicion=info.position;
        switch (item.getItemId()) {
            case R.id.modificar:
                Bundle b=new Bundle();
                b.putSerializable("contacto", listaC.get(posicion));
                lanzarResultadoDatos(Modifica.class, MODIFICAR, b);
                return true;
            case R.id.borrar:
                listaC.get(posicion).setEdicion(2);
                escribir();
                listaC.remove(posicion);
                actualizar();
                tostada("Contacto Borrado");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /****************************************************************/
    /****************************************************************/
    /****************************************************************/
    /*****************   Auxiliares y Inicio      *******************/
    /****************************************************************/
    /****************************************************************/
    /****************************************************************/

    private void ini() {
        lv=(ListView)findViewById(R.id.lvContacto);
        tv=(TextView)findViewById(R.id.tvFecha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarResultado(Modifica.class,AÑADIR);
            }
        });

        ed= sp.edit();
        muestra();
        listaC=new ArrayList<>();
        sincroniza();
        actualizar();
    }

    private void actualizar(){
        ad=new Adaptador(this,R.layout.lista_detalle, listaC);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
    }

    private void sincroniza() {
        if(sp.getBoolean("sincroniza",false)&& sp.getBoolean("tipo",false)==false){
            listaC = (ArrayList<Contacto>) Telefonos.getListaContactos(this);
            añadirTlf(listaC);
            escribir();
            fecha();
        }else if(sp.getBoolean("sincroniza",false)&& sp.getBoolean("tipo",true)){
            ArrayList<Contacto> soporte;
            soporte= (ArrayList<Contacto>) Telefonos.getListaContactos(this);
            listaC=leer();
            for (int i=0;i<soporte.size();i++){
                if(soporte.get(i).compareTo(listaC.get(i))!=0){
                        listaC.add(soporte.get(i));
                }
            }
            Collections.sort(listaC);
            escribir();
            fecha();
        }else if(sp.getBoolean("sincroniza",false)==false){
            listaC=leer();
        }

    }

    private void muestra(){
        if(sp.getBoolean("mostrar",true)){
            tv.setVisibility(View.VISIBLE);
            tv.setText(sp.getString("ultimascr", R.string.str_sinactualizar +""));
        }else {
            tv.setVisibility(View.GONE);
        }
    }

    private void añadirTlf(ArrayList <Contacto> lista){
            //Añade los telefonos correspondientes a cada elemento de la lista de contactos
            for (int i = 0; i <lista.size() ; i++) {
                Contacto c=lista.get(i);
                c.setTlfs((ArrayList<String>) Telefonos.getListaTelefonos(this, c.getId()));
            }
    }

    private  void tostada(String texto){
        Toast.makeText(this,texto,Toast.LENGTH_SHORT).show();
    }

    private void fecha(){
        ed.putString("ultimascr", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        ed.commit();
    }

    private void escribir(){
        try {
            XmlIO.escribir(this,listaC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Contacto> leer() {
        try {
            return XmlIO.leer(this, listaC);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    /****************************************************************/
    /****************************************************************/
    /****************************************************************/
    /*****************           Resultado      *  ******************/
    /****************************************************************/
    /****************************************************************/
    /****************************************************************/
    public void tlfs(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Telefonos");
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.telefonos, null);
        final TextView tv = (TextView) vista.findViewById(R.id.tv);
        alert.setView(vista);
        String todo="",tlf="";
        for (int i = 0; i <listaC.get(posicion).getTlfs().size() ; i++) {
            tlf=listaC.get(posicion).getTlfs().get(i);
            todo+=tlf+"\n";
        }
        tv.setText(todo);
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Contacto c;
        if(resultCode==Principal.RESULT_OK){
            Log.v("ACTIVIDAD CANCELADA","OK");
            Bundle b=data.getExtras();
            switch (requestCode){
                case AÑADIR:
                    c=(Contacto)b.get("contacto");
                    c.setEdicion(0);
                    listaC.add(c);
                    escribir();
                    Collections.sort(listaC);
                    ad.notifyDataSetChanged();
                    tostada("Contacto añadido");

                    break;
                case MODIFICAR:
                    c=(Contacto)b.get("contacto");
                    c.setEdicion(1);
                    listaC.set(posicion,c);
                    escribir();
                    Collections.sort(listaC);
                    ad.notifyDataSetChanged();
                    tostada("Contacto Modificado");
                    break;
            }

        }
        }


}



