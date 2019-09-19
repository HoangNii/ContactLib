package com.hoangit.library;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import java.util.List;

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

    public Contact getContactFromNumber(Context context,String phone) {
        if(TextUtils.isEmpty(phone))return null;
        Contact.ContactCursor cursor = getContactCursor(context,phone);
        if(cursor!=null&&cursor.moveToFirst()){
            Contact contact = cursor.getContact();
            cursor.close();
            String number = contact.getNumber();
            if(TextUtils.isEmpty(number)){
                number = contact.getMobileNumber(context).get();
                Log.e("ContactManager",number+"");
            }
            if(TextUtils.isEmpty(number)){
                List<String> numbers =  contact.getNumbers(context);
                if(numbers.size()>0){
                    for (String s:numbers){
                        if(s.replaceAll(" ","").equals(phone.replaceAll(" ",""))){
                            return contact;
                        }
                    }
                }
            }
            if(!TextUtils.isEmpty(number)&&number.replaceAll(" ","")
                    .equals(phone.replaceAll(" ",""))){
                return contact;
            }

        }
        return null;
    }
    public Contact getContactFromId(Context context,String id) {
        if(TextUtils.isEmpty(id))return null;
        Contact.ContactCursor cursor = getContactCursor(context);
        if(cursor!=null&&cursor.moveToFirst()){
            do {
                Contact contact = cursor.getContact();
                if(contact.getId().equals(id)){
                    return contact;
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        return null;
    }
    

}
