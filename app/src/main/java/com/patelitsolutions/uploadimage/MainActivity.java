package com.patelitsolutions.uploadimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button pickimg, saveimg;
    private ImageView nimage;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmapsimg;
    private String apiUrl = "http://192.168.1.12/api/index.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickimg = (Button) findViewById(R.id.getimage);
        saveimg = (Button) findViewById(R.id.saveimage);
        nimage = (ImageView) findViewById(R.id.imageview);

        pickimg.setOnClickListener(this);
        saveimg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.getimage:
                selectimage();
                break;
            case R.id.saveimage:
                uploadimage();
                break;
        }
    }

    public void selectimage(){

        Intent imgintent = new Intent();
        imgintent.setType("image/*");
        imgintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imgintent,IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode  == IMG_REQUEST && resultCode==RESULT_OK &&  data != null){
            Uri path = data.getData();

            try {
                bitmapsimg = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
            nimage.setImageBitmap(bitmapsimg);
            nimage.setVisibility(View.VISIBLE);


            } catch (IOException e){

            }
        }

    }


    private void uploadimage(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject  = new JSONObject(response);

                            String Response = jsonObject.getString("response");
                            Toast.makeText(MainActivity.this, Response,Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } )
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("image",imageToString(bitmapsimg));
                return params;
            }
        };

        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes, Base64.DEFAULT);

    }
}
