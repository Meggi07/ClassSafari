package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.AppConfiguration;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.TeacherInfo.TeacherInfoModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivitySearchByUserBinding;
import com.adms.classsafari.databinding.ChangePasswordDialogBinding;
import com.adms.classsafari.databinding.LayoutMenuBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchByUser extends AppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener {
    ActivitySearchByUserBinding searchByUserBinding;
    LayoutMenuBinding menuBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;

    Context mContext;
    SessionDetailModel dataResponse;
    String selectedSessionNameStr = "", selectedLocationStr = "", sessionName = "";
    String passWordStr, confirmpassWordStr, currentpasswordStr;
    Dialog menuDialog, changeDialog;
    //Use for Menu Dialog
    Button btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily;
    TextView userNameTxt;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchByUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_user);
        mContext = SearchByUser.this;
        init();
        setListner();
    }

    public void init() {
        searchByUserBinding.locationEdt.setText("Ahmedabad");
//        selectedLocationStr = searchByUserBinding.searchClassEdt.getText().toString();
        if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
            if (Utils.getPref(mContext, "LoginType").equalsIgnoreCase("Coach")) {
                Intent iDash = new Intent(mContext, DashBoardActivity.class);
                iDash.putExtra("frontLogin", "beforeLogin");
                startActivity(iDash);
            }
//            searchByUserBinding.logout.setVisibility(View.VISIBLE);
            searchByUserBinding.loginTxt.setText(Html.fromHtml("Logged as " + "<u> <b>" + Utils.getPref(mContext, "RegisterUserName") + "</u></b>"));

        } else {
            searchByUserBinding.loginTxt.setText(Html.fromHtml("<u><b>Login or Register<u></b>"));
            searchByUserBinding.logout.setVisibility(View.GONE);
        }
    }

    public void setListner() {
        callSessionListApi();
        searchByUserBinding.letsStudyTxt.setOnClickListener(this);
        searchByUserBinding.searchClassEdt.setOnClickListener(this);
        searchByUserBinding.searchTxt.setOnClickListener(this);
        searchByUserBinding.letsPlayTxt.setOnClickListener(this);
        searchByUserBinding.loginTxt.setOnClickListener(this);
        searchByUserBinding.locationEdt.setOnClickListener(this);
//        searchByUserBinding.locationEdt.setOnItemClickListener(this);
//        searchByUserBinding.searchClassEdt.setOnItemClickListener(this);
        searchByUserBinding.searchClassEdt.setOnEditorActionListener(this);


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
                fillCity();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lets_study_txt:
                AppConfiguration.ClassLocation = searchByUserBinding.locationEdt.getText().toString();
                Intent inClassDetail = new Intent(mContext, ClassSearchScreen.class);
                inClassDetail.putExtra("flag", "study");
                inClassDetail.putExtra("withOR", "withOR");
                inClassDetail.putExtra("SearchBy", "2");
                inClassDetail.putExtra("sessionType", "1");
                inClassDetail.putExtra("firsttimesearch", "true");
                startActivity(inClassDetail);
                break;
            case R.id.search_class_edt:
                searchByUserBinding.searchClassEdt.showDropDown();
                break;
            case R.id.search_txt:
                validation();
                break;
            case R.id.lets_play_txt:
                AppConfiguration.ClassLocation = searchByUserBinding.locationEdt.getText().toString();
                Intent inClassDetail1 = new Intent(mContext, ClassSearchScreen.class);
                inClassDetail1.putExtra("flag", "play");
                inClassDetail1.putExtra("withOR", "withOR");
                inClassDetail1.putExtra("SearchBy", "2");
                inClassDetail1.putExtra("sessionType", "2");
                inClassDetail1.putExtra("firsttimesearch", "true");
                startActivity(inClassDetail1);
                break;
            case R.id.login_txt:
                if (!Utils.getPref(mContext, "RegisterUserName").equalsIgnoreCase("")) {
//                    PopupWindow popupwindow_obj = popupDisplay();
//                    popupwindow_obj.showAsDropDown(searchByUserBinding.linearLogin, 250, 10);
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
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if(view.getId()==R.id.search_class_edt){
//            sessionName = String.valueOf(adapterView.getItemAtPosition(i));
//                Log.d("session", sessionName);
//                fillCity();
//        }else if(view.getId()==R.id.location_edt){
//            selectedLocationStr = String.valueOf(adapterView.getItemAtPosition(i));
//                searchByUserBinding.searchClassEdt.setText("");
//                fillSessionList();
//        }

//        switch (view.getId()) {
//            case R.id.search_class_edt:
//                sessionName = String.valueOf(adapterView.getItemAtPosition(i));
//                Log.d("session", sessionName);
//                fillCity();
//                break;
//            case R.id.location_edt:
//                selectedLocationStr = String.valueOf(adapterView.getItemAtPosition(i));
//                searchByUserBinding.searchClassEdt.setText("");
//                fillSessionList();
//                break;
//                default:
//                    break;
//        }
//    }
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (textView.getId()){
            case R.id.search_class_edt:
                if (i == EditorInfo.IME_ACTION_DONE) {
                    validation();
                }
                break;
        }
        return false;
    }
//    //Use for SessionList
//    public void callSessionListApi() {
//        if (Utils.checkNetwork(mContext)) {
//
//            Utils.showDialog(mContext);
//            ApiHandler.getApiService().get_SessionList(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
//                @Override
//                public void success(SessionDetailModel cityInfo, Response response) {
//                    Utils.dismissDialog();
//                    if (cityInfo == null) {
//                        Utils.ping(mContext, getString(R.string.something_wrong));
//                        return;
//                    }
//                    if (cityInfo.getSuccess() == null) {
//                        Utils.ping(mContext, getString(R.string.something_wrong));
//                        return;
//                    }
//                    if (cityInfo.getSuccess().equalsIgnoreCase("false")) {
//                        Utils.ping(mContext, getString(R.string.false_msg));
//                        return;
//                    }
//                    if (cityInfo.getSuccess().equalsIgnoreCase("True")) {
//                        Utils.dismissDialog();
//                        if (cityInfo.getData().size() > 0) {
//                            dataResponse = cityInfo;
//                            fillCity();
//                            fillSessionList();
//                        }
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Utils.dismissDialog();
//                    error.printStackTrace();
//                    error.getMessage();
//                    Utils.ping(mContext, getString(R.string.something_wrong));
//                }
//            });
//        } else {
//            Utils.ping(mContext, getString(R.string.internet_connection_error));
//        }
//    }
//
//    private Map<String, String> getSessionListDetail() {
//        Map<String, String> map = new HashMap<>();
//        return map;
//    }

    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

            Utils.showDialog(mContext);
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
                            fillCity();
                            fillSessionList();
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
    }

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

    public void validation() {
        selectedSessionNameStr = searchByUserBinding.searchClassEdt.getText().toString();
        selectedLocationStr = searchByUserBinding.locationEdt.getText().toString();
        AppConfiguration.ClassLocation = selectedLocationStr;
        if (!selectedSessionNameStr.equalsIgnoreCase("") && !selectedLocationStr.equalsIgnoreCase("")) {
            Utils.setPref(mContext, "location", selectedLocationStr);
            Utils.setPref(mContext, "sessionName", selectedSessionNameStr);
            Intent inClassDetail = new Intent(mContext, ClassDeatilScreen.class);
            inClassDetail.putExtra("city", selectedLocationStr);
            inClassDetail.putExtra("sessionName", selectedSessionNameStr);
            inClassDetail.putExtra("SearchBy", "1");
            inClassDetail.putExtra("withOR", "withOutOR");
            inClassDetail.putExtra("searchfront", "searchfront");
//            inClassDetail.putExtra("searchType", "study");
            startActivity(inClassDetail);
        } else {
            Utils.ping(mContext, getResources().getString(R.string.blank_value));
        }
    }

    public PopupWindow popupDisplay() {

        popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_menu, null);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popupWindow.showAtLocation(searchByUserBinding.linearLogin, Gravity.LEFT, 300, 0);
        popupWindow.setContentView(view);


        menuBinding.btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                startActivity(imyaccount);
            }
        });
        menuBinding.btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                startActivity(isession);
            }
        });
        menuBinding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog();
            }
        });
        return popupWindow;
    }

    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) searchByUserBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

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
                            changePasswordDialogBinding.edtcurrentpassword.setError("Confirm Password does not match.");
                        }
                    } else {
                        changePasswordDialogBinding.edtconfirmpassword.setError("Password must be 4-8 Characters.");
                        changePasswordDialogBinding.edtconfirmpassword.setText("");
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
                        Utils.ping(mContext, "Please Enter Valid Password.");
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

    public void menuDialog() {
//        menuBinding = DataBindingUtil.
//                inflate(LayoutInflater.from(mContext), R.layout.layout_menu, (ViewGroup) searchByUserBinding.getRoot(), false);

        menuDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = menuDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        menuDialog.getWindow().getAttributes().verticalMargin = 0.1F;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
        menuDialog.setContentView(R.layout.layout_menu);

        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
        btnLogout = (Button) menuDialog.findViewById(R.id.btnLogout);
        btnmyfamily = (Button) menuDialog.findViewById(R.id.btnmyfamily);

        userNameTxt = (TextView) menuDialog.findViewById(R.id.user_name_txt);
        userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
        btnMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
                imyaccount.putExtra("wheretocometype", "menu");
                startActivity(imyaccount);
            }
        });
        btnMySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isession = new Intent(mContext, MySession.class);
                isession.putExtra("wheretocometype", "menu");
                startActivity(isession);
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
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.dismiss();
                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                        .setCancelable(false)
                        .setTitle("Logout")
                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
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
                        .setIcon(R.drawable.safari)
                        .show();
            }
        });


