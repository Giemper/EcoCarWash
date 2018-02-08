package com.giemper.ecocarwash;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gmoma on 2/6/2018.
 */

public class SquareButton extends AppCompatButton
{
    public SquareButton(Context context)
    {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        if(width > height)
            setMeasuredDimension(height, height);
        else
            setMeasuredDimension(width, width);
    }
}
