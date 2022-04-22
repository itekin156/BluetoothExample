package com.example.bluetoothexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity
{
    private static final int LOCATION_PERMISSION_REQUEST =101;
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private final static int SELECT_DEVICES = 102;
    public static final int MESSAGE_STATE_CHANGED = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_DEVICE_NAME = 3;
    public static final int MESSAGE_TOAST = 4;

    public static final String DEVICE_NAME = "deviceName";
    public static final String TOAST = "TOAST";
    private String connectedDevice;

    Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(@NonNull Message message)
        {
            switch (message.what)
            {
                case MESSAGE_STATE_CHANGED:
                    break;
                case MESSAGE_READ:
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_DEVICE_NAME:
                    connectedDevice = message.getData().getString(DEVICE_NAME);
                    Toast.makeText(context, connectedDevice, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(context, message.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context= this;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            Toast.makeText(context, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
        } else
            {
                Toast.makeText(context, "Bluetooth is available", Toast.LENGTH_SHORT).show();

            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_chat_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_searchdevices:
                 checkPermission();
                   return true;
            case R.id.menu_enable_bluetooth:
                if (!bluetoothAdapter.isEnabled())
                {
                    bluetoothAdapter.enable();
                }
                if(bluetoothAdapter.getScanMode()!=bluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
                {
                    Intent intent = new Intent(bluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    intent.putExtra(bluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,200);
                    startActivityForResult(intent, SELECT_DEVICES);
                }
                return true;
            default:
                 return super.onOptionsItemSelected(item);

        }

    }

    private void checkPermission()
    {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);

        }
        else{
            Intent in = new Intent( ChatActivity.this, ListActivity.class);
            startActivityForResult(in , SELECT_DEVICES);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == SELECT_DEVICES  && resultCode == RESULT_OK)
        {
            String address =  data.getStringExtra("deviceAddress");
            Toast.makeText(context, "Address : "+ address, Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == LOCATION_PERMISSION_REQUEST)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Intent in = new Intent( ChatActivity.this, ListActivity.class);
                startActivity(in);

            }else
            {
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("Location permission is require.\n please grant it")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                             checkPermission();
                            }
                        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChatActivity.this.finish();
                    }
                }) .show();
            }
        }else
            {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
    }
}