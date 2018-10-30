package com.enovatesoft.paperless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class QRCapture extends AppCompatActivity {

    private Button GetImageFromGalleryButton, UploadImageOnServerButton;

    private ImageView ShowSelectedImage;

    private EditText rim, sid;


    private Bitmap FixBitmap;

    private Uri picUri;

    private String ImageTag = "image_tag" ;
    private String ImageData = "image_data" ;
    private String rimTag = "customer_rim";
    private String SettingsId = "settings_id";

    private ProgressDialog progressDialog ;

    private ByteArrayOutputStream byteArrayOutputStream ;

    private  byte[] byteArray ;

    private String ConvertImage ;

    private String GetCustomerRimFromEditText, GetSidFromEditText, GetImageNamePaperless;

    private HttpURLConnection httpURLConnection ;

    private  URL url;

    private  OutputStream outputStream;

    private  BufferedWriter bufferedWriter ;

    private int RC ;

    private  BufferedReader bufferedReader ;

    private  StringBuilder stringBuilder;

    private boolean check = true;

    private int CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcapture);
        GetImageFromGalleryButton = findViewById(R.id.buttonSelect);

        UploadImageOnServerButton = findViewById(R.id.buttonUpload);

        ShowSelectedImage = findViewById(R.id.imageView);

        rim = findViewById(R.id.customerRim);

        sid =  findViewById(R.id.settingId);

        Intent capture = getIntent();
        String rimPassed = capture.getExtras().getString("rim");
        String sidPassed = capture.getExtras().getString("sid");

        rim.setText(rimPassed);
        sid.setText(sidPassed);

        rim.setEnabled(false);
        sid.setEnabled(false);

        byteArrayOutputStream = new ByteArrayOutputStream();

        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(QRCapture.this, "Capturing Document. . . ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
            }
        });


        UploadImageOnServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetCustomerRimFromEditText = rim.getText().toString();

                GetSidFromEditText = sid.getText().toString();

                GetImageNamePaperless = GetCustomerRimFromEditText+"_"+GetSidFromEditText;

                UploadImageToServer();

            }
        });

        if (ContextCompat.checkSelfPermission(QRCapture.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == CAMERA && resultCode==RESULT_OK) {
            Uri cameraURI = data.getData();

            try {
                CropImage.activity(cameraURI)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
                FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cameraURI);
                ShowSelectedImage.setImageBitmap(FixBitmap);
                UploadImageOnServerButton.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(QRCapture.this, "Failed!", Toast.LENGTH_SHORT).show();
            }

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);

                    ShowSelectedImage.setImageBitmap(FixBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(QRCapture.this,""+error, Toast.LENGTH_LONG).show();
            }
        }

    }
    public void UploadImageToServer(){

        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(QRCapture.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                Toast.makeText(QRCapture.this,string1,Toast.LENGTH_SHORT).show();
                try {

                    onBackPressed();

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(),

                            Toast.LENGTH_LONG).show();



                }
            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageTag, GetImageNamePaperless);

                HashMapParams.put(rimTag, GetCustomerRimFromEditText);

                HashMapParams.put(SettingsId, GetSidFromEditText);

                HashMapParams.put(ImageData, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest("http://192.168.8.9/Android%20Upload%20Image/upload.php", HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }
    public void onBackPressed(){
        //Your Code hia
        super.onBackPressed();
    }


    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {

                Toast.makeText(QRCapture.this, "Unable to use Camera..Please Allow Conkev Paperless App to use your Camera", Toast.LENGTH_LONG).show();

            }
        }
    }

}
