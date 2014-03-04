package mmreport.ui;

import mmreport.ui.R;

import core.ListItem;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class LoginWindchill extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_windchill);
		this.setTitle("Login into Windchill");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_windchill, menu);
		return true;
	}
	public void signIn(View v)
	{
		//alimenter le xml en question
		ListItem lLocal = new ListItem("Windchill-Local", -65446, true);
		ListItem lWc = new ListItem("Windchill-Component", -65446, true);
		lWc.addItem("comp1");
		lWc.addItem("comp2");
		lWc.addItem("comp3");
		lWc.addItem("comp4");
		lWc.addItem("comp5");
		lWc.addItem("comp6");
		lWc.addItem("comp7");
		lWc.addItem("comp8");
		lWc.addItem("comp9");
		lWc.addItem("comp10");
		lWc.addItem("comp11");
		ManageWcItems.refreshAll();
		Toast.makeText(getApplicationContext(), "Articles Loaded..", Toast.LENGTH_LONG).show();
		this.finish();
	}
}
