package com.arp.practicaxml12.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.arp.practicaxml12.R;
import com.arp.practicaxml12.pojo.Contacto;

import java.util.ArrayList;

/**
 * Created by Alex on 25/10/2015.
 */
public class Adaptador extends ArrayAdapter<Contacto> {
    private Context contexto;
    private int recurso;
    private ArrayList<Contacto> lista;
    private LayoutInflater i;

    public Adaptador(Context context, int resource, ArrayList<Contacto> lista) {
        super(context, resource, lista);
        this.contexto=context;
        this.recurso=resource;
        this.lista=lista;
        i=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if(vh==null){
            vh=new ViewHolder();
            convertView=i.inflate(recurso,null);
            vh.iv=(ImageView)convertView.findViewById(R.id.ivFoto);
            vh.tvNombre=(TextView)convertView.findViewById(R.id.tvNombre);
            vh.tvTelefono=(TextView)convertView.findViewById(R.id.tvTlf);
            vh.btMostrar=(ImageButton)convertView.findViewById(R.id.btMostrar);
            convertView.setTag(vh);
        }else {
            vh=(ViewHolder)convertView.getTag();
        }
        Contacto c = lista.get(position);
        final ArrayList<String> telefonos=c.getTlfs();
        vh.tvNombre.setText(c.getNombre());
        vh.tvTelefono.setText(c.getTlfs().get(0));
        if(telefonos.size()>1) {
            vh.btMostrar.setImageResource(R.drawable.mas);
        }else{
            vh.btMostrar.setVisibility(View.GONE);
        }



        return convertView;
    }
    static class ViewHolder{
        private TextView tvNombre,tvTelefono;
        private ImageView iv;
        private ImageButton btMostrar;

    }
}
