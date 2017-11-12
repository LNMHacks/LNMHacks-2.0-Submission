package com.example.plotit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int RC_PICK_CSV = 1;
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 2;
    private static final String ANONYMOUS = "anonymous";
    private static final int RC_PERMISSION = 300;

    @BindView(R.id.upload_button)
    Button uploadBtn;

    @BindView(R.id.seek_process)
    SeekBar mProgressBar;

    @BindView(R.id.spinner_chooser_X)
    Spinner mSpinnerX;

    @BindView(R.id.spinner_chooser_Y)
    Spinner mSpinnerY;

    // Firebase Stuff
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mFirebaseStorgeReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    //Volley
    private RequestQueue mRequestQueue;

    // Local Variables
    private String mUsername;
    private String xValue;
    private String yValue;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFirebaseAuthListener != null)
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.GONE);
        getSupportActionBar().hide();
        mUsername = ANONYMOUS;
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRequestQueue = Volley.newRequestQueue(this);
        //mFirebaseDatabaseReference = mFirebaseDatabase.getReference().child("head"); /* TODO:// Update here! */
        mFirebaseStorgeReference = mFirebaseStorage.getReference().child("csvs");
        mFirebaseAuth = FirebaseAuth.getInstance();


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION);
        }

        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    //user is signed in
                    onSignedInInitialize(firebaseAuth.getCurrentUser().getDisplayName());
                } else {
                    // user is not signed in
                    Log.i(TAG, "User is not signed in");
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };


        /**
         * Below two method calls, get us the headers selected by the user!
         */
        mSpinnerX.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                xValue = adapterView.getItemAtPosition(i).toString();
                if (xValue==yValue)
                    showAlertDialog();
                Toast.makeText(MainActivity.this, "selectedItemis: " + xValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing for now
            }
        });

        mSpinnerY.setContentDescription("Select Y");
        mSpinnerY.setSelection(2);
        mSpinnerY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yValue = adapterView.getItemAtPosition(i).toString();
                if (yValue == xValue)
                    showAlertDialog();

                /**
                 * @params xValue, yValue These two are the selected index by the user, and is sent
                 * to the server via a POST request, Using Volley.
                 */
                makePostRequest(xValue, yValue);
                Toast.makeText(MainActivity.this, "selectedItemis: " + yValue, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing for now
            }
        });
    }

    private void showAlertDialog(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage(getString(R.string.sameXY_values_error))
                .setTitle(getString(R.string.same_XY_dialog_title));
        alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alert.create().show();
    }
    @OnClick(R.id.upload_button)
    void upload() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);
        csvFilePicker();
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }

    void csvFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        startActivityForResult(intent, RC_PICK_CSV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PICK_CSV && resultCode == RESULT_OK) {
            importCSV(new File(data.getData().getPath()), data);
        }
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, getString(R.string.logIn_sucess), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, getString(R.string.sign_in_err), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Please grant permission to continue!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.double_back_press), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

        LinearLayout layoutBefore = findViewById(R.id.layoutBefore);
        layoutBefore.setVisibility(View.VISIBLE);
        LinearLayout layoutAfter = findViewById(R.id.layoutAfter);
        layoutAfter.setVisibility(View.GONE);
    }

    private void importCSV(File path, Intent data) {
        List<String> headerList = null;
        Log.d(TAG, "path of the file is : " + path.toString());
        File csvFile = new File(data.getData().getPath());
        csvFile = new File(csvFile.getAbsolutePath());
        try {
            headerList = new ArrayList<>();
            BufferedReader csvFileRead = new BufferedReader(new FileReader(csvFile));
            String headerLine = csvFileRead.readLine();
            String[] headerArray = headerLine.split(",");
            headerList = Arrays.asList(headerArray);
            for (String item : headerList)
                Log.d(TAG, item);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinearLayout layoutBefore = findViewById(R.id.layoutBefore);
        layoutBefore.setVisibility(View.GONE);
        LinearLayout layoutAfter = findViewById(R.id.layoutAfter);
        layoutAfter.setVisibility(View.VISIBLE);
        setupspinner(headerList);

        Uri selectedCsvUri = data.getData();
        StorageReference csvReference = mFirebaseStorgeReference.child(selectedCsvUri.getLastPathSegment());
        csvReference.putFile(selectedCsvUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "File Sucessfully Uploaded", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Helper method for setting up spinners
     *
     * @param headers The contents of spinner.
     */
    private void setupspinner(List<String> headers) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, headers);
        mSpinnerX.setAdapter(spinnerAdapter);
        mSpinnerY.setAdapter(spinnerAdapter);
    }

    /**
     * Post method, Used to post to the server, via volley
     *
     * @param xValue The x coordinate for the graph selected by the user.
     * @param yValue The y coordinate for the graph selected by the user.
     */
    private void makePostRequest(final String xValue, final String yValue) {
        final String url = "http://localhost:8080";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "success!, file transfer to local host!");
                Log.d("Respone", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("xvalue", xValue);
                params.put("yvalue", yValue);
                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    /**
     * Helper method for getting the response back from the server, Server gives a json result
     * of image, which is inserted into the image view using Glide.
     */
    private void makeGetRequest() {
        final String url = "";

        // Prepare the Request.
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        String imageUrl = parseJson(response);
                        Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                        intent.putExtra(getString(R.string.image_url), imageUrl);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error Response", error.toString());
                    }
                });
        mRequestQueue.add(getRequest);
    }

    private String parseJson(JSONObject resultData){
        //TODO: To be implemented after the API's has been made
        String imageURl = null;
        return imageURl;
    }
}
