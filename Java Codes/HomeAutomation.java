package com.homeautomation.android.homeautomation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.lang.String;

public class HomeAutomation extends AppCompatActivity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String address = null;
    BluetoothSocket btSocket = null;
    ImageButton imageButtonOneOff, imageButtonTwoOff, imageButtonThreeOff, imageButtonFourOff,
                    imageButtonOneOn, imageButtonTwoOn, imageButtonThreeOn, imageButtonFourOn,
                        voiceButton;
    Button allOn, allOff, buttonExit, buttonDisconnect, buttonAbout;
    private boolean isBtConnected = false;
    public ListView mList;
    BluetoothAdapter myBluetooth = null;
    private ProgressDialog progress;

    String bulbOff = "light off";
    String fanOff = "fan off";
    String tvOff = "tv off";
    String washingMachineOff = "washing machine off";

    String bulbOn = "light on";
    String fanOn = "fan on";
    String tvOn = "tv on";
    String washingMachineOn = "washing machine on";

    @SuppressLint("StaticFieldLeak")
    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess;

        private ConnectBT() {
            this.ConnectSuccess = true;
        }

        protected void onPreExecute() {
            HomeAutomation.this.progress = ProgressDialog.show(HomeAutomation.this, "Connecting...", "Please wait!!!");
        }

        protected Void doInBackground(Void... devices) {
            try {
                if (HomeAutomation.this.btSocket == null || !HomeAutomation.this.isBtConnected) {
                    HomeAutomation.this.myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = HomeAutomation.this.myBluetooth.getRemoteDevice(HomeAutomation.this.address);
                    HomeAutomation.this.btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(HomeAutomation.myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    HomeAutomation.this.btSocket.connect();
                }
            } catch (IOException e) {
                this.ConnectSuccess = false;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (this.ConnectSuccess) {
                HomeAutomation.this.msg("Connected.");
                HomeAutomation.this.isBtConnected = true;
            } else {
                HomeAutomation.this.msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                HomeAutomation.this.finish();
            }
            HomeAutomation.this.progress.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.address = getIntent().getStringExtra(DeviceList.EXTRA_ADDRESS);
        setContentView(R.layout.activity_led_control);
        this.imageButtonOneOff = findViewById(R.id.bulb_off);
        this.imageButtonTwoOff = findViewById(R.id.fan_off);
        this.imageButtonThreeOff = findViewById(R.id.tv_off);
        this.imageButtonFourOff = findViewById(R.id.washing_machine_off);

        this.imageButtonOneOn = findViewById(R.id.bulb_on);
        this.imageButtonTwoOn = findViewById(R.id.fan_on);
        this.imageButtonThreeOn = findViewById(R.id.tv_on);
        this.imageButtonFourOn = findViewById(R.id.washing_machine_on);

        this.voiceButton = findViewById(R.id.voice_icon);
        this.allOn = findViewById(R.id.buttonStart);
        this.allOff = findViewById(R.id.buttonStop);
        this.buttonDisconnect = findViewById(R.id.disconnect_button);
        this.buttonExit = findViewById(R.id.exit_button);
        this.buttonAbout = findViewById(R.id.about_button);

        this.voiceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.voiceinputbuttons();
            }
        });

        new ConnectBT().execute();

        this.imageButtonOneOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.bulbOff();
            }
        });

        this.imageButtonOneOn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.bulbOn();
            }
        });

        this.imageButtonTwoOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.fanOff();
            }
        });

        this.imageButtonTwoOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.fanOn();
            }
        });

        this.imageButtonThreeOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.tvOff();
            }
        });

        this.imageButtonThreeOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.tvOn();
            }
        });

        this.imageButtonFourOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.washingMachineOff();
            }
        });

        this.imageButtonFourOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.washingMachineOn();
            }
        });

        this.buttonDisconnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.Disconnect();
            }
        });

        this.buttonExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeAutomation.this.Exit();
            }
        });

        this.buttonAbout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAutomation.this, About.class);
                startActivity(intent);
            }
        });
    }

    public void informationMenu() {
        startActivity(new Intent("android.intent.action.INFOSCREEN"));
    }

    public void voiceinputbuttons() {
        this.voiceButton = findViewById(R.id.voice_icon);
        this.mList = findViewById(R.id.list);
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.PROMPT", "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void onClick(View v) {
        startVoiceRecognitionActivity();
    }

    private void Disconnect() {
        if (this.btSocket != null) {
            try {
                this.btSocket.close();
            } catch (IOException e) {
                msg("Error");
            }
        }
        finish();
    }

    private void Exit() {
        if (this.btSocket != null) {
            try {
                this.btSocket.close();
            } catch (IOException e) {
                msg("Error");
            }
        }
        finish();
    }

    private void bulbOff() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write(bulbOff.getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void bulbOn() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write(bulbOn.getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void fanOff() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write(fanOff.getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void fanOn() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write(fanOn.getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void tvOff() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write(tvOff.getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void tvOn() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write(tvOn.getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void washingMachineOff() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write(washingMachineOff.getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void washingMachineOn() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write(washingMachineOn.getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == -1) {
            ArrayList<String> matches = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            this.mList.setAdapter(new ArrayAdapter<>(this, 17367043, matches));
            if (matches.contains("information")) {
                informationMenu();
            }
        }
    }
}
