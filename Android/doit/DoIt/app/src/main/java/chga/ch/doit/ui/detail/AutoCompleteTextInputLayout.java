package chga.ch.doit.ui.detail;

import android.content.Context;
import android.graphics.Canvas;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by chsc on 23.04.17.
 */

public class AutoCompleteTextInputLayout extends TextInputLayout {

    private boolean mIsHintSet;
    private CharSequence mHint;

    public AutoCompleteTextInputLayout(Context context) {
        super(context);
    }

    public AutoCompleteTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            mHint = ((EditText) child).getHint();
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!mIsHintSet && ViewCompat.isLaidOut(this)) {
            setHint(null);

            EditText editText = getEditText();
            if (editText == null) {
                return;
            }
            CharSequence currentEditTextHint = editText.getHint();
            if (!TextUtils.isEmpty(currentEditTextHint)) {
                mHint = currentEditTextHint;
                editText.setHint("");
            }
            setHint(mHint);
            mIsHintSet = true;
        }
    }
}
