package com.mxian.smartcity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class ReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String lati,longi;
    TextView Lati,Longi;

    String finalResult ;
    String HttpURL = "https://smartcitypsg.000webhostapp.com/smartcity/ReportComplaint.php";
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();


    @BindView(R.id.rlPickLocation)
    RelativeLayout rlPick;

    String usernamepasser,selection,address,pcategory,ptype,pdesc;
    Button pic,report;
    ImageView imageview;
    EditText descrip;
    ArrayList<String> categories = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        pic=(Button)findViewById(R.id.takepicture);
        imageview=(ImageView)findViewById(R.id.imageview);
        rlPick=findViewById(R.id.rlPickLocation);
        Lati=(TextView)findViewById(R.id.lati);
        Longi=(TextView)findViewById(R.id.longi);
        report=(Button)findViewById(R.id.btnreport);
        descrip=(EditText)findViewById(R.id.editdescribe);


        Intent intent = getIntent();

        Bundle bundle = getIntent().getExtras();


        if (intent.hasExtra("lati" )) {
            if(!report.isEnabled())
                report.setEnabled(true);

            lati = bundle.getString("lati");
            longi = bundle.getString("longi");
            address = bundle.getString("address");
            Lati.setText("Latitude: "+lati);
            Longi.setText("Longitude: "+longi);

        } else {
            // Do something else
        }


        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        usernamepasser=pref.getString("username", null);
        selection=pref.getString("category",null);

        if(selection.equalsIgnoreCase("water")) {
            categories.add("Contamination");
            categories.add("Leakage");
            categories.add("Lack of water");
            ptype="Water";

        }
        else if(selection.equalsIgnoreCase("sanitory")) {

            categories.add("Garbage");
            categories.add("Sewage");
            ptype="Sanitory";

        }
        else if(selection.equalsIgnoreCase("electricity")) {
            categories.add("Power Failure");
            categories.add("Powerline cut");
            categories.add("Transformer issue");
            ptype="Electricity";

        }
        else if(selection.equalsIgnoreCase("road")) {

            categories.add("Pot holes");
            categories.add("Accident");
            ptype="Road";

        }


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
       // List<String> categories = new ArrayList<String>();

        //googleSignInClient= extras.getString(GOOGLE_ACCOUNT);
    //    categories = (ArrayList<String>) getIntent().getSerializableExtra("mylist");
        /*
        categories.add("Item 1");
        categories.add("Item 2");
        categories.add("Item 3");
        categories.add("Item 4");
        categories.add("Item 5");
        categories.add("Item 6");
    */


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        pic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });

        rlPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent i=new Intent(getApplicationContext(),MyLocationUsingLocationAPI.class);
                 startActivity(i);
                }
            });


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                pdesc = descrip.getText().toString();


                Scoreupdate(usernamepasser, ptype, pcategory, pdesc, lati, longi, address);
            }
        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(photo);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        pcategory = item;
        // Showing selected spinner item
    //    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {

        categories.clear();
        if(categories.isEmpty()) {

            //Intent i=new Intent(getApplicationContext(),SelectionActivity.class);
            //startActivity(i);


            finish();
        }
    }

    //DB Connectivity

    public void Scoreupdate(final String username,final String ptype,final String pcategory,final String pdesc,final String lati,final String longi,final String address){


        class UserLoginClass extends AsyncTask<String,Void,String> {

            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                  progressDialog = ProgressDialog.show(ReportActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();

                String temp= httpResponseMsg;

                Toast.makeText(ReportActivity.this,temp ,Toast.LENGTH_SHORT).show();

            }

            @Override
            protected String doInBackground(String... params) {


                hashMap.put("fromuser",params[0]);
                hashMap.put("problem_type",params[1]);
                hashMap.put("problem_category",params[2]);
                hashMap.put("descrip",params[3]);
                hashMap.put("latitude",params[4]);
                hashMap.put("longitude",params[5]);
                hashMap.put("address",params[6]);

                // hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(username, ptype,pcategory,pdesc,lati,longi,address );
    }

}
