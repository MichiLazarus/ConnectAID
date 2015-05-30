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

import model.Patient;
import myhttp.MyHttpClient;

/**
 * Created by Benedikt on 16.05.2015.
 */
public class PatientActivity extends Activity {

    protected static String scanResult;
    protected static String phpfile = "https://81.217.54.146/showPatient.php";
    private boolean insert = false;
    private TabHost mytabhost;
    EditText vorname;
    EditText nachname;
    EditText svnr;
    EditText gebdatum;
    EditText krankenhaus;
    EditText transport;
    EditText prioritaet;
    EditText bewusstsein;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

         vorname = (EditText) findViewById(R.id.Vorname);
         nachname = (EditText) findViewById(R.id.Nachname);
         svnr = (EditText) findViewById(R.id.SVNR);
         gebdatum = (EditText) findViewById(R.id.Gebdatum);
         krankenhaus = (EditText) findViewById(R.id.Krankenhaus);
         transport = (EditText) findViewById(R.id.transport);
         prioritaet = (EditText) findViewById(R.id.prioritaet);
         bewusstsein = (EditText) findViewById(R.id.bewusstsein);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", scanResult));

        mytabhost = (TabHost) findViewById(R.id.tabHost);
        mytabhost.setup();

        TabHost.TabSpec spec = mytabhost.newTabSpec("tab_info");
        spec.setIndicator("Info");
        spec.setContent(R.id.tab1);
        mytabhost.addTab(spec);
        mytabhost.addTab(mytabhost.newTabSpec("tab_behandlung").setIndicator("Behandlung").setContent(R.id.tab2));

        try {
            String result = new PatientData(PatientActivity.this).execute(nameValuePairs).get();

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
            }else{

                this.insert = true;

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public void onClick(View view){



        if(insert){
            String result="";
            phpfile = "https://81.217.54.146/insertPatient.php";
            try {
                result = new PatientData(PatientActivity.this).execute(getValueList()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if("success".equals(result)){
                insert = false;
                Toast.makeText(this,
                        "Daten wurden gespeichert", Toast.LENGTH_LONG)
                        .show();
            }

        }else{
            String result="";
            phpfile = "https://81.217.54.146/updatePatient.php";
            try {
                result = new PatientData(PatientActivity.this).execute(getValueList()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if("success".equals(result)){

                Toast.makeText(this,
                        "Daten wurden gespeichert", Toast.LENGTH_LONG)
                        .show();

            }
        }
    }


    public List<NameValuePair> getValueList(){
        Patient p1 = new Patient(scanResult,this.vorname.getText().toString(),this.nachname.getText().toString(),this.svnr.getText().toString(),this.gebdatum.getText().toString(),
        this.krankenhaus.getText().toString(),this.transport.getText().toString(),this.prioritaet.getText().toString(),this.bewusstsein.getText().toString());
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", scanResult));
        nameValuePairs.add(new BasicNameValuePair("vorname", p1.getVorname()));
        nameValuePairs.add(new BasicNameValuePair("nachname", p1.getNachname()));
        nameValuePairs.add(new BasicNameValuePair("svnr", p1.getSvnr()));
        nameValuePairs.add(new BasicNameValuePair("gebdatum", p1.getGebdatum()));
        nameValuePairs.add(new BasicNameValuePair("krankenhaus", p1.getKrankenhaus()));
        nameValuePairs.add(new BasicNameValuePair("transport", p1.getTransport()));
        nameValuePairs.add(new BasicNameValuePair("prioritaet", p1.getPrioritaet()));
        nameValuePairs.add(new BasicNameValuePair("bewusstsein", p1.getBewusstsein()));
        return nameValuePairs;
    }


    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }
}




class PatientData extends AsyncTask<List,String,String> {

    private Context context;

    public PatientData(PatientActivity context){
     this.context = context;

    }

    @Override
    protected String doInBackground(List... params) {

        String result="";
        String ergebnis ="";
        List<NameValuePair> nameValuePairs = params[0];

        InputStream is = null;


        //http post
        try {
            HttpClient httpclient = new MyHttpClient(this.context);
            System.out.println(PatientActivity.phpfile);
            System.out.println(nameValuePairs.toString());
            HttpPost httppost = new HttpPost(PatientActivity.phpfile);
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
        PatientActivity.phpfile="https://81.217.54.146/showPatient.php";
        return result;
    }


}