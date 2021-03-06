package com.adms.searchclasses.Activites;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.ActivitySearchByUserBinding;
import com.adms.searchclasses.databinding.ChangePasswordDialogBinding;
import com.adms.searchclasses.databinding.LayoutMenuBinding;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchByUser extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, LocationListener {
    public final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    ActivitySearchByUserBinding searchByUserBinding;
    LayoutMenuBinding menuBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;
    LocationManager locationManager;
    Context mContext;
    SessionDetailModel dataResponse;
    String selectedSessionNameStr = "", selectedLocationStr = "", sessionName = "";
    String passWordStr, confirmpassWordStr, currentpasswordStr, cityName = "";
    Dialog menuDialog, changeDialog;
    //Use for Menu Dialog
    Button btnHome, btnMyReport, btnMySession, btnChangePassword,
            btnLogout, btnmyfamily, btnMyenroll, btnMyprofile;
    View view_home, view_profile, view_myenroll, view_mysession,
            view_myreport, view_btnfamily, view_changepass;
    TextView userNameTxt;
    String SearchPlaystudy,currentVersion;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchByUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_user);
        mContext = SearchByUser.this;
        init();
        setListner();
    }

    //Use for initlize view
    public void init() {
//        getLocation();
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
            if (Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Coach")) {
                Intent iDash = new Intent(mContext, DashBoardActivity.class);
                iDash.putExtra("frontLogin", "beforeLogin");
                startActivity(iDash);
            }
            String[] userName = Utils.getPref(mContext, "RegisterUserName").split("\\s+");
            searchByUserBinding.loginTxt.setTextColor(getResources().getColor(R.color.blue));
            searchByUserBinding.regiTxt.setTextColor(getResources().getColor(R.color.blue));
            searchByUserBinding.loginTxt.setText(Html.fromHtml("<b>" + " Hi" + "</b>"));
            searchByUserBinding.regiTxt.setText(Html.fromHtml("<b>" + "" + userName[0] + "</b>"));
            searchByUserBinding.loginTxt.setBackgroundResource(0);
            searchByUserBinding.regiTxt.setBackground(getResources().getDrawable(R.drawable.orange_line));
            searchByUserBinding.loginMiddleTxt.setVisibility(View.GONE);


        } else {
            searchByUserBinding.regiTxt.setVisibility(View.VISIBLE);
            searchByUserBinding.loginMiddleTxt.setVisibility(View.VISIBLE);
            searchByUserBinding.loginTxt.setText(Html.fromHtml("<b>Login</b>"));
            searchByUserBinding.regiTxt.setText(Html.fromHtml("<b>Register</b>"));
        }
    }

    //Use for Click Event
    public void setListner() {
        callSessionListApi();
        searchByUserBinding.letsStudyTxt.setOnClickListener(this);
        searchByUserBinding.searchClassEdt.setOnClickListener(this);
        searchByUserBinding.searchTxt.setOnClickListener(this);
        searchByUserBinding.letsPlayTxt.setOnClickListener(this);
        searchByUserBinding.loginTxt.setOnClickListener(this);
        searchByUserBinding.locationEdt.setOnClickListener(this);
        searchByUserBinding.searchClassEdt.setOnEditorActionListener(this);
        searchByUserBinding.regiTxt.setOnClickListener(this);

        searchByUserBinding.locationEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLocationStr = String.valueOf(adapterView.getItemAtPosition(i));
                searchByUserBinding.searchClassEdt.setText("");
                fillSessionList();
            }
        });
        searchByUserBinding.searchClassEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sessionName = String.valueOf(adapterView.getItemAtPosition(i));
                Log.d("session", sessionName);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lets_study_txt:
                AppConfiguration.ClassLocation = searchByUserBinding.locationEdt.getText().toString();
                flag = true;
                SearchPlaystudy = "1";
                validation();
                break;
            case R.id.search_class_edt:
