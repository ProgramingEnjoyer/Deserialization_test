package com.example.deserialization_test;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import wave_test.SineWaveData;

import java.util.List;

public class SineWaveView extends View {
    private List<SineWaveData> dataList;
    private Paint paint = new Paint();

    public SineWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(0xFF0077CC); // 蓝色
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setDataList(List<SineWaveData> dataList) {
        this.dataList = dataList;
        invalidate(); // 重新绘制视图
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        float width = getWidth();
        float height = getHeight();
        float lastX = -1;
        float lastY = -1;

        for (int i = 0; i < dataList.size(); i++) {
            SineWaveData data = dataList.get(i);
            float x = i * width / dataList.size();
            float y = height / 2 + (float) (data.getSineValue() * height / 2);

            if (lastX != -1 && lastY != -1) {
                canvas.drawLine(lastX, lastY, x, y, paint);
            }

            lastX = x;
            lastY = y;
        }
    }
}
