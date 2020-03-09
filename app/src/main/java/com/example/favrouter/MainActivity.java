package com.example.favrouter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.favrouter.backendRecycler.AdapterCustom;
import com.example.favrouter.backendRecycler.DataProvider;
import com.example.favrouter.database.DatabaseOper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static AdapterCustom adapterCustom;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DataProvider dataProvider;
    private FloatingActionButton btn_add;
    static List<DataProvider> dataProviderList;
    private DatabaseOper databaseOper;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_main);
        requestPermission(permissions);
        databaseOper = new DatabaseOper(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadData();
        add();
        clickCelula();


    }


    public void loadData() {
        dataProviderList = databaseOper.readAllData();
        adapterCustom = new AdapterCustom(dataProviderList);
        recyclerView.setAdapter(adapterCustom);
    }

    public void add() {
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void clickCelula() {
        adapterCustom.setOnOtemClickListener(new AdapterCustom.OnItemClickListener() {
            @Override
            public void setOnClick(int position) {
                adapterCustom.notifyItemChanged(position);
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                dataProvider = dataProviderList.get(position);

                intent.putExtra("position", position);
                intent.putExtra("latitude", dataProvider.getLatitude());
                intent.putExtra("longitude", dataProvider.getLongitude());

                startActivity(intent);

            }
        });
    }

    public void onClick(View view) {
        int id = (int) view.getTag();
        long idSupport = databaseOper.getAid().get(id);
        databaseOper.deleteData(idSupport);
        loadData();
        clickCelula();
    }

    public void requestPermission(String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, permissions, i);
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i])) {
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, i);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                alertAndFinish();
                return;
            }
        }
    }

    public void alertAndFinish() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setTitle("Uso do dispositivo.");
        alerta.setMessage("Para usar o app \nfavor autorizar o uso do hardware");
        alerta.setCancelable(false);
        alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alerta.show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadData();
        clickCelula();
    }
}