//        menuBinding.userNameTxt.setText(Utils.getPref(mContext, "RegisterUserName"));
//        menuBinding.btnMyReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent imyaccount = new Intent(mContext, MyAccountActivity.class);
//                imyaccount.putExtra("wheretocometype", "menu");
//                startActivity(imyaccount);
//            }
//        });
//        menuBinding.btnMySession.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent isession = new Intent(mContext, MySession.class);
//                isession.putExtra("wheretocometype", "menu");
//                startActivity(isession);
//                menuDialog.dismiss();
//            }
//        });
//        menuBinding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                menuDialog.dismiss();
//                changePasswordDialog();
//            }
//        });
//        menuBinding.btnmyfamily.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, FamilyListActivity.class);
//                intent.putExtra("froncontanct", "true");
//                intent.putExtra("wheretocometype", "menu");
//                intent.putExtra("familyNameStr", Utils.getPref(mContext, "RegisterUserName"));
//                intent.putExtra("familyID", Utils.getPref(mContext, "coachTypeID"));
//                startActivity(intent);
//            }
//        });
//        menuBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                menuDialog.dismiss();
//                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
//                        .setCancelable(false)
//                        .setTitle("Logout")
//                        .setIcon(mContext.getResources().getDrawable(R.drawable.safari))
//                        .setMessage("Are you sure you want to logout?")
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Utils.setPref(mContext, "coachID", "");
//                                Utils.setPref(mContext, "coachTypeID", "");
//                                Utils.setPref(mContext, "RegisterUserName", "");
//                                Utils.setPref(mContext, "RegisterEmail", "");
//                                Utils.setPref(mContext, "LoginType", "");
//                                Utils.setPref(mContext, "Password", "");
//                                Utils.setPref(mContext, "FamilyID", "");
//                                Utils.setPref(mContext, "location", "");
//                                Utils.setPref(mContext, "sessionName", "");
//                                Intent intentLogin = new Intent(mContext, SearchByUser.class);
//                                intentLogin.putExtra("frontLogin", "beforeLogin");
//                                startActivity(intentLogin);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//
//                            }
//                        })
//                        .setIcon(R.drawable.safari)
//                        .show();
//            }
//        });
        menuDialog.show();
    }



}
