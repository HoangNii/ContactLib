package com.hoangit.contactlib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.hoangit.library.Contact;
import com.hoangit.library.ContactManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.edt);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Contact contact = ContactManager.get().getContactFromId(MainActivity.this,charSequence.toString());
                if(contact!=null){
                    Log.e("ContactManager",contact.getDisplayName()+"/"+contact.getId()
                    +"/"+contact.getMobileNumber(MainActivity.this));

                }else{
                    Log.e("ContactManager","null");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
