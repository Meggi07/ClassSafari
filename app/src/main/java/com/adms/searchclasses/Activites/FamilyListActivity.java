package com.adms.searchclasses.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.adms.searchclasses.Adapter.ExapndableListAdapterFromFront;
import com.adms.searchclasses.Adapter.ExpandableListAdapterIn;
import com.adms.searchclasses.AppConstant.ApiHandler;
import com.adms.searchclasses.AppConstant.AppConfiguration;
import com.adms.searchclasses.AppConstant.Utils;
import com.adms.searchclasses.Interface.onChlidClick;
import com.adms.searchclasses.Interface.onViewClick;
import com.adms.searchclasses.Model.Session.SessionDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.ChildDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.FamilyDetailModel;
import com.adms.searchclasses.Model.TeacherInfo.TeacherInfoModel;
import com.adms.searchclasses.R;
import com.adms.searchclasses.databinding.ActivityAddFamilyBinding;
import com.adms.searchclasses.databinding.ChangePasswordDialogBinding;
import com.adms.searchclasses.databinding.SessiondetailConfirmationDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class FamilyListActivity extends AppCompatActivity implements View.OnClickListener {

    public Context mContext;
    ActivityAddFamilyBinding familyBinding;
    ChangePasswordDialogBinding changePasswordDialogBinding;
    SessiondetailConfirmationDialogBinding sessiondetailConfirmationDialogBinding;
    SessionDetailModel dataResponse;

    List<FamilyDetailModel> finalFamilyDetail;
    List<String> listDataHeader;
    HashMap<String, List<ChildDetailModel>> listDataChild;
    ExpandableListAdapterIn expandableListAdapterIn;
    //Conformation Dialog
    Dialog confimDialog;
    String paymentStatusstr, sessionIDStr, familysessionnameStr,
            orderIDStr, contatIDstr, type, familyIdStr = "", familyNameStr = "", locationStr,
            boardStr, standardStr, streamStr,subjectStr, searchfront, arraowStr, TeacherName,
            froncontanctStr, wheretocometypeStr,firsttimesearch, selectedfamilyNameStr,
            selectedfamilytagStr, bactStr, SearchPlaystudy;
    String fNstr, lNstr, cIdstr, eAstr, gIdstr, dobstr, pNstr, cnIdstr, updateTypestr;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String SessionDuration;
    ArrayList<String> selectedId;

    //Purchase dialog
    ArrayList<String> purchaseSessionIDArray;
    String purchaseSessionIDStr = "";

    //Use for Menu Dialog
    String passWordStr, confirmpassWordStr, currentpasswordStr;
    Dialog menuDialog, changeDialog;
    Button btnHome, btnMyReport, btnMySession, btnChangePassword, btnaddChild, btnLogout, btnmyfamily, btnMyenroll, btnMyprofile;
    TextView userNameTxt;
    View view_home, view_profile, view_myenroll, view_mysession,
            view_myreport, view_btnfamily, view_changepass;
    ArrayList<Integer> totalHours;
    ArrayList<Integer> totalMinit;
    int avgHoursvalue, avgMinitvalue;
    ExapndableListAdapterFromFront exapndableListAdapterFromFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        familyBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_family);

        arraowStr = "Activity";
        mContext = this;
        getIntenetValue();
        init();
        setListner();
    }

    public void getIntenetValue() {
        sessionIDStr = getIntent().getStringExtra("sessionID");
        SearchPlaystudy = getIntent().getStringExtra("SearchPlaystudy");
        familysessionnameStr = getIntent().getStringExtra("sessionName");
        locationStr = getIntent().getStringExtra("city");
        subjectStr = getIntent().getStringExtra("lessionName");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        boardStr = getIntent().getStringExtra("board");
        froncontanctStr = getIntent().getStringExtra("froncontanct");
        searchfront = getIntent().getStringExtra("searchfront");
        wheretocometypeStr = getIntent().getStringExtra("wheretocometype");
        firsttimesearch = getIntent().getStringExtra("firsttimesearch");
        bactStr = getIntent().getStringExtra("back");
        TeacherName = getIntent().getStringExtra("TeacherName");
    }

    //Use for initlize view
    public void init() {
        familyBinding.lvExpfamilylist.setEnabled(false);
        callFamilyListApi();
    }

    //Use for Click Event
    public void setListner() {
        familyBinding.back.setOnClickListener(this);
        familyBinding.addchildTxt.setOnClickListener(this);
        familyBinding.menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (!froncontanctStr.equalsIgnoreCase("true")) {
                    if (!bactStr.equalsIgnoreCase("classDeatil")) {
                        Intent intent = new Intent(mContext, SessionName.class);
                        intent.putExtra("sessionID", sessionIDStr);
                        intent.putExtra("SearchPlaystudy", SearchPlaystudy);
//                        intent.putExtra("duration", durationStr);
//                        intent.putExtra("sessiondate", sessionDateStr);
                        intent.putExtra("sessionName", familysessionnameStr);
//                        intent.putExtra("location", familylocationStr);
//                        intent.putExtra("sessionfees", familysessionfeesStr);
//                        intent.putExtra("sessionStudent", familysessionStudentStr);
                        intent.putExtra("city", locationStr);
                        intent.putExtra("board", boardStr);
                        intent.putExtra("stream", streamStr);
                        intent.putExtra("standard", standardStr);
//                        intent.putExtra("searchType", searchTypeStr);
                        intent.putExtra("lessionName", subjectStr);
//                        intent.putExtra("gender", genderStr);
                        intent.putExtra("searchfront", searchfront);
//                        intent.putExtra("sessionType", sessionType);
                        intent.putExtra("firsttimesearch", firsttimesearch);
//                        intent.putExtra("RegionName", RegionName);
                        intent.putExtra("back", bactStr);
                        intent.putExtra("TeacherName", TeacherName);
                        startActivity(intent);
                    } else {
                        Intent intentClassDetail = new Intent(mContext, ClassDeatilScreen.class);
                        intentClassDetail.putExtra("back", bactStr);
                        intentClassDetail.putExtra("sessionID", sessionIDStr);
                        intentClassDetail.putExtra("city", locationStr);
                        intentClassDetail.putExtra("sessionName", familysessionnameStr);
                        intentClassDetail.putExtra("froncontanct", "false");
                        intentClassDetail.putExtra("back", "classDeatil");
                        intentClassDetail.putExtra("frontLogin", "afterLogin");
                        intentClassDetail.putExtra("SearchPlaystudy", SearchPlaystudy);
                        intentClassDetail.putExtra("board", boardStr);
                        intentClassDetail.putExtra("stream", streamStr);
                        intentClassDetail.putExtra("standard", standardStr);
//                        intentClassDetail.putExtra("searchType", searchTypeStr);
                        intentClassDetail.putExtra("lessionName", subjectStr);
//                        intentClassDetail.putExtra("gender", genderStr);
                        intentClassDetail.putExtra("searchfront", searchfront);
//                        intentClassDetail.putExtra("sessionType", sessionType);
                        intentClassDetail.putExtra("TeacherName", TeacherName);
                        startActivity(intentClassDetail);

                        startActivity(intentClassDetail);
                    }
                } else {
                    if (wheretocometypeStr.equalsIgnoreCase("mySession")) {
                        Intent intent = new Intent(mContext, MySession.class);
                        intent.putExtra("wheretocometype", wheretocometypeStr);
                        startActivity(intent);
                    } else if (wheretocometypeStr.equalsIgnoreCase("myAccount")) {
                        Intent intent = new Intent(mContext, MyAccountActivity.class);
                        intent.putExtra("wheretocometype", wheretocometypeStr);
                        startActivity(intent);
                    } else if (wheretocometypeStr.equalsIgnoreCase("menu")) {
                        Intent intent = new Intent(mContext, SearchByUser.class);
                        intent.putExtra("wheretocometype", wheretocometypeStr);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, SearchByUser.class);
                        intent.putExtra("wheretocometype", wheretocometypeStr);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.addchild_txt:
                if (!froncontanctStr.equalsIgnoreCase("true")) {
//                    getFamilyID();
                    Intent addchild = new Intent(mContext, AddStudentScreen.class);
                    addchild.putExtra("type", "book");
                    addchild.putExtra("familyID", familyIdStr);
                    addchild.putExtra("familyNameStr", familyNameStr);
                    addchild.putExtra("sessionID", sessionIDStr);
//                    addchild.putExtra("duration", durationStr);
//                    addchild.putExtra("sessiondate", sessionDateStr);
                    addchild.putExtra("sessionName", familysessionnameStr);
//                    addchild.putExtra("location", familylocationStr);
//                    addchild.putExtra("sessionfees", familysessionfeesStr);
//                    addchild.putExtra("sessionStudent", familysessionStudentStr);
                    addchild.putExtra("city", locationStr);
                    addchild.putExtra("board", boardStr);
                    addchild.putExtra("stream", streamStr);
                    addchild.putExtra("standard", standardStr);
//                    addchild.putExtra("searchType", searchTypeStr);
                    addchild.putExtra("lessionName", subjectStr);
//                    addchild.putExtra("gender", genderStr);
                    addchild.putExtra("froncontanct", froncontanctStr);
                    addchild.putExtra("searchfront", searchfront);
//                    addchild.putExtra("sessionType", sessionType);
                    addchild.putExtra("firsttimesearch", firsttimesearch);
//                    addchild.putExtra("RegionName", RegionName);
                    addchild.putExtra("back", bactStr);
                    addchild.putExtra("SearchPlaystudy", SearchPlaystudy);
                    addchild.putExtra("TeacherName", TeacherName);
                    addchild.putExtra("updateProfile", "false");
                    addchild.putExtra("phone", pNstr);
                    startActivity(addchild);
                } else {
                    Intent addchild = new Intent(mContext, AddStudentScreen.class);
                    addchild.putExtra("familyNameStr", familyNameStr);
                    addchild.putExtra("froncontanct", froncontanctStr);
                    addchild.putExtra("wheretocometype", wheretocometypeStr);
                    addchild.putExtra("searchfront", searchfront);
                    addchild.putExtra("type", "menu");
                    addchild.putExtra("firsttimesearch", firsttimesearch);
                    addchild.putExtra("updateProfile", "false");
                    addchild.putExtra("phone", pNstr);
                    startActivity(addchild);
                }
                break;
            case R.id.menu:
                menuDialog();
                break;
        }
    }

    //Use for Get FamilyList
    public void callFamilyListApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_FamiliyByFamilyID(getFamilyListDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel familyInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (familyInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        familyBinding.listLinear.setVisibility(View.GONE);
                        familyBinding.noRecordTxt.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        finalFamilyDetail = familyInfoModel.getData();
//                        callSessionReportApi();
                        if (familyInfoModel.getData() != null) {
                            if (familyInfoModel.getData().size() > 0) {
                                if (!froncontanctStr.equalsIgnoreCase("true")) {
                                    familyBinding.text.setVisibility(View.VISIBLE);
                                } else {
                                    familyBinding.text.setVisibility(View.GONE);
                                }

                                familyBinding.listLinear.setVisibility(View.VISIBLE);
                                familyBinding.noRecordTxt.setVisibility(View.GONE);
                                fillExpLV();
                                if (froncontanctStr.equalsIgnoreCase("true")) {
                                    exapndableListAdapterFromFront = new ExapndableListAdapterFromFront(mContext, listDataHeader, listDataChild, new onViewClick() {
                                        @Override
                                        public void getViewClick() {
                                            selectedId = new ArrayList<String>();

                                            selectedId = exapndableListAdapterFromFront.getFamilyID();
                                            Log.d("selectedId", "" + selectedId);
                                            for (int i = 0; i < selectedId.size(); i++) {
                                                String[] spilt = selectedId.get(i).split("\\|");
                                                fNstr = spilt[0];
                                                lNstr = spilt[1];
                                                cIdstr = spilt[3];
                                                eAstr = spilt[4];
                                                gIdstr = spilt[5];
                                                dobstr = spilt[6];
                                                pNstr = spilt[2];
                                                cnIdstr = "1";
                                                updateTypestr = spilt[7];


                                            }
                                            Intent addchild = new Intent(mContext, AddStudentScreen.class);
                                            addchild.putExtra("familyNameStr", familyNameStr);
                                            addchild.putExtra("froncontanct", froncontanctStr);
                                            addchild.putExtra("wheretocometype", wheretocometypeStr);
                                            addchild.putExtra("searchfront", searchfront);
                                            addchild.putExtra("type", "menu");
                                            addchild.putExtra("firsttimesearch", firsttimesearch);
                                            addchild.putExtra("firstName", fNstr);
                                            addchild.putExtra("lastName", lNstr);
                                            addchild.putExtra("contactId", cIdstr);
                                            addchild.putExtra("email", eAstr);
                                            addchild.putExtra("gender", gIdstr);
                                            addchild.putExtra("dob", dobstr);
                                            addchild.putExtra("phone", pNstr);
                                            addchild.putExtra("contacttypeId", cnIdstr);
                                            addchild.putExtra("updateProfile", "true");
                                            addchild.putExtra("Family", updateTypestr);
                                            startActivity(addchild);
                                        }
                                    }, new onChlidClick() {
                                        @Override
                                        public void getChilClick() {
                                            selectedId = new ArrayList<String>();

                                            selectedId = exapndableListAdapterFromFront.getFamilyID();
                                            Log.d("selectedId", "" + selectedId);
                                            for (int i = 0; i < selectedId.size(); i++) {
                                                String[] spilt = selectedId.get(i).split("\\|");
                                                fNstr = spilt[1];
                                                lNstr = spilt[2];
                                                cIdstr = spilt[0];
                                                gIdstr = spilt[4];
                                                dobstr = spilt[5];
                                                cnIdstr = spilt[3];
                                                updateTypestr = spilt[6];
                                                pNstr = spilt[7];
                                                familyNameStr = spilt[8] + " " + spilt[9];

                                            }
                                            Intent addchild = new Intent(mContext, AddStudentScreen.class);
                                            addchild.putExtra("familyNameStr", familyNameStr);
                                            addchild.putExtra("froncontanct", froncontanctStr);
                                            addchild.putExtra("wheretocometype", wheretocometypeStr);
                                            addchild.putExtra("searchfront", searchfront);
                                            addchild.putExtra("type", "menu");
                                            addchild.putExtra("firsttimesearch", firsttimesearch);
                                            addchild.putExtra("firstName", fNstr);
                                            addchild.putExtra("lastName", lNstr);
                                            addchild.putExtra("contactId", cIdstr);
                                            addchild.putExtra("gender", gIdstr);
                                            addchild.putExtra("dob", dobstr);
                                            addchild.putExtra("contacttypeId", cnIdstr);
                                            addchild.putExtra("updateProfile", "true");
                                            addchild.putExtra("phone", pNstr);
                                            addchild.putExtra("Family", updateTypestr);
                                            startActivity(addchild);
                                        }
                                    });
                                    familyBinding.lvExpfamilylist.setAdapter(exapndableListAdapterFromFront);
                                } else {
                                    expandableListAdapterIn = new ExpandableListAdapterIn(mContext, listDataHeader, listDataChild, froncontanctStr, arraowStr, new onChlidClick() {
                                        @Override
                                        public void getChilClick() {
//                                        getFamilyID();

                                        }
                                    }, new onViewClick() {
                                        @Override
                                        public void getViewClick() {
                                            getsessionID();
                                            callSessionReportApi();
                                        }
                                    });
                                    familyBinding.lvExpfamilylist.setAdapter(expandableListAdapterIn);

                                }
                                familyBinding.lvExpfamilylist.expandGroup(0);
                            } else {
                                familyBinding.text.setVisibility(View.GONE);
                                familyBinding.listLinear.setVisibility(View.GONE);
                                familyBinding.noRecordTxt.setVisibility(View.VISIBLE);
                            }
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

    private Map<String, String> getFamilyListDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("FamilyID", Utils.getPref(mContext, "coachTypeID"));
        return map;
    }

    //Use for fill Family List
    public void fillExpLV() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<ChildDetailModel>>();
        for (int i = 0; i < finalFamilyDetail.size(); i++) {
            listDataHeader.add(finalFamilyDetail.get(i).getFirstName() + "|"
                    + finalFamilyDetail.get(i).getLastName() + "|"
                    + finalFamilyDetail.get(i).getContactPhoneNumber() + "|"
                    + finalFamilyDetail.get(i).getFamilyID() + "|"
                    + finalFamilyDetail.get(i).getContactID() + "|"
                    + finalFamilyDetail.get(i).getEmailAddress() + "|"
                    + finalFamilyDetail.get(i).getGenderID() + "|"
                    + finalFamilyDetail.get(i).getDateofBirth());
            pNstr = finalFamilyDetail.get(i).getContactPhoneNumber();
            familyNameStr = finalFamilyDetail.get(i).getFirstName() + ""
                    + finalFamilyDetail.get(i).getLastName();
            Log.d("header", "" + listDataHeader);
            ArrayList<ChildDetailModel> row = new ArrayList<ChildDetailModel>();
            for (int j = 0; j < finalFamilyDetail.get(i).getFamilyContact().size(); j++) {
                row.add(finalFamilyDetail.get(i).getFamilyContact().get(j));
                Log.d("row", "" + row);
            }
            listDataChild.put(listDataHeader.get(i), row);
            Log.d("child", "" + listDataChild);
        }
    }

    //Use for paymentRequest
    public void callpaymentRequestApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_GeneratePaymentRequest(getpaymentRequestdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel paymentRequestModel, Response response) {
                    Utils.dismissDialog();
                    if (paymentRequestModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("True")) {
                        orderIDStr = paymentRequestModel.getOrderID();

                        if (!AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
                            Intent ipayment = new Intent(mContext, PaymentActivity.class);
                            ipayment.putExtra("orderID", orderIDStr);
                            ipayment.putExtra("amount", AppConfiguration.classsessionPrice);
                            ipayment.putExtra("mode", AppConfiguration.Mode);
                            ipayment.putExtra("username", Utils.getPref(mContext, "RegisterUserName"));
                            ipayment.putExtra("sessionID", sessionIDStr);
                            ipayment.putExtra("contactID", Utils.getPref(mContext, "coachID"));
                            ipayment.putExtra("type", Utils.getPref(mContext, "LoginType"));
                            startActivity(ipayment);
                        } else {
                            paymentStatusstr = "1";
                            callSessionConfirmationApi();
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

    private Map<String, String> getpaymentRequestdetail() {

        Map<String, String> map = new HashMap<>();
        map.put("ContactID", contatIDstr);//contatIDstr  // Utils.getPref(mContext, "coachID")
        map.put("SessionID", sessionIDStr);
        map.put("Amount", AppConfiguration.classsessionPrice);

        return map;
    }


    //Use for Family and Child Session Confirmation
    public void callSessionConfirmationApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Session_ContactEnrollment(getSessionConfirmationdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel sessionconfirmationInfoModel, Response response) {
                    Utils.dismissDialog();
                    if (sessionconfirmationInfoModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        //Utils.ping(mContext, "Login succesfully");
                        confimDialog.dismiss();
                        Intent isearchBYuser = new Intent(mContext, PaymentSuccessActivity.class);
                        isearchBYuser.putExtra("transactionId", orderIDStr);
                        isearchBYuser.putExtra("responseCode", "0");
                        isearchBYuser.putExtra("order_id", orderIDStr);
                        startActivity(isearchBYuser);

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

    private Map<String, String> getSessionConfirmationdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        map.put("ContactID", contatIDstr);//contatIDstr  //Utils.getPref(mContext, "coachID")
        map.put("PaymentStatus", paymentStatusstr);
        return map;
    }

    //Use for Get select class detail
    public void getsessionID() {
        selectedId = new ArrayList<String>();

        selectedId = expandableListAdapterIn.getSessionDetail();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            String[] spilt = selectedId.get(i).split("\\|");
            contatIDstr = spilt[2];
            Utils.setPref(mContext, "FamilyID", contatIDstr);
            selectedfamilyNameStr = spilt[0] + " " + spilt[1];
            selectedfamilytagStr = spilt[3];
            type = spilt[4];
            AppConfiguration.UserName = selectedfamilyNameStr;
            Utils.setPref(mContext, "Type", type);
            Log.d("selectedIdStr", contatIDstr);
        }
    }

    public void getFamilyID() {
        selectedId = new ArrayList<String>();

        selectedId = expandableListAdapterIn.getFamilyID();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            String[] spiltValue = selectedId.get(i).split("\\|");
//            familyIdStr = spiltValue[0];
            familyNameStr = spiltValue[1] + " " + spiltValue[2];
            Log.d("selectedIdStr", familyIdStr);
        }
    }

    //Use for GetSession Report
    public void callSessionReportApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_FamilySessionList_ByContactID(getSessionReportDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionModel, Response response) {
                    Utils.dismissDialog();
                    if (sessionModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("false")) {
                        purchaseSessionIDArray = new ArrayList<>();
                        SessionConfirmationDialog();
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        purchaseSessionIDStr="";
                        if (sessionModel.getData().size() > 0) {
                            purchaseSessionIDArray = new ArrayList<>();
                            for (int i = 0; i < sessionModel.getData().size(); i++) {
                                if (sessionIDStr.equalsIgnoreCase(sessionModel.getData().get(i).getSessionID())) {
                                    purchaseSessionIDStr = sessionModel.getData().get(i).getSessionID();
                                }
                            }
                            if (!purchaseSessionIDStr.equalsIgnoreCase("")) {
                                new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                                        .setMessage("You have already purchased.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                            } else {
                                // ConformSessionDialog();
                                SessionConfirmationDialog();
                            }

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

    private Map<String, String> getSessionReportDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("ContactID", contatIDstr);//contatIDstr  //Utils.getPref(mContext, "coachID")
        return map;
    }

    //Use for GetSession Report
    public void callSessioncapacityApi() {
        if (Utils.isNetworkConnected(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_Check_SpotAvailability_By_SessionID(getSessioncapacityDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel sessionModel, Response response) {
                    Utils.dismissDialog();
                    if (sessionModel == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionModel.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("false")) {
                        new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme))
                                .setMessage(getResources().getString(R.string.fail_msg))
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                        return;
                    }
                    if (sessionModel.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("")) {// && !AppConfiguration.classsessionPrice.equalsIgnoreCase("0.00")) {
                            callpaymentRequestApi();
                        }
//                        else {
//                            paymentStatusstr = "1";
//                            callSessionConfirmationApi();
//                        }
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

    private Map<String, String> getSessioncapacityDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);//contatIDstr  //Utils.getPref(mContext, "coachID")
        return map;
    }

    //Use for Change password
    public void changePasswordDialog() {
        changePasswordDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.change_password_dialog, (ViewGroup) familyBinding.getRoot(), false);

        changeDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = changeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        changeDialog.getWindow().getAttributes().verticalMargin = 0.0f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        //changeDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
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
        wlp.x = 10;
        menuDialog.getWindow().getAttributes().verticalMargin = 0.07F;
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        window.setAttributes(wlp);

        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setCanceledOnTouchOutside(true);
//        menuDialog.setContentView(menuBinding.getRoot());
        menuDialog.setContentView(R.layout.layout_menu);

        btnHome = (Button) menuDialog.findViewById(R.id.btnHome);
        btnMyReport = (Button) menuDialog.findViewById(R.id.btnMyReport);
        btnMySession = (Button) menuDialog.findViewById(R.id.btnMySession);
        btnChangePassword = (Button) menuDialog.findViewById(R.id.btnChangePassword);
//        btnaddChild = (Button) menuDialog.findViewById(R.id.btnaddChild);
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
        btnmyfamily.setVisibility(View.GONE);
        view_btnfamily.setVisibility(View.GONE);

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

    //Use for class confirmation
    public void SessionConfirmationDialog() {
        sessiondetailConfirmationDialogBinding = DataBindingUtil.
                inflate(LayoutInflater.from(mContext), R.layout.sessiondetail_confirmation_dialog, (ViewGroup) familyBinding.getRoot(), false);
        confimDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.20f;
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        // confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);
        confimDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
//        confimDialog.setContentView(R.layout.confirm_session_dialog);
        confimDialog.setContentView(sessiondetailConfirmationDialogBinding.getRoot());
        callSessionListApi();
        sessiondetailConfirmationDialogBinding.cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        sessiondetailConfirmationDialogBinding.confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfiguration.TeacherSessionIdStr = sessionIDStr;
                AppConfiguration.TeacherSessionContactIdStr = contatIDstr;
                callSessioncapacityApi();
                confimDialog.dismiss();
            }
        });
        confimDialog.show();

    }

    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionBySessionID(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionDetailInfo, Response response) {
                    Utils.dismissDialog();
                    if (sessionDetailInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionDetailInfo.getSuccess().equalsIgnoreCase("True")) {

                        if (sessionDetailInfo.getData().size() > 0) {
                            dataResponse = sessionDetailInfo;
                            for (int j = 0; j < dataResponse.getData().size(); j++) {
                                AppConfiguration.bookingsubjectName = dataResponse.getData().get(j).getSessionName();
                                AppConfiguration.bookingteacherName = dataResponse.getData().get(j).getName();
                                AppConfiguration.bookingdate = dataResponse.getData().get(j).getStartDate();
                                AppConfiguration.bookingtime = dataResponse.getData().get(j).getSchedule();
                                AppConfiguration.bookingamount = dataResponse.getData().get(j).getSessionAmount();
                                AppConfiguration.bookinhEnddate = dataResponse.getData().get(j).getEndDate();

                                sessiondetailConfirmationDialogBinding.sessionNameTxt.setText(dataResponse.getData().get(j).getSessionName());
                                sessiondetailConfirmationDialogBinding.ratingBar.setRating(Float.parseFloat(dataResponse.getData().get(j).getRating()));
                                sessiondetailConfirmationDialogBinding.tutorNameTxt.setText(dataResponse.getData().get(j).getName());
                                sessiondetailConfirmationDialogBinding.locationTxt.setText(dataResponse.getData().get(j).getRegionName());
                                sessiondetailConfirmationDialogBinding.startDateTxt.setText(dataResponse.getData().get(j).getStartDate());
                                sessiondetailConfirmationDialogBinding.endDateTxt.setText(dataResponse.getData().get(j).getEndDate());
                                if (dataResponse.getData().get(j).getSessionAmount().equalsIgnoreCase("0.00")) {
                                    sessiondetailConfirmationDialogBinding.priceTxt.setText("Free");
                                } else {
                                    sessiondetailConfirmationDialogBinding.priceTxt.setText("₹" + dataResponse.getData().get(j).getSessionAmount());
                                }
                                if (!dataResponse.getData().get(j).getTotalRatingUser().equalsIgnoreCase("0")) {
                                    sessiondetailConfirmationDialogBinding.ratingUserTxt.setText("( " + dataResponse.getData().get(j).getTotalRatingUser() + " )");
                                }
                                AppConfiguration.classsessionPrice = dataResponse.getData().get(j).getSessionAmount();
                                totalHours = new ArrayList<>();
                                totalMinit = new ArrayList<>();
                                String[] spiltPipes = dataResponse.getData().get(j).getSchedule().split("\\|");
                                String[] spiltComma;
                                String[] spiltDash;
                                Log.d("spilt", "" + spiltPipes.toString());
                                for (int i = 0; i < spiltPipes.length; i++) {
                                    spiltComma = spiltPipes[i].split("\\,");
                                    spiltDash = spiltComma[1].split("\\-");
                                    calculateHours(spiltDash[0], spiltDash[1]);
                                    dataResponse.getData().get(j).setDateTime(spiltDash[0]);
                                    Log.d("DateTime", spiltDash[0]);

//

                                    switch (spiltComma[0]) {
                                        case "sun":
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sundayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.sunTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.sunHoursTxt.setText(SessionDuration);
                                            break;
                                        case "mon":
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.mondayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.monTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.monHoursTxt.setText(SessionDuration);
                                            break;
                                        case "tue":
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.tuesTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.tuesHoursTxt.setText(SessionDuration);
                                            break;
                                        case "wed":
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wednesdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.wedTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.wedHoursTxt.setText(SessionDuration);
                                            break;
                                        case "thu":
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thursdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.thurTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.thurHoursTxt.setText(SessionDuration);
                                            break;
                                        case "fri":
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.fridayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.friTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.friHoursTxt.setText(SessionDuration);
                                            break;
                                        case "sat":
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setEnabled(true);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.saturdayBtn.setAlpha(1);
                                            sessiondetailConfirmationDialogBinding.satTimeTxt.setText(dataResponse.getData().get(j).getDateTime());
                                            sessiondetailConfirmationDialogBinding.satHoursTxt.setText(SessionDuration);
                                            break;
                                        default:

                                    }
                                }
                            }
//                            averageHours(totalHours);
//                            averageMinit(totalMinit);
//
//                            if (avgMinitvalue == 0) {
//                                sessiondetailConfirmationDialogBinding.durationTxt.setText(avgHoursvalue + " hr ");
//                            } else {
//                                sessiondetailConfirmationDialogBinding.durationTxt.setText(avgHoursvalue + " hr " + avgMinitvalue + " min");//+ " min"
//                            }
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
        map.put("SessionID", sessionIDStr);
        return map;
    }

    //Use for calculate class time
    public void calculateHours(String time1, String time2) {
        Date date1, date2;
        int days, hours, min;
        String hourstr, minstr;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);

            long difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            SessionHour = hours;
            SessionMinit = min;
            Log.i("======= Hours", " :: " + hours + ":" + min);

            if (SessionMinit > 0) {
                if (SessionMinit < 10) {
                    SessionDuration = SessionHour + ":" + "0" + SessionMinit + " hrs";
                } else {
                    SessionDuration = SessionHour + ":" + SessionMinit + " hrs";
                }
            } else {
                SessionDuration = SessionHour + ":" + "00" + " hrs";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}
