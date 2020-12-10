package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class AutoCompleteTextViewSFProDisplayRegular extends AppCompatAutoCompleteTextView {
    public AutoCompleteTextViewSFProDisplayRegular(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public AutoCompleteTextViewSFProDisplayRegular(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public AutoCompleteTextViewSFProDisplayRegular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/NeoSans_Pro_Regular.ttf"));
        }
    }
}