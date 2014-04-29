package com.rhcloud.jop.formfactor.views;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.OpenImportActionType;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.User;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.UserService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class MainActivity extends Activity implements IDatabaseReadyListener
{
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private boolean mIsRegistering = false;
	private boolean mIsDBReady = false;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private FormFactorDb mFormFactorDB = null;
	private boolean mHasLoggedOff = false;
	private String mIntentURL;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mHasLoggedOff  = false;
		if(savedInstanceState == null)
		{
			Intent intent = this.getIntent();
			if(intent != null)
			{
				savedInstanceState = intent.getExtras();
			}
			
			if(savedInstanceState != null && savedInstanceState.containsKey(BundleKeys.LoggedOff))
			{
				mHasLoggedOff = savedInstanceState.getBoolean(BundleKeys.LoggedOff);
			}

			if(intent != null)
			{
				String action = intent.getAction();
				if (action != null && action.equals(Intent.ACTION_VIEW))
				{
			        Uri data = intent.getData();
			        if(data != null)
			        {
			        	this.mIntentURL = data.toString();
			        }
			    }
			}
		}

		setContentView(R.layout.activity_main);
		
		this.mFormFactorDB = FormFactorDb.getInstance(this);
		
		if(this.mFormFactorDB != null && this.mFormFactorDB.getDB() != null)
		{
			this.loginPreferredUser();
		}

		// Set up the login form.
		mEmail = getIntent().getStringExtra(BundleKeys.LoginEmail);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
			{
				if (id == R.id.login || id == EditorInfo.IME_NULL)
				{
					attemptLogin();
					return true;
				}
				
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
			new View.OnClickListener() 
			{
				@Override
				public void onClick(View view)
				{
					attemptLogin();
				}
			});

		findViewById(R.id.register_button).setOnClickListener(
			new View.OnClickListener() 
			{
				@Override
				public void onClick(View view)
				{
					registerUser();
				}
			});
	}

	@Override
	public void OnDatabaseReady()
	{
		mIsDBReady = true;
		this.loginPreferredUser();
	}
	
	private void loginPreferredUser()
	{
		UnitOfWork unitOfWork = new UnitOfWork(this.mFormFactorDB);
		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
		
		UserService userService = new UserService(dataContext);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		String username = pref.getString("pref_username", null);
		
		String temp = pref.getString("pref_password", null);
		
		if(temp != null && username != null)
		{
			char[] password = temp.toCharArray();
			
			User user = userService.GetUser(username, password);

			if(user != null && user.ID != 0)
			{
				if(this.mIntentURL != null && !this.mIntentURL.equals(""))
				{
					Intent intent = new Intent(this, OpenImportFormActivity.class);
					intent.putExtra(BundleKeys.OpenFormActionID, OpenImportActionType.ImportCreate.GetIndex());
					intent.putExtra(BundleKeys.IntentURL, this.mIntentURL);
					this.startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(this, MainMenuActivity.class);
					this.startActivity(intent);
				}
			}
		}
		else
		{
			if(!mHasLoggedOff)
			{
				User user = userService.GetDefaultUser();
				
				if(user != null && user.ID != 0)
				{
					if(this.mIntentURL != null && !this.mIntentURL.equals(""))
					{
						Intent intent = new Intent(this, OpenImportFormActivity.class);
						intent.putExtra(BundleKeys.OpenFormActionID, OpenImportActionType.ImportCreate.GetIndex());
						intent.putExtra(BundleKeys.IntentURL, this.mIntentURL);
						this.startActivity(intent);
					}
					else
					{
						Intent intent = new Intent(this, MainMenuActivity.class);
						this.startActivity(intent);
					}
				}
			}
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		savedInstanceState.putBoolean(BundleKeys.LoggedOff, this.mHasLoggedOff);
		savedInstanceState.putString(BundleKeys.IntentURL, this.mIntentURL);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		if(savedInstanceState != null)
		{
			if(savedInstanceState.containsKey(BundleKeys.LoggedOff))
			{
				this.mHasLoggedOff = savedInstanceState.getBoolean(BundleKeys.LoggedOff);
			}
			
			if(savedInstanceState.containsKey(BundleKeys.IntentURL))
			{
				this.mIntentURL = savedInstanceState.getString(BundleKeys.IntentURL);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin()
	{
		if (mAuthTask != null)
		{
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword))
		{
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} 
		else if (mPassword.length() < 4)
		{
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail))
		{
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} 
		else if (!mEmail.contains("@"))
		{
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel)
		{
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} 
		else
		{
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	private void showProgress(final boolean show)
	{
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) 
		{
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
				.alpha(show ? 1 : 0)
				.setListener(new AnimatorListenerAdapter()
				{
					@Override
					public void onAnimationEnd(Animator animation) 
					{
						mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
					}
				});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
				.alpha(show ? 0 : 1)
				.setListener(new AnimatorListenerAdapter()
				{
					@Override
					public void onAnimationEnd(Animator animation) 
					{
						mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
					}
				});
		} 
		else
		{
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private void registerUser()
	{
		this.mIsRegistering = true;
		this.attemptLogin();
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{
			boolean result = true;
			
			UnitOfWork unitOfWork = new UnitOfWork(mFormFactorDB);
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			
			UserService userService = new UserService(dataContext);
			
			byte[] hash = null;
			MessageDigest digest;
			try
			{
				digest = MessageDigest.getInstance("SHA-256");
				hash = digest.digest(mPassword.getBytes("UTF-8"));
			}
			catch (NoSuchAlgorithmException e)
			{
				result = false;
			}
			catch (UnsupportedEncodingException e)
			{
				result = false;
			}
			
			if(hash != null)
			{
				char[] password = null;
				
				try
				{
					password = new String(hash, "UTF-8").toCharArray();
				} 
				catch (UnsupportedEncodingException e)
				{
					result = false;
				}
				
				if(result)
				{
					User user = userService.GetUser(mEmail, password);
	
					if(user != null && user.ID != 0)
					{
						if(mIsRegistering)
						{
							result = false;
						}
						else
						{
							Editor settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
							
							settings.putString("pref_username", mEmail);
							settings.putString("pref_password", new String(password));
							settings.commit();
						}
					}
					else
					{
						if(mIsRegistering)
						{
							user = new User();
							
							user.Username = mEmail;
							user.Email = mEmail;
							user.Password = password;
							
							result = !userService.IsValidAuthentication(user.Username, user.Password);
							
							if(result)
							{
								Result userResult = userService.CreateUpdateUser(user);
						    	
								if(userResult.Success)
								{
									Editor settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
									
									settings.putString("pref_username", mEmail);
									settings.putString("pref_password", new String(password));
									settings.commit();
								}
								else
								{
									result = false;
								}
							}
						}
						else
						{
							result = false;
						}
					}
				}
			}

			// TODO: register the new account here.
			return result;
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			mAuthTask = null;
			showProgress(false);

			if (success)
			{
				if(mIntentURL != null && !mIntentURL.equals(""))
				{
					Intent intent = new Intent(MainActivity.this, OpenImportFormActivity.class);
					intent.putExtra(BundleKeys.OpenFormActionID, OpenImportActionType.ImportCreate.GetIndex());
					intent.putExtra(BundleKeys.IntentURL, mIntentURL);
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
					startActivity(intent);
				}
				
				mIsRegistering = false;
			} 
			else 
			{
				if(!mIsRegistering)
				{
					mPasswordView.setError(getString(R.string.error_incorrect_password));
				}
				else
				{
					mPasswordView.setError(getString(R.string.error_register_failed));
				}
				
				mPasswordView.requestFocus();
				
				mIsRegistering = false;
			}
		}

		@Override
		protected void onCancelled()
		{
			mAuthTask = null;
			showProgress(false);
		}
	}
}
