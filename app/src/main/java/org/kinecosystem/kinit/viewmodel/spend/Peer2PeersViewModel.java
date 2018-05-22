package org.kinecosystem.kinit.viewmodel.spend;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import org.kinecosystem.kinit.CoreComponentsProvider;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.network.OperationResultCallback;
import org.kinecosystem.kinit.view.spend.Peer2PeerActions;

public class Peer2PeersViewModel {

    private Activity context;
    private CoreComponentsProvider coreComponents;
    private Navigator navigator;
    private String address;
    private Peer2PeerActions actions;
    public ObservableField<String> name = new ObservableField<>("some banen");

    public Peer2PeersViewModel(Activity context, CoreComponentsProvider coreComponentsProvider, Navigator navigator) {
        this.context = context;
        this.coreComponents = coreComponentsProvider;
        this.navigator = navigator;

    }

    public void setPeer2PeerActions(Peer2PeerActions actions) {
        this.actions = actions;
    }

    public void onPermissionDenied() {
        //TODO
    }

    public void onPermissionGranted() {
       // getContacts();
        actions.pickContact();
    }

    public void onContactSelected(Intent data) {

        Uri contactUri = data.getData();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(contactUri, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer
                    .parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    name.set(contactName);
                    final String phoneNumber = PhoneNumberUtils.getNumberFromIntent(data, context);
                    if (phoneNumber != null && !phoneNumber.isEmpty()) {
                        sendPhone(phoneNumber);
                    }
                    //name.set(contactName + " (" + phoneNumber + ")");


                } else {
                    actions.onContactParseError();
                }
            }
        }
        cursor.close();
    }

    public void onErrorPickContact() {

    }

    private void sendPhone(String phoneNumber) {
        List<String> numbers = new ArrayList<>();
        final String formatted = PhoneNumberUtils.stripSeparators(phoneNumber);
        numbers.add(formatted);
        new Thread(() -> coreComponents.services().getOfferService().sendContact(numbers, new OperationResultCallback<String>() {
            @Override
            public void onResult(String addressResponse) {
                address = addressResponse;
                onGotValidAddress();
            }

            @Override
            public void onError(int errorCode) {
                address = "";
                onNoAddressMatch();
            }
        })).start();

    }

    private void onGotValidAddress() {
        Log.d("###", "### got address " + address);
    }

    private void onNoAddressMatch() {
    }


    public void onCloseButtonClicked(View view) {
        if (actions != null) {
            actions.closeScreen();
        }
    }

    public void onPickContactClicked(View view) {
        if (actions != null) {
            actions.pickContact();
        }
    }

    public void checkReadContactPermission() {
        if (actions != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
                    actions.requestReadContactsPermission();
                } else if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    actions.pickContact();
                }
            } else {
                actions.pickContact();
            }
        }
    }


}
