package universityofvienna.connectaid;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import myhttp.MyHttpClient;

/**
 * Created by Benedikt on 16.05.2015.
 */
public class PatientActivity extends Activity {

    protected static String scanResult;
    private TabHost mytabhost;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        EditText vorname = (EditText) findViewById(R.id.Vorname);
        EditText nachname = (EditText) findViewById(R.id.Nachname);
        EditText svnr = (EditText) findViewById(R.id.SVNR);
        EditText gebdatum = (EditText) findViewById(R.id.Gebdatum);
        EditText krankenhaus = (EditText) findViewById(R.id.Krankenhaus);
        EditText transport = (EditText) findViewById(R.id.transport);
        EditText prioritaet = (EditText) findViewById(R.id.prioritaet);
        EditText bewusstsein = (EditText) findViewById(R.id.bewusstsein);

        mytabhost = (TabHost) findViewById(R.id.tabHost);
        mytabhost.setup();

        TabHost.TabSpec spec = mytabhost.newTabSpec("tab_info");
        spec.setIndicator("Info");
        spec.setContent(R.id.tab1);
        mytabhost.addTab(spec);
        mytabhost.addTab(mytabhost.newTabSpec("tab_behandlung").setIndicator("Behandlung").setContent(R.id.tab2));

        try {
            String result = new PatientData(PatientActivity.this).execute(scanResult).get();

            if(!result.equals("failed")) {
                try {

                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        vorname.setText(json_data.getString("vorname"));
                        nachname.setText(json_data.getString("nachname"));
                        svnr.setText(json_data.getString("svnr"));
                        gebdatum.setText(json_data.getString("geburtsdatum"));
                        krankenhaus.setText(json_data.getString("krankenhaus"));
                        transport.setText(json_data.getString("transport"));
                        prioritaet.setText(json_data.getString("prioritaetBehandlung"));
                        bewusstsein.setText(json_data.getString("bewusstsein"));
                    }

                } catch (JSONException e) {
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }
}




class PatientData extends AsyncTask<String,String,String> {

    private Context context;

    public PatientData(PatientActivity context){
     this.context = context;

    }

    @Override
    protected String doInBackground(String... params) {

        String result="";
        String ergebnis ="";
        String id = params[0];

        InputStream is = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", id));

        //http post
        try {
            HttpClient httpclient = new MyHttpClient(this.context);
            HttpPost httppost = new HttpPost("https://81.217.54.146/showPatient.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();

            System.out.println(result);
            result = result.replaceAll("\\n","");
            result = result.replaceAll(" ","");
            if("failed".equals(result)){
                return "failed";
            }

        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        return result;
    }


}