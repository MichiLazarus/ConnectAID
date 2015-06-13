package universityofvienna.connectaid;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import qrreader.IntentIntegrator;
import qrreader.IntentResult;
import session.SessionManager;



public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private TextView status;
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnScan;
    private ListView listPatient;
    private TextView noEntries;
    private SessionManager session;
    private ArrayList<String> einsatzIDs = new ArrayList<String>();
    private ArrayList<String> einsaetze = new ArrayList<String>();
    private ArrayList<String> patienten = new ArrayList<String>();
    private ArrayList<String> patientenIDs = new ArrayList<String>();
    private ArrayAdapter<String> patientAdapter;
    private Spinner einsatzSelect;
    private String einsatzID = null;
    protected static String helferID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = (TextView) findViewById(R.id.status);
        noEntries = (TextView) findViewById(R.id.noEntry);
        fillList();
        runThread();
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnScan = (Button) findViewById(R.id.btnScan);
        // session manager
        session = new SessionManager(getApplicationContext());

       /* if (!session.isLoggedIn()) {
            logoutUser();
        }*/



        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    public void fillList(){
        einsatzIDs.clear();
        einsaetze.clear();
        einsatzSelect = (Spinner) findViewById(R.id.einsaetze);
        PatientActivity.phpfile = "https://81.217.54.146/showAktEinsaetze.php";
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("helferId", helferID));

        try {
            String result = new PatientData(MainActivity.this).execute(nameValuePairs).get();
            System.out.println(result);
            if (!result.equals("failed")) {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    einsatzIDs.add(json_data.getString("id"));
                    einsaetze.add(json_data.getString("strasse") + " " + json_data.getString("strasseNr") + " \n " + json_data.getString("plz") + " " + json_data.getString("land"));
                }
            }else{
                einsatzSelect.setVisibility(View.GONE);
                noEntries.setVisibility(View.VISIBLE);
            }
            }catch(InterruptedException e){
                e.printStackTrace();
            }catch(ExecutionException e){
                e.printStackTrace();
            }catch(JSONException e){
                e.printStackTrace();
            }


        einsatzSelect.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinnereinsatz, einsaetze);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        einsatzSelect.setAdapter(dataAdapter);
    }

    public void fillPatientList(){
        patientenIDs.clear();
        patienten.clear();
        PatientActivity.phpfile = "https://81.217.54.146/showPatientenEinsatz.php";
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("einsatzId", einsatzID));
        try {
            String result = new PatientData(MainActivity.this).execute(nameValuePairs).get();
            System.out.println(result);
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                patientenIDs.add(json_data.getString("id"));
                if(json_data.getString("svnr").equals("0")){
                    patienten.add("ID: "+json_data.getString("id")+"\n"+json_data.getString("vorname") + " " + json_data.getString("nachname"));
                }else {
                    patienten.add("ID: " + json_data.getString("id") + "\n" + json_data.getString("vorname") + " " + json_data.getString("nachname") + " SVNR: " + json_data.getString("svnr"));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        patientAdapter = new ArrayAdapter<String>(this,R.layout.patientlist,patienten);
        listPatient = (ListView) findViewById(R.id.listviewSelectPatient);
        listPatient.setAdapter(patientAdapter);
        listPatient.setEmptyView(findViewById(R.id.emptyPatient));
        listPatient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                PatientActivity.scanResult = patientenIDs.get(position);
                showPatient();
            }
        });
    }

    public void showPatient(){
        Intent nextScreen = new Intent(this,PatientActivity.class);
        this.startActivity(nextScreen);

    }

    public void onClickPatient(View view){
        if(einsatzID != null) {
            setContentView(R.layout.select_patient);
            fillPatientList();
        }else{
            Toast.makeText(this,
                    "Kein Einsatz ausgewählt", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void onClick(View view){
        if(einsatzID != null) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.initiateScan();
        }else{
            Toast.makeText(this,
                    "Kein Einsatz ausgewählt", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void onClickBack(View view){
        setContentView(R.layout.activity_main);
        fillList();
    }

    public void onClickState(View view) {
        String newStatus;
        if (einsatzID == null) {
            newStatus = "Einsatz beendet";
        } else {
            newStatus = "Zu Einsatzort";
        }

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        System.out.println("helferID: " + MainActivity.getHelferID());
        System.out.println("Status: " + newStatus);

        nameValuePairs.add(new BasicNameValuePair("helferId", MainActivity.getHelferID()));
        nameValuePairs.add(new BasicNameValuePair("status", newStatus));
        PatientActivity.setPhpfile("https://81.217.54.146/updateState.php");
        try {
            String result = new PatientData(MainActivity.this).execute(nameValuePairs).get();
            System.out.println("#######"+result);
            if (result.equals("success")) {
                Toast.makeText(this,
                        "Status wurde geändert", Toast.LENGTH_LONG)
                        .show();
                status.setText(newStatus);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String barcode;
            barcode = scanResult.getContents();
            PatientActivity.scanResult = barcode;
            Intent nextScreen = new Intent(this,PatientActivity.class);
            this.startActivity(nextScreen);
        }
        // else continue with any other code you need in the method

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity1.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String item = parent.getItemAtPosition(position).toString();
        einsatzID = einsatzIDs.get(position);
        PatientActivity.einsatzID = einsatzID;

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void runThread() {

        new Thread() {
            public void run() {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
                wl.acquire();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("helferId", helferID));
                String ergebnis = null;
                while (true) {

                    try {
                       String result = new CheckState(MainActivity.this).execute(nameValuePairs).get();
                        System.out.println(result);
                        if (!result.equals("failed")) {
                            JSONArray jArray = new JSONArray(result);
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json_data = jArray.getJSONObject(i);
                                //state.setText(json_data.getString("status"));
                                ergebnis = json_data.getString("status");
                            }
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    try {
                        final String state = ergebnis;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                              status.setText(state);
                            }
                        });
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static String getHelferID() {
        return helferID;
    }

    public static void setHelferID(String helferID) {
        MainActivity.helferID = helferID;
    }
}