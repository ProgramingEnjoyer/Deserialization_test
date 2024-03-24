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
        paint.setColor(0xFF0077CC); // Blue
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setDataList(List<SineWaveData> dataList) {
        this.dataList = dataList;
        invalidate(); // Redraw the view
    }

    public void addData(SineWaveData data) {
        // If the data list exceeds the maximum size, remove the oldest data point
        if (dataList.size() == MAX_DATA_SIZE) {
            dataList.remove(0);
        }
        dataList.add(data);
        invalidate(); // Notify the view to redraw
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // Define the paint for drawing axes
        Paint axisPaint = new Paint();
        axisPaint.setColor(0xFF000000); // Black
        axisPaint.setStrokeWidth(3);
        axisPaint.setTextSize(30); // Set text size

        // Get the width and height of the view
        float width = getWidth();
        float height = getHeight();

        // Draw the X axis
        canvas.drawLine(0, height / 2, width, height / 2, axisPaint);
        // Draw the Y axis
        canvas.drawLine(width / 2, 0, width / 2, height, axisPaint);

        // Draw the arrows for the X and Y axes (optional)
        canvas.drawLine(width, height / 2, width - 20, height / 2 - 10, axisPaint);
        canvas.drawLine(width, height / 2, width - 20, height / 2 + 10, axisPaint);
        canvas.drawLine(width / 2, 0, width / 2 - 10, 20, axisPaint);
        canvas.drawLine(width / 2, 0, width / 2 + 10, 20, axisPaint);

        // Keep the drawing area to span the entire screen width and half the screen height vertically
        float drawingWidth = width; // Span the entire screen width horizontally
        float drawingHeight = height / 2; // Vertically, occupy only half the screen height
        float startX = 0; // Drawing start X coordinate, starting from the screen's left edge
        float startY = height / 4; // Drawing start Y coordinate, centering the drawing area vertically

        String originLabel = "(0,0)";
        // Calculate the text width to center it
        float textWidth = axisPaint.measureText(originLabel);
        // Draw the text label below and to the left of the origin
        canvas.drawText(originLabel, width / 2 - textWidth - 10, height / 2 + axisPaint.getTextSize() + 5, axisPaint);

        // Calculate scaling factors to fit the drawing area
        float scaleX = drawingWidth / (dataList.size() - 1); // Horizontal scaling factor to ensure points span the entire screen width
        float maxYValue = 50; // Define the maximum value for sineValue
        float scaleY = drawingHeight / (2 * maxYValue); // Vertical scaling factor, considering the sine wave's maximum and minimum values of 1 and -1, respectively

        float lastX = -1;
        float lastY = -1;

        for (int i = 0; i < dataList.size(); i++) {
            SineWaveData data = dataList.get(i);
            float x = startX + i * scaleX; // Considering the drawing start point
            // Assuming the range of sineValue is [-1, 1], map it to the height of the drawing area
            float y = startY + drawingHeight / 2 - (float) (data.getSineValue() * scaleY);

            if (lastX != -1 && lastY != -1) {
                canvas.drawLine(lastX, lastY, x, y, paint);
            }

            lastX = x;
            lastY = y;
        }
    }



}
