package mmreport.ui;

import java.util.ArrayList;

import mmreport.ui.R;

import core.Item;
import core.ListItem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ManageWcItems extends Activity {
	
	static ArrayList<String> ListeWC=new ArrayList<String>();
	static ArrayAdapter<String> adapterListeWC;
	static ArrayList<String> ListeLocal=new ArrayList<String>();
	static ArrayAdapter<String> adapterListeLocal;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_wc_items);
		this.setTitle("Manage Windchill Articles");


		adapterListeWC=new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1,
	            ListeWC);
		ListView lvListeWC = (ListView)findViewById(R.id.items_list_wc);
		lvListeWC.setAdapter(adapterListeWC);
		lvListeWC.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        	//add to the other
		        	ListItem l = new ListItem("Windchill-Local", true);
		        	l.addItem(ListeWC.get(position));
		        	refreshListLocal();
		        }});
		refreshListWc();		
		
		
		adapterListeLocal=new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1,
	            ListeLocal);
		ListView lvListeLocal = (ListView)findViewById(R.id.items_list_local);
		lvListeLocal.setAdapter(adapterListeLocal);
		lvListeLocal.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	        		//remove from it
	                int position, long id) {
	        	ListItem l = new ListItem("Windchill-Local", true);
	        	l.removeItem(ListeLocal.get(position));
	        	refreshListLocal();
	        }});
		refreshListLocal();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_wc_items, menu);
		return true;
	}
	
	public void pull(View v)
	{
		Intent intent = new Intent(getApplicationContext(), LoginWindchill.class);
		startActivity(intent);
	}
	public void closeAct(View v)
	{
		this.finish();
	}
	static public void refreshListLocal()
	{
		//Update ListeWC+ListeLocal
		ManageWcItems.ListeLocal.clear();
		ListItem l = new ListItem("Windchill-Local", true);
		for(Item item:l.getList())
		{
			ManageWcItems.ListeLocal.add(item.toString());
		}
		ManageWcItems.adapterListeLocal.notifyDataSetChanged();
	}
	static public void refreshListWc()
	{
		ManageWcItems.ListeWC.clear();
		ListItem l = new ListItem("Windchill-Component", true);
		for(Item item:l.getList())
		{
			ManageWcItems.ListeWC.add(item.toString());
		}
		ManageWcItems.adapterListeWC.notifyDataSetChanged();
	}
	static public void refreshAll()
	{
		refreshListLocal();
		refreshListWc();
	}
}
