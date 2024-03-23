package com.example.deserialization_test;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import wave_test.SineWaveData;

import java.util.ArrayList;
import java.util.List;

public class SineWaveView extends View {
    private static final int MAX_DATA_SIZE = 100;
    private List<SineWaveData> dataList = new ArrayList<>(MAX_DATA_SIZE);
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

    public void addData(SineWaveData data) {
        // 如果数据列表超过最大大小，移除最旧的数据点
        if (dataList.size() == MAX_DATA_SIZE) {
            dataList.remove(0);
        }
        dataList.add(data);
        invalidate(); // 通知视图重新绘制
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // 获取视图的宽度和高度
        float width = getWidth();
        float height = getHeight();

        // 保持绘图区域横向占据整个屏幕，竖直方向上占据半个屏幕高度
        float drawingWidth = width; // 横向占据整个屏幕宽度
        float drawingHeight = height / 2; // 竖直方向上只占据半个屏幕高度
        float startX = 0; // 绘图起始点X坐标，从屏幕最左侧开始
        float startY = height / 4; // 绘图起始点Y坐标，使绘图区域在竖直方向上居中

        // 计算缩放因子以适应绘图区域
        float scaleX = drawingWidth / (dataList.size() - 1); // 横向缩放因子，确保所有点横向铺满整个屏幕
        float maxYValue = 50; // 定义sineValue的最大值
        float scaleY = drawingHeight / (2 * maxYValue); // 纵向缩放因子，考虑到正弦波的最大值为1，最小值为-1

        float lastX = -1;
        float lastY = -1;

        for (int i = 0; i < dataList.size(); i++) {
            SineWaveData data = dataList.get(i);
            float x = startX + i * scaleX; // 考虑绘图起始点
            // 假设sineValue的范围为[-1, 1]，将其映射到绘图区域的高度
            float y = startY + drawingHeight / 2 - (float) (data.getSineValue() * scaleY);

            if (lastX != -1 && lastY != -1) {
                canvas.drawLine(lastX, lastY, x, y, paint);
            }

            lastX = x;
            lastY = y;
        }
    }



}
