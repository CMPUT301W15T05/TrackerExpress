package group5.trackerexpress;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ViewImageDialog extends Activity {

	private ImageView receiptImageView;

	EditBitmap editBitmap = new EditBitmap();

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dialog);
        
        receiptImageView = (ImageView)findViewById(R.id.ivReceipt);

        Intent intent = getIntent();
        UUID claimUuid = (UUID) intent.getSerializableExtra("claimId");
        UUID expenseUuid = (UUID) intent.getSerializableExtra("expenseId");        
        Claim claim;
        
        Bitmap bitmap;
		try {
			claim = new ElasticSearchEngineClaims().getClaim(this, claimUuid);
			
			bitmap = claim.getExpenseList().getExpense(expenseUuid).getReceipt().getBitmap();			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		} catch (ExpenseNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException();			
		}
		
        
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
