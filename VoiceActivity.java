package com.swarup.kayhan.voice;

import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesMetadata;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.plus.Account;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class VoiceActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, ResultCallback<People.LoadPeopleResult> {
    ListFragment mFragment;
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;


    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked=false;

    /* Store the connection result from onConnectionFailed callbacks so that we can
     * resolve them when the user clicks sign-in.
     */
    private ConnectionResult mConnectionResult;
    Plus.PlusOptions plusOptions;
    int AUTH_CODE_REQUEST_CODE = 1101;
    String CLIENT_ID="44297698706-l6noq1n5kp9e3njpnv3ou5pefcgkbh6i.apps.googleusercontent.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
//       plusOptions = new Plus.PlusOptions.Builder()
//                .addActivityTypes("http://schema.org/AddAction", "http://schema.org/BuyAction")
//                .build();
        if(savedInstanceState==null){
            getFragmentManager().beginTransaction().add(R.id.container, new Login()).commit();
        }
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API, plusOptions)
//                .addScope(Plus.SCOPE_PLUS_LOGIN)
//                .build();

//        findViewById(R.id.sign_in_button).setOnClickListener(this);


    }
    private void resolveSignInError() {
        if (mConnectionResult!=null&&mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }
    String email;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    ArrayList<String> userIds;
    Person mPerson;
    String userId="";
    @Override
    public void onConnected(Bundle bundle) {

        if(!mGoogleApiClient.isConnectionCallbacksRegistered(this))
        mGoogleApiClient.registerConnectionCallbacks(this);
        if(!mGoogleApiClient.isConnectionFailedListenerRegistered(this))
        mGoogleApiClient.registerConnectionFailedListener(this);
        if(mSignInClicked){

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        }

        userId = Plus.AccountApi.getAccountName(mGoogleApiClient);

        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                .setResultCallback(this);






//        if (Games.Players.getCurrentPlayer(mGoogleApiClient) != null) {
//            Player player = Games.Players.getCurrentPlayer(mGoogleApiClient);
//            userId = player.getDisplayName();
////            String personName = currentPerson.getDisplayName();
////            String personPhoto = currentPerson.getImage();
////            String personGooglePlusProfile = currentPerson.getUrl();
//            ((TextView)findViewById(R.id.textView)).setText(userId);
//        }else ((TextView)findViewById(R.id.textView)).setText(userId);

        ((TextView)findViewById(R.id.textView)).setText(userId);






    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    ConnectionResult result;
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        if (!mIntentInProgress&& result.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(result.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                if (mGoogleApiClient.isConnected()) {
                    mSignInClicked=true;
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                }
                break;
            default:
                break;
        }
    }

    String TAG="tag";


    HashMap<String,Person> map = new HashMap<String,Person>();
    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        String str = "";
        userIds = new ArrayList<String>();
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();

            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
//                    map.put(personBuffer.get(i).getDisplayName(),personBuffer.get(i));
                    str += personBuffer.get(i).getDisplayName()+"\n";
                    userIds.add(personBuffer.get(i).getId());
//                    ((TextView)findViewById(R.id.textView)).setText(personBuffer.get(i).getDisplayName());
                }
            } finally {
                ((EditText)findViewById(R.id.editText)).setText(str);
                personBuffer.close();
//
            }
        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
        }



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
