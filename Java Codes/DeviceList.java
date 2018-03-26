package com.homeautomation.android.homeautomation;

//  @author: Darth Vader
//      May the force be with you.

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeviceList extends AppCompatActivity {
    public static String EXTRA_ADDRESS = "device_address";
    Button btnPaired;
    ListView deviceList;
    private BluetoothAdapter myBluetooth = null;
    private OnItemClickListener myListClickListener = new C01352();

    class C01341 implements OnClickListener {
        C01341() {
        }

        public void onClick(View v) {
            DeviceList.this.pairedDevicesList();
        }
    }

    class C01352 implements OnItemClickListener {
        C01352() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3) {
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent i = new Intent(DeviceList.this, HomeAutomation.class);
            i.putExtra(DeviceList.EXTRA_ADDRESS, address);
            DeviceList.this.startActivity(i);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        this.btnPaired = findViewById(R.id.button);
        this.deviceList = findViewById(R.id.listView);
        this.myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (this.myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_SHORT).show();
            finish();
        } else if (!this.myBluetooth.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        this.btnPaired.setOnClickListener(new C01341());
    }

    private void pairedDevicesList() {
        Set<BluetoothDevice> pairedDevices = this.myBluetooth.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_SHORT).show();
        }
        this.deviceList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        this.deviceList.setOnItemClickListener(this.myListClickListener);
    }
}
