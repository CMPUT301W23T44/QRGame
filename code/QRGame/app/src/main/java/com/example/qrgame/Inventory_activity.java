package com.example.qrgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Inventory_activity extends AppCompatActivity {

    private ArrayList<QRCode> QrDataList;
    private ListView QrCodeList;
    private QRCodeArrayAdapter QrAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        QrDataList=new ArrayList<>();

        QrCodeList=findViewById(R.id.inventory_qr_list);
        QrAdapter=new QRCodeArrayAdapter(this,QrDataList);
        QrCodeList.setAdapter(QrAdapter);

        Button sortButton=findViewById(R.id.inventory_sort_button);
        Button backButton=findViewById(R.id.inventory_back_button);



        QrCodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Button deleteButton=findViewById(R.id.inventory_delete_button);
                Button detailsButton=findViewById(R.id.inventory_qr_details);


                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (i >=0) {
                            QrDataList.remove(i);
                            QrAdapter.notifyDataSetChanged();
                        }
                    }
                });

                detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent =new Intent(Inventory_activity.this,QRInfoActivity.class);
                        intent.putExtra("qrCode",QrDataList.get(i));
                        startActivity(intent);
                    }
                });

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Inventory_activity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });










    }
}