//                searchByUserBinding.searchClassEdt.showDropDown();
                break;
            case R.id.search_txt:
                flag = false;
                SearchPlaystudy = "3";
                validation();
                break;
            case R.id.lets_play_txt:
                AppConfiguration.ClassLocation = searchByUserBinding.locationEdt.getText().toString();
                flag = true;
                SearchPlaystudy = "2";
                validation();
                break;
            case R.id.login_txt:
                if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
                    menuDialog();
                } else {
                    Intent inClassDetail2 = new Intent(mContext, LoginActivity.class);
                    inClassDetail2.putExtra("frontLogin", "beforeLogin");
                    inClassDetail2.putExtra("ratingLogin", "false");
                    startActivity(inClassDetail2);
                }
                break;
            case R.id.location_edt:
                searchByUserBinding.locationEdt.showDropDown();
                break;
            case R.id.regi_txt:
                if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
                    menuDialog();
                } else {
                    Intent intent = new Intent(mContext, RegistrationActivity.class);
                    intent.putExtra("frontLogin", "beforeLogin");
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.search_class_edt:
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    validation();
                }
                break;
        }
        return false;
    }

    //Use for check appVersion
    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + mContext.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogTheme))
                            .setCancelable(false)
                            .setMessage("Please update to a new version of the app.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adms.searchclasses"));
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(i);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    Utils.ping(mContext, "You wont be able to use other funcationality without updating to a newer version");
                                    finish();
                                }
                            })
                            .show();
                }else{
                    callSessionListApi();
                }
            }
            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
        }
    }

    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {
            ApiHandler.getApiService().get_SessionList_Automplated(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel cityInfo, Response response) {
                    Utils.dismissDialog();
                    if (cityInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (cityInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (cityInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (cityInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (cityInfo.getData().size() > 0) {
                            dataResponse = cityInfo;
                            searchByUserBinding.locationEdt.setText("Ahmedabad");
                            fillCity();
//                            fillSessionList();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Utils.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Utils.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    //Use for fill class city
    public void fillCity() {
       ArrayList<String> CityName = new ArrayList<String>();

        for (int j = 0; j < dataResponse.getData().size(); j++) {
            CityName.add(dataResponse.getData().get(j).getAddressCity());
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(CityName);
        CityName.clear();
        CityName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, CityName);
        searchByUserBinding.locationEdt.setThreshold(1);
        searchByUserBinding.locationEdt.setAdapter(adapterTerm);
        searchByUserBinding.locationEdt.setSelection(searchByUserBinding.locationEdt.getText().length());

//        for (int i = 0; i < CityName.size(); i++) {
//            if (!cityName.equalsIgnoreCase("")) {
//                if (cityName.equalsIgnoreCase(CityName.get(i))) {
//                    searchByUserBinding.locationEdt.setText(CityName.get(i));
//                }
//            }
//        }


    }

    //Use for fill class list
    public void fillSessionList() {
        ArrayList<String> SesisonNameArray = new ArrayList<String>();

        for (int j = 0; j < dataResponse.getData().size(); j++) {
            if (searchByUserBinding.locationEdt.getText().toString().trim().equalsIgnoreCase(dataResponse.getData().get(j).getAddressCity().trim())) {
                SesisonNameArray.add(dataResponse.getData().get(j).getSessionName());
            }
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(SesisonNameArray);
        SesisonNameArray.clear();
        SesisonNameArray.addAll(hashSet);
        ArrayAdapter<String> adapterSessionName = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, SesisonNameArray);
        searchByUserBinding.searchClassEdt.setThreshold(1);
        searchByUserBinding.searchClassEdt.setAdapter(adapterSessionName);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    //Use for validation
    public void validation() {
        selectedSessionNameStr = searchByUserBinding.searchClassEdt.getText().toString();
        selectedLocationStr = searchByUserBinding.locationEdt.getText().toString();

        if (!selectedSessionNameStr.equalsIgnoreCase("")) {
            String A = selectedSessionNameStr;
            String ACaps = A.toUpperCase().charAt(0) + A.substring(1, A.length());
            selectedSessionNameStr = ACaps;
            Log.d("sessionName", ACaps);
        }
        AppConfiguration.ClassLocation = selectedLocationStr;
        if (!selectedLocationStr.equalsIgnoreCase("")) {//!selectedSessionNameStr.equalsIgnoreCase("") &&
            Utils.setPref(mContext, "location", selectedLocationStr);
            Utils.setPref(mContext, "sessionName", selectedSessionNameStr);
            Intent inClassDetail = new Intent(mContext, ClassDeatilScreen.class);
            inClassDetail.putExtra("city", selectedLocationStr);
            inClassDetail.putExtra("sessionName", selectedSessionNameStr);
            inClassDetail.putExtra("searchfront", "searchfront");
            if (flag = true) {
                inClassDetail.putExtra("SearchPlaystudy", SearchPlaystudy);
            } else {
                inClassDetail.putExtra("SearchPlaystudy", "");
            }
            startActivity(inClassDetail);
        } else {
            Utils.ping(mContext, getResources().getString(R.string.location_validation));
        }
    }

    //Use for Change Password
    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) searchByUserBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        // changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        changeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeDialog.setCancelable(false);
        changeDialog.setContentView(changePasswordDialogBinding.getRoot());

        changePasswordDialogBinding.changepwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpasswordStr = changePasswordDialogBinding.edtcurrentpassword.getText().toString();
                confirmpassWordStr = changePasswordDialogBinding.edtconfirmpassword.getText().toString();
                passWordStr = changePasswordDialogBinding.edtnewpassword.getText().toString();
                if (currentpasswordStr.equalsIgnoreCase(Utils.getPref(mContext, "Password"))) {
                    if (!passWordStr.equalsIgnoreCase("") && passWordStr.length() >= 4 && passWordStr.length() <= 8) {
                        if (passWordStr.equalsIgnoreCase(confirmpassWordStr)) {
                            callChangePasswordApi();
                        } else {
                            changePasswordDialogBinding.edtconfirmpassword.setError("Confirm Password does not match.");
                        }
                    } else {
                        changePasswordDialogBinding.edtnewpassword.setError("Password must be 4-8 Characters.");
                        changePasswordDialogBinding.edtnewpassword.setText("");
                        changePasswordDialogBinding.edtconfirmpassword.setText("");
                    }
                } else {
                    changePasswordDialogBinding.edtcurrentpassword.setError("Password does not match to current password.");
                }

            }
        });
        changePasswordDialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDialog.dismiss();
            }
        });

        changeDialog.show();

    }

    //USe for Change Password
    public void callChangePasswordApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Change_Password(getChangePasswordDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel forgotInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (forgotInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, "Please enter valid password");
                        return;
                    }
                    if (forgotInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.ping(mContext, getResources().getString(R.string.changPassword));
                        Utils.setPref(mContext, "Password", passWordStr);
                        changeDialog.dismiss();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Utils.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Utils.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getChangePasswordDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", Utils.getPref(mContext, "RegisterEmail"));
        map.put("Password", passWordStr);
        return map;
    }

    //Use for Menu
    public void menuDialog() {
        menuDialog = new Dialog(mContext);//, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.x = 10; // right margin
        menuDialog.getWindow().getAttributes().verticalMargin = 0.09F;
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
        menuDialog.setContentView(R.layout.layout_menu);
        btnHome = (Button) menuDialog.findViewById(R.id.btnHome);
        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);
        btnMyenroll = (Button) menuDialog.findViewById(R.id.btnMyenroll);
        btnMyprofile = (Button) menuDialog.findViewById(R.id.btnMyprofile);
        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);

        view_home = (View) menuDialog.findViewById(R.id.view_home);
        view_profile = (View) menuDialog.findViewById(R.id.view_home);
        view_myenroll = (View) menuDialog.findViewById(R.id.view_myenroll);
        view_mysession = (View) menuDialog.findViewById(R.id.view_mysession);
        view_myreport = (View) menuDialog.findViewById(R.id.view_myreport);
        view_btnfamily = (View) menuDialog.findViewById(R.id.view_btnfamily);
        view_changepass = (View) menuDialog.findViewById(R.id.view_changepass);


        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        btnHome.setVisibility(View.GONE);
        view_home.setVisibility(View.GONE);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, SearchByUser.class);
                startActivity(i);
            }
        });
        btnMyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, AddStudentScreen.class);
                imyaccount.putExtra("wheretocometype", "menu");
                imyaccount.putExtra("myprofile", "true");
                imyaccount.putExtra("type", "myprofile");
                startActivity(imyaccount);
                menuDialog.dismiss();
            }
        });
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "menu");
                startActivity(imyaccount);
                menuDialog.dismiss();
            }
        });
        btnMyenroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "menu");
                startActivity(isession);
                menuDialog.dismiss();
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpcomingActivity.class);
                intent.putExtra("wheretocometype", "menu");
                startActivity(intent);
                menuDialog.dismiss();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                changePasswordDialog();
            }
        });
        btnmyfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FamilyListActivity.class);
                intent.putExtra("froncontanct", "true");
                intent.putExtra("wheretocometype", "menu");
                intent.putExtra("familyNameStr", Utils.getPref(mContext, "RegisterUserName"));
                intent.putExtra("familyID", Utils.getPref(mContext, "coachTypeID"));
                startActivity(intent);
                menuDialog.dismiss();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogTheme))
                        .setCancelable(false)
