package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;

public class EditTextSFProDisplayRegular extends AppCompatEditText {
    public EditTextSFProDisplayRegular(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public EditTextSFProDisplayRegular(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public EditTextSFProDisplayRegular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/NeoSans_Pro_Regular.ttf"));
        }
    }
}
