package com.ajguevara.netperformance;

import static android.location.LocationManager.GPS_PROVIDER;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;

import androidx.core.app.ActivityCompat;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        JSONObject r = new JSONObject();

        TextView txtView = (TextView) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(v -> {

            try {
                r.put("simOperatorName", getSimOperatorName());
                r.put("simOperator", getSimOperator());
                r.put("networkOperatorName", getNetworkOperatorName());
                r.put("networkOperator", getNetworkOperator());
                r.put("networkCountryIso", getNetworkCountryIso());
                r.put("deviceSoftwareVersion", getDeviceSoftwareVersion());
                r.put("phoneType", getPhoneType());
                r.put("isNetworkRoaming", isNetworkRoaming());
                r.put("simState", getSimState());
                r.put("networkType", getNetworkType());
                r.put("callState", getCallState());
                r.put("dataState", getDataState());
                r.put("groupIdLevel", getGroupIdLevel1());
                r.put("simCountryIso", getSimCountryIso());
                r.put("voiceMailAlphaTag", getVoiceMailAlphaTag());
                r.put("voiceMailNumber", getVoiceMailNumber());
                r.put("hasIccCard", hasIccCard());
                r.put("dataActivity", getDataActivity());
                r.put("signalQuality", getSignalQuality());
//                r.put("latitude", getLatitude());
//                r.put("longitude", getLongitude());
                r.put("location", getLocation());
                r.put("eNodeB", getENodeB());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            txtView.setText(r.toString());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------

    public String getPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getLine1Number();
    }

    public String getSimOperatorName() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperatorName();
    }

    public String getSimOperator() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperator();
    }

    public String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperatorName();
    }

    public String getNetworkOperator() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperator();
    }

    public String getNetworkCountryIso() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkCountryIso();
    }

    public String getDeviceSoftwareVersion() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getDeviceSoftwareVersion();
    }

    public String getPhoneType() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int phoneType = tm.getPhoneType();
        String returnValue = "";
        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                returnValue = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                returnValue = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                returnValue = "NONE";
                break;
            case (TelephonyManager.PHONE_TYPE_SIP):
                returnValue = "SIP";
                break;
        }
        return returnValue;
    }

    public String isNetworkRoaming() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        boolean isRoaming = tm.isNetworkRoaming();
        String returnValue;
        if (isRoaming) {
            returnValue = "YES";
        } else {
            returnValue = "NO";
        }
        return returnValue;
    }

    public String getSimState() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int simState = tm.getSimState();
        String returnValue = "";
        switch (simState) {
            case (TelephonyManager.SIM_STATE_ABSENT):
                returnValue = "ABSENT";
                break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED):
                returnValue = "NETWORK_LOCKED";
                break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED):
                returnValue = "PIN_REQUIRED";
                break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED):
                returnValue = "PUK_REQUIRED";
                break;
            case (TelephonyManager.SIM_STATE_READY):
                returnValue = "READY";
                break;
            case (TelephonyManager.SIM_STATE_UNKNOWN):
                returnValue = "UNKNOWN";
                break;
        }
        return returnValue;
    }

    public String getNetworkType() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = 0;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            networkType = tm.getDataNetworkType();
        } else {
            networkType = tm.getNetworkType();
        }
        String returnValue = "";
        switch (networkType) {
            case (TelephonyManager.NETWORK_TYPE_1xRTT):
                returnValue = "1xRTT";
                break;
            case (TelephonyManager.NETWORK_TYPE_CDMA):
                returnValue = "CDMA";
                break;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                returnValue = "EDGE";
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_0):
                returnValue = "EVDO_0";
                break;
            case (TelephonyManager.NETWORK_TYPE_LTE):
                returnValue = "LTE";
                break;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                returnValue = "GPRS";
                break;
            case (TelephonyManager.NETWORK_TYPE_UNKNOWN):
                returnValue = "UNKNOWN";
                break;
        }
        return returnValue;
    }

    public String getCallState() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int callState = tm.getCallState();
        String returnValue = "";
        switch (callState) {
            case (TelephonyManager.CALL_STATE_RINGING):
                returnValue = "RINGING";
                break;
            case (TelephonyManager.CALL_STATE_OFFHOOK):
                returnValue = "OFFHOOK";
                break;
            case (TelephonyManager.CALL_STATE_IDLE):
                returnValue = "IDLE";
                break;
        }
        return returnValue;
    }

    public String getDataState() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int dataState = tm.getDataState();
        String returnValue = "";
        switch (dataState) {
            case (TelephonyManager.DATA_DISCONNECTED):
                returnValue = "DISCONNECTED";
                break;
            case (TelephonyManager.DATA_CONNECTING):
                returnValue = "CONNECTING";
                break;
            case (TelephonyManager.DATA_CONNECTED):
                returnValue = "CONNECTED";
                break;
            case (TelephonyManager.DATA_SUSPENDED):
                returnValue = "SUSPENDED";
                break;
        }
        return returnValue;
    }

    public String getGroupIdLevel1() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getGroupIdLevel1();
    }

    public String getSimCountryIso() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimCountryIso();
    }

    public String getVoiceMailAlphaTag() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getVoiceMailAlphaTag();
    }

    public String getVoiceMailNumber() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getVoiceMailNumber();
    }

    public String hasIccCard() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        boolean hasIccCard = tm.hasIccCard();
        String returnValue;
        if (hasIccCard) {
            returnValue = "TRUE";
        } else {
            returnValue = "FALSE";
        }
        return returnValue;
    }

    public String getDataActivity() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int dataActivity = tm.getDataActivity();
        String returnValue = "";
        switch (dataActivity) {
            case (TelephonyManager.DATA_ACTIVITY_NONE):
                returnValue = "NONE";
                break;
            case (TelephonyManager.DATA_ACTIVITY_IN):
                returnValue = "IN";
                break;
            case (TelephonyManager.DATA_ACTIVITY_OUT):
                returnValue = "OUT";
                break;
            case (TelephonyManager.DATA_ACTIVITY_INOUT):
                returnValue = "INOUT";
                break;
            case (TelephonyManager.DATA_ACTIVITY_DORMANT):
                returnValue = "DORMANT";
                break;
        }
        return returnValue;
    }

    public String getLatitude() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            return Double.toString(latitude);
        } else {
            return "";
        }
    }

    public String getLongitude() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
        if (location != null) {
            double longitude = location.getLongitude();
            return Double.toString(longitude);
        } else {
            return "";
        }
    }

    public JSONObject getLocation() throws JSONException {
        JSONObject jLocation = new JSONObject();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
//        String bestProvider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            jLocation.put("latitude", "");
            jLocation.put("longitude", "");

            return jLocation;
        }

        Location location;
        LocationListener loc_listener = new LocationListener() {

            public void onLocationChanged(Location l) {
            }

            public void onProviderEnabled(String p) {
            }

            public void onProviderDisabled(String p) {
            }

            public void onStatusChanged(String p, int status, Bundle extras) {
            }
        };
        locationManager
                .requestLocationUpdates(GPS_PROVIDER, 0, 0, loc_listener);
        location = locationManager.getLastKnownLocation(GPS_PROVIDER);
        try {
            jLocation.put("latitude", Double.toString(location.getLatitude()));
            jLocation.put("longitude", Double.toString(location.getLongitude()));

        } catch (NullPointerException e) {
            jLocation.put("latitude", "");
            jLocation.put("longitude", "");
        }

        return jLocation;
    }

    public String getENodeB() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        List<CellInfo> info = tm.getAllCellInfo();
        for (CellInfo i:info) {
            if (i instanceof CellInfoLte){
                int longCid = ((CellInfoLte) i).getCellIdentity().getCi();

                String cellIdHex = DecToHex(longCid);
                String eNBHex = cellIdHex.substring(0, cellIdHex.length()-2);

                int eNB = HexToDec(eNBHex);

                return Integer.toString(eNB);
            }
        }

        return "";
    }

    public String getSpeedDownload(){

        return "";
    }

    // Decimal -> hexadecimal
    public String DecToHex(int dec){
        return String.format("%x", dec);
    }

    // hex -> decimal
    public int HexToDec(String hex){
        return Integer.parseInt(hex, 16);
    }

    public JSONObject getSignalQuality() {
        JSONObject jsonQuality = new JSONObject();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return new JSONObject();
        }
        List<CellInfo> cellInfoList = tm.getAllCellInfo();
        int qualitySignal;
        for (CellInfo cellInfo : cellInfoList) {
            if (cellInfo instanceof CellInfoLte) {
                try {
                    jsonQuality.put("Dbm",String.valueOf(((CellInfoLte)cellInfo).getCellSignalStrength().getDbm()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        jsonQuality.put("Rsrp",String.valueOf(((CellInfoLte)cellInfo).getCellSignalStrength().getRsrp()));
                        jsonQuality.put("Rsrq",String.valueOf(((CellInfoLte)cellInfo).getCellSignalStrength().getRsrq()));
                        jsonQuality.put("Cqi",String.valueOf(((CellInfoLte)cellInfo).getCellSignalStrength().getCqi()));
                        jsonQuality.put("Rssnr",String.valueOf(((CellInfoLte)cellInfo).getCellSignalStrength().getRssnr()));
                    }else{
                        jsonQuality.put("Rsrp","");
                        jsonQuality.put("Rsrq","");
                        jsonQuality.put("Cqi","");
                        jsonQuality.put("Rssnr","");
                    }
                    qualitySignal = ((CellInfoLte) cellInfo).getCellSignalStrength().getLevel();
                    if (qualitySignal == 1) {
                        jsonQuality.put("QualitySignal", "Pobre -" + qualitySignal);
                    } else if (qualitySignal == 2) {
                        jsonQuality.put("QualitySignal", "Moderado -" + qualitySignal);
                    } else if (qualitySignal == 3) {
                        jsonQuality.put("QualitySignal", "Bueno -" + qualitySignal);
                    } else if (qualitySignal == 4) {
                        jsonQuality.put("QualitySignal", "Estupendo -" + qualitySignal);
                    } else if (qualitySignal == 0) {
                        jsonQuality.put("QualitySignal", "Nulo -" + qualitySignal);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }else if(cellInfo instanceof CellInfoGsm){
                qualitySignal = ((CellInfoGsm) cellInfo).getCellSignalStrength().getLevel();
                try {
                    jsonQuality.put("Dbm",String.valueOf(((CellInfoGsm)cellInfo).getCellSignalStrength().getDbm()));
                    jsonQuality.put("Rsrp","");
                    jsonQuality.put("Rsrq","");
                    jsonQuality.put("Cqi","");
                    jsonQuality.put("Rssnr","");
                    if (qualitySignal == 1) {
                        jsonQuality.put("QualitySignal", "Pobre -" + qualitySignal);
                    } else if (qualitySignal == 2) {
                        jsonQuality.put("QualitySignal", "Moderado -" + qualitySignal);
                    } else if (qualitySignal == 3) {
                        jsonQuality.put("QualitySignal", "Bueno -" + qualitySignal);
                    } else if (qualitySignal == 4) {
                        jsonQuality.put("QualitySignal", "Estupendo -" + qualitySignal);
                    } else if (qualitySignal == 0) {
                        jsonQuality.put("QualitySignal", "Nulo -" + qualitySignal);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }else if(cellInfo instanceof CellInfoWcdma){
                try {
                    jsonQuality.put("Dbm",String.valueOf(((CellInfoWcdma)cellInfo).getCellSignalStrength().getDbm()));
                    jsonQuality.put("Rsrp","");
                    jsonQuality.put("Rsrq","");
                    jsonQuality.put("Cqi","");
                    jsonQuality.put("Rssnr","");
                    jsonQuality.put("QualitySignal", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonQuality;
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_NUMBERS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    },
                    PERMISSION_REQUEST_CODE
            );
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_NUMBERS,
                            Manifest.permission.READ_SMS
                    },
                    PERMISSION_REQUEST_CODE
            );
        }
    }
}