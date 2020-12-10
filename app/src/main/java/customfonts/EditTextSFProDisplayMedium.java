package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;

public class EditTextSFProDisplayMedium extends AppCompatEditText {
    public EditTextSFProDisplayMedium(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public EditTextSFProDisplayMedium(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public EditTextSFProDisplayMedium(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/NeoSansPro_Medium.ttf"));
        }
    }
}