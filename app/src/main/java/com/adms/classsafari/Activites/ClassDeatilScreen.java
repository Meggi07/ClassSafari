package com.adms.classsafari.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.adms.classsafari.Adapter.ClassDetailAdapter;
import com.adms.classsafari.Adapter.PopularClassListAdapter;
import com.adms.classsafari.AppConstant.ApiHandler;
import com.adms.classsafari.AppConstant.Utils;
import com.adms.classsafari.BottomNavigationViewHelper;
import com.adms.classsafari.Model.Session.SessionDetailModel;
import com.adms.classsafari.Model.Session.sessionDataModel;
import com.adms.classsafari.R;
import com.adms.classsafari.databinding.ActivityClassDeatilScreenBinding;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClassDeatilScreen extends AppCompatActivity {

    ActivityClassDeatilScreenBinding binding;
    Context mContext;
    ClassDetailAdapter classDetailAdapter;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListPopular;
    public Dialog popularDialog, priceDialog, sortDialog;
    PopularClassListAdapter popularClassListAdapter;
    RecyclerView popularListrcView;
    TextView done;
    CrystalRangeSeekbar rangeSeekbar;
    TextView price_range1_txt, price_range2_txt;
    String subjectStr, boardStr = "", standardStr = "", streamStr = "", locationStr, classNameStr, searchByStr, searchTypeStr, wheretoComeStr, genderStr;
    SessionDetailModel dataResponse;
    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
    boolean arraynull = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_class_deatil_screen);
        mContext = ClassDeatilScreen.this;

        init();
        setListner();
    }

    public void init() {
        searchByStr = getIntent().getStringExtra("SearchBy");
        subjectStr = getIntent().getStringExtra("lessionName");
        standardStr = getIntent().getStringExtra("standard");
        streamStr = getIntent().getStringExtra("stream");
        boardStr = getIntent().getStringExtra("board");
        searchTypeStr = getIntent().getStringExtra("searchType");
        locationStr = getIntent().getStringExtra("city");
        classNameStr = getIntent().getStringExtra("sessionName");
        wheretoComeStr = getIntent().getStringExtra("withOR");
        genderStr = getIntent().getStringExtra("gender");
        BottomNavigationViewHelper.removeShiftMode(binding.bottomNavigationView);
        if (!searchByStr.equalsIgnoreCase("1")) {
            if (!boardStr.equalsIgnoreCase("") || !standardStr.equalsIgnoreCase("")
                    || !streamStr.equalsIgnoreCase("")) {
                binding.boardTxt.setText("\u2022" + boardStr);
                binding.standardTxt.setText("\u2022" + standardStr);
                binding.streamTxt.setText("\u2022" + streamStr);

            }
            binding.cityTxt.setText(locationStr);
            binding.subjectTxt.setText(classNameStr);
        }
        callSessionListApi();
    }

    public void setListner() {

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_popular:
                        PopularDialog();
                        break;
                    case R.id.action_price:
                        PriceDialog();
                        break;
                    case R.id.action_sort:
                        SortDialog();
                        break;
                    case R.id.action_filter:
                        Toast.makeText(mContext, "FILTER", Toast.LENGTH_LONG).show();
                        break;
                }


                return true;
            }
        });
        binding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSearchUser = new Intent(mContext, SearchByUser.class);
                startActivity(inSearchUser);
            }
        });
        binding.searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insession = new Intent(mContext, ClassSearchScreen.class);
                insession.putExtra("flag", searchTypeStr);
                insession.putExtra("withOR", wheretoComeStr);
                startActivity(insession);
            }
        });

        binding.multiautocompe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                binding.inforTxt.setVisibility(View.GONE);
                if (arraynull) {
                    filterFinalArray = new ArrayList<sessionDataModel>();
                }
                String str = binding.multiautocompe.getText().toString();
                if (!str.equalsIgnoreCase("")) {
                    arraynull = false;
                    String[] spilt = adapterView.getItemAtPosition(i).toString().trim().split("\\,");

                    for (sessionDataModel arrayObj : dataResponse.getData()) {
                        if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) && arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim())) {
                            if (arrayObj.getRegionName().trim().equalsIgnoreCase(spilt[0])) {
                                filterFinalArray.add(arrayObj);

                            }
                        }
                    }
                    Log.d("FilterArray", "" + filterFinalArray.size());
                    fillData(filterFinalArray);
                } else {

                }
            }
        });
        binding.multiautocompe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.multiautocompe.showDropDown();
            }
        });

        binding.multiautocompe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {

                Log.d("valueOf OnTextChange", "" + charSequence + i + i1);
                if (charSequence.length() == 0) {
                    binding.inforTxt.setVisibility(View.VISIBLE);
                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                    for (sessionDataModel arrayObj : dataResponse.getData()) {
                        if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) && arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim())) {
                            filterFinalArray.add(arrayObj);
                        }
                    }
                    fillData(filterFinalArray);
                    arraynull = true;
                } else {
                    String[] spilt = charSequence.toString().trim().split("\\,");
                    Log.d("spiltOnText", "" + spilt.length);
                    List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                    for (int k = 0; k < spilt.length; k++) {
                        for (sessionDataModel arrayObj : dataResponse.getData()) {
                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) && arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim())) {
                                if (arrayObj.getRegionName().trim().equalsIgnoreCase(spilt[k])) {
                                    filterFinalArray.add(arrayObj);
                                }
                            }

                        }
                        fillData(filterFinalArray);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent inback = new Intent(mContext, SearchByUser.class);
        inback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inback);
    }

    public void PopularDialog() {
        popularDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = popularDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        popularDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        popularDialog.getWindow().setBackgroundDrawableResource(R.drawable.poop_p3);

        popularDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popularDialog.setCancelable(true);
        popularDialog.setContentView(R.layout.dialog_popular);

        popularListrcView = (RecyclerView) popularDialog.findViewById(R.id.popular_list_rcView);
        done = (TextView) popularDialog.findViewById(R.id.done_txt);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popularDialog.dismiss();
            }
        });
        arrayListPopular = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayListPopular.add(String.valueOf(i));
        }

        popularClassListAdapter = new PopularClassListAdapter(mContext, arrayListPopular);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        popularListrcView.setLayoutManager(mLayoutManager);
        popularListrcView.setItemAnimator(new DefaultItemAnimator());
        popularListrcView.setAdapter(popularClassListAdapter);
        popularDialog.show();
    }

    public void PriceDialog() {
        priceDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = priceDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        priceDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        priceDialog.getWindow().setBackgroundDrawableResource(R.drawable.price_p3);

        priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        priceDialog.setCancelable(true);
        priceDialog.setContentView(R.layout.dialog_price);

        done = (TextView) priceDialog.findViewById(R.id.done_txt);
        rangeSeekbar = (CrystalRangeSeekbar) priceDialog.findViewById(R.id.rangeSeekbar);
        price_range1_txt = (TextView) priceDialog.findViewById(R.id.price_range1_txt);
        price_range2_txt = (TextView) priceDialog.findViewById(R.id.price_range2_txt);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceDialog.dismiss();
            }
        });
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                price_range1_txt.setText("₹ " + String.valueOf(minValue));
                price_range2_txt.setText("₹ " + String.valueOf(maxValue));
                Log.d("select vlue", "min value" + minValue + "maxvalue" + maxValue);
            }
        });
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                price_range1_txt.setText("₹ " + String.valueOf(minValue));
                price_range2_txt.setText("₹ " + String.valueOf(maxValue));
                Log.d("final vlue", "min value" + minValue + "maxvalue" + maxValue);
            }
        });
        priceDialog.show();


    }

    public void SortDialog() {
        sortDialog = new Dialog(mContext, R.style.Theme_Dialog);
        Window window = sortDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        sortDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        sortDialog.getWindow().setBackgroundDrawableResource(R.drawable.sort_p3);

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sortDialog.setCancelable(true);
        sortDialog.setContentView(R.layout.dialog_sort);

        done = (TextView) sortDialog.findViewById(R.id.done_txt);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDialog.dismiss();
            }
        });

        sortDialog.show();
    }


    //Use for SessionList
    public void callSessionListApi() {
        if (Utils.checkNetwork(mContext)) {

            Utils.showDialog(mContext);
            ApiHandler.getApiService().get_SessionList(getSessionListDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionInfo, Response response) {
                    Utils.dismissDialog();
                    if (sessionInfo == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionInfo.getSuccess() == null) {
                        Utils.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("false")) {
                        Utils.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Utils.dismissDialog();
                        if (sessionInfo.getData().size() > 0) {
                            dataResponse = sessionInfo;
                            fillArea();
                            if (searchByStr.equalsIgnoreCase("1")) {
                                List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                                for (sessionDataModel arrayObj : dataResponse.getData()) {
                                    if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) && arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim())) {
                                        filterFinalArray.add(arrayObj);
                                    }
                                    fillData(filterFinalArray);
                                }

                            } else {
                                if (searchTypeStr.equalsIgnoreCase("play")) {
                                    if (!subjectStr.equalsIgnoreCase("")) {
                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                                    arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
                                                    arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim())) {
                                                filterFinalArray.add(arrayObj);
                                            }
                                            fillData(filterFinalArray);
                                        }
                                    } else {
                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                                    arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim())) {
                                                filterFinalArray.add(arrayObj);
                                            }
                                            fillData(filterFinalArray);
                                        }
                                    }

                                } else {
                                    if (!subjectStr.equalsIgnoreCase("") && !boardStr.equalsIgnoreCase("") &&
                                            !standardStr.equalsIgnoreCase("") && !streamStr.equalsIgnoreCase("")) {
                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                                    arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim()) &&
                                                    arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim()) &&
                                                    arrayObj.getBoard().trim().equalsIgnoreCase(boardStr.trim()) &&
                                                    arrayObj.getStandard().trim().equalsIgnoreCase(standardStr.trim()) &&
                                                    arrayObj.getStream().trim().equalsIgnoreCase(streamStr.trim()) &&
                                                    arrayObj.getGenderID().trim().equalsIgnoreCase(genderStr.trim())) {
                                                filterFinalArray.add(arrayObj);
                                            }
                                            fillData(filterFinalArray);
                                        }
                                    } else if (!subjectStr.equalsIgnoreCase("") || !boardStr.equalsIgnoreCase("") ||
                                            !standardStr.equalsIgnoreCase("") || !streamStr.equalsIgnoreCase("")) {
                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                                    arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim())) {
                                                if (arrayObj.getLessionTypeName().trim().equalsIgnoreCase(subjectStr.trim()) ||
                                                        arrayObj.getBoard().equalsIgnoreCase(boardStr.trim()) ||
                                                        arrayObj.getStandard().equalsIgnoreCase(standardStr.trim()) ||
                                                        arrayObj.getStream().equalsIgnoreCase(streamStr.trim()) ||
                                                        arrayObj.getGenderID().equalsIgnoreCase(genderStr)){
                                                    filterFinalArray.add(arrayObj);
                                                }
                                            }
                                            fillData(filterFinalArray);
                                        }
                                    } else {
                                        List<sessionDataModel> filterFinalArray = new ArrayList<sessionDataModel>();
                                        for (sessionDataModel arrayObj : dataResponse.getData()) {
                                            if (arrayObj.getAddressCity().trim().equalsIgnoreCase(locationStr.trim()) &&
                                                    arrayObj.getSessionName().trim().equalsIgnoreCase(classNameStr.trim())) {
                                                filterFinalArray.add(arrayObj);
                                            }
                                            fillData(filterFinalArray);
                                        }
                                    }
                                }

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

    private Map<String, String> getSessionListDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillData(List<sessionDataModel> array) {
        for (int i = 0; i < array.size(); i++) {
            if (!array.get(i).getSessionAmount().equalsIgnoreCase("Free")) {
                array.get(i).setSessionAmount(String.valueOf(Math.round(Float.parseFloat(array.get(i).getSessionAmount()))));
                if (array.get(i).getSessionAmount().equalsIgnoreCase("0")) {
                    array.get(i).setSessionAmount("Free");
                }
            }
        }
        classDetailAdapter = new ClassDetailAdapter(mContext, array,searchByStr,locationStr,classNameStr,boardStr,streamStr,standardStr,searchTypeStr,wheretoComeStr);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        binding.classListRecView.setLayoutManager(mLayoutManager);
        binding.classListRecView.setItemAnimator(new DefaultItemAnimator());
        binding.classListRecView.setAdapter(classDetailAdapter);

    }

    public void fillArea() {
        ArrayList<String> AreaName = new ArrayList<String>();

        for (int j = 0; j < dataResponse.getData().size(); j++) {
            if (locationStr.trim().equalsIgnoreCase(dataResponse.getData().get(j).getAddressCity().trim())
                    && classNameStr.trim().equalsIgnoreCase(dataResponse.getData().get(j).getSessionName().trim())) {
                AreaName.add(dataResponse.getData().get(j).getRegionName());
            }
        }
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(AreaName);
        AreaName.clear();
        AreaName.addAll(hashSet);
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, AreaName);
        binding.multiautocompe.setThreshold(1);
        binding.multiautocompe.setAdapter(adapterTerm);
        binding.multiautocompe.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

}
