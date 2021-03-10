package com.tde.framework.widget.card;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;


class RoundRectHelper {

    /**
     * This helper is set by CardView implementations.
     * <p>
     * Prior to API 17, canvas.drawRoundRect is expensive; which is why we need this method
     * to draw efficient rounded rectangles before 17.
     * */
    static void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint);
        } else {
            final float twoRadius = cornerRadius * 2;
            final float innerWidth = bounds.width() - twoRadius - 1;
            final float innerHeight = bounds.height() - twoRadius - 1;
            if (cornerRadius >= 1) {
                // increment corner radius to account for half pixels.
                RectF mCornerRect = new RectF();
                float roundedCornerRadius = cornerRadius + .5f;
                mCornerRect.set(-roundedCornerRadius, -roundedCornerRadius, roundedCornerRadius,
                        roundedCornerRadius);
                int saved = canvas.save();
                canvas.translate(bounds.left + roundedCornerRadius,
                        bounds.top + roundedCornerRadius);
                canvas.drawArc(mCornerRect, 180, 90, true, paint);
                canvas.translate(innerWidth, 0);
                canvas.rotate(90);
                canvas.drawArc(mCornerRect, 180, 90, true, paint);
                canvas.translate(innerHeight, 0);
                canvas.rotate(90);
                canvas.drawArc(mCornerRect, 180, 90, true, paint);
                canvas.translate(innerWidth, 0);
                canvas.rotate(90);
                canvas.drawArc(mCornerRect, 180, 90, true, paint);
                canvas.restoreToCount(saved);
                //draw top and bottom pieces
                canvas.drawRect(bounds.left + roundedCornerRadius - 1f, bounds.top,
                        bounds.right - roundedCornerRadius + 1f,
                        bounds.top + roundedCornerRadius, paint);

                canvas.drawRect(bounds.left + roundedCornerRadius - 1f,
                        bounds.bottom - roundedCornerRadius,
                        bounds.right - roundedCornerRadius + 1f, bounds.bottom, paint);
            }
            // center
            canvas.drawRect(bounds.left, bounds.top + cornerRadius,
                    bounds.right, bounds.bottom - cornerRadius, paint);
        }

    }

}
