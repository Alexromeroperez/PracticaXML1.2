package com.arp.practicaxml12.soporte;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.arp.practicaxml12.pojo.Contacto;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex on 24/10/2015.
 */
public class XmlIO {

    public static void escribir(Context c,ArrayList<Contacto> lista) throws IOException {
        FileOutputStream fosxml = new FileOutputStream(new File(c.getExternalFilesDir(null),"agenda.xml"));
        XmlSerializer docxml = android.util.Xml.newSerializer();
        docxml.setOutput(fosxml, "UTF-8");
        docxml.startDocument(null, Boolean.valueOf(true));
        docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        docxml.startTag(null, "agenda");
        for (int i = 0; i < lista.size(); i++) {
            docxml.startTag(null, "contacto");
            docxml.attribute(null, "nombre", lista.get(i).getNombre());
            docxml.attribute(null, "edicion", lista.get(i).getEdicion()+"");

            for (int k = 0; k <lista.get(i).getTlfs().size() ; k++) {
                docxml.startTag(null, "tlf");
                docxml.text(lista.get(i).getTlfs().get(k));
                docxml.endTag(null,"tlf");
            }

            docxml.endTag(null, "contacto");
        }

        docxml.endDocument();
        docxml.flush();
        fosxml.close();
    }

    public static ArrayList leer(Context c,ArrayList<Contacto> lista) throws IOException, XmlPullParserException {
        XmlPullParser lectorxml = android.util.Xml.newPullParser();
        lectorxml.setInput(new FileInputStream(new File(c.getExternalFilesDir(null),"agenda.xml")),"utf-8");
        Contacto contacto=null;
        ArrayList <String>tlf=null;
        int evento = lectorxml.getEventType();
        while (evento != XmlPullParser.END_DOCUMENT){
            if(evento == XmlPullParser.START_TAG){
                String etiqueta = lectorxml.getName();
                if(etiqueta.compareTo("contacto")==0){
                    contacto=new Contacto();
                    tlf=new ArrayList<>();
                    contacto.setNombre(lectorxml.getAttributeValue(null, "nombre"));
                    contacto.setEdicion(Integer.parseInt(lectorxml.getAttributeValue(null,"edicion")));
                }else if(etiqueta.compareTo("tlf")==0){
                    tlf.add(lectorxml.nextText());
                }
            }else if(evento==XmlPullParser.END_TAG){
                if(lectorxml.getName().compareTo("contacto")==0){
                    contacto.setTlfs(tlf);
                    if(contacto.getEdicion()!=2) {
                        lista.add(contacto);
                    }
                }
            }
            evento = lectorxml.next();
        }
        return lista;
    }
}
