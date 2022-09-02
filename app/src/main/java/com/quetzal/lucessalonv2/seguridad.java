package com.quetzal.lucessalonv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Diego on 26/11/2015.
 */
public class seguridad extends Activity{

    private static final String TAG = "Bluetooth";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "98:D3:31:70:3F:CC";

    private ImageButton salir, cercaOn, cercaOff;
    private Button smAct, smDes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seguridad);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        EstadoBT();

        salir = (ImageButton) findViewById(R.id.salir);
        smAct   = (Button) findViewById(R.id.encender);
        smDes = (Button) findViewById(R.id.off);
        cercaOn = (ImageButton) findViewById(R.id.cercaon);
        cercaOff = (ImageButton) findViewById(R.id.cercaoff);

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("S");
            }
        });
        smAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("D");
            }
        });
        smDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarDato("d");
            }
        });
        cercaOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmarEncendido();
            }
        });
        cercaOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmarApagado();
            }
        });
    }

    private void ConfirmarEncendido(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage("¿Seguro que desea encender la cerca electrica?");
        dialogo.setTitle("Cerca electrica");
        dialogo.setIcon(android.R.drawable.ic_dialog_alert);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EnviarDato("E");
                Toast.makeText(getBaseContext(), "Activado", Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.show();
    }

    private void ConfirmarApagado(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage("¿Seguro que desea apagar la cerca electrica?");
        dialogo.setTitle("Cerca electrica");
        dialogo.setIcon(android.R.drawable.ic_dialog_alert);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EnviarDato("e");
                Toast.makeText(getBaseContext(), "Desactivado", Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        dialogo.show();
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
