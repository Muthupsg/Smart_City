package com.mxian.smartcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    Button report,status;
    String usernamepasser;
    public static final String GOOGLE_ACCOUNT = "google_account";
    private GoogleSignInClient googleSignInClient;
    private TextView profileName, profileEmail;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private ImageView profileImage;
    private Button signOut,Vote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        report=(Button) findViewById(R.id.complainbtn);
        status=(Button) findViewById(R.id.statusbtn);
        signOut=(Button) findViewById(R.id.sign_out);
        Vote=(Button) findViewById(R.id.votebtn);

        profileName = findViewById(R.id.welcometxt);
        profileEmail = findViewById(R.id.profile_email);
        profileImage = findViewById(R.id.profile_image);
        signOut=findViewById(R.id.sign_out);


        report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Report clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SelectionActivity.class);

                Bundle c = new Bundle();
                c.putString("username", usernamepasser); // Logging username
                intent.putExtras(c);

                startActivity(intent);
                //finish();
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Status clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, StatusActivity.class);

                Bundle c = new Bundle();
                c.putString("username", usernamepasser); // Logging username
                intent.putExtras(c);

                startActivity(intent);
                //finish();

            }
        });


        Vote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Vote complaints around you!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, VoteActivity.class);

                Bundle c = new Bundle();
                c.putString("username", usernamepasser); // Logging username
                intent.putExtras(c);

                startActivity(intent);
                //finish();

            }
        });



        //Bundle extras = getIntent().getExtras();
        //googleSignInClient= extras.getString(GOOGLE_ACCOUNT);


        setDataOnView();
        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signOut.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()){

                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.clear();


                                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Session not close", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
          /*
          Sign-out is initiated by simply calling the googleSignInClient.signOut API. We add a
          listener which will be invoked once the sign out is the successful
           */
    }


    private void setDataOnView() {
        GoogleSignInAccount googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);
        Picasso.get().load(googleSignInAccount.getPhotoUrl()).centerInside().fit().into(profileImage);
        profileName.setText("Welcome "+googleSignInAccount.getDisplayName()+"!");
        profileEmail.setText(googleSignInAccount.getEmail());

        usernamepasser=googleSignInAccount.getEmail();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", usernamepasser);
        editor.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
