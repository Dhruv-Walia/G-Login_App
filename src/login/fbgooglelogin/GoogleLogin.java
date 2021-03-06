package login.fbgooglelogin;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

public class GoogleLogin extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener {

	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;

	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	/* A flag indicating that a PendingIntent is in progress and prevents
	* us from starting further intents.*/  
	private boolean mIntentInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        // initialize the GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
          mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
    	// We've resolved any connection errors.  mGoogleApiClient can be used to
    	// access Google APIs on behalf of the user.
    }

    @Override
    public void onConnectionSuspended(int i) {
    	mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    	if (!mIntentInProgress && result.hasResolution()) {
    	    try {
    	      mIntentInProgress = true;
    	      startIntentSenderForResult(result.getResolution().getIntentSender(),
    	          RC_SIGN_IN, null, 0, 0, 0);
    	    } catch (SendIntentException e) {
    	      // The intent was canceled before it was sent.  Return to the default
    	      // state and attempt to connect to get an updated ConnectionResult.
    	      mIntentInProgress = false;
    	      mGoogleApiClient.connect();
    	    }
    	  }

    }
    
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
    	  if (requestCode == RC_SIGN_IN) {
    	    mIntentInProgress = false;

    	    if (!mGoogleApiClient.isConnecting()) {
    	      mGoogleApiClient.connect();
    	    }
    	  }
    	}
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_google_login, menu);
       return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       // Handle action bar item clicks here. The action bar will
       // automatically handle clicks on the Home/Up button, so long
       // as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();

       //noinspection SimplifiableIfStatement
       if (id == R.id.action_settings) {
           return true;
       }

       return super.onOptionsItemSelected(item);
   }
}
