package org.kinecosystem.tippic.view.spend;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kinecosystem.tippic.R;
import org.kinecosystem.tippic.databinding.PeersSendKinLayoutBinding;
import org.kinecosystem.tippic.util.GeneralUtils;
import org.kinecosystem.tippic.view.BaseFragment;
import org.kinecosystem.tippic.viewmodel.spend.Peer2PeerViewModel;

public class Peer2PeerSendFragment extends BaseFragment implements Peer2PeerActions {

    public static final String TAG = Peer2PeerSendFragment.class.getSimpleName();
    private static final int PICK_CONTACT = 200;
    private Peer2PeerViewModel model;
    private AlertDialog alertDialog;
    private PeersSendKinLayoutBinding binding;
    private static int NO_STRING_PARAM = -1;
    private ContactData contactData;

    public static Peer2PeerSendFragment newInstance() {
        return new Peer2PeerSendFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.peers_send_kin_layout, container, false);
        model = new Peer2PeerViewModel();
        model.setPeer2PeerActions(this);
        binding.setModel(model);
        contactData = new ContactData(getActivity());
        pickContact();
        return binding.getRoot();
    }

    @Override
    public void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == PICK_CONTACT && resultCode == RESULT_OK && data != null && contactData.parse(data)) {
                model.updateContact(contactData);
            } else {
                model.onErrorPickContact();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        model.onResume();
    }

    @Override
    public void closeScreen() {
        getActivity().finish();
    }

    @Override
    public void showDialog(int titleStringRes, int messageStringRes, int messageParam, int actionStringRes,
        boolean showContacts) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        String message;
        if (messageParam == NO_STRING_PARAM) {
            message = getResources().getString(messageStringRes);
        } else {
            message = getResources().getString(messageStringRes, messageParam);
        }
        builder.setTitle(titleStringRes).setMessage(message).setPositiveButton(actionStringRes,
            (dialogInterface, i) -> {
                dialogInterface.dismiss();
                if (showContacts) {
                    model.onPickContactClicked(null);
                }
            });
        if (alertDialog != null) {
            alertDialog.dismiss();
            binding.amount.requestFocus();
        }
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void showDialog(int titleStringRes, int messageStringRes, int actionStringRes, boolean showContacts) {
        showDialog(titleStringRes, messageStringRes, NO_STRING_PARAM, actionStringRes, showContacts);
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

    @Override
    public void onContactParseError() {

    }

    @Override
    public void onReadyForTransaction() {
        binding.amount.requestFocus();
        GeneralUtils.openKeyboard(getActivity(), binding.amount);
    }

    @Override
    public void onStartTransaction() {
        GeneralUtils.closeKeyboard(getActivity(), binding.transactionCompleteBg);
    }

    @Override
    public void onTransactionComplete() {
        GeneralUtils.closeKeyboard(getActivity(), binding.transactionCompleteBg);
        binding.transactionCompleteBg.animate().alpha(.8f).setDuration(250);
        binding.transactionCompleteImage.animate().alpha(1).setDuration(250).setStartDelay(250);
        binding.transactionCompleteTitle.animate().alpha(1).setDuration(250).setStartDelay(500);
        binding.transactionCompleteSubtitle.animate().alpha(1).setDuration(250).setStartDelay(550);
    }
}
