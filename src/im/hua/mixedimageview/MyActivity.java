package im.hua.mixedimageview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class MyActivity extends Activity {
    private MixedView mixedView;
    private Button mBtmCapture;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mixedView = (MixedView) findViewById(R.id.main_mv);
        mBtmCapture = (Button) findViewById(R.id.main_btn_capture);

        mBtmCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

    }

    public void captureImage()
    {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0 && resultCode==Activity.RESULT_OK)
        {
            Bitmap myBitmap = (Bitmap) data.getExtras().get("data");
            mixedView.setBackgroundBitmap(myBitmap);
        }
    }
}
