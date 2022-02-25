package per.goweii.shadowlayout.simple;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import per.goweii.popupshadowlayout.PopupShadowLayout;
import per.goweii.shadowlayout.simple.databinding.ActivityPopupShadowLayoutSimpleBinding;

public class PopupShadowLayoutSimpleActivity extends AppCompatActivity {
    private ActivityPopupShadowLayoutSimpleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPopupShadowLayoutSimpleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sbShadowColor.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                int color = ContextCompat.getColor(PopupShadowLayoutSimpleActivity.this, R.color.purple_500);
                float[] hsl = new float[3];
                ColorUtils.colorToHSL(color, hsl);
                hsl[1] = hsl[1] * (progress / 254F);
                color = ColorUtils.HSLToColor(hsl);
                color = ColorUtils.setAlphaComponent(color, binding.sbShadowColorAlpha.getProgress());
                binding.popupShadowLayout.setShadowColor(color);
            }
        });
        binding.sbShadowColor.setMax(254);
        binding.sbShadowColor.setProgress(200);

        binding.sbShadowColorAlpha.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                int color = binding.popupShadowLayout.getShadowColor();
                color = ColorUtils.setAlphaComponent(color, progress);
                binding.popupShadowLayout.setShadowColor(color);
            }
        });
        binding.sbShadowColorAlpha.setMax(254);
        binding.sbShadowColorAlpha.setProgress(100);

        binding.sbShadowRadius.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.popupShadowLayout.setShadowRadius(progress);
            }
        });
        binding.sbShadowRadius.setMax(200);
        binding.sbShadowRadius.setProgress(60);

        binding.sbShadowOffsetX.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.popupShadowLayout.setShadowOffsetX(progress - 200);
            }
        });
        binding.sbShadowOffsetX.setMax(400);
        binding.sbShadowOffsetX.setProgress(200);

        binding.sbShadowOffsetY.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.popupShadowLayout.setShadowOffsetY(progress - 200);
            }
        });
        binding.sbShadowOffsetY.setMax(400);
        binding.sbShadowOffsetY.setProgress(200);

        binding.sbShadowSymmetry.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.popupShadowLayout.setShadowSymmetry(progress == 1);
            }
        });
        binding.sbShadowSymmetry.setMax(1);
        binding.sbShadowSymmetry.setProgress(0);

        int maxCornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        binding.sbPopupCornerRadius.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.popupShadowLayout.setCornerRadius(progress);
            }
        });
        binding.sbPopupCornerRadius.setMax(maxCornerRadius);
        binding.sbPopupCornerRadius.setProgress((int) (maxCornerRadius * 0.2));

        binding.sbArrowSide.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                int side;
                switch (progress) {
                    case 1:
                        side = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_TOP;
                        break;
                    case 2:
                        side = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_LEFT;
                        break;
                    case 3:
                        side = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_RIGHT;
                        break;
                    case 4:
                        side = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_BOTTOM;
                        break;
                    default:
                        side = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_SIDE_NONE;
                        break;
                }
                binding.popupShadowLayout.setArrowSide(side);
            }
        });
        binding.sbArrowSide.setMax(4);
        binding.sbArrowSide.setProgress(1);

        binding.sbArrowWidth.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.popupShadowLayout.setArrowWidth(progress);
            }
        });
        binding.sbArrowWidth.setMax(maxCornerRadius);
        binding.sbArrowWidth.setProgress((int) (maxCornerRadius * 0.2));

        binding.sbArrowHeight.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.popupShadowLayout.setArrowHeight(progress);
            }
        });
        binding.sbArrowHeight.setMax(maxCornerRadius);
        binding.sbArrowHeight.setProgress((int) (maxCornerRadius * 0.2));

        binding.sbArrowRoundedRadius.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.popupShadowLayout.setArrowRadius(progress);
            }
        });
        binding.sbArrowRoundedRadius.setMax(30);
        binding.sbArrowRoundedRadius.setProgress(6);

        binding.sbArrowAlign.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                int align;
                switch (progress) {
                    case 0:
                        align = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_ALIGN_START;
                        break;
                    case 2:
                        align = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_ALIGN_END;
                        break;
                    default:
                        align = PopupShadowLayout.PopupShadowOutlineProvider.ARROW_ALIGN_CENTER;
                        break;
                }
                binding.popupShadowLayout.setArrowAlign(align);
            }
        });
        binding.sbArrowAlign.setMax(2);
        binding.sbArrowAlign.setProgress(1);

        binding.sbArrowOffset.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.popupShadowLayout.setArrowOffset(progress);
            }
        });
        binding.sbArrowOffset.setMax(maxCornerRadius);
        binding.sbArrowOffset.setProgress(0);
    }
}