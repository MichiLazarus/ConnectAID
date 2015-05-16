package universityofvienna.connectaid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import myhttp.MyHttpClient;

import static android.support.v4.app.ActivityCompat.startActivity;


public class LoginActivity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText usernameField = (EditText) findViewById((R.id.userid));
        final EditText passwordField = (EditText) findViewById((R.id.password));

        final Button button = (Button) findViewById(R.id.btn_Login);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String username = usernameField.getText().toString();
                final String password = passwordField.getText().toString();

                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    new Connect(LoginActivity1.this,(TextView) findViewById(R.id.textView)).execute(username, password);

                    // Check for empty ID field
                } else if (username.trim().length() == 0 && password.trim().length() > 0) {
                    // Prompt user to enter his ID
                    Toast.makeText(getApplicationContext(),
                            "Bitte ID eingeben.", Toast.LENGTH_LONG)
                            .show();

                    // Check for empty Password field
                } else if (username.trim().length() > 0 && password.trim().length() == 0) {
                    // Prompt user to enter his Password
                    Toast.makeText(getApplicationContext(),
                            "Bitte Passwort eingeben.", Toast.LENGTH_LONG)
                            .show();
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Bitte ID und Passwort eingeben.", Toast.LENGTH_LONG)
                            .show();
                }


            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_activity1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}

 class Connect extends AsyncTask<String,String,String>{

     private TextView success;
     private Context context;


     public Connect(LoginActivity1 context,TextView field){

         this.context = context;
         success = field;
     }




     @Override
     protected String doInBackground(String... params) {

         String result="";
         String ergebnis ="";
         String kennung = params[0];
         String passwort = params[1];
         InputStream is = null;
         List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
         nameValuePairs.add(new BasicNameValuePair("kennung", kennung));
         nameValuePairs.add(new BasicNameValuePair("passwort", passwort));

         //http post
         try {
             HttpClient httpclient = new MyHttpClient(context);
             HttpPost httppost = new HttpPost("https://81.217.54.146/test.php");
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
                 return "Login fehlgeschlagen";
             }

         } catch (Exception e) {
             Log.e("log_tag", "Error converting result " + e.toString());
         }

         //parse json data
         try {

             JSONArray jArray = new JSONArray(result);
             for (int i = 0; i < jArray.length(); i++) {
                 JSONObject json_data = jArray.getJSONObject(i);
                 Log.i("log_tag", "kennung: " + json_data.getInt("kennung"));
                 ergebnis = json_data.getString("nachname");

             }

         } catch (JSONException e) {
             Log.e("log_tag", "Error parsing data " + e.toString());
         }


         return ergebnis;
     }

     @Override
     protected void onPostExecute(String result){
         if(!result.equals("Login fehlgeschlagen") ){
             Intent nextScreen = new Intent(context,MainActivity.class);
             context.startActivity(nextScreen);
         }else {
             Toast.makeText(context,
                     result, Toast.LENGTH_LONG)
                     .show();
             this.success.setText(result);
         }
     }


 }