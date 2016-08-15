package com.infantry.milscanner.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.infantry.milscanner.Config.ApiService;
import com.infantry.milscanner.Models.UsersModel;
import com.infantry.milscanner.R;
import com.infantry.milscanner.Utils.MyCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawFragment extends Fragment {


    @Bind(R.id.etUserCode)
    EditText etUserCode;
    @Bind(R.id.btnQrCode)
    ImageButton btnQrCode;

    public WithdrawFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        ButterKnife.bind(this, view);
        setOnClick();
        return view;
    }



    private void setOnClick() {
        btnQrCode.setOnClickListener(OnClickSetup);
    }

    View.OnClickListener OnClickSetup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnQrCode:
                    IntentIntegrator integrator = new IntentIntegrator(getActivity());
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt(com.infantry.milscanner.Utils.Enum.TEXT_SCAN_PERSON.getStringValue());
                    integrator.setCameraId(0);  // Use a specific camera of the device
                    integrator.initiateScan();
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
