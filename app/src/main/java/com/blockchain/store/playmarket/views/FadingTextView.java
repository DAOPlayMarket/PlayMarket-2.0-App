package com.blockchain.store.playmarket.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

/* https://stackoverflow.com/questions/49295526/how-to-fade-out-the-end-of-the-last-line-in-a-textview
 * */
public class FadingTextView extends AppCompatTextView {

    private final int FADE_LENGTH_FACTOR = 4;

    private final Shader shader;
    private final Matrix matrix;
    private final Paint paint;
    private final Rect bounds;
    private boolean isNeedToDrawFading = true;

    public FadingTextView(Context context) {
        this(context, null);
    }

    public FadingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public FadingTextView(Context context, AttributeSet attrs, int defStyleAttribute) {
        super(context, attrs, defStyleAttribute);

        matrix = new Matrix();
        paint = new Paint();
        bounds = new Rect();
        shader = new LinearGradient(0f, 0f, 1f, 0f, 0, 0xFF000000, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }


    public void setNeedToDrawFading(boolean needToDrawFading) {
        isNeedToDrawFading = needToDrawFading;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Locals
        if (!isNeedToDrawFading) {
            super.onDraw(canvas);
        }
        final Matrix m = matrix;
        final Rect b = bounds;
        final Layout l = getLayout();

        // Last line index
        final int line = getLineCount() - 1;

        // Determine text direction
        final boolean isRtl = l.getParagraphDirection(line) == Layout.DIR_RIGHT_TO_LEFT;

        // Last line bounds
        getLineBounds(line, b);

        // Adjust end bound to text length
        final int lineStart = l.getLineStart(line);
        final int lineEnd = l.getLineEnd(line);
        final CharSequence text = getText().subSequence(lineStart, lineEnd);
        final int measure = (int) (getPaint().measureText(text, 0, text.length()) + .5f);
        if (isRtl) {
            b.left = b.right - measure;
        } else {
            b.right = b.left + measure;
        }

        // Figure fade length
        final int fadeLength = b.width() / FADE_LENGTH_FACTOR;
        // Adjust start bound to fade start
        if (isRtl) {
            b.right = b.left + fadeLength;
        } else {
            b.left = b.right - fadeLength;
        }

        // Save the layer
        final int saveCount = canvas.saveLayer(b.left, b.top, b.right,
                b.bottom, null,
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);

        // Let TextView draw itself
        super.onDraw(canvas);

        // Adjust and set the Shader Matrix
        m.reset();
        m.setScale(fadeLength, 1f);
        if (isRtl) {
            m.postRotate(180f, fadeLength / 2f, 0f);
        }
        m.postTranslate(b.left, 0f);
        shader.setLocalMatrix(matrix);

        // Finally draw the fade
        canvas.drawRect(b, paint);

        canvas.restoreToCount(saveCount);
    }
}
