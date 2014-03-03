package com.example.mm_project;

import java.util.ArrayList;

import com.example.mm_project.ColorPickerDialog.OnColorChangedListener;

import core.Item;
import core.ListItem;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManageItems extends Activity implements OnColorChangedListener {
	public static Button b;
	public static TextView t;
	public static int selectedItem=-1;
	public static int selectedList=-1;
	
	static ArrayList<String> ListeItems=new ArrayList<String>();
	ArrayAdapter<String> adapterListeItems;
	
	ArrayList<String> ListeListe=new ArrayList<String>();
	ArrayAdapter<String> adapterListeListe;
	
	
	static ArrayList<ListItem> Listee=new ArrayList<ListItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_items);
		
		b = (Button) findViewById(R.id.btnColor);
		adapterListeItems=new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1,
	            ListeItems);
		adapterListeListe=new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1,
	            ListeListe);
		
		ListView lvListeListe = (ListView)findViewById(R.id.items_list_list);
	    lvListeListe.setAdapter(adapterListeListe);
		
	    ListView lvListeItems = (ListView)findViewById(R.id.items_list_items);
		lvListeItems.setAdapter(adapterListeItems);
	    updateListeListe();

	    final ListView lv = (ListView)findViewById(R.id.items_list_list);
		lv.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	        	View vieww = findViewById(R.id.part2);
	    		vieww.setVisibility(View.VISIBLE);
	        	updateListeItems(position);
	        	ManageItems.selectedList=position;
	        }});
		
		final ListView lv2 = (ListView)findViewById(R.id.items_list_items);
		lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

	            for (int j = 0; j < adapterView.getChildCount(); j++)
	                adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

	            // change the background color of the selected element
	            view.setBackgroundColor(Color.LTGRAY);
	            ManageItems.selectedItem = i;
	        }});
		
		t = (TextView)findViewById(R.id.textTitleList);
	}
	void updateListeListe()
	{
		ListeListe.clear();
		ArrayList<String> Liste = ListItem.getItemsListDirectory();	
		Listee.clear();
		for (String in : Liste) 
		{
			ListItem l = new ListItem(in);
			Listee.add(l);
			ListeListe.add(l.getListName());
		}
		adapterListeListe.notifyDataSetChanged();
	}
	void updateListeItems(int i)
	{
		ListeItems.clear();
		ListItem tmp = Listee.get(i);
		for (Item in : tmp.getList()) 
		{
			ListeItems.add(in.toString());
		}
		adapterListeItems.notifyDataSetChanged();
		TextView t = (TextView)findViewById(R.id.textTitleList);
		t.setText(tmp.getListName());
		b.setBackgroundColor(tmp.getListColor());
	}
	
	public static String color;
	public static int colorInt;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_items, menu);
		return true;
	}
	public void selectColor(View v)
	{
		new ColorPickerDialog(this, this, -16056065).show();
	}
	public void add_items_list(View v)
	{
		//lvListeAttendee.add
		TextView text = (TextView) findViewById(R.id.text_items_list);
		if(text.getText().toString().length()>0)
		{
			ListeListe.add(text.getText().toString());
			adapterListeListe.notifyDataSetChanged();
			text.setText("");
			ListItem l = new ListItem(text.getText().toString(),-16056065);
			Listee.add(l);
		}
		else 
			Toast.makeText(getApplicationContext(), "Forbiden action:empty field", Toast.LENGTH_LONG).show();
	}
	public void add_item(View v)
	{
		//lvListeAttendee.add
		TextView text = (TextView) findViewById(R.id.text_items);
		if(text.getText().toString().length()>0)
		{
			ListeItems.add(text.getText().toString());
			adapterListeItems.notifyDataSetChanged();
			TextView t = (TextView)findViewById(R.id.textTitleList);
			for(ListItem i: Listee)
			{
				if(i.getListName() == t.getText())
				{
					i.addItem(text.getText().toString());
					text.setText("");
					break;
				}
			}
		}
		else 
			Toast.makeText(getApplicationContext(), "Forbiden action:empty field", Toast.LENGTH_LONG).show();
	}
	static public void update_color(int c)
	{
		
		for(ListItem i: Listee)
		{
			if(i.getListName() == t.getText())
			{
				i.changeColor(c);
				break;
			}
		}
	}
	@Override
	public void colorChanged(int color) {
		// TODO Auto-generated method stub
		
	}
	public void remove_list(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Remove an ItemsList");
		builder.setMessage("Do you want confirm this action?");

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        // Do do my action here
		    	if(ManageItems.selectedList>-1)
		    	{
			    	ListItem.removeMeeting(t.getText().toString()+".xml");
			    	Toast.makeText(getApplicationContext(), "State : Done", Toast.LENGTH_LONG).show();
			    	View view = findViewById(R.id.part2);
		    		view.setVisibility(View.INVISIBLE);
		    		updateListeListe();
			        dialog.dismiss();
			        ManageItems.selectedList = -1;
			        
		    	}
		    	else
		    		Toast.makeText(getApplicationContext(), "Choose a list first!", Toast.LENGTH_LONG).show();
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
	public void removeItem(View v)
	{
    	if(ManageItems.selectedItem > -1)
    	{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	
			builder.setTitle("Remove this Item?");
			builder.setMessage("Do you want confirm this action?");
	
			builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	
			    public void onClick(DialogInterface dialog, int which) {
			        // Do do my action here
			    	//get LitItems
			    	//get selected items
			    	//remove it from xml
			    	//remove it from Listeview
	
				    	for(ListItem i: Listee)
						{
							if(i.getListName() == t.getText())
							{
								i.removeItem(ManageItems.ListeItems.get(ManageItems.selectedItem));
								break;
							}
						}
				    	updateListeItems(ManageItems.selectedList);
				    	ManageItems.selectedItem=-1;
				    	Toast.makeText(getApplicationContext(), "State : Done", Toast.LENGTH_LONG).show();
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
    	else
    		Toast.makeText(getApplicationContext(), "Choose an Item first!", Toast.LENGTH_LONG).show();
    	}	
	

	public void closeAct(View v)
	{
		this.finish();
	}
}
