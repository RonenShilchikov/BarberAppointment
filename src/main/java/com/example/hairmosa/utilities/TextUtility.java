package com.example.hairmosa.utilities;

import android.text.TextUtils;
import android.widget.EditText;

import com.example.hairmosa.fragments.AppointmentFragment;
import com.example.hairmosa.models.MyAppointmentWrapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class TextUtility {

    public static boolean isValid(EditText... etText) {
        for (EditText editText : etText) {
            if (editText.getText().toString().trim().length() <= 0) {
                return false;
            }
        }
        return true;
    }

    public static <T> String toJson(T src) {
        return new Gson().toJson(src);
    }

    public static <T> T fromJson(String src, Class<T> tClass) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }
        return new Gson().fromJson(src, TypeToken.of(tClass).getType());
    }


}
