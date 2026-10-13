package com.coderpage.mine.app.tally.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coderpage.base.utils.UIUtils;
import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.module.edit.record.CommonNoteStore;
import com.coderpage.mine.app.tally.module.edit.record.CommonNotes;
import com.coderpage.mine.app.tally.persistence.model.CategoryModel;

import java.util.List;

/** Note editor with optional per-category shortcuts; other text dialogs are unaffected. */
public class CommonNoteDialog extends Dialog {
    private final Activity mActivity;
    private final CategoryModel mCategory;
    private final CommonNoteStore mStore;
    private final EditText mContent;
    private final LinearLayout mNotes;
    private final TextView mEmpty;
    private final TextView mTitle;
    private TextEditDialog.Listener mListener;
    private AlertDialog mPresetEditor;

    public CommonNoteDialog(Activity activity, CategoryModel category) {
        super(activity, R.style.Widget_Dialog_BottomSheet);
        mActivity = activity;
        mCategory = category;
        mStore = new CommonNoteStore(activity);
        setContentView(R.layout.tally_dialog_common_note);
        mContent = findViewById(R.id.etCommonNote);
        mNotes = findViewById(R.id.lyCommonNotes);
        mEmpty = findViewById(R.id.tvCommonNotesEmpty);
        mTitle = findViewById(R.id.tvCommonNoteTitle);

        findViewById(R.id.tvEditCommonNotes).setOnClickListener(v -> editPresets());
        findViewById(R.id.tvCommonNoteCancel).setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onNegativeClick(mContent, this);
            }
        });
        findViewById(R.id.tvCommonNoteConfirm).setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onPositiveClick(mContent, this, mContent.getText().toString());
            }
        });
        refreshPresets();
    }

    public CommonNoteDialog setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    public CommonNoteDialog setHint(String hint) {
        mContent.setHint(hint);
        return this;
    }

    public CommonNoteDialog setContent(String content) {
        mContent.setText(content);
        mContent.setSelection(mContent.length());
        return this;
    }

    public CommonNoteDialog setListener(TextEditDialog.Listener listener) {
        mListener = listener;
        return this;
    }

    @Override public void show() {
        Window window = getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            window.setGravity(Gravity.BOTTOM);
        }
        super.show();
        if (window != null) {
            window.setLayout(mActivity.getResources().getDisplayMetrics().widthPixels,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    private void refreshPresets() {
        mNotes.removeAllViews();
        List<String> notes = mStore.get(mCategory);
        mEmpty.setVisibility(notes.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
        for (String note : notes) {
            TextView chip = new TextView(mActivity);
            chip.setText(note);
            chip.setTextSize(14);
            chip.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
            chip.setBackgroundResource(R.drawable.bg_accent_btn_round_stroke);
            chip.setGravity(Gravity.CENTER);
            chip.setPadding(dp(16), dp(8), dp(16), dp(8));
            chip.setMinHeight(dp(48));
            chip.setMaxWidth(dp(240));
            chip.setSingleLine(true);
            chip.setFocusable(true);
            chip.setEllipsize(TextUtils.TruncateAt.END);
            chip.setContentDescription(mActivity.getString(R.string.tally_use_common_note, note));
            chip.setOnClickListener(v -> {
                setContent(note);
                UIUtils.hideSoftKeyboard(mActivity, mContent);
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = dp(8);
            mNotes.addView(chip, params);
        }
    }

    private void editPresets() {
        UIUtils.hideSoftKeyboard(mActivity, mContent);
        EditText input = new EditText(mActivity);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setGravity(Gravity.TOP);
        input.setMinLines(3);
        input.setMaxLines(6);
        input.setHint(R.string.tally_common_notes_hint);
        input.setContentDescription(mActivity.getString(R.string.tally_edit_common_notes));
        input.setText(CommonNotes.format(mStore.get(mCategory)));
        input.setSelection(input.length());
        LinearLayout container = new LinearLayout(mActivity);
        container.setPadding(dp(20), 0, dp(20), 0);
        container.addView(input, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mPresetEditor = new AlertDialog.Builder(mActivity)
                .setTitle(R.string.tally_edit_common_notes)
                .setMessage(R.string.tally_common_notes_help)
                .setView(container)
                .setNegativeButton(R.string.cancel, (dialog, which) ->
                        UIUtils.hideSoftKeyboard(mActivity, input))
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    mStore.save(mCategory, input.getText().toString());
                    refreshPresets();
                    UIUtils.hideSoftKeyboard(mActivity, input);
                }).create();
        mPresetEditor.setOnDismissListener(dialog -> mPresetEditor = null);
        mPresetEditor.show();
        input.requestFocus();
        Window window = mPresetEditor.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    @Override public void dismiss() {
        if (mPresetEditor != null) {
            mPresetEditor.dismiss();
        }
        super.dismiss();
    }

    private int dp(int value) {
        return Math.round(value * mActivity.getResources().getDisplayMetrics().density);
    }
}
