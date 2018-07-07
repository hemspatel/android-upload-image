package com.patelitsolutions.uploadimage;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
        private static MySingleton mInstance;
        private RequestQueue mrequestQueue;
        private static Context mcontext;


        private  MySingleton(Context context){
            mcontext = context;
            mrequestQueue = getrequestqueue();
        }

        private RequestQueue getrequestqueue(){
            if(mrequestQueue == null){
                mrequestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());
            }

            return mrequestQueue;
        }


  public static synchronized MySingleton getmInstance(Context context){
            if(mInstance == null){
                mInstance = new MySingleton(context);
            }
            return mInstance;
  }

  public <T> void addToRequestQueue(Request<T> request){
        getrequestqueue().add(request);
  }





}
