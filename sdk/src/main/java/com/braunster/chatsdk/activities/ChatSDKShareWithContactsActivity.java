package com.braunster.chatsdk.activities;

import android.content.Intent;
import android.os.Bundle;

import com.braunster.chatsdk.R;
import com.braunster.chatsdk.Utils.Debug;
import com.braunster.chatsdk.fragments.ChatSDKContactsFragment;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by braunster on 27/07/14.
 */
public class ChatSDKShareWithContactsActivity extends ChatSDKBaseActivity {

    public static final String TAG = ChatSDKShareWithContactsActivity.class.getSimpleName();
    public static final boolean DEBUG = Debug.ShareWithContactsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_sdk_fragment_activity);

        String action = getIntent().getAction();
        String type = getIntent().getType();

        if (StringUtils.isEmpty(action) && StringUtils.isEmpty(type))
            return;

        if (!action.equals(Intent.ACTION_SEND))
            return;

        if (getIntent().getExtras() != null)
        {
            Object extraData = null;

            if (type.equals("text/plain"))
            {
                extraData = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            }
            else if (type.startsWith("image/"))
            {
                extraData = getIntent().getExtras().get(Intent.EXTRA_STREAM);
            }

            ChatSDKContactsFragment fragment = ChatSDKContactsFragment.newInstance(ChatSDKContactsFragment.MODE_LOAD_CONTACTS, ChatSDKContactsFragment.CLICK_MODE_SHARE_CONTENT, extraData);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }
        else showAlertToast("Error getting the image.");



    }
}