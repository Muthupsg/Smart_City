package com.mxian.smartcity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class StatusActivity extends AppCompatActivity {

    TextView pcategory,pstatus;
    String usernamepasser;

    JSONObject jsonObject;
    JSONArray jsonArray = null;


    String finalResult ;
    String HttpURL = "https://smartcitypsg.000webhostapp.com/smartcity/Admin/ListProblemsStatus.php";
   // String HttpURL = "https://smartcitypsg.000webhostapp.com/smartcity/Status.php";
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Bundle b = getIntent().getExtras();
        usernamepasser = b.getString("username");

        Log.d("USERNAME:::",usernamepasser);

        pcategory = (TextView)findViewById(R.id.statustxt1);
        pstatus = (TextView)findViewById(R.id.statustxt2);



        Statusupdate(usernamepasser);

    }


    public void Statusupdate(final String username){

        class StatusClass extends AsyncTask<String,Void,String> {

            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(StatusActivity.this, "Loading","Wait....", true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                String temp= httpResponseMsg;

                Log.d("STATUSSSS:: " , temp);

                int i=1;
                String s1= temp;
                String[] words=s1.split("\\s");//splits the string based on string


                //pcategory.append(temp);
                /*
                for(String w:words){
                    if(i==1)
                    {
                        pcategory.append(""+w);
                    }
                    else
                    {
                       // textResulttwo.setText(w);
                        pstatus.append(""+w);
                    }
                    ++i;


                }

                */


                try {

                    jsonArray = new JSONArray(temp);
                    Log.d("JSON:: ", String.valueOf(jsonArray.length()));


                    //    int[] intArr = new int[jsonArray.length()];

                    for(int j=0; j<jsonArray.length(); j++)
                    {

                        jsonObject = jsonArray.getJSONObject(j);

                            // Adding Student Id TO IdList Array.

                            pstatus.append("\n\nProblem Type: "+jsonObject.getString("problem_type").toString());
                            pstatus.append("\tStatus : "+jsonObject.getString("status").toString());

                        }

                }
                catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                if(httpResponseMsg != null && httpResponseMsg.equalsIgnoreCase("")){

                    Toast.makeText(StatusActivity.this,"Null"+httpResponseMsg, Toast.LENGTH_LONG).show();

                }
                else {
                    //finish();

                    //Intent intent = new Intent(ResultActivity.this, Start_Activity.class);
                    //textResultmath.setText(temp);

                    Toast.makeText(StatusActivity.this,"Complaint Status!",Toast.LENGTH_LONG).show();

                    //Bundle b = new Bundle();
                    //b.putString("username", UsernameHolder); // Logging username
                    //intent.putExtras(b);
                    //startActivity(intent);

                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("username",params[0]);

                // hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        StatusClass userClass = new StatusClass();

        userClass.execute(username);
    }




}
