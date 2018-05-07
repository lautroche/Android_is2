package com.example.alberto.appcontrol;

/**
 * Created by Marcos Trinidad on 5/1/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LstViewTareasAdapter extends ArrayAdapter<String> {
    int groupid;
    String[] item_list;
    ArrayList<String> desc;
    Context context;
    public LstViewTareasAdapter(Context context, int vg, int id, String[] item_list){
        super(context,vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;

    }
    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView id;
        public TextView descripcion;
        public TextView tiempo;
        public TextView sprint;
        public TextView proyecto;
        public TextView usuario;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // Inflate the rowlayout.xml file if convertView is null
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.id= (TextView) rowView.findViewById(R.id.id);
            viewHolder.descripcion= (TextView) rowView.findViewById(R.id.descripcion);
            viewHolder.tiempo= (TextView) rowView.findViewById(R.id.tiempo);
            viewHolder.sprint= (TextView) rowView.findViewById(R.id.sprint);
            viewHolder.proyecto= (TextView) rowView.findViewById(R.id.proyecto);
            viewHolder.usuario= (TextView) rowView.findViewById(R.id.usuario);
            rowView.setTag(viewHolder);

        }
        // Set text to each TextView of ListView item
        String[] items=item_list[position].split("__");
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.id.setText(items[0]);
        holder.descripcion.setText(items[1]);
        holder.tiempo.setText(items[2]);
        holder.sprint.setText(items[3]);
        holder.proyecto.setText(items[4]);
        holder.usuario.setText(items[5]);
        return rowView;
    }

}