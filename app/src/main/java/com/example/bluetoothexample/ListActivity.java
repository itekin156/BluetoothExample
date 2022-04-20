package com.example.bluetoothexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class ListActivity extends AppCompatActivity
{

    private BluetoothAdapter bluetoothAdapter;

    private ListView listPairedDevices;
    private ProgressBar progressScanner;
    private ListView listAvailableDevices;
    private ArrayAdapter<String> adapterPaired , adapterAvailable;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        context = this;
        listAvailableDevices= findViewById(R.id.listAvailableDevices);
        listPairedDevices= findViewById(R.id.listPairedDevices);
        progressScanner = findViewById(R.id.progressScanner);
        adapterPaired = new ArrayAdapter<String>(context,R.layout.list_item);
        adapterAvailable = new ArrayAdapter<String>(context,R.layout.list_item);
        listPairedDevices.setAdapter(adapterPaired);
        listAvailableDevices.setAdapter(adapterAvailable);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> paireDevices = bluetoothAdapter.getBondedDevices();

        listAvailableDevices.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String info = ((TextView)view).getText().toString();
                String address = info.substring(info.length()-17);

                Intent intent= new Intent();
                intent.putExtra("devicesAddress" , address);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



        if(paireDevices != null &&  paireDevices.size()>0)
        for(BluetoothDevice device: paireDevices)
        {
            adapterPaired.add(device.getName() + "\n" + device.getAddress());
        }

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver , intentFilter);

        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver , intentFilter1);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return super.onCreateOptionsMenu(menu);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState()!= BluetoothDevice.BOND_BONDED)
                {
                    adapterAvailable.add(device.getName()+ " " + device.getAddress());
                }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
                {
                    progressScanner.setVisibility(View.GONE);
                    if(adapterAvailable.getCount() == 0)
                    {
                        Toast.makeText(context, "No New devices found  ", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(context, "click on the device to start chat", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_scan_devices:

                ScanDevices();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }




    private void ScanDevices()
    {
        progressScanner.setProgress(View.VISIBLE);
        adapterAvailable.clear();
        Toast.makeText(context, "Scan STARTED", Toast.LENGTH_SHORT).show();

        if(bluetoothAdapter.isDiscovering())
        {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }
}