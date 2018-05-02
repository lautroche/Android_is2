package com.example.alberto.appcontrol;

/**
 * Created by Marcos Trinidad on 5/1/2018.
 */

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
public class LstViewAdapter extends ArrayAdapter<String> {
    int groupid;
    String[] item_list;
    ArrayList<String> desc;
    Context context;
    public LstViewAdapter(Context context, int vg, int id, String[] item_list){
        super(context,vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;

    }
    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView cod;
        public TextView correo;
        public TextView identi;
        public TextView empresa;
        public TextView telefono;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // Inflate the rowlayout.xml file if convertView is null
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.cod= (TextView) rowView.findViewById(R.id.cod);
            viewHolder.correo= (TextView) rowView.findViewById(R.id.correo);
            viewHolder.identi= (TextView) rowView.findViewById(R.id.identi);
            viewHolder.empresa= (TextView) rowView.findViewById(R.id.empresa);
            viewHolder.telefono= (TextView) rowView.findViewById(R.id.telefono);
            rowView.setTag(viewHolder);

        }
        // Set text to each TextView of ListView item
        String[] items=item_list[position].split("__");
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.cod.setText(items[0]);
        holder.correo.setText(items[1]);
        holder.identi.setText(items[2]);
        holder.empresa.setText(items[3]);
        holder.telefono.setText(items[4]);

        return rowView;
    }

}