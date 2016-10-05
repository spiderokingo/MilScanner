package com.infantry.milscanner.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.infantry.milscanner.Activity.ScanActivity;
import com.infantry.milscanner.Config.ApiService;
import com.infantry.milscanner.Models.BaseModel;
import com.infantry.milscanner.Models.UsersModel;
import com.infantry.milscanner.Models.WeaponModel;
import com.infantry.milscanner.R;
import com.infantry.milscanner.Utils.*;
import com.infantry.milscanner.Utils.Enum;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DepositFragment extends Fragment {

    @Bind(R.id.etUserCode)
    EditText etUserCode;
    @Bind(R.id.btnQrCode)
    ImageButton btnQrCode;
    @Bind(R.id.btnEnterQr)
    ImageButton btnEnterQr;

    @Bind(R.id.btnRefreshPerson)
    ImageButton btnRefreshPerson;
    @Bind(R.id.btnRefreshWeapon)
    ImageButton btnRefreshWeapon;
    @Bind(R.id.holdScanInput)
    LinearLayout holdScanInput;
    @Bind(R.id.holdPerson)
    LinearLayout holdPerson;
    @Bind(R.id.holdWeapon)
    LinearLayout holdWeapon;
    @Bind(R.id.holdSubmit)
    LinearLayout holdSubmit;
    @Bind(R.id.ivPerson)
    ImageView ivPerson;
    @Bind(R.id.tvFullName)
    TextView tvFullName;
    @Bind(R.id.tvCompany)
    TextView tvCompany;
    @Bind(R.id.ivWeapon)
    ImageView ivWeapon;
    @Bind(R.id.tvWpType)
    TextView tvWpType;
    @Bind(R.id.tvWpNo)
    TextView tvWpNo;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;
    @Bind(R.id.btnCancel)
    Button btnCancel;

    public String scanState = Enum.MODE_WEAPON.getStringValue();
    UsersModel usersModel;
    WeaponModel weaponModel;

    public DepositFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);
        ButterKnife.bind(this, view);
        setOnClick();
        setOnChangeListener();
        return view;
    }

    private void setOnClick() {
        btnQrCode.setOnClickListener(OnClickSetup);
        btnRefreshPerson.setOnClickListener(OnClickSetup);
        btnRefreshWeapon.setOnClickListener(OnClickSetup);
        btnEnterQr.setOnClickListener(OnClickSetup);
        btnSubmit.setOnClickListener(OnClickSetup);
        btnCancel.setOnClickListener(OnClickSetup);
    }

    private void setOnChangeListener() {
        etUserCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    btnQrCode.setVisibility(View.GONE);
                    btnEnterQr.setVisibility(View.VISIBLE);
                } else {
                    btnQrCode.setVisibility(View.VISIBLE);
                    btnEnterQr.setVisibility(View.GONE);
                }
            }
        });
    }

    View.OnClickListener OnClickSetup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnEnterQr:
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    ((ScanActivity)getActivity()).getDepositDetails(etUserCode.getText().toString());
                    break;
                case R.id.btnQrCode:
                    IntentIntegrator integrator = new IntentIntegrator(getActivity());
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt(scanState.equals(Enum.MODE_USER.getStringValue())?
                            Enum.TEXT_SCAN_PERSON.getStringValue():
                            Enum.TEXT_SCAN_WEAPON.getStringValue());
                    integrator.setCameraId(0);  // Use a specific camera of the device
                    integrator.initiateScan();
                    break;
                case R.id.btnRefreshPerson:
                    scanState = Enum.MODE_USER.getStringValue();
                    etUserCode.setHint(Enum.TEXT_KEY_IDENTITY_ID.getStringValue());
                    etUserCode.setText("");
                    holdScanInput.setVisibility(View.VISIBLE);
                    holdPerson.setVisibility(View.GONE);
                    holdSubmit.setVisibility(View.GONE);
                    break;
                case R.id.btnRefreshWeapon:
                    scanState = Enum.MODE_WEAPON.getStringValue();
                    etUserCode.setHint(Enum.TEXT_KEY_WEAPON_NUMBER.getStringValue());
                    etUserCode.setText("");
                    holdScanInput.setVisibility(View.VISIBLE);
                    holdWeapon.setVisibility(View.GONE);
                    holdSubmit.setVisibility(View.GONE);
                    break;
                case R.id.btnSubmit:
                    submitDepositToServer();
                    break;
                case R.id.btnCancel:
                    holdSubmit.setVisibility(View.GONE);
                    holdWeapon.setVisibility(View.GONE);
                    holdPerson.setVisibility(View.GONE);
                    holdScanInput.setVisibility(View.VISIBLE);
                    etUserCode.setHint(Enum.TEXT_KEY_WEAPON_NUMBER.getStringValue());
                    etUserCode.setText("");
                    break;
            }
        }
    };

    private void submitDepositToServer() {
        ApiService.getApiEndpointInterface().submitDeposit(
                Enum.MODE_DEPOSIT.getStringValue(),
                ModelCaches.getInstance().getUsersDetails().PersonalID,
                usersModel.PersonalID,
                weaponModel.WithdrawID,
                weaponModel.WeaponID,
                new MyCallback<BaseModel>() {
                    @Override
                    public void good(BaseModel model) {
                        if (model != null) {
                            if (model.result) {
                                holdSubmit.setVisibility(View.GONE);
                                holdPerson.setVisibility(View.GONE);
                                holdWeapon.setVisibility(View.GONE);
                                holdScanInput.setVisibility(View.VISIBLE);
                                scanState = Enum.MODE_USER.getStringValue();
                                etUserCode.setHint(Enum.TEXT_KEY_WEAPON_NUMBER.getStringValue());
                                etUserCode.setText("");
                            }
                            Singleton.toast(getContext(), model.message, Toast.LENGTH_LONG);
                        }
                    }
                }

        );
    }

    public void showPersonUI(UsersModel model){
        holdPerson.setVisibility(View.VISIBLE);
        holdScanInput.setVisibility(View.GONE);
        String name = model.TitleName + " " + model.FirstName + "  " + model.LastName;
        String company = model.Company;
        Glide.with(getContext()).load(ModelCaches.getInstance().getApiCompletePath() + model.ImageFullPath).into(ivPerson);
        tvFullName.setText(name);
        tvCompany.setText(company);

        usersModel = model;

        checkComplete();
    }

    public void showWeaponUI(WeaponModel model) {
        etUserCode.setHint(Enum.TEXT_KEY_IDENTITY_ID.getStringValue());
        etUserCode.setText("");
        holdWeapon.setVisibility(View.VISIBLE);
        String type = model.WeaponType + " (" + model.WeaponCompany + ")";
        String number = "หมายเลขอาวุธ: " + model.WeaponNumber;
        Glide.with(getContext()).load(ModelCaches.getInstance().getApiCompletePath() + model.ImageFullPath).into(ivWeapon);
        tvWpType.setText(type);
        tvWpNo.setText(number);

        if(holdPerson.getVisibility() == View.VISIBLE)
            holdScanInput.setVisibility(View.GONE);

        weaponModel = model;

        checkComplete();
    }

    private void checkComplete(){
        if(holdPerson.getVisibility() == View.VISIBLE && holdWeapon.getVisibility() == View.VISIBLE){
            holdSubmit.setVisibility(View.VISIBLE);
        }

        if(weaponModel != null && usersModel != null) {
            if (!weaponModel.PersonalID.equalsIgnoreCase(usersModel.PersonalID)) {
                MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                        .title(Enum.TITLE_WARNING_RETURN.getStringValue())
                        .titleColor(getResources().getColor(R.color.red))
                        .positiveText(Enum.OK.getStringValue())
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .content("อาวุธหมายนี้ถูกเบิกออกโดย\n" + weaponModel.PersonalName + "\nและจะส่งคืนโดย\n"
                                + usersModel.TitleName + " " + usersModel.FirstName + "  " + usersModel.LastName )
                        .contentGravity(GravityEnum.CENTER)
                        .build();
                dialog.show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