//                        .setTitle("Logout")
//                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.setPref(mContext, "coachID", "");
                                Utils.setPref(mContext, "coachTypeID", "");
                                Utils.setPref(mContext, "RegisterUserName", "");
                                Utils.setPref(mContext, "RegisterEmail", "");
                                Utils.setPref(mContext, "LoginType", "");
                                Utils.setPref(mContext, "Password", "");
                                Utils.setPref(mContext, "FamilyID", "");
                                Utils.setPref(mContext, "location", "");
                                Utils.setPref(mContext, "sessionName", "");
                                Intent intentLogin = new Intent(mContext, SearchByUser.class);
                                intentLogin.putExtra("frontLogin", "beforeLogin");
                                startActivity(intentLogin);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing

                            }
                        })
//                        .setIcon(R.drawable.safari)
                        .show();
            }
        });
        menuDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            cityName = addresses.get(0).getLocality();

//            Toast.makeText(mContext,cityName,Toast.LENGTH_LONG).show();
            Log.d("cityName", cityName);

        } catch (Exception e) {

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Toast.makeText(mContext, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
//    private static boolean hasPermissions(Context context, String... permissions) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//    public  static final int REQUEST = 112;
//
//    public void getLocation() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION};
//            if (!hasPermissions(mContext, PERMISSIONS)) {
//                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
//            } else {
//                try {
//                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
//                }
//                catch(SecurityException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            try {
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
//            }
//            catch(SecurityException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    try {
//                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
//                    }
//                    catch(SecurityException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(mContext, "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
}
