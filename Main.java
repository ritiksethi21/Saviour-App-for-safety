package com.example.driversafety;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    final String TAG = "Contact Tag";
    private  int PICK_CONTACT=1;
    SharedPreferences sharedPreferences;
    ArrayList<String>phoneToSMS;
    String str;
    private double lat, lon;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    String Latitude;
    String Longitude;

       /* super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (1) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    ContentResolver cr = getContentResolver();
                    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                            null, null, null);
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        // TODO Whatever you want to do with the selected contact name.
                        phoneToSMS[i++]=name;
                    }
                }
                break;
        }
    }*/
        /*ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    // Query phone here. Covered next
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneToSMS[i++] = phoneNumber;
                    }
                    phones.close();
                }
            }
        }
    }




       /* if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactData = data.getData();
            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                // TODO Whatever you want to do with the selected contact name.
                phoneToSMS[i++] = name;
            }
            //String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            //ContentResolver resolvr = getContentResolver();
            // Cursor cursor = resolvr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);

            // If the cursor returned is valid, get the phone number
            // if (cursor != null && cursor.moveToFirst()) {
            //int numberIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.NORMALIZED_NUMBER);
            //String number = cursor.getString(numberIndex);
            // Do something with the phone number*/


                    final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
                    CardView a, b, c, d;
                    ImageView p,q;
                    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        sharedPreferences=this.getSharedPreferences("com.example.driversafety",MODE_PRIVATE);
        phoneToSMS=new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
            }


        a=findViewById(R.id.cv1);
        b=findViewById(R.id.cv2);
        c=findViewById(R.id.cv3);
        p=findViewById(R.id.music);
        button=findViewById(R.id.button);


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),HospitalData.class);
                startActivity(intent);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AccidentData.class);
                startActivity(intent);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),DrivingInstruction.class);
                startActivity(intent);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Entertainment.class);
                startActivity(intent);
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, "https://api.thingspeak.com/channels/1238071/fields/2.json?api_key=CEFR1CTC5R23FC4D&results=2",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(HomeActivity.this, "onResponse", Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray = response.getJSONArray("feeds");
                    JSONObject jsonObject = jsonArray.getJSONObject(1);
                    Longitude = jsonObject.getString("field2");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        requestQueue.add(jsonObjectRequest1);

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, "https://api.thingspeak.com/channels/1238071/fields/1.json?api_key=CEFR1CTC5R23FC4D&results=2",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("feeds");
                    JSONObject jsonObject = jsonArray.getJSONObject(1);
                    Latitude = jsonObject.getString("field1");
                    Log.d(TAG, "Latitude: " + Latitude + " Longitude: " + Longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest2);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.contacts:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 3);
                return true;
            default:
                return false;
        }
    }

    int i=0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactsData = data.getData();
                CursorLoader loader = new CursorLoader(this, contactsData, null, null, null, null);
                Cursor c = loader.loadInBackground();
                if (c.moveToFirst()) {
                    String phone_number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneToSMS.add(phone_number);
                    try {

                            sharedPreferences.edit().putString("phoneToSMS",ObjectSerializer.serialize(phoneToSMS)).apply();

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public void clicked(View v)
    {
        String phone = "9053191376";
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getApplicationContext().startActivity(callIntent);
        Timer timerSMS = new Timer();
        TimerTask tSMS = new TimerTask() {
            @Override
            public void run() {
                SmsManager sms = SmsManager.getDefault();
                ArrayList<String>temp=new ArrayList<>();
                try {
                    temp=(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("phoneToSMS",ObjectSerializer.serialize(new ArrayList<String>())));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                String url ="https://www.google.com/maps/search/Hospitals/"+"@"+Latitude+ "," +Longitude;
                for (int j = 0; j <temp.size(); j++)
                {
                    str=temp.get(j);

                    sms.sendTextMessage(str, null, url, null, null);
                }

            }
        };
        timerSMS.schedule(tSMS,1);
        
    }
}
