package com.example.deserialization_test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TangentForceView extends View {
    private static final float MIN_VALUE = 30.0f; // Minimum value
    private static final float MAX_VALUE = 70.0f; // Maximum value
    private static final int MAX_DATA_POINTS = 50; // Maximum number of data points
    private Paint paint1;
    private Paint paint2;
    private Paint axisPaint;
    private Paint textPaint;
    private List<Float> dataList1 = new ArrayList<>();
    private List<Float> dataList2 = new ArrayList<>();
    private float lastValue1 = Float.NaN;
    private float lastValue2 = Float.NaN;

    public TangentForceView(Context context) {
        super(context);
        init();
    }

    public TangentForceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint1 = new Paint();
        paint1.setColor(0xFFFF0000); // Set paint color to red
        paint1.setStrokeWidth(5); // Set paint stroke width

        paint2 = new Paint();
        paint2.setColor(0xFF0000FF); // Set paint color to blue
        paint2.setStrokeWidth(5); // Set paint stroke width

        axisPaint = new Paint();
        axisPaint.setColor(0xFF000000); // Set axis color to black
        axisPaint.setStrokeWidth(2); // Set axis stroke width
        axisPaint.setTextSize(24); // Set axis text size

        textPaint = new Paint();
        textPaint.setColor(0xFF000000); // Set text color to black
        textPaint.setTextSize(40); // Set text size
    }

    public void addData(float value1, float value2) {
        if (value1 != lastValue1 || value2 != lastValue2) {
            if (dataList1.size() >= MAX_DATA_POINTS) {
                dataList1.remove(0);
                dataList2.remove(0);
            }
            dataList1.add(value1);
            dataList2.add(value2);
            lastValue1 = value1;
            lastValue2 = value2;
            invalidate(); // Redraw the view only if data has changed
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = getHeight(); // Get view height
        float width = getWidth(); // Get view width
        float xInterval = width / (float) MAX_DATA_POINTS; // Calculate horizontal interval between each data point

        // Draw title
        canvas.drawText("Tangent Force", width / 2 - 100, 50, textPaint);
        canvas.drawText("Red: Left Wheel, Blue: Right Wheel", width / 2 - 150, 100, textPaint);

        // Draw latest values
        if (!dataList1.isEmpty() && !dataList2.isEmpty()) {
            canvas.drawText("Latest Left Wheel: " + dataList1.get(dataList1.size() - 1), 10, 150, textPaint);
            canvas.drawText("Latest Right Wheel: " + dataList2.get(dataList2.size() - 1), 10, 200, textPaint);
        }

        // Draw axes
        canvas.drawLine(0, height, width, height, axisPaint); // X axis
        canvas.drawLine(0, 0, 0, height, axisPaint); // Y axis

        // Label Y axis
        for (int i = 0; i <= 5; i++) {
            float y = height - (i * height / 5);
            canvas.drawLine(0, y, 10, y, axisPaint);
            canvas.drawText(String.valueOf(MIN_VALUE + i * (MAX_VALUE - MIN_VALUE) / 5), 15, y, axisPaint);
        }

        // Draw data points and connect them with lines
        for (int i = 0; i < dataList1.size() - 1; i++) {
            float x1 = i * xInterval;
            float y1_1 = height - ((dataList1.get(i) - MIN_VALUE) / (MAX_VALUE - MIN_VALUE) * height);
            float y1_2 = height - ((dataList2.get(i) - MIN_VALUE) / (MAX_VALUE - MIN_VALUE) * height);
            float x2 = (i + 1) * xInterval;
            float y2_1 = height - ((dataList1.get(i + 1) - MIN_VALUE) / (MAX_VALUE - MIN_VALUE) * height);
            float y2_2 = height - ((dataList2.get(i + 1) - MIN_VALUE) / (MAX_VALUE - MIN_VALUE) * height);

            canvas.drawLine(x1, y1_1, x2, y2_1, paint1); // Draw red line
            canvas.drawLine(x1, y1_2, x2, y2_2, paint2); // Draw blue line
        }
    }
}
