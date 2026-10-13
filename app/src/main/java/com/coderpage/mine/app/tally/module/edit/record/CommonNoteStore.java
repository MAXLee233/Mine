package com.coderpage.mine.app.tally.module.edit.record;

import android.content.Context;
import android.content.SharedPreferences;

import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.data.CategoryContant;
import com.coderpage.mine.app.tally.persistence.model.CategoryModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Local preferences only; existing records and database schema are unchanged. */
public final class CommonNoteStore {
    private final Context mContext;
    private final SharedPreferences mPreferences;

    public CommonNoteStore(Context context) {
        mContext = context.getApplicationContext();
        mPreferences = mContext.getSharedPreferences("tally_common_notes", Context.MODE_PRIVATE);
    }

    public List<String> get(CategoryModel category) {
        String key = key(category);
        // A saved empty list means the user deliberately removed the defaults.
        if (mPreferences.contains(key)) {
            return CommonNotes.parse(mPreferences.getString(key, ""));
        }
        if (category.getType() == CategoryModel.TYPE_EXPENSE
                && CategoryContant.NAME_CAN_YIN.equals(category.getUniqueName())) {
            return Arrays.asList(mContext.getResources().getStringArray(R.array.tally_food_common_notes));
        }
        return Collections.emptyList();
    }

    public void save(CategoryModel category, String text) {
        mPreferences.edit().putString(key(category),
                CommonNotes.format(CommonNotes.parse(text))).apply();
    }

    private String key(CategoryModel category) {
        return CommonNotes.key(category.getAccountId(), category.getType(), category.getUniqueName());
    }
}
