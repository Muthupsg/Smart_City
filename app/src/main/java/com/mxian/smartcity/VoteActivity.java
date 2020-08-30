package com.mxian.smartcity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoteActivity extends AppCompatActivity implements LocationListener {

    String prob;
    ListView ProbListView;
    ProgressBar progressBar;
    TextView txttitle;

    String finalResult;
    ProgressDialog pDialog;
    String FinalJSonObject;

    String ParseResult;
    String VoteHolder;
    int votenum;

    JSONObject jsonObject;
    JSONArray jsonArray = null;

    HashMap<String, String> ResultHash = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    int selected;

    //    String HttpURL = "https://smartcitypsg.000webhostapp.com/smartcity/Admin/AllProblemData.php";
    String HttpURL = "https://smartcitypsg.000webhostapp.com/smartcity/Admin/ListProblemsUser.php";
    String HttpURLVote = "https://smartcitypsg.000webhostapp.com/smartcity/Admin/UpdateVote.php";

    //String HttpURL = "https://smartcitypsg.000webhostapp.com/smartcity/Admin/VoteProblems.php";


    List<String> IdList = new ArrayList<>();
    HashMap<String,String> hashMap = new HashMap<>();
    ProgressDialog progressDialog;


    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        //      final ListView lv = (ListView) findViewById(R.id.lv);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ProbListView = (ListView) findViewById(R.id.listview1);
        txttitle = (TextView) findViewById(R.id.txtprob);


        Intent i = getIntent();
        prob = i.getStringExtra("probtype");

//        Toast.makeText(getApplicationContext(),prob,Toast.LENGTH_SHORT).show();

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.popup_layout, null);
        AlertDialog alertDialog = new AlertDialog.Builder(
                VoteActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Instructions");
        // Setting Dialog Message
        alertDialog.setView(alertLayout);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.smartcity);



        // Showing Alert Message
        alertDialog.show();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        if(prob != null)
        {
            txttitle.setText("Vote Received "+prob+" complaints");
        }

        // Initializing a new String Array
        // DataBind ListView with items from ArrayAdapter
        //      lv.setAdapter(arrayAdapter);

        //       btn.setOnClickListener(new View.OnClickListener() {
        //           @Override
        //           public void onClick(View v) {
        // Add new Items to List

        //            prob_list.add("Loquat");
        //              prob_list.add("Pear");
                /*
                    notifyDataSetChanged ()
                        Notifies the attached observers that the underlying
                        data has been changed and any View reflecting the
                        data set should refresh itself.
                 */
