package com.quetzal.lucessalonv2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Diego on 26/11/2015.
 */
public class Iluminacion extends Activity {

    private static final String TAG = "Bluetooth";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "98:D3:31:70:3F:CC";

    private ImageButton s1,s2,s3,s4,fiesta,s5, s6, s7, s8;
    private ImageButton blanco, rojo, verde, azul, morado, cyan, amarillo, off;
    private ImageButton m1,m2,m3,m4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iluminacion);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        EstadoBT();

        s1 = (ImageButton) findViewById(R.id.s1);
        s2 = (ImageButton) findViewById(R.id.s2);
        s3 = (ImageButton) findViewById(R.id.s3);
        s4 = (ImageButton) findViewById(R.id.s4);
        //s5 = (ImageButton) findViewById(R.id.s1d);
        //s6 = (ImageButton) findViewById(R.id.s2d);
        //s7 = (ImageButton) findViewById(R.id.s3d);
        //s8 = (ImageButton) findViewById(R.id.s4d);
        fiesta = (ImageButton) findViewById(R.id.fiesta);
        blanco = (ImageButton) findViewById(R.id.blanco);
        rojo = (ImageButton) findViewById(R.id.rojo);
        verde = (ImageButton) findViewById(R.id.verde);
        azul = (ImageButton) findViewById(R.id.azul);
        morado = (ImageButton) findViewById(R.id.morado);
        cyan = (ImageButton) findViewById(R.id.cyan);
        amarillo = (ImageButton) findViewById(R.id.amarillo);
        off = (ImageButton) findViewById(R.id.off);
        m1 = (ImageButton) findViewById(R.id.modo1);
        m2 = (ImageButton) findViewById(R.id.modo2);
        m3 = (ImageButton) findViewById(R.id.modo3);
        m4 = (ImageButton) findViewById(R.id.modo4);

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("A");
            }
        });
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("B");
            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("C");
            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("O");
            }
        });
       /* s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("C");
            }
        });
        s6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("c");
            }
        });
        s7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("O");
            }
        });
        s8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("o");
            }
        });*/
        fiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("F");
            }
        });
        blanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("7");
            }
        });
        rojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("1");
            }
        });
        verde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("2");
            }
        });
        azul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("3");
            }
        });
        morado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("4");
            }
        });
        cyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("5");
            }
        });
        amarillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("8");
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("6");
            }
        });
        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("G");
            }
        });
        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("H");
            }
        });
        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("I");
            }
        });
        m4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("J");
            }
        });
    }

    private void EnviarDato(String dato){
        byte[] buffer = dato.getBytes();
        Log.d(TAG, "...Enviando dato: " + dato + "...");
        try {
            outStream.write(buffer);
        }catch(IOException e){
            String msg = "Receptor Bluetooth no localizado"/*+e.getMessage()*/;
            if (address.equals("00:00:00:00:00:00")) {
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
                msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
            }
            errorExit("Error: ", msg);
        }
    }

    private void EstadoBT(){
        if (btAdapter == null){
            errorExit("Error fatal", "Bluetooth not support");
        }
        else{
            if(btAdapter.isEnabled()){
                Log.d(TAG,"...Bluetooth encendido...");
            }
            else{
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i, 1);
            }
        }
    }

    private void errorExit(String title, String mensaje){
        Toast.makeText(getBaseContext(), title + "-" + mensaje, Toast.LENGTH_LONG).show();
        //finish();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException{
        if (Build.VERSION.SDK_INT >= 10){
            try{
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "No se puede crear un a coneccion FRComm insegura", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - intentando conectar...");
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Error fatal", "En onResume(): fallo en crear el socket: " + e1.getMessage() + ".");
        }
        btAdapter.cancelDiscovery();
        Log.d(TAG, "...Conectando...");
        try{
            btSocket.connect();
            Log.d(TAG,"...Conexion establecida...");
        }catch(IOException e){
            try{
                btSocket.close();
            }catch (IOException e2){
                errorExit("Error Fatal", "En onResume(), incapaz de cerrar el socket de la conexion fallida" + e2.getMessage() + ".");
            }
        }
        Log.d(TAG, "...Creando socket...");
        try{
            outStream = btSocket.getOutputStream();
        }catch(IOException e){
            errorExit("Error fatal", "En onResume() Fallo el crear la salida de coreiente:" + e.getMessage() + ".");
        }
    }

    public void onPause(){
        super.onPause();
        Log.d(TAG, "...En onPause()...");
        if (outStream != null){
            try{
                outStream.flush();
            }catch (IOException e){
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }
        try{
            btSocket.close();
        } catch (IOException e2){
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }
}
