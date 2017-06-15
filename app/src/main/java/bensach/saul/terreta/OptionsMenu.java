package bensach.saul.terreta;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OptionsMenu extends AppCompatActivity {

    private ArrayList<Member> members;
    private MemberAdapter memberAdapter;
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;

    private static final String KEY = "57238004e784498bbc2f8bf984565090";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        members = new ArrayList<>();
        memberAdapter = new MemberAdapter(this, members);
        setContentView(R.layout.activity_options_menu);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(memberAdapter);
        new ApiConnector().execute("http://88.14.119.64:8000/getMembers");
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new ApiConnector().execute("http://88.14.119.64:8000/getMembers");
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_member:
                Intent i = new Intent(this, AddMember.class);
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private class ApiConnector extends AsyncTask<String, Integer, String>{
        public String data;

        public ApiConnector(){
            data = "";
            members.clear();
            memberAdapter.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL("http://88.14.119.64:8000/getMembers");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    data = readStream(in);
                } catch (Exception e){
                    e.printStackTrace();
                }finally {
                    urlConnection.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("My app", "no se puede leer nada");
            }
            return data;
        }

        private String readStream(InputStream stream){
            StringBuilder stringBuilder = new StringBuilder();
            int c = 0;
            try {
                while ((c = stream.read()) != -1){
                    stringBuilder.append((char) c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            Security security = new Security();

            serializeJSON(security.decrypt(result, KEY));
        }

        private void serializeJSON(String data){
            try {
                JSONArray jsonArray = new JSONArray(data);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i).getJSONObject("member");
                    members.add(new Member(
                            object.getString("nif"),
                            object.getString("name"),
                            object.getString("surname"),
                            object.getString("email"),
                            object.getString("phone")
                    ));
                    JSONArray array = jsonArray.getJSONObject(i).getJSONArray("payments");
                    for(int j = 0; j < array.length(); j++){
                        object = array.getJSONObject(j);
                        members.get(i).addPayment(new Payments(object.getInt("price"),object.getString("date")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            memberAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }

    }

}
