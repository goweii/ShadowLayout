package per.goweii.roundedshadowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import per.goweii.shadowlayout.ShadowLayout;

/**
 * 圆角阴影布局
 */
public class RoundedShadowLayout extends ShadowLayout {
    private final RoundedShadowOutlineProvider mRoundedShadowOutlineProvider = new RoundedShadowOutlineProvider();

    private boolean mRoundedCornerRadiusAdaptation = true;

    public RoundedShadowLayout(Context context) {
        this(context, null);
    }

    public RoundedShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setShadowOutlineProvider(mRoundedShadowOutlineProvider);
        setClipToShadowOutline(true);
        setClipToPadding(false);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedShadowLayout);
        mRoundedCornerRadiusAdaptation = typedArray.getBoolean(R.styleable.RoundedShadowLayout_roundedCornerRadiusAdaptation, mRoundedCornerRadiusAdaptation);
        float cornerRadius = typedArray.getDimension(R.styleable.RoundedShadowLayout_roundedCornerRadius, 0F);
        float cornerRadiusTopLeft = typedArray.getDimension(R.styleable.RoundedShadowLayout_roundedCornerRadiusTopLeft, cornerRadius);
        float cornerRadiusTopRight = typedArray.getDimension(R.styleable.RoundedShadowLayout_roundedCornerRadiusTopRight, cornerRadius);
        float cornerRadiusBottomRight = typedArray.getDimension(R.styleable.RoundedShadowLayout_roundedCornerRadiusBottomRight, cornerRadius);
        float cornerRadiusBottomLeft = typedArray.getDimension(R.styleable.RoundedShadowLayout_roundedCornerRadiusBottomLeft, cornerRadius);
        typedArray.recycle();
        setCornerRadius(cornerRadiusTopLeft, cornerRadiusTopRight, cornerRadiusBottomRight, cornerRadiusBottomLeft);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        if (mRoundedCornerRadiusAdaptation) {
            return super.getSuggestedMinimumWidth();
        }
        int radiusLeft = (int) Math.max(getTopLeftCornerRadius(), getBottomLeftCornerRadius());
        int radiusRight = (int) Math.max(getTopRightCornerRadius(), getBottomRightCornerRadius());
        return radiusLeft + radiusRight + super.getSuggestedMinimumWidth();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        if (mRoundedCornerRadiusAdaptation) {
            return super.getSuggestedMinimumHeight();
        }
        int radiusTop = (int) Math.max(getTopLeftCornerRadius(), getTopRightCornerRadius());
        int radiusBottom = (int) Math.max(getBottomLeftCornerRadius(), getBottomRightCornerRadius());
        return radiusTop + radiusBottom + super.getSuggestedMinimumHeight();
    }

    @NonNull
    @Override
    public RoundedShadowOutlineProvider getShadowOutlineProvider() {
        return mRoundedShadowOutlineProvider;
    }

    @VisibleForTesting
    @Override
    public void setShadowOutlineProvider(@Nullable ShadowOutlineProvider shadowOutlineProvider) {
        throw new UnsupportedOperationException("setShadowOutlineProvider() is not supported");
    }

    public float getTopLeftCornerRadius() {
        return mRoundedShadowOutlineProvider.getTopLeftCornerRadius();
    }

    public float getTopRightCornerRadius() {
        return mRoundedShadowOutlineProvider.getTopRightCornerRadius();
    }

    public float getBottomRightCornerRadius() {
        return mRoundedShadowOutlineProvider.getBottomRightCornerRadius();
    }

    public float getBottomLeftCornerRadius() {
        return mRoundedShadowOutlineProvider.getBottomLeftCornerRadius();
    }

    public float getMaxCornerRadius() {
        return mRoundedShadowOutlineProvider.getMaxCornerRadius();
    }

    public boolean hasRoundedCorner() {
        return mRoundedShadowOutlineProvider.hasRoundedCorner();
    }

    public boolean areCornersRadiusSame() {
        return mRoundedShadowOutlineProvider.areCornersRadiusSame();
    }

    public boolean isRoundedCornerRadiusAdaptation() {
        return mRoundedCornerRadiusAdaptation;
    }

    public void setRoundedCornerRadiusAdaptation(boolean roundedCornerRadiusAdaptation) {
        if (mRoundedCornerRadiusAdaptation != roundedCornerRadiusAdaptation) {
            mRoundedCornerRadiusAdaptation = roundedCornerRadiusAdaptation;
            requestLayout();
        }
    }

    public void setCornerRadius(float cornerRadius) {
        setCornerRadius(cornerRadius, cornerRadius, cornerRadius, cornerRadius);
    }

    public void setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        mRoundedShadowOutlineProvider.setCornerRadius(topLeft, topRight, bottomRight, bottomLeft);
    }

    public static class RoundedShadowOutlineProvider extends ShadowOutlineProvider {
        private final RectF mRoundRect = new RectF();
        private final float[] mRoundRadius = new float[8];

        @Override
        public void buildShadowOutline(@NonNull ShadowLayout shadowLayout,
                                       @NonNull Path shadowOutline,
                                       @NonNull RectF shadowInsets) {
            if (shadowLayout.isInnerShadow()) {
                mRoundRect.set(Math.max(shadowInsets.left, 0), Math.max(shadowInsets.top, 0),
                        shadowLayout.getWidth() - Math.max(shadowInsets.right, 0),
                        shadowLayout.getHeight() - Math.max(shadowInsets.bottom, 0));
            } else {
                mRoundRect.set(0, 0, shadowLayout.getWidth(), shadowLayout.getHeight());
            }
            shadowOutline.addRoundRect(mRoundRect, mRoundRadius, Path.Direction.CW);
        }

        public float getTopLeftCornerRadius() {
            return Math.max(getTopLeftCornerRadiusX(), getTopLeftCornerRadiusY());
        }

        public float getTopRightCornerRadius() {
            return Math.max(getTopRightCornerRadiusX(), getTopRightCornerRadiusY());
        }

        public float getBottomRightCornerRadius() {
            return Math.max(getBottomRightCornerRadiusX(), getBottomRightCornerRadiusY());
        }

        public float getBottomLeftCornerRadius() {
            return Math.max(getBottomLeftCornerRadiusX(), getBottomLeftCornerRadiusY());
        }

        public float getMaxCornerRadius() {
            return Math.max(
                    Math.max(getTopLeftCornerRadius(), getTopRightCornerRadius()),
                    Math.max(getBottomRightCornerRadius(), getBottomLeftCornerRadius())
            );
        }

        public boolean hasRoundedCorner() {
            return getMaxCornerRadius() > 0;
        }

        public boolean areCornersRadiusSame() {
            return mRoundRadius[0] == mRoundRadius[1] &&
                    mRoundRadius[0] == mRoundRadius[2] &&
                    mRoundRadius[0] == mRoundRadius[3] &&
                    mRoundRadius[0] == mRoundRadius[4] &&
                    mRoundRadius[0] == mRoundRadius[5] &&
                    mRoundRadius[0] == mRoundRadius[6] &&
                    mRoundRadius[0] == mRoundRadius[7];
        }

        public void setCornerRadius(float cornerRadius) {
            setCornerRadius(cornerRadius, cornerRadius, cornerRadius, cornerRadius);
        }

        public void setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
            topLeft = Math.max(topLeft, 0F);
            topRight = Math.max(topRight, 0F);
            bottomRight = Math.max(bottomRight, 0F);
            bottomLeft = Math.max(bottomLeft, 0F);
            boolean changed = false;
            if (getTopLeftCornerRadius() != topLeft) {
                changed = true;
                setTopLeftCornerRadiusX(topLeft);
                setTopLeftCornerRadiusY(topLeft);
            }
            if (getTopRightCornerRadius() != topRight) {
                changed = true;
                setTopRightCornerRadiusX(topRight);
                setTopRightCornerRadiusY(topRight);
            }
            if (getBottomRightCornerRadius() != bottomRight) {
                changed = true;
                setBottomRightCornerRadiusX(bottomRight);
                setBottomRightCornerRadiusY(bottomRight);
            }
            if (getBottomLeftCornerRadius() != bottomLeft) {
                changed = true;
                setBottomLeftCornerRadiusX(bottomLeft);
                setBottomLeftCornerRadiusY(bottomLeft);
            }
            if (changed) {
                invalidateShadowOutline();
            }
        }

        private float getTopLeftCornerRadiusX() {
            return mRoundRadius[0];
        }

        private float getTopLeftCornerRadiusY() {
            return mRoundRadius[1];
        }

        private float getTopRightCornerRadiusX() {
            return mRoundRadius[2];
        }

        private float getTopRightCornerRadiusY() {
            return mRoundRadius[3];
        }

        private float getBottomRightCornerRadiusX() {
            return mRoundRadius[4];
        }

        private float getBottomRightCornerRadiusY() {
            return mRoundRadius[5];
        }

        private float getBottomLeftCornerRadiusX() {
            return mRoundRadius[6];
        }

        private float getBottomLeftCornerRadiusY() {
            return mRoundRadius[7];
        }

        private void setTopLeftCornerRadiusX(float cornerRadius) {
            mRoundRadius[0] = cornerRadius;
        }

        private void setTopLeftCornerRadiusY(float cornerRadius) {
            mRoundRadius[1] = cornerRadius;
        }

        private void setTopRightCornerRadiusX(float cornerRadius) {
            mRoundRadius[2] = cornerRadius;
        }

        private void setTopRightCornerRadiusY(float cornerRadius) {
            mRoundRadius[3] = cornerRadius;
        }

        private void setBottomRightCornerRadiusX(float cornerRadius) {
            mRoundRadius[4] = cornerRadius;
        }

        private void setBottomRightCornerRadiusY(float cornerRadius) {
            mRoundRadius[5] = cornerRadius;
        }

        private void setBottomLeftCornerRadiusX(float cornerRadius) {
            mRoundRadius[6] = cornerRadius;
        }

        private void setBottomLeftCornerRadiusY(float cornerRadius) {
            mRoundRadius[7] = cornerRadius;
        }
    }
}
