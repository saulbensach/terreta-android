package bensach.saul.terreta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayMember extends AppCompatActivity {

    private TextView nombre, apellidos, nif, email, telefono;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_member);
        Member m = (Member) getIntent().getSerializableExtra("member");
        nombre      = (TextView) findViewById(R.id.nombre);
        apellidos   = (TextView) findViewById(R.id.apellidos);
        nif         = (TextView) findViewById(R.id.nif);
        email       = (TextView) findViewById(R.id.email);
        telefono    = (TextView) findViewById(R.id.telefono);
        listView    = (ListView) findViewById(R.id.list_pagos);
        nombre.setText(m.getName());
        apellidos.setText(m.getSurname());
        nif.setText(m.getNif());
        email.setText(m.getEmail());
        telefono.setText(m.getPhone());
    }
}
