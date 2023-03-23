package com.example.qrgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
/**
 *custom adapter for list of qrcodes
 *  */
public class QRCodeArrayAdapter extends ArrayAdapter {

    public QRCodeArrayAdapter(Context context, ArrayList<QRCode> QrCodeList){
        super (context,0,QrCodeList);
    }


    /**
     * add a new element to the array
     * @return View of new list with added content
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView==null){
            view= LayoutInflater.from(super.getContext()).inflate(R.layout.inventory_content,parent,false);
        }
        else{
            view=convertView;
        }

        QRCode qrCode=(QRCode) getItem(position);

        TextView point=view.findViewById(R.id.inventory_qr_point);
        TextView name=view.findViewById(R.id.inventory_qr_name);


        point.setText("points:"+qrCode.getScore());
        name.setText("name:"+qrCode.getName());



        return view;
    }
}
