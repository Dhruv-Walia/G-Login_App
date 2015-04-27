package login.fbgooglelogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

public class FBLoginActivity extends ActionBarActivity {
    private LoginButton btnFBLogin;
    private TextView tvUserName, tvBirthday, tvLocation;
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, statusCallBack);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fblogin);

        tvUserName = (TextView) findViewById(R.id.username_main);
        tvBirthday = (TextView) findViewById(R.id.txtViewBirthday);
        tvLocation = (TextView) findViewById(R.id.txtViewLocation);
        btnFBLogin = (LoginButton) findViewById(R.id.fb_login_button_main);

        btnFBLogin.setReadPermissions(Arrays.asList("email"));
        btnFBLogin.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    tvUserName.setText("Hello, " + user.getName());
                    tvBirthday.setText(user.getBirthday().toString());
                    tvLocation.setText(user.getLocation().getProperty("name").toString());
                    Log.d("FB=====>", user.getName());
                } else {
                    tvUserName.setText("Still not logged in");
                    Log.d("FB=====>", "No user found.");
                }
            }
        });
    }

    private Session.StatusCallback statusCallBack = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Log.d("FB=====>", "Facebook session opened");
            } else if (state.isClosed()) {
                Log.d("FB=====>", "Facebook session closed.");
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }
}
