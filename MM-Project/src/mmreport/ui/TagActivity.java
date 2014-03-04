package mmreport.ui;

import java.io.File;
import java.util.ArrayList;

import mmreport.ui.R;

import core.Item;
import core.ListItem;
import core.Meeting;
import core.Tools;
import android.R.string;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TagActivity extends Activity implements OnClickListener, OnSeekBarChangeListener,OnSeekCompleteListener,OnCompletionListener,OnPreparedListener, OnItemClickListener{
	SeekBar seek_bar;
	ImageView play_button, pause_button;
    MediaPlayer player;
    TextView text_shown;
    Handler seekHandler = new Handler();
    Meeting currentMeeting;
    String currentOffset = null;
    

    
	class CustomList
	{
		ArrayList<String> ListeItems=new ArrayList<String>();
		ArrayAdapter<String> adapterListeItems;
		ListView lvListeListe;
		String listeName;
		int colorList=10;
		CustomList(Context ctx, ListView lvListeListe, int c,String s)
		{
			this.listeName=s;
			colorList=c;
			this.lvListeListe = lvListeListe;
			adapterListeItems=new ArrayAdapter<String>(ctx,
		            android.R.layout.simple_list_item_1,
		            ListeItems){
			       @Override
			        public View getView(int position, View convertView,
			                ViewGroup parent) {
			            View view =super.getView(position, convertView, parent);

			            TextView textView=(TextView) view.findViewById(android.R.id.text1);

			            /*YOUR CHOICE OF COLOR*/
			            textView.setTextColor(colorList);

			            return view;
			        }
			};
			lvListeListe.setAdapter(adapterListeItems);
		}
		void addItem(String s)
		{
			ListeItems.add(s);
			adapterListeItems.notifyDataSetChanged();
		}
		void removeItem(String s)
		{
			for(int i=0;i<ListeItems.size();i++)
			{
				if(ListeItems.get(i).toString().compareTo(s) == 0)
					{
						ListeItems.remove(i);
						adapterListeItems.notifyDataSetChanged();
						break;
					}
			}
		}
		public void clear() {
			ListeItems.clear();
			adapterListeItems.notifyDataSetChanged();
		}
	}
	
	CustomList sequenceItems;
	ArrayList<CustomList> listList = new ArrayList<CustomList>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tag);
		
		this.setFinishOnTouchOutside(false);
		
		
		Bundle extras = getIntent().getExtras();
		this.currentMeeting = new Meeting(extras.getString("meeting"));
		this.currentMeeting.read_sequences();
		
		
		 getInit();
		 
		 player.setOnCompletionListener(this);
	     player.setOnPreparedListener(this);
	     player.setOnSeekCompleteListener((OnSeekCompleteListener) this);
	     seek_bar.setProgress(0);
	     seek_bar.setOnSeekBarChangeListener(this);
	     
	     listview = (ListView) findViewById(R.id.items_list);
	     listview.setOnItemClickListener(this);
	     sequenceItems = new CustomList(this, listview, Color.WHITE,"");
	     
	     loadItems();
	     seekUpdation();
	     
	     
	}
	ListView listview;
    private void loadItems() 
    {
    	ArrayList <String>itemsList = new ArrayList<String>();
		File meeting_folder = new File(Tools.items_directory);
		LinearLayout containerList = (LinearLayout) findViewById(R.id.list_list);
		 File[] listItems_tmp = meeting_folder.listFiles();
		 File[] listItems = new File[listItems_tmp.length + 1];
		 listItems[0] = new File("Windchill-Local.xml");
		 for(int i = 0; i<listItems_tmp.length; i++)
		 {
			 listItems[i+1] = listItems_tmp[i];
		 }
		 
		boolean tmp = true;
		for (File inFile : listItems) 
		{
				ListItem list = new ListItem(inFile.getName(),tmp);
				tmp = false;
				if(list.getList().size()>0)
				{
					LinearLayout LL = new LinearLayout(this);
					ListView newList = new ListView(this);
					TextView txt= new TextView(this);
					
					
			    	LayoutParams lp1 = new LayoutParams(new ViewGroup.MarginLayoutParams(200,400));
			    	LayoutParams lp2 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			    	LayoutParams lp3 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			    	
			    	LL.setLayoutParams(lp1);
			    	newList.setLayoutParams(lp3);
			    	txt.setLayoutParams(lp2);
			    	
			    	LL.setOrientation(LinearLayout.VERTICAL);
			    	
			    	newList.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
			    	newList.setClickable(true);
			    	
			    	txt.setText(list.getListName());
			    	txt.setTextSize(25);
			    	//LL.setGravity(Gravity.CENTER_HORIZONTAL);
			    	
			    	CustomList cl = new CustomList(this, newList, list.getListColor(),list.getListName());
			    	for(Item item:list.getList())
			    	{
			    		cl.addItem(item.toString());
			    	}
			    	
			    	LL.addView(txt);
			    	LL.addView(newList);
			    	
			    	
			    	containerList.addView(LL);
			    	ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) LL.getLayoutParams();
			    	mlp.setMargins(10, 0, 10, 0);
			    	//creer la listView
			    	listList.add(cl);
			    	newList.setOnItemClickListener(this);
				}
		}
			
	}

	public void updateInfo(String offset)
    {
			if(offset != null)
			{
				if(currentOffset == null  || currentOffset.compareTo(offset)!=0)
				{
					if(currentOffset!=null)
					{
						this.currentMeeting.save_sequence(currentOffset, this.sequenceItems.ListeItems);
					}
					currentOffset = offset;
					TextView tx1 = (TextView)findViewById(R.id.textWho);
			    	tx1.setText("Attendee : "+this.currentMeeting.read_sequence(offset).getWhoSpeacking().getName());
			    	TextView tx2 = (TextView)findViewById(R.id.textWhy);
			    	tx2.setText("Question : "+this.currentMeeting.read_sequence(offset).getQuestion().getQuestion());
			    	TextView tx3 = (TextView)findViewById(R.id.texthowmuch);
			    	tx3.setText("Duration(ms) from "+this.currentMeeting.getStartOffset(String.valueOf(this.currentMeeting.read_sequence(offset).getfinishOffset()))+" To "+this.currentMeeting.read_sequence(offset).getfinishOffset());
			    	TextView tx4 = (TextView)findViewById(R.id.textcomment);
			    	tx4.setText("Comment :  "+this.currentMeeting.read_sequence(offset).getComment());
			    	if(sequenceItems != null) 
			    		{
			    			sequenceItems.clear();
			    		}
			    	
			    	for(int i=0;i<this.currentMeeting.read_sequence(offset).getItemsList().size();i++)
					{
						sequenceItems.addItem(this.currentMeeting.read_sequence(offset).getItemsList().get(i).toString());
					}
			    }
			}
	}
   
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag, menu);
		return true;
	}

	public void getInit() {
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
        play_button = (ImageView) findViewById(R.id.img_play);
        pause_button = (ImageView) findViewById(R.id.img_pause);
        ImageView pause_save = (ImageView) findViewById(R.id.img_save);
        play_button.setOnClickListener(this);
        pause_button.setOnClickListener(this);
        pause_save.setOnClickListener(this);
        player = MediaPlayer.create(this, Uri.parse(Tools.meetings_directory + "/" + this.currentMeeting.getDirectoryName() + "/audio.amr"));
        seek_bar.setMax(player.getDuration());
        
    }
 
    Runnable run = new Runnable() {
 
        @Override
        public void run() {
            seekUpdation();
        }
    };
 
    public void seekUpdation() {
        seek_bar.setProgress(player.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
        updateInfo(this.currentMeeting.getThe0ffset(String.valueOf(player.getCurrentPosition())));
        
    }
 
    @Override
    public void onClick(View view) 
    {
        switch (view.getId()) {
        case R.id.img_play:
            player.start();
            break;
        case R.id.img_pause:
            player.pause();
            break;
        case R.id.img_save:
        	finish_tag();
        	break;
        }
 
    }

	private void finish_tag() 
	{
		player.pause();
		this.currentMeeting.finishTagingStep();
		Toast.makeText(this, "Meeting Saved..", Toast.LENGTH_SHORT).show();
		seekHandler.removeCallbacksAndMessages(run);
        
		this.finish();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
	        boolean fromUser) {
	    if(fromUser){
	        player.seekTo(progress);
	        seek_bar.setProgress(progress);

	    }}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
	}

	
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(arg0 == this.listview)
		{
			this.sequenceItems.removeItem(this.sequenceItems.ListeItems.get(arg2));
			this.currentMeeting.removeItemFromSequence(Float.parseFloat(currentOffset), new Item(this.sequenceItems.ListeItems.get(arg2)));
		}
		else
		{
			for(int i=0;i< this.listList.size();i++)
			{
				if(this.listList.get(i).lvListeListe == arg0)
					{
						this.sequenceItems.addItem(this.listList.get(i).ListeItems.get(arg2));
						this.currentMeeting.addItemToSequence(Float.parseFloat(currentOffset), new Item(this.listList.get(i).ListeItems.get(arg2)));
					}
			}
		}
		
	}



}
