package universityofvienna.connectaid;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import qrreader.IntentIntegrator;
import qrreader.IntentResult;
import session.SessionManager;


public class MainActivity extends ActionBarActivity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnScan;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    public void onClick(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();

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
}