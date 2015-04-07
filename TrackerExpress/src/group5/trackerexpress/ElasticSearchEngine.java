/**
 * 
 */
package group5.trackerexpress;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

/**
 * Performs elastic search operations needed by this app.
 * 
 * 
 * @author crinklaw
 *
 */
public class ElasticSearchEngine {

	
	private final ElasticSearchEngineUnthreaded elasicSearchEngineUnthreaded = new ElasticSearchEngineUnthreaded();
	

	public Claim[] getClaims() {
		
		final Claim[][] claims = new Claim[1][];

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				claims[0] = elasicSearchEngineUnthreaded.getClaims();
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
		return claims[0];
	}
	
	/**
	 * Gets the claims that user can approve, i.e. claims that will appear in Global claims list. 
	 * As of now, that means claims that have no attached approver besides the current user,
	 * were not submitted by the current user, and are not in returned or approved state.
	 * @return filtered claims list
	 */
	public Claim[] getClaimsForGlobalClaimList(Context context) {
		
		Claim[] claimsUnfiltered = getClaims();
		List<Claim> claims = new ArrayList<Claim>();
		Log.e("USER", Controller.getUser(context).getEmail().toString());
		for (Claim claim : claimsUnfiltered){
			Log.e(claim.getClaimName(), claim.getSubmitterEmail());
			try{
			Log.e(claim.getClaimName(), claim.getApproverEmail());
			} catch (NullPointerException e) {Log.e(claim.getClaimName(), "NULL APPROVER");}
			if (    !claim.getSubmitterEmail().equals(Controller.getUser(context).getEmail()) &&
					(claim.getApproverEmail() == null || claim.getApproverEmail().equals(Controller.getUser(context).getEmail())) &&
					 claim.getStatus() != Claim.IN_PROGRESS){
				
				claims.add(claim);
			}
		}
		
		return claims.toArray(new Claim[claims.size()]);
	}
	
	
	
	public Claim getClaim(UUID id) {
		
		final Claim[] claim = new Claim[1];
		final UUID idFinal = id;

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					claim[0] = elasicSearchEngineUnthreaded.getClaim(idFinal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
		return claim[0];
	}	




	public void submitClaim(Context context, Claim claim) {
		
		claim.setStatus(context, Claim.SUBMITTED);
		claim.setSubmitterName(context, Controller.getUser(context).getName());
		claim.setSubmitterEmail(context, Controller.getUser(context).getEmail());
		Log.e("RECEIPTSUBMIT", "Submitting Receipt");
		
		//convert UriBitmaps to actual bitmaps
		for (Expense expense : claim.getExpenseList().toList()){

			try {
				expense.getReceipt().switchToStoringActualBitmap();
			} catch (NullPointerException e) {}

		}
		Log.e("RECEIPTSTORED", "Submitted RECEIPT!!!!!!");
		final Claim claimFinal = claim;
		
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.submitClaim(claimFinal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
		
		//switch back for saving:
		for (Expense expense : claim.getExpenseList().toList()){
			try {
				expense.getReceipt().stopStoringActualBitmap();
			} catch (NullPointerException e) {}
		}		

	}



	public void deleteClaim(UUID id){
		final UUID idFinal = id;

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.deleteClaim(idFinal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}



	public void approveClaim(Context context, UUID id, String comments){
		
		final UUID idFinal = id;
		final String commentsFinal = comments;
		final String approverName = Controller.getUser(context).getName();
		final String approverEmail = Controller.getUser(context).getEmail();

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.approveClaim(idFinal, commentsFinal, approverName, approverEmail);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		/*try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}*/
	}


	public void returnClaim(Context context, UUID id, String comments){
		
		final UUID idFinal = id;
		final String commentsFinal = comments;
		final String approverName = Controller.getUser(context).getName();
		final String approverEmail = Controller.getUser(context).getEmail();

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.returnClaim(idFinal, commentsFinal, approverName, approverEmail);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		/*try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}*/
	}


}
