package com.hoangit.library;

import android.net.Uri;

/**
 * Represents a person who can send or receive messages.
 */
public interface User {
    String getDisplayName();
    Uri getPhotoUri();
}
