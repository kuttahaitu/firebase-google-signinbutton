package com.crio.firebasegooglelogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
	private SignInButton signInButton;
	private static final int SIGN_IN_CODE = 777;
	private FirebaseAuth firebaseAuth;
	private FirebaseAuth.AuthStateListener firebaseAuthListener;
	GoogleSignInClient googleSignInClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		GoogleSignInOptions googlesignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken("222383098612-5o43htineqgnf7ttbjndpr6vvppags98.apps.googleusercontent.com")
				.requestEmail().build();
		googleSignInClient = GoogleSignIn.getClient(this, googlesignInOptions);
		signInButton = (SignInButton) findViewById(R.id.signinbutton);
		signInButton.setSize(SignInButton.SIZE_WIDE);
		signInButton.setColorScheme(SignInButton.COLOR_DARK);
		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivityForResult(googleSignInClient.getSignInIntent(), 100);
			}

		});
		firebaseAuth = FirebaseAuth.getInstance();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
			if (googleSignInAccountTask.isSuccessful()) {
				Toast.makeText(getApplicationContext(), "SignIn Success", Toast.LENGTH_LONG).show();
				try {
					GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.getResult(ApiException.class);
					if (googleSignInAccount != null) {
						AuthCredential authCredential = GoogleAuthProvider
								.getCredential(googleSignInAccount.getIdToken(), null);
						firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this,
								new OnCompleteListener<AuthResult>() {
									@Override
									public void onComplete(Task<AuthResult> task) {
										if (task.isSuccessful()) {
											startActivity(new Intent(getApplicationContext(), MainActivity.class)
													.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
											finish();
											Toast.makeText(getApplicationContext(), "SignIn Success", Toast.LENGTH_LONG)
													.show();
										} else {
											Toast.makeText(getApplicationContext(), task.getException().getMessage(),
													Toast.LENGTH_LONG).show();
										}
									}

								});
					}
				} catch (ApiException e) {
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		FirebaseUser fireuser = FirebaseAuth.getInstance().getCurrentUser();
		if (fireuser != null) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}
}