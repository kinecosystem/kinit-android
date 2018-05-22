package org.kinecosystem.kinit.view.spend;

import static android.app.Activity.RESULT_OK;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.databinding.PeersSendKinLayoutBinding;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.view.BaseFragment;
import org.kinecosystem.kinit.viewmodel.spend.Peer2PeersViewModel;

public class Peer2PeerSendFragment extends BaseFragment implements Peer2PeerActions {

    public static final String TAG = Peer2PeerSendFragment.class.getSimpleName();
    private static final int READ_CONTACTS_REQUEST = 100;
    private static final int PICK_CONTACT = 200;
    private Peer2PeersViewModel model;
    private AlertDialog alertDialog;
    private PeersSendKinLayoutBinding binding;

    public Peer2PeersViewModel getModel() {
        return model;
    }

    public static Peer2PeerSendFragment newInstance() {
        return new Peer2PeerSendFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.peers_send_kin_layout, container, false);
        Navigator navigator = new Navigator(getActivity());
        model = new Peer2PeersViewModel(getActivity(), getCoreComponents(), navigator);
        model.setPeer2PeerActions(this);
        binding.setModel(model);
        binding.amount.requestFocus();
        binding.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    if (editable.charAt(0) == '0') {
                        editable.delete(0, 1);
                    }
                }
                binding.send.setEnabled(editable.length() >= 1 && !editable.equals("0"));
            }
        });
        model.checkReadContactPermission();
        return binding.getRoot();
    }

    @Override
    public void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_REQUEST: {
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    model.onPermissionGranted();
                } else {
                    model.onPermissionDenied();
                    // permission denied,Disable the functionality that depends on this permission.
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK && data != null) {
            model.onContactSelected(data);
        }else{
            model.onErrorPickContact();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //model.onResume();
    }

    @Override
    public void closeScreen() {
        getActivity().finish();
    }

    @Override
    public void showDialog(int titleStringRes, int messageStringRes, int actionStringRes, boolean finish,
        String errorType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        if (errorType != null) {
            //model.logViewErrorPopup(errorType);
        }
        builder.setTitle(titleStringRes).setMessage(messageStringRes).setPositiveButton(actionStringRes,
            (dialogInterface, i) -> {
                if (errorType != null) {
                    // model.logCloseErrorPopupClicked(errorType);
                }
                dialogInterface.dismiss();
                if (finish) {
                    getActivity().finish();
                }
            });
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (model != null) {
            model.setPeer2PeerActions(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (model != null) {
            model.setPeer2PeerActions(null);
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @RequiresApi(api = VERSION_CODES.M)
    public void requestReadContactsPermission() {
        if (shouldShowRequestPermissionRationale(permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            requestPermissions(new String[]{permission.READ_CONTACTS}, READ_CONTACTS_REQUEST);
        }
    }

    @Override
    public void onContactParseError() {

    }
}
