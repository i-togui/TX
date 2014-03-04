package mmreport.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import mmreport.ui.ColorPickerDialog.OnColorChangedListener;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Files;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import mmreport.ui.R;

import core.DummyContent;
import core.DummyContent.DummyItem;
import core.Meeting;
import core.Tools;

/**
 * An activity representing a list of Meetings. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link MeetingDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MeetingListFragment} and the item details (if present) is a
 * {@link MeetingDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link MeetingListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class MeetingListActivity extends FragmentActivity implements
		MeetingListFragment.Callbacks, OnColorChangedListener {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_meeting_list);
	
		if (findViewById(R.id.meeting_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((MeetingListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.meeting_list))
					.setActivateOnItemClick(true);
		}

		initFiles();
	}


	
	void initFiles()
	{
		File item_folder = new File(Tools.meetings_directory);
		if(!item_folder.exists()) item_folder.mkdirs();
		File extra_folder = new File(Tools.extra_directory);
		if(!extra_folder.exists()) 
			{
				extra_folder.mkdirs();
			}
		File items_folder = new File(Tools.items_directory);
		if(!items_folder.exists()) 
			{
			items_folder.mkdirs();
			}
		
	}
	/**
	 * Callback method from {@link MeetingListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(MeetingDetailFragment.ARG_ITEM_ID, id);
			MeetingDetailFragment fragment = new MeetingDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.meeting_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, MeetingDetailActivity.class);
			detailIntent.putExtra(MeetingDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.activity_main_actions, menu);
	        return super.onCreateOptionsMenu(menu);
	    }
	
	
	public static void refresh()
	{
		
		
		
		ArrayList<String> ListeMeetingDirectory=new ArrayList<String>();
			ListeMeetingDirectory = Meeting.getMeetingsListDirectory();	
			DummyContent.ITEM_MAP.clear();
			DummyContent.ITEMS.clear();
			for (String meeting : ListeMeetingDirectory) 
			{
				DummyContent.addItem(new DummyItem(meeting));
			}
			MeetingListFragment.adapterListe.notifyDataSetChanged();
			
			
			
	}
	public static void deSelect(Activity activity)
	{
		FrameLayout f = (FrameLayout)activity.findViewById(R.id.meeting_detail_container);
		for(int i=0;i<f.getChildCount();i++)
		{
			f.getChildAt(i).setVisibility(FrameLayout.INVISIBLE);
		}
		
	}
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
	    case R.id.action_refresh:
	    	refresh();
	    	deSelect(this);
	      break;
	    case R.id.action_add:
	    	deSelect(this);
			intent=new Intent(getApplicationContext(), AddMeeting.class);
			startActivity(intent);
			
	      break;
	      
	    case R.id.action_remove:
	    	String m = MeetingListFragment.selected;
	    	if(m == null)
	    		Toast.makeText(getApplicationContext(), "Select a Meeting First!", Toast.LENGTH_LONG).show();
	    	else
	    	{
	    		removeMeeting(m);
	    		View view = findViewById(R.id.scroll);
	    		view.setVisibility(View.INVISIBLE);
	    		MeetingListFragment.selected = null;
	    		deSelect(this);
	    	}

	      break;
	      
	    case R.id.action_manage:
	    	intent=new Intent(getApplicationContext(), ManageItems.class);
			startActivity(intent);
			break;
			
/**
 *  ADD first to Menu
 *   <item android:id="@+id/action_manage_wc"
		          android:icon="@drawable/ic_action_edit"
		          android:title="Windchill Configuration"
		          android:showAsAction="ifRoom"/>

 * 
	    case R.id.action_manage_wc:
	    	intent=new Intent(getApplicationContext(), ManageWcItems.class);
			startActivity(intent);
			break;

	   **/
		case R.id.action_about:
	    	intent=new Intent(getApplicationContext(), WebActivity.class);
	    	intent.putExtra("title", "Github Page");
	    	intent.putExtra("url", "https://github.com/i-togui/TX");
			startActivity(intent);
    	  break;
	    default:
	    	deSelect(this);
	      break;
	    }

	    return true;
	  }
	
	void removeMeeting(final String s)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Remove a Meeting");
		builder.setMessage("Do you want confirm this action?");

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        // Do do my action here
		    	
		    	Toast.makeText(getApplicationContext(), "State : " + Meeting.removeMeeting(s), Toast.LENGTH_LONG).show();
		    	refresh();
		    	
		        dialog.dismiss();
		    }

		});

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		       dialog.dismiss();
		    }
		});

		AlertDialog alert = builder.create();
		alert.show();
	}
	@Override
	public void colorChanged(int color) {
		// TODO Auto-generated method stub
	}
}
