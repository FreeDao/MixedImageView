package im.hua.mixedimageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by ReindeerJob on 15/5/31.
 */
public class MixedView extends View {
    private Context context;

    private Bitmap mBmpBack;

    private Bitmap mBmpFront;

    public MixedView(Context context) {
        super(context);
        init(context);
    }

    public MixedView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);

    }

    public MixedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private Matrix backScaleMatrix;
    private Matrix frontMatrix;

    private Bitmap frontTmpBitmap;

    private float initScale = 0.3f;

    private float maxScale = 1f;

    private float minScale = 0.5f;

    private void init(Context context) {
        this.context = context;

        //mBmpBack = BitmapFactory.decodeResource(getResources(), R.drawable.welcome);

        mBmpFront = BitmapFactory.decodeResource(getResources(), R.drawable.welcome);

        backScaleMatrix = new Matrix();


        frontMatrix = new Matrix();
        frontMatrix.postScale(0.3f, 0.3f);


    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        this.mBmpBack = null;
        this.mBmpBack = backgroundBitmap;
        if (mBmpBack != null) {

            float scale = 1f * this.getWidth() / mBmpBack.getWidth();
            backScaleMatrix.postScale(scale, scale);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (mBmpBack != null) {
            canvas.drawBitmap(mBmpBack, backScaleMatrix, null);

        }
        
        frontTmpBitmap = Bitmap.createBitmap(mBmpFront, 0, 0, mBmpFront.getWidth(), mBmpFront.getHeight(), frontMatrix, false);
        canvas.drawBitmap(frontTmpBitmap, this.getWidth() / 2 - frontTmpBitmap.getWidth() / 2, this.getHeight() / 2 - frontTmpBitmap.getHeight() / 2, null);

    }

    private float oldAngle;

    private float oldDistance;

    private PointF midPoint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("view", "touch getPointerCount = " + event.getPointerCount());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() >= 2) {
                    Log.d("view", "down getPointerCount = " + event.getPointerCount());

                    midPoint = getMidPoint(event);
                    oldAngle = getAngle(event);
                    oldDistance = getDistance(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {

                } else if (event.getPointerCount() >= 2) {

                    //获取旋转角，这里可以不用
                    float angle = getAngle(event) - oldAngle;//getAngle(oldEvent);


                    float mScale = getDistance(event) / oldDistance;
                    mScale = (mScale > 1f ? 1f : (mScale < 0.5f ? 0.5f : mScale));
                    Log.d("view", "angle" + angle + " scale = " + mScale);
                    //以中点为中心，进行缩放
                    frontMatrix.postScale(mScale, mScale, midPoint.x, midPoint.y);
                    //以中点为中心，进行旋转，这里可以不用
                    frontMatrix.postRotate(angle, midPoint.x, midPoint.y);

                    //oldAngle = getAngle(event);

                    this.invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }

    //获得旋转角
    public float getAngle(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() >= 2) {
            double deltalX = motionEvent.getX(0) - motionEvent.getX(1);
            double deltalY = motionEvent.getY(0) - motionEvent.getY(1);

            float angle = (float) Math.atan2(deltalX, deltalY);
            Log.d("view", "getAngle = " + angle);

            return angle;
        }
        return 0f;
    }

    //返回两点的中点
    @SuppressLint("FloatMath")
    public PointF getMidPoint(MotionEvent Event) {
        float X = Event.getX(0) + Event.getX(1);
        float Y = Event.getY(0) + Event.getY(1);
        return new PointF(X / 2, Y / 2);
    }

    //返回两点间的距离
    public float getDistance(MotionEvent Event) {
        //计算X的变化量
        float DeltalX = Event.getX(0) - Event.getX(1);
        //计算Y的变化量
        float DeltalY = Event.getY(0) - Event.getY(1);
        //计算距离
        return FloatMath.sqrt(DeltalX * DeltalX + DeltalY * DeltalY);
    }
}
