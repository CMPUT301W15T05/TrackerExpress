/**
 * 
 */
package group5.trackerexpress;

import java.util.UUID;

/**
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
				try {
					claims[0] = elasicSearchEngineUnthreaded.getClaims();
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
		return claims[0];
	}



	public void submitClaim(Claim claim) {
		
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



	public void approveClaim(UUID id){
		final UUID idFinal = id;

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.approveClaim(idFinal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}


	public void returnClaim(UUID id, String comments){
		final UUID idFinal = id;
		final String commentsFinal = comments;		

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					elasicSearchEngineUnthreaded.returnClaim(idFinal, commentsFinal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();

	}


}
