package com.example.onsitetwo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

public class PaintView extends View {

    private Context context;
    private int brushSize = 12,erasersize = 12;
    private Paint paint = new Paint (  );
    private Path path = new Path ( );
    private Canvas canvas;
    private Bitmap bitmap;
    public static Bitmap current;
    private float X,Y;
    private FragmentViewModel viewModel;
    public static ArrayList<Bitmap> listAction = new ArrayList<> (  );

    public PaintView( Context context , @Nullable AttributeSet attrs ) {
        super ( context , attrs );
        this.context = context;
        initializePaint();
    }

    public void setBrushSize( int brushSize ) {
        paint.setStrokeWidth ( px ( brushSize ) );
    }

    private void initializePaint() {
        paint.setColor ( Color.BLACK );
        paint.setAntiAlias ( true );
        paint.setDither ( true );
        paint.setStrokeCap ( Paint.Cap.ROUND );
        paint.setStyle ( Paint.Style.STROKE );
        paint.setStrokeWidth ( px ( brushSize ) );
    }

    public void addLastAction(Bitmap bitmap){
        listAction.add ( bitmap );
    }

    private float px( int size){
        return size*(getResources ().getDisplayMetrics ().density);
    }

    @Override
    protected void onSizeChanged( int w , int h , int oldw , int oldh ) {
        super.onSizeChanged ( w , h , oldw , oldh );
        bitmap = Bitmap.createBitmap ( w,h,Bitmap.Config.ARGB_8888 );
        canvas = new Canvas ( bitmap );
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        super.onDraw ( canvas );
        canvas.drawColor ( Color.TRANSPARENT );
        canvas.drawBitmap ( bitmap,0,0,null );
    }


    @Override
    public boolean onTouchEvent( MotionEvent event ) {

        float x = event.getX ();
        float y = event.getY ();

        switch (event.getAction ()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo ( x,y );
                current = bitmap;
                addLastAction ( bitmap );
                X = x;
                Y = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs ( x - X );
                float dy = Math.abs ( y - Y );

                if (dx >= 4 || dy >= 4){
                    path.lineTo ( x,y );
                    X = x;
                    Y = y;
                    canvas.drawPath ( path,paint );
                    invalidate ();
                    current = bitmap;
                    addLastAction ( bitmap );
                }

                break;
            case MotionEvent.ACTION_UP:
                current = bitmap;
                addLastAction ( bitmap );
                path.reset ();
                break;
        }

        return true;

    }



    public Bitmap getBitmap(){
        this.setDrawingCacheEnabled ( true );
        this.buildDrawingCache ();
        Bitmap bitmap = Bitmap.createBitmap ( this.getDrawingCache () );
        this.setDrawingCacheEnabled ( false );
        return bitmap;
    }

    public void SetBitmap(Bitmap bitmap){
        canvas.setBitmap ( listAction.get ( listAction.size ()-1 ) );
    }

    public void enableEraser(){
        paint.setXfermode ( new PorterDuffXfermode ( PorterDuff.Mode.CLEAR ) );
    }

    public void disableEraser(){
        paint.setXfermode ( null );
        paint.setShader ( null );
        paint.setMaskFilter ( null );
    }

    public void setBrushColor( int brushColor ) {
        paint.setColor ( brushColor );
    }

    public void setSizeEraser( int eraserSize ) {
        erasersize = eraserSize;
        paint.setStrokeWidth ( px ( erasersize ) );
    }
}
