package sdk.chat.ui.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import sdk.chat.core.dao.User;
import sdk.chat.core.events.NetworkEvent;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.AccountDetails;
import sdk.chat.core.utils.Checker;
import sdk.chat.core.utils.Dimen;
import sdk.chat.ui.R;
import sdk.chat.ui.chat.MediaSelector;
import sdk.chat.ui.databinding.ActivityPostRegistrationBinding;
import sdk.chat.ui.icons.Icons;
import sdk.chat.ui.module.UIModule;
import sdk.chat.ui.utils.ImagePickerUploader;
import sdk.chat.ui.utils.UserImageBuilder;
import sdk.chat.ui.views.IconEditView;
import sdk.guru.common.RX;

public class PostRegistrationActivity extends BaseActivity {

    CircleImageView avatarImageView;
    IconEditView nameEditView;
    IconEditView passwordEditView;
    IconEditView passwordRepeatEditView;
    @Nullable
    IconEditView locationEditView;
    IconEditView phoneEditView;
    IconEditView emailEditView;
    LinearLayout iconLinearLayout;
    FloatingActionButton doneFab;
    RelativeLayout root;
    String name = "";

    String avatarImageURL = null;
    String phoneNumber = "";
    String email = "";
    String password = "";
    String passwordRepeat = "";
    private ActivityPostRegistrationBinding binding;
    private boolean authenticating = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_post_registration;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostRegistrationBinding.inflate(getLayoutInflater());
        initViews();
        setContentView(binding.getRoot());
    }

    protected void initViews() {
        super.initViews();
        avatarImageView = binding.avatarImageView;
        nameEditView = binding.nameEditView;
        phoneEditView = binding.phoneEditView;
        passwordEditView = binding.passwordEditView;
        passwordRepeatEditView = binding.passwordRepeatEditView;
        emailEditView = binding.emailEditView;
        iconLinearLayout = binding.iconLinearLayout;
        doneFab = binding.doneFab;
        root = binding.root;

        avatarImageView.setOnClickListener(view -> {
            ImagePickerUploader uploader = new ImagePickerUploader(MediaSelector.CropType.Circle);
            dm.add(uploader.choosePhoto(this, false).subscribe(results -> {
                avatarImageView.setImageURI(Uri.fromFile(new File(results.get(0).uri)));
                avatarImageURL = results.get(0).url;
            }, this));
        });

        doneFab.setImageDrawable(Icons.get(this, Icons.choose().check, Icons.shared().actionBarIconColor));
        doneFab.setOnClickListener(v -> {
            doneFab.setEnabled(false);
            next();
            doneFab.setEnabled(true);
        });

        reloadData();
    }


    protected void next() {

        name = nameEditView.getText();
        //   String location = locationEditView.getText();
        phoneNumber = phoneEditView.getText();
        email = emailEditView.getText().trim();
        password = passwordEditView.getText();
        passwordRepeat = passwordRepeatEditView.getText();


        if (Checker.isNullOrEmpty(name)) {
            showToast(R.string.name_field_must_be_set);
            return;
        }
        if (!Checker.isValidEmail(email)) {
            showToast("Email address isn't correct");
            return;
        }
        if (Checker.isNullOrEmpty(password)) {
            showToast("Password must be set");
            return;
        }
        if (!password.equals(passwordRepeat)) {
            showToast("Passwords don't match");
            return;
        }
//         This is login
//        AccountDetails accountDetails = AccountDetails.username(email, password);
//        Log.d("TAG", "next: " + accountDetails.username);
//        dm.add(ChatSDK.auth().authenticate(accountDetails).observeOn(RX.main())
//                .subscribe(this::afterLogin, e -> {
//                    dismissProgressDialog();
//                    Log.d("TAG", "next INSIDE: " + e.getLocalizedMessage());
//                    Log.d("TAG", "next INSIDE: " + e.getMessage());
//                    ChatSDK.events().onError(e);
//                }));
//

        AccountDetails details = new AccountDetails();
        details.type = AccountDetails.Type.Register;
        details.username = email;
        details.password = password;
        authenticateWithDetails(details);


//        currentUser.setName(name, false);
//        // currentUser.setLocation(location, false);
//        currentUser.setPhoneNumber(phoneNumber, false);
//        currentUser.setEmail(email, false);
////
//        // If this is a new avatar, reset the hash code, this will prompt
//        // the XMPP client to update the image
//        if (avatarImageURL != null) {
//            currentUser.setAvatarURL(avatarImageURL, null, false);
//        }

//        View v = getCurrentFocus();
//        if (v instanceof EditText) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            ChatSDK.events().source().accept(NetworkEvent.userMetaUpdated(currentUser));
//        }

    }


    public void authenticateWithDetails(AccountDetails details) {

        if (authenticating) {
            return;
        }
        authenticating = true;

        showProgressDialog(getString(R.string.connecting));

        dm.add(ChatSDK.auth().authenticate(details)
                .observeOn(RX.main())
                .doFinally(() -> {
                    authenticating = false;
                })
                .subscribe(this::afterLogin, e -> {
                    dismissProgressDialog();
                    ChatSDK.events().onError(e);
                }));
    }

    protected void afterLogin() {

        User currentUser = ChatSDK.currentUser();
        currentUser.setName(name, false);
        // currentUser.setLocation(location, false);
        currentUser.setPhoneNumber(phoneNumber, false);
        currentUser.setEmail(email, false);
//
        // If this is a new avatar, reset the hash code, this will prompt
        // the XMPP client to update the image
        if (avatarImageURL != null) {
            currentUser.setAvatarURL(avatarImageURL, null, false);
        }

        View v = getCurrentFocus();
        if (null != v) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            ChatSDK.events().source().accept(NetworkEvent.userMetaUpdated(currentUser));
        }
        dm.add(ChatSDK.core().pushUser().observeOn(RX.main())
                .subscribe(this::startMain, e -> {
                    dismissProgressDialog();
                    Log.d("TAG", "next INSIDE: " + e.getLocalizedMessage());
                    Log.d("TAG", "next INSIDE: " + e.getMessage());
                    ChatSDK.events().onError(e);
                }));
    }

    private void startMain() {
        Log.d("TAG", "next INSIDE: Main Starting");
        ChatSDK.ui().startMainActivity(this, extras);
        finish();
    }

    protected void reloadData() {

        User currentUser = ChatSDK.currentUser();
        String name = "";
        String phoneNumber = "";
        String email = "";
        int width = Dimen.from(this, R.dimen.large_avatar_width);
        int height = Dimen.from(this, R.dimen.large_avatar_height);
        if (currentUser != null) {
            name = currentUser.getName() != null ? currentUser.getName() : "";
            phoneNumber = currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "";
            email = currentUser.getEmail() != null ? currentUser.getEmail() : "";
            if (currentUser.getAvatarURL() != null)
                Glide.with(this).load(currentUser.getAvatarURL()).dontAnimate().placeholder(UIModule.config().defaultProfilePlaceholder).into(avatarImageView);
            UserImageBuilder.loadAvatar(currentUser, avatarImageView, width, height);
        }

        nameEditView.setText(name);
        nameEditView.setNextFocusDown(R.id.emailEditView);
        nameEditView.setIcon(Icons.get(this, Icons.choose().user, R.color.edit_profile_icon_color));
        nameEditView.setHint(R.string.name_hint_required);
        nameEditView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        emailEditView.setText(email);
        emailEditView.setNextFocusDown(R.id.emailEditView);
        emailEditView.setIcon(Icons.get(this, Icons.choose().email, R.color.edit_profile_icon_color));
        emailEditView.setHint(R.string.email_hint);
        emailEditView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        phoneEditView.setText(phoneNumber);
        phoneEditView.setNextFocusDown(R.id.emailEditView);
        phoneEditView.setIcon(Icons.get(this, Icons.choose().phone, R.color.edit_profile_icon_color));
        phoneEditView.setHint(R.string.phone_number_hint);
        phoneEditView.setInputType(InputType.TYPE_CLASS_PHONE);

        passwordEditView.setText("");
        passwordEditView.setNextFocusDown(R.id.passwordRepeatEditView);
        passwordEditView.setIcon(Icons.get(this, Icons.choose().password, R.color.edit_profile_icon_color));
        passwordEditView.setHint(R.string.password_hint);
        passwordEditView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        passwordRepeatEditView.setText("");
        passwordRepeatEditView.setIcon(Icons.get(this, Icons.choose().password, R.color.edit_profile_icon_color));
        passwordRepeatEditView.setHint(R.string.password_repeat_hint);
        passwordRepeatEditView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doneFab.setEnabled(true);
    }

}