//                arrayAdapter.notifyDataSetChanged();


        HttpWebCall("water");
        // new GetHttpResponse(ListProblemsActivity.this).execute();

        //Adding ListView Item click Listener.
        ProbListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                // TODO Auto-generated method stub

           //     Intent intent = new Intent(ListProblemsActivity.this,ComplaintDetails.class);

                Log.d("CLICKED:",IdList.get(position).toString());
                selected = Integer.parseInt(IdList.get(position).toString());


                // Sending ListView clicked value using intent.
        //        intent.putExtra("ListViewValue", IdList.get(position).toString());
         //       intent.putExtra("id",IdList.get(position).toString());

         //       startActivity(intent);

                //Finishing current activity after open next activity.
           //     finish();

                final Dialog dialog = new Dialog(VoteActivity.this);
                dialog.setContentView(R.layout.vote);
                dialog.setTitle("Vote");

                Button btnVote=(Button)dialog.findViewById(R.id.buttonVote);
                dialog.show();

                // Set On ClickListener
                btnVote.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        try {

                            jsonObject = jsonArray.getJSONObject(selected);

                            Log.d("CLICKED JSON OBJECT::", String.valueOf(jsonObject));



                            VoteHolder = jsonObject.getString("vote_count").toString();
                            Log.d("VOTECOUNT:",VoteHolder);
                            votenum = Integer.parseInt(VoteHolder);
                            votenum++;
                            VoteHolder = String.valueOf(votenum);
                            Log.d("VOTECOUNT After:",VoteHolder);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Adding Student Id TO IdList Array.


                        // Sending Student Name, Phone Number, Class to method to update on server.
                        StudentRecordUpdate(String.valueOf(selected),VoteHolder);



                        dialog.dismiss();
                    }
                });
            }


        });
    }

    // Method to Update Student Record.
    public void StudentRecordUpdate(final String ID, final String S_Bal){

        class StudentRecordUpdateClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(VoteActivity.this,"Voting",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(VoteActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("id",params[0]);

                hashMap.put("vote",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURLVote);

                return finalResult;
            }
        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();

        studentRecordUpdateClass.execute(ID,S_Bal);
    }


    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(VoteActivity.this,"Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(VoteActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                progressBar.setVisibility(View.GONE);


                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg ;

                //   Toast.makeText(getApplicationContext(),FinalJSonObject,Toast.LENGTH_SHORT).show();

                if(!FinalJSonObject.equalsIgnoreCase("No Results Found.<br />")) {

                    //Parsing the Stored JSOn String to GetHttpResponse Method.
                    new GetHttpResponse(VoteActivity.this).execute();
                }
                else
                {
                    List<Student> studentList;

                    Student student = new Student();
                    studentList = new ArrayList<Student>();

                    student.StudentName = "No records";
                    studentList.add(student);

                    ListAdapterClass adapter = new ListAdapterClass(studentList, getApplicationContext());
                    ProbListView.setAdapter(adapter);

                    Toast.makeText(getApplicationContext(),"No Complaints Found!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("ptype",params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }



    // JSON parse class started from here.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        String JSonResult;

        List<Student> studentList;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            Log.d("JSON ONE:: ", "DOINSTART");


            // Passing HTTP URL to HttpServicesClass Class.
            //  HttpServicesClass httpServicesClass = new HttpServicesClass(HttpURL);
            try
            {
                //     httpServicesClass.ExecutePostRequest();

                //   if(httpServicesClass.getResponseCode() == 200)
                if(FinalJSonObject != null)
                {
                    //    JSonResult = httpServicesClass.getResponse();
                    JSonResult=FinalJSonObject;

                    Log.d("JSON ONE:: ", String.valueOf(JSonResult));


                    if(JSonResult != null)
                    {

                        try {
                            jsonArray = new JSONArray(JSonResult);


                            Student student;

                            studentList = new ArrayList<Student>();

                            Log.d("JSON:: ", String.valueOf(jsonArray.length()));


                            String displist="";
                            int z=0;

                            //    int[] intArr = new int[jsonArray.length()];

                            Student stud = new Student();
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                student = new Student();

                                jsonObject = jsonArray.getJSONObject(i);



                                String status= jsonObject.getString("status").toString();

                                if (status.equalsIgnoreCase("finished"))
                                {
                                    z = 1;
                                }
                                else {
                                    z = 0;

                                    // Adding Student Id TO IdList Array.
                                    IdList.add(jsonObject.getString("id").toString());

                                    displist = jsonObject.getString("id").toString() + " Type: " + jsonObject.getString("problem_type").toString();

                                    displist = displist + "\n Category: " + jsonObject.getString("problem_category").toString();

                                    displist = displist + "\nDescrip: " + jsonObject.getString("descrip").toString();

                                    displist = displist + "\n" + jsonObject.getString("address").toString();

                                    displist = displist + "\nVote Count: " + jsonObject.getString("vote_count").toString();

//                                VoteHolder = jsonObject.getString("vote_count").toString();
                                    //                              votenum = Integer.parseInt(VoteHolder);
                                    //Adding Student Name.
                                    //    student.StudentName = jsonObject.getString("fromuser").toString();

                                    student.StudentName = displist;
                                    student.i = z;
                                    //   studentList.add(z);
                                    studentList.add(student);

                                }

                            }

                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    // Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            progressBar.setVisibility(View.GONE);

            ProbListView.setVisibility(View.VISIBLE);

            ListAdapterClass adapter = new ListAdapterClass(studentList, context);

            ProbListView.setAdapter(adapter);

        }
    }





    //   });
    // }




}



