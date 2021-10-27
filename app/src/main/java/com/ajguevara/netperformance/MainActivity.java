package com.ajguevara.netperformance;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.telephony.TelephonyManager.CALL_STATE_IDLE;
import static android.telephony.TelephonyManager.CALL_STATE_OFFHOOK;
import static android.telephony.TelephonyManager.CALL_STATE_RINGING;
import static android.telephony.TelephonyManager.DATA_ACTIVITY_DORMANT;
import static android.telephony.TelephonyManager.DATA_ACTIVITY_IN;
import static android.telephony.TelephonyManager.DATA_ACTIVITY_INOUT;
import static android.telephony.TelephonyManager.DATA_ACTIVITY_NONE;
import static android.telephony.TelephonyManager.DATA_ACTIVITY_OUT;
import static android.telephony.TelephonyManager.DATA_CONNECTED;
import static android.telephony.TelephonyManager.DATA_CONNECTING;
import static android.telephony.TelephonyManager.DATA_DISCONNECTED;
import static android.telephony.TelephonyManager.DATA_SUSPENDED;
import static android.telephony.TelephonyManager.NETWORK_TYPE_1xRTT;
import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;
import static android.telephony.TelephonyManager.PHONE_TYPE_NONE;
import static android.telephony.TelephonyManager.PHONE_TYPE_SIP;
import static android.telephony.TelephonyManager.SIM_STATE_ABSENT;
import static android.telephony.TelephonyManager.SIM_STATE_CARD_IO_ERROR;
import static android.telephony.TelephonyManager.SIM_STATE_CARD_RESTRICTED;
import static android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED;
import static android.telephony.TelephonyManager.SIM_STATE_NOT_READY;
import static android.telephony.TelephonyManager.SIM_STATE_PERM_DISABLED;
import static android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED;
import static android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED;
import static android.telephony.TelephonyManager.SIM_STATE_READY;
import static android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ajguevara.netperformance.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        requestPermission();

        JSONObject r = new JSONObject();
        try {
            r.put("phone", this.getPhoneNumber());
            r.put("simOperatorName", this.getSimOperatorName());
            r.put("simOperator", this.getSimOperator());
            r.put("networkOperatorName", this.getNetworkOperatorName());
            r.put("networkOperator", this.getNetworkOperator());
            r.put("networkCountryIso", this.getNetworkCountryIso());
            r.put("deviceSoftwareVersion", this.getDeviceSoftwareVersion());
            r.put("phoneType", this.getPhoneType());
            r.put("isNetworkRoaming", this.isNetworkRoaming());
            r.put("simState", this.getSimState());
            r.put("networkType", this.getNetworkType());
            r.put("callState", this.getCallState());
            r.put("dataState", this.getDataState());
            r.put("groupIdLevel", this.getGroupIdLevel1());
            r.put("simCountryIso", this.getSimCountryIso());
            r.put("voiceMailAlphaTag", this.getVoiceMailAlphaTag());
            r.put("voiceMailNumber", this.getVoiceMailNumber());
            r.put("hasIccCard", this.hasIccCard());
            r.put("dataActivity", this.getDataActivity());
            r.put("signalQuality", this.getSignalQuality());
            r.put("latitude", this.getLatitude());
            r.put("longitude", this.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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

    public String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
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

    public String getSubscriberId() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
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
        Location location = new Location(GPS_PROVIDER);
        double latitude = location.getLatitude();
        return Double.toString(latitude);
    }

    public String getLongitude() {
        Location location = new Location(GPS_PROVIDER);
        double longitude = location.getLongitude();
        return Double.toString(longitude);
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