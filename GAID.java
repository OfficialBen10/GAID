package com.ben10.GAID;

import android.content.Context;
import android.os.AsyncTask;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.ComponentCategory;

import com.google.android.gms.ads.identifier.*;
import com.google.android.gms.common.*;

import java.io.IOException;

@DesignerComponent(version = 1,  description = "An Extension to get user's GAID(Google Advertising ID), Developed by Ben 10",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,   iconName = "https://res.cloudinary.com/dgyoyowu5/image/upload/v1609050256/logo_firebase_icon_1_o1pzsy.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "ads-identifier.jar, gms-basement.jar")


public class GAID extends AndroidNonvisibleComponent {
    private Context context;
    private ComponentContainer container;

    /**
     * @param container container, component will be placed in
     */
    public GAID(ComponentContainer container) {
        super(container.$form());
        this.container = container;
        context = container.$context();
    }

    @SimpleFunction(description = "use this function to get user's GAID(Google Advertisement ID), this fuction will return GAID as a string argument in GotGAID Event if there is no error.")
    public void GetGAID(){
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());
                }
                catch (GooglePlayServicesNotAvailableException e) {
                    Error(e.getLocalizedMessage());
                } catch (GooglePlayServicesRepairableException e) {
                    Error(e.getLocalizedMessage());
                } catch (IOException e) {
                    Error(e.getLocalizedMessage());
                }
                String advertId = null;
                try{
                    advertId = idInfo.getId();
                }
                catch (NullPointerException e){
                    Error(e.getMessage());
                }
                return advertId;
            }
            @Override
            protected void onPostExecute(String advertId) {
                GotGAID(advertId);
            }
        };
        task.execute();
    }


    @SimpleEvent(description = "Fail to get user's GAID.")
    public void Error(String error){
        EventDispatcher.dispatchEvent(this, "Error", error);
    }

    @SimpleEvent(description = "Got GAID of the user.")
    public void GotGAID(String GAID){
        EventDispatcher.dispatchEvent(this, "GotGAID", GAID);
    }

}