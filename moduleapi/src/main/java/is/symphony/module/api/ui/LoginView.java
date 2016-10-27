package is.symphony.module.api.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by dastko on 10/24/16.
 */

public class LoginView extends LinearLayout {

    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     * @param context Activity context
     * @param resource Custom
     */
    public LoginView(Context context, int resource){
        super(context);
        inflate(context, resource, this);

    }
}
