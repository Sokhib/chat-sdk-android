package sdk.chat.core.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by ben on 9/4/17.
 */

public class Checker {

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNullOrEmpty(Editable editable) {
        return editable == null || editable.toString().isEmpty();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
