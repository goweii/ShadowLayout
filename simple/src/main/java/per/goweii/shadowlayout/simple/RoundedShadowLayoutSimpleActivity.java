package per.goweii.shadowlayout.simple;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import per.goweii.shadowlayout.simple.databinding.ActivityRoundedShadowLayoutSimpleBinding;

public class RoundedShadowLayoutSimpleActivity extends AppCompatActivity {
    private ActivityRoundedShadowLayoutSimpleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoundedShadowLayoutSimpleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sbShadowColor.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                int color = ContextCompat.getColor(RoundedShadowLayoutSimpleActivity.this, R.color.purple_500);
                float[] hsl = new float[3];
                ColorUtils.colorToHSL(color, hsl);
                hsl[1] = hsl[1] * (progress / 254F);
                color = ColorUtils.HSLToColor(hsl);
                color = ColorUtils.setAlphaComponent(color, binding.sbShadowColorAlpha.getProgress());
                binding.roundedShadowLayout.setShadowColor(color);
            }
        });
        binding.sbShadowColor.setMax(254);
        binding.sbShadowColor.setProgress(200);

        binding.sbShadowColorAlpha.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                int color = binding.roundedShadowLayout.getShadowColor();
                color = ColorUtils.setAlphaComponent(color, progress);
                binding.roundedShadowLayout.setShadowColor(color);
            }
        });
        binding.sbShadowColorAlpha.setMax(254);
        binding.sbShadowColorAlpha.setProgress(100);

        binding.sbShadowRadius.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.roundedShadowLayout.setShadowRadius(progress - 400);
            }
        });
        binding.sbShadowRadius.setMax(600);
        binding.sbShadowRadius.setProgress(460);

        binding.sbShadowOffsetX.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.roundedShadowLayout.setShadowOffsetX(progress - 200);
            }
        });
        binding.sbShadowOffsetX.setMax(400);
        binding.sbShadowOffsetX.setProgress(200);

        binding.sbShadowOffsetY.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.roundedShadowLayout.setShadowOffsetY(progress - 200);
            }
        });
        binding.sbShadowOffsetY.setMax(400);
        binding.sbShadowOffsetY.setProgress(200);

        binding.sbShadowSymmetry.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.roundedShadowLayout.setShadowSymmetry(progress == 1);
            }
        });
        binding.sbShadowSymmetry.setMax(1);
        binding.sbShadowSymmetry.setProgress(0);

        int maxCornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        binding.sbRoundedCornerRadiusTl.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.roundedShadowLayout.setCornerRadius(
                        progress,
                        binding.roundedShadowLayout.getTopRightCornerRadius(),
                        binding.roundedShadowLayout.getBottomRightCornerRadius(),
                        binding.roundedShadowLayout.getBottomLeftCornerRadius()
                );
            }
        });
        binding.sbRoundedCornerRadiusTl.setMax(maxCornerRadius);

        binding.sbRoundedCornerRadiusTr.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.roundedShadowLayout.setCornerRadius(
                        binding.roundedShadowLayout.getTopLeftCornerRadius(),
                        progress,
                        binding.roundedShadowLayout.getBottomRightCornerRadius(),
                        binding.roundedShadowLayout.getBottomLeftCornerRadius()
                );
            }
        });
        binding.sbRoundedCornerRadiusTr.setMax(maxCornerRadius);

        binding.sbRoundedCornerRadiusBr.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.roundedShadowLayout.setCornerRadius(
                        binding.roundedShadowLayout.getTopLeftCornerRadius(),
                        binding.roundedShadowLayout.getTopRightCornerRadius(),
                        progress,
                        binding.roundedShadowLayout.getBottomLeftCornerRadius()
                );
            }
        });
        binding.sbRoundedCornerRadiusBr.setMax(maxCornerRadius);

        binding.sbRoundedCornerRadiusBl.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.roundedShadowLayout.setCornerRadius(
                        binding.roundedShadowLayout.getTopLeftCornerRadius(),
                        binding.roundedShadowLayout.getTopRightCornerRadius(),
                        binding.roundedShadowLayout.getBottomRightCornerRadius(),
                        progress
                );
            }
        });
        binding.sbRoundedCornerRadiusBl.setMax(maxCornerRadius);

        binding.sbRoundedCornerRadius.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean touch) {
                binding.sbRoundedCornerRadiusTl.setProgress(progress);
                binding.sbRoundedCornerRadiusTr.setProgress(progress);
                binding.sbRoundedCornerRadiusBr.setProgress(progress);
                binding.sbRoundedCornerRadiusBl.setProgress(progress);
            }
        });
        binding.sbRoundedCornerRadius.setMax(maxCornerRadius);
        binding.sbRoundedCornerRadius.setProgress((int) (maxCornerRadius * 0.5));
    }
}