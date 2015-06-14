package universityofvienna.connectaid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import myhttp.MyHttpClient;

public class CheckState extends AsyncTask<List,String,String> {

    private Context context;

    public CheckState(Context context){
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
            //System.out.println(PatientActivity.phpfile);
            System.out.println(nameValuePairs.toString());

            HttpPost httppost = new HttpPost("https://81.217.54.146/getState.php");
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
            result = result.trim();
            if("failed".equals(result)){
                return "failed";
            }


        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        return result;
    }


}

