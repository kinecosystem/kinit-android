package org.kinecosystem.tippic.view.spend;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

public class ContactData {

    private String name, phoneNumber;
    private Context context;
    private ContentResolver contentResolver;

    public ContactData(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    public boolean parse(Intent data) {
        Uri contactUri = data.getData();
        Cursor cursor = contentResolver.query(contactUri, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer
                    .parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    phoneNumber = PhoneNumberUtils.getNumberFromIntent(data, context);
                    if (phoneNumber != null && !phoneNumber.isEmpty()) {
                        cursor.close();
                        return true;
                    }
                }
            }
            cursor.close();
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
