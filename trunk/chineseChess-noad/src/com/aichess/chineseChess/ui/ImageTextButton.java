package com.aichess.chineseChess.ui;

import com.aichess.chineseChess.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

public class ImageTextButton extends Button {
    private int resourceId = 0;
    private Bitmap bitmap;
    private String text;
    public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ImageTextButton(Context context, AttributeSet attrs) {
       super(context, attrs);
       setClickable(true);
       TypedArray a = context.obtainStyledAttributes(attrs,
               R.styleable.ImageTextButton);
       resourceId = a.getResourceId(R.styleable.ImageTextButton_bitmap, R.drawable.selected);
       bitmap = BitmapFactory.decodeResource(getResources(), resourceId); 
       bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, true);
	   this.text = a.getString(R.styleable.ImageTextButton_text);
	   a.recycle();
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        // 图片顶部居中显示
    	this.setBackgroundColor(0xff1C86EE);
        int x = (this.getMeasuredWidth() - bitmap.getWidth()) >> 1;
        int y = 2;
        canvas.drawBitmap(bitmap, x, y, null);
        // 坐标需要转换，因为默认情况下Button中的文字居中显示
        // 这里需要让文字在底部显示
        canvas.translate(0,
                (this.getMeasuredHeight() >> 1) - (int) this.getTextSize());
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(18);
        float width = paint.measureText(text);
        canvas.drawText(text, (this.getMeasuredWidth() - width)/2, bitmap.getHeight()-2, paint);
        super.onDraw(canvas);
    }
 
    public void setIcon(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }
 
    public void setIcon(int resourceId) {
        this.bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        invalidate();
    }
}
