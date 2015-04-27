package login.fbgooglelogin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private LoginButton btnLogin;
    private Button btnPostImage;
    private Button btnUpdateStatus;

    private String TAG = "LOGIN=====>";

    private UiLifecycleHelper uiHelper;

    private TextView userName;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

    private static String message = "Sample status posted from cloud app";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        userName = (TextView) findViewById(R.id.user_name);
        btnLogin = (LoginButton) findViewById(R.id.fb_login_button);
        btnLogin.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    userName.setText("Hello, " + user.getName());
                    Toast.makeText(MainActivity.this, user.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    userName.setText("You are not logged");
                    Toast.makeText(MainActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPostImage = (Button) findViewById(R.id.post_image);
        btnPostImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                postImage();
            }
        });

        btnUpdateStatus = (Button) findViewById(R.id.update_status);
        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        buttonsEnabled(false);

    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                buttonsEnabled(true);
                Log.d(TAG, "Facebook session opened");
            } else if (state.isClosed()) {
                buttonsEnabled(false);
                Log.d(TAG, "Facebook session closed");
            }
        }
    };

    public void buttonsEnabled(boolean isEnabled) {
        btnPostImage.setEnabled(isEnabled);
        btnUpdateStatus.setEnabled(isEnabled);
    }

    public void postImage() {
        if (checkPermissions()) {
            Bitmap img = BitmapFactory.decodeResource(getResources(),
                    R.drawable.com_facebook_button_blue);
            Request uploadRequest = Request.newUploadPhotoRequest(
                    Session.getActiveSession(), img, new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            Toast.makeText(MainActivity.this,
                                    "Photo uploaded successfully",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            uploadRequest.executeAsync();
        } else {
            requestPermissions();
        }
    }

    public void postStatusMessage() {
        if (checkPermissions()) {
            Request request = Request.newStatusUpdateRequest(
                    Session.getActiveSession(), message,
                    new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            if (response.getError() == null)
                                Toast.makeText(MainActivity.this,
                                        "Status updated successfully",
                                        Toast.LENGTH_LONG).show();
                        }
                    });
            request.executeAsync();
        } else {
            requestPermissions();
        }
    }

    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }
}
