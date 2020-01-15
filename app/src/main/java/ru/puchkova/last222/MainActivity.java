package ru.puchkova.last222;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private EditText login;
    private EditText pass;
    private Button registration;
    private Button log;
    private CheckBox hoard;

    private static final String EMPTY = "";
    private String inputLog;
    private String inputPass;

    private String currentLogin;
    private String currentPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        login = findViewById(R.id.login);
        pass = findViewById(R.id.pass);
        registration = findViewById(R.id.registration);
        log = findViewById(R.id.log);
        hoard = findViewById(R.id.hoard);

        hoard.setOnCheckedChangeListener(hoardOnCheckedChangeListener);

        SharedPreferences check = getPreferences(MODE_PRIVATE);
        hoard.setChecked(check.getBoolean("checked", true));


        if (hoard.isChecked()) {
            log.setOnClickListener(logOnclickListener);
            registration.setOnClickListener(registrationOnClickListener);
        } else {
            log.setOnClickListener(logExOnClickListener);
            registration.setOnClickListener(registrationExOnClickListener);
        }
    }

    View.OnClickListener logOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(checkEmpty()){
                FileInputStream fileInputStreamLog = null;
                FileInputStream fileInputStreamPass = null;
                try {
                    fileInputStreamLog = openFileInput("login_file");
                    fileInputStreamPass = openFileInput("pass_file");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                InputStreamReader inputStreamReaderLog = new InputStreamReader(fileInputStreamLog);
                InputStreamReader inputStreamReaderPass = new InputStreamReader(fileInputStreamPass);

                BufferedReader readerLog = new BufferedReader(inputStreamReaderLog);
                BufferedReader readerPass = new BufferedReader(inputStreamReaderPass);
                try {
                    currentLogin = readerLog.readLine();
                    currentPass = readerPass.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                inputLog = login.getText().toString();
                inputPass = pass.getText().toString();
                if(inputLog.equals(currentLogin) && inputPass.equals(currentPass)){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Удачный вход.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Некорректный ввод.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    };

    View.OnClickListener registrationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(checkEmpty()){
                FileOutputStream fileOutputStreamLog = null;
                FileOutputStream fileOutputStreamPass = null;
                try {
                    fileOutputStreamLog = openFileOutput("login_file", MODE_PRIVATE);
                    fileOutputStreamPass = openFileOutput("pass_file", MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                OutputStreamWriter outputStreamWriterLog = new OutputStreamWriter(fileOutputStreamLog);
                OutputStreamWriter outputStreamWriterPass = new OutputStreamWriter(fileOutputStreamPass);

                BufferedWriter bwl = new BufferedWriter(outputStreamWriterLog);
                BufferedWriter bwp = new BufferedWriter(outputStreamWriterPass);

                try {
                    bwl.write(inputLog);
                    bwl.close();
                    bwp.write(inputPass);
                    bwp.close();
                    inputPass = null;
                    inputLog = null;
                    login.setText(EMPTY);
                    pass.setText(EMPTY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private boolean checkEmpty(){
        inputLog = login.getText().toString();
        inputPass = pass.getText().toString();

        if (inputLog.equals(EMPTY) || inputPass.equals(EMPTY)){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Введены не все данные.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else {

            return true;
        }
    }

    CompoundButton.OnCheckedChangeListener hoardOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences check = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = check.edit();
            editor.putBoolean("checked", isChecked);
        }
    };

    View.OnClickListener logExOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener registrationExOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            inputLog = login.getText().toString();
            inputPass = pass.getText().toString();
            File file = new File("log_info");
            try {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.write(inputLog + "\n" + inputPass);
                myOutWriter.close();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
