package per.goweii.shadowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * 阴影布局
 * <p>
 * 支持自定义边界形状
 * 可以通过自定义{@link ShadowLayout#setShadowOutlineProvider(ShadowOutlineProvider)}自行控制阴影的边界。
 * <p>
 * 支持内阴影和外阴影
 * 通过设置{@link ShadowLayout#setShadowRadius(float)}为正数或者负数可实现内阴影或外阴影效果。
 * 内阴影：阴影会占据布局空间，即留出阴影的padding。
 * 外阴影：阴影会不会占据布局空间。
 */
public class ShadowLayout extends FrameLayout {
    private final Paint mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mShadowInsets = new RectF();
    private final Path mShadowOutline = new Path();
    private final PorterDuffXfermode mXfermodeDstOut = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    private boolean mShadowSymmetry = false;
    private int mShadowColor = Color.argb(25, 0, 0, 0);
    private float mShadowRadius = 0F;
    private float mShadowOffsetX = 0F;
    private float mShadowOffsetY = 0F;

    private boolean mClipToShadowOutline = true;

    private boolean mShadowOutlineInvalidate = false;
    private ShadowOutlineProvider mShadowOutlineProvider = null;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowColor, mShadowColor);
        mShadowSymmetry = typedArray.getBoolean(R.styleable.ShadowLayout_shadowSymmetry, mShadowSymmetry);
        mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadius, mShadowRadius);
        mShadowOffsetX = typedArray.getDimension(R.styleable.ShadowLayout_shadowOffsetX, mShadowOffsetX);
        mShadowOffsetY = typedArray.getDimension(R.styleable.ShadowLayout_shadowOffsetY, mShadowOffsetY);
        typedArray.recycle();
    }

    public void setShadowOutlineProvider(@Nullable ShadowOutlineProvider shadowOutlineProvider) {
        if (shadowOutlineProvider == null) {
            if (mShadowOutlineProvider != null) {
                mShadowOutlineProvider.detachFromShadowLayout();
                mShadowOutlineProvider = null;
                invalidateShadowOutline();
            }
        } else {
            if (mShadowOutlineProvider != shadowOutlineProvider) {
                if (mShadowOutlineProvider != null) {
                    mShadowOutlineProvider.detachFromShadowLayout();
                }
                mShadowOutlineProvider = shadowOutlineProvider;
                mShadowOutlineProvider.attachToShadowLayout(this);
                invalidateShadowOutline();
            }
        }
    }

    @Nullable
    public ShadowOutlineProvider getShadowOutlineProvider() {
        return mShadowOutlineProvider;
    }

    public void setClipToShadowOutline(boolean clipToShadowOutline) {
        if (mClipToShadowOutline != clipToShadowOutline) {
            mClipToShadowOutline = clipToShadowOutline;
            invalidateShadowOutline();
        }
    }

    public boolean isClipToShadowOutline() {
        return mClipToShadowOutline;
    }

    public void setShadowColor(int shadowColor) {
        if (mShadowColor != shadowColor) {
            mShadowColor = shadowColor;
            invalidate();
        }
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public void setShadowRadius(float shadowRadius) {
        if (mShadowRadius != shadowRadius) {
            mShadowRadius = shadowRadius;
            invalidateShadowOutline();
            invalidate();
        }
    }

    public float getShadowRadius() {
        return mShadowRadius;
    }

    public boolean isInnerShadow() {
        return mShadowRadius > 0;
    }

    public boolean isOuterShadow() {
        return mShadowRadius < 0;
    }

    public boolean hasShadow() {
        return mShadowRadius != 0;
    }

    public void setShadowSymmetry(boolean shadowSymmetry) {
        if (mShadowSymmetry != shadowSymmetry) {
            mShadowSymmetry = shadowSymmetry;
            invalidateShadowOutline();
            invalidate();
        }
    }

    public boolean isShadowSymmetry() {
        return mShadowSymmetry;
    }

    public void setShadowOffsetX(float shadowOffsetX) {
        if (mShadowOffsetX != shadowOffsetX) {
            mShadowOffsetX = shadowOffsetX;
            invalidateShadowOutline();
            invalidate();
        }
    }

    public float getShadowOffsetX() {
        return mShadowOffsetX;
    }

    public void setShadowOffsetY(float shadowOffsetY) {
        if (mShadowOffsetY != shadowOffsetY) {
            mShadowOffsetY = shadowOffsetY;
            invalidateShadowOutline();
            invalidate();
        }
    }

    public float getShadowOffsetY() {
        return mShadowOffsetY;
    }

    public RectF getShadowInsets() {
        return mShadowInsets;
    }

    public Path getShadowOutline() {
        return mShadowOutline;
    }

    public void invalidateShadowOutline() {
        mShadowOutlineInvalidate = true;
        setParentClipChildren(!isOuterShadow());
        updateShadowInsets(mShadowInsets);
        updatePadding();
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setParentClipChildren(!isOuterShadow());
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        int padding = getPaddingLeft() + getPaddingRight();
        if (isInnerShadow()) {
            int insides = (int) (mShadowInsets.left + mShadowInsets.right + 0.5F);
            return Math.max(padding, insides);
        } else {
            return padding;
        }
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        int padding = getPaddingTop() + getPaddingBottom();
        if (isInnerShadow()) {
            int insides = (int) (mShadowInsets.top + mShadowInsets.bottom + 0.5F);
            return Math.max(padding, insides);
        } else {
            return padding;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setParentClipChildren(!isOuterShadow());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidateShadowOutline();
    }

    protected void updateShadowInsets(@NonNull RectF shadowInsets) {
        float l = 0, r = 0, t = 0, b = 0;
        if (isInnerShadow()) {
            l = Math.max(mShadowRadius - mShadowOffsetX, 0);
            r = Math.max(mShadowRadius + mShadowOffsetX, 0);
            t = Math.max(mShadowRadius - mShadowOffsetY, 0);
            b = Math.max(mShadowRadius + mShadowOffsetY, 0);
            if (mShadowSymmetry) {
                l = r = Math.max(l, r);
                t = b = Math.max(t, b);
            }
        }
        shadowInsets.set(l, t, r, b);
    }

    protected void updatePadding() {
        if (mClipToShadowOutline) {
            super.setPadding((int) (mShadowInsets.left + 0.5F),
                    (int) (mShadowInsets.top + 0.5F),
                    (int) (mShadowInsets.right + 0.5F),
                    (int) (mShadowInsets.bottom + 0.5F));
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mShadowOutlineInvalidate) {
            rebuildOutlinePath();
        }

        drawShadow(canvas);

        if (mClipToShadowOutline) {
            if (isInEditMode()) {
                canvas.save();
                canvas.clipPath(mShadowOutline);
                super.draw(canvas);
                canvas.restore();
            } else {
                int saveLayerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
                super.draw(canvas);
                mShadowOutline.toggleInverseFillType();
                mShadowPaint.setStyle(Paint.Style.FILL);
                mShadowPaint.clearShadowLayer();
                mShadowPaint.setColor(Color.BLACK);
                mShadowPaint.setXfermode(mXfermodeDstOut);
                canvas.drawPath(mShadowOutline, mShadowPaint);
                mShadowPaint.setXfermode(null);
                mShadowOutline.toggleInverseFillType();
                canvas.restoreToCount(saveLayerId);
            }
        } else {
            super.draw(canvas);
        }
    }

    protected void drawShadow(@NonNull Canvas canvas) {
        int saveLayerId;

        if (isInnerShadow()) {
            saveLayerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        } else if (isOuterShadow()) {
            saveLayerId = canvas.saveLayer(
                    mShadowRadius + mShadowOffsetX,
                    mShadowRadius + mShadowOffsetY,
                    getWidth() - mShadowRadius + mShadowOffsetX,
                    getHeight() - mShadowRadius + mShadowOffsetY,
                    null, Canvas.ALL_SAVE_FLAG
            );
        } else {
            return;
        }

        mShadowPaint.setStyle(Paint.Style.FILL);

        mShadowPaint.setShadowLayer(Math.abs(mShadowRadius) * 0.75F, mShadowOffsetX, mShadowOffsetY, mShadowColor);
        mShadowPaint.setColor(Color.TRANSPARENT);
        mShadowPaint.setXfermode(null);
        canvas.drawPath(mShadowOutline, mShadowPaint);

        mShadowPaint.clearShadowLayer();
        mShadowPaint.setColor(Color.BLACK);
        mShadowPaint.setXfermode(mXfermodeDstOut);
        canvas.drawPath(mShadowOutline, mShadowPaint);
        mShadowPaint.setXfermode(null);

        canvas.restoreToCount(saveLayerId);
    }

    private void setParentClipChildren(boolean clipChildren) {
        ViewParent viewParent = getParent();
        if (viewParent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) viewParent;
            viewGroup.setClipChildren(clipChildren);
        }
    }

    private void rebuildOutlinePath() {
        mShadowOutline.reset();
        mShadowOutline.rewind();
        if (mShadowOutlineProvider != null) {
            mShadowOutlineProvider.buildShadowOutline(this, mShadowOutline, mShadowInsets);
        }
        if (!mShadowOutline.isEmpty()) {
            mShadowOutline.close();
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
    }

    public static abstract class ShadowOutlineProvider {
        private WeakReference<ShadowLayout> mShadowLayoutRef = null;

        private void attachToShadowLayout(@NonNull ShadowLayout shadowLayout) {
            if (mShadowLayoutRef == null || mShadowLayoutRef.get() != shadowLayout) {
                mShadowLayoutRef = new WeakReference<>(shadowLayout);
            }
        }

        private void detachFromShadowLayout() {
            if (mShadowLayoutRef != null) {
                mShadowLayoutRef.clear();
                mShadowLayoutRef = null;
            }
        }

        public void invalidateShadowOutline() {
            if (mShadowLayoutRef != null && mShadowLayoutRef.get() != null) {
                mShadowLayoutRef.get().invalidateShadowOutline();
            }
        }

        public abstract void buildShadowOutline(
                @NonNull ShadowLayout shadowLayout,
                @NonNull Path shadowOutline,
                @NonNull RectF shadowInsets
        );
    }
}
