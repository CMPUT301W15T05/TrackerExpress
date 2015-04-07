package group5.trackerexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ViewImageDialog extends Activity {

    private ImageView receiptImageView;

    EditBitmap editBitmap = new EditBitmap();
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dialog);
        
        receiptImageView = (ImageView)findViewById(R.id.ivReceipt);

        Intent intent = getIntent();
        String filePath = (String) intent.getStringExtra("filePath");
        
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        Bitmap rotatedBitmap = editBitmap.rotateBitmap(bitmap);
        receiptImageView.setImageBitmap(rotatedBitmap);
        
        receiptImageView.setClickable(true);


        //finish the activity (dismiss the image dialog) if the user clicks 
        //anywhere on the image
        receiptImageView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
        });

    }
}
