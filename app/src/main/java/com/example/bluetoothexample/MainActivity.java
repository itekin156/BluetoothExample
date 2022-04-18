package com.example.bluetoothexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;


    private BluetoothAdapter mBlueAdapter;
    private TextView txtStatusBluetooth;
    private TextView txtPairedTv;
    private Button btnPaired;
    private Button btnDiscover;
    private Button btnOf;
    private Button btnOn;
    private ImageView imgBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOn = findViewById(R.id.btnOn);
        btnOf = findViewById(R.id.btnOf);
        btnPaired = findViewById(R.id.btnPaired);
        btnDiscover = findViewById(R.id.btnDiscover);
        txtPairedTv = findViewById(R.id.txtPairedTv);
        txtStatusBluetooth = findViewById(R.id.txtStatusBluetooth);
        imgBluetooth = findViewById(R.id.imgBluetooth);

        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueAdapter == null) {
            txtStatusBluetooth.setText("Bluetooth is not available");
        } else {
            txtStatusBluetooth.setText("Bluetooth is available");
        }

        if (mBlueAdapter.isEnabled()) {
            imgBluetooth.setImageResource(R.drawable.bluetooth_connected);
        } else {
            imgBluetooth.setImageResource(R.drawable.bluetooth_disabled);
        }


        btnOn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!mBlueAdapter.isEnabled())
                {
                    showmsg("Turning On Bluetooth...");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else
                    {
                        showmsg("Bluetooth is already On");
                    }
            }
        });


        btnDiscover.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mBlueAdapter.isDiscovering())
                {
                    showmsg("Making your device discovering");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }


            }
        });


        btnOf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mBlueAdapter.isEnabled())
                {
                    mBlueAdapter.disable();
                    showmsg("Turning Bluetooth Of ...");
                    imgBluetooth.setImageResource(R.drawable.bluetooth_disabled);

                } else
                {
                    showmsg("Bluetooth is already Of");
                }

            }
        });

        btnPaired.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mBlueAdapter.isEnabled())
                {
                  txtPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for(BluetoothDevice device: devices)
                    {
                       txtPairedTv.append("\nDevices: "+ device.getName()+","+ device);
                    }
                }else
                {
                    showmsg("turn on bluetooth to get paired devices");
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK)
                {
                    imgBluetooth.setImageResource(R.drawable.bluetooth_connected);
                    showmsg("BlueTooth is on");
                }else
                {
                    showmsg("couldn't on BlueTooth ");
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showmsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}