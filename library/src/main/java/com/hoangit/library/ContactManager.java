package com.hoangit.library;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactManager {

    private static ContactManager contactManager;

    public static ContactManager get() {
        if(contactManager==null)
            contactManager = new ContactManager();
        return contactManager;
    }


    public Contact.ContactCursor getContactCursor(Context context) {
        return getContactCursor(context,Contact.Sort.Alphabetical);
    }

    public Contact.ContactCursor getContactCursor(Context context,Contact.Sort sortOrder) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        return new Contact.ContactCursor(contentResolver.query(uri, null, null, null, sortOrder.getKey()));
    }

    public Contact.ContactCursor getContactCursor(Context context,String partialName) {
        return getContactCursor(context,partialName, Contact.Sort.Alphabetical);
    }


    public Contact.ContactCursor getContactCursor(Context context,String partialName, Contact.Sort sortOrder) {
        String clause = String.format("%s = 1",
                ContactsContract.PhoneLookup.HAS_PHONE_NUMBER);

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, partialName);
        return new Contact.ContactCursor(contentResolver.query(uri, null, clause, null, sortOrder.getKey()));
    }

    public Contact.ContactCursor getContactCursorFromNumber(Context context,String number) {
        return getContactCursor(context,number, Contact.Sort.Alphabetical);
    }

    public Contact.ContactCursor getContactCursorFromNumber(Context context,String number, Contact.Sort sortOrder) {
        String[] mProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, number);
        return new Contact.ContactCursor(contentResolver.query(uri, mProjection, null, null, sortOrder.getKey()));
    }

    public Contact.ContactCursor getContactCursorFromId(Context context,String id) {
        return getContactCursor(context,id, Contact.Sort.Alphabetical);
    }

    public Contact.ContactCursor getContactCursorFromId(Context context,String id, Contact.Sort sortOrder) {
        String[] mProjection = { ContactsContract.PhoneLookup._ID};
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, id);
        return new Contact.ContactCursor(contentResolver.query(uri, mProjection, null, null, sortOrder.getKey()));
    }


    public boolean contactExists(Context context, String number) {
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        if (cur!=null&&cur.moveToFirst()) {
            cur.close();
            return true;
        }else return false;
    }

}
