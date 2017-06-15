package bensach.saul.terreta;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import static android.R.attr.data;

public class AddMember extends AppCompatActivity {

    private EditText name, surname, nif, phone, email;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        name = (EditText)findViewById(R.id.add_name);
        surname = (EditText)findViewById(R.id.add_surname);
        nif = (EditText)findViewById(R.id.add_nif);
        phone = (EditText)findViewById(R.id.add_phone);
        email = (EditText)findViewById(R.id.add_email);
        button = (Button)findViewById(R.id.add_member_button);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        myToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member member = new Member(
                        nif.getText().toString(),
                        name.getText().toString(),
                        surname.getText().toString(),
                        email.getText().toString(),
                        phone.getText().toString()
                );
                new Sender().execute(member);
            }
        });

    }
    private class Sender extends AsyncTask<Member, String, String>{

        @Override
        protected String doInBackground(Member... member) {
            try {
                BufferedReader reader = null;
                URL urlObj = new URL("http://88.14.119.64:8000/addMember");
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                Security security = new Security();
                String aa = security.encrypt(member[0].toJSON(),"57238004e784498bbc2f8bf984565090");
                String payload = "&" + URLEncoder.encode("data", "UTF-8") + "="
                        + URLEncoder.encode(aa, "UTF-8");
                wr.write(payload);
                wr.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                Log.w("ss",sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }

    }

}
