package core;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
// equivaux a la liste de criteres/types/..
public class ListItem 
{
	
	//Attributs & Constructor
	String listName;
	String listColor;
	ArrayList<Item> list = new ArrayList<Item>();

	XmlPullParserFactory pullParserFactory;
	public final static String files_directory = "/storage/extSdCard/MM/ITEMS";
	public ListItem(String listName, String listColor) 
	{
		this.listName = listName;
		this.listColor = listColor;
		//save();
	}
	public ListItem(String listName) 
	{
		load(listName);
	}
//	public ListItem() 
//	{
//		super();
//	}
	
	//Core Functions
	public void addItem(String data)
	{
		this.list.add(new Item(data));
		//save();
	}
	public void removeItem(String data)
	{
		int tmp = findItem(data);
		if (tmp > -1)
			this.list.remove(tmp);
		//save();
	}
	public int findItem(String data)
	{
		for (int i=0;i<list.size();i++)
		{
			if (list.get(i).toString() == data ) 
				return i;
		}
		return -1;
	}
	@Override
	public String toString() 
	{
		return "ListItem [listName=" + listName + ", listColor=" + listColor
				+ ", list=" + list + "]";
	}
	public boolean load(String listName)
	{
		File item = new File(ListItem.files_directory + "/" + listName + ".xml");
		if (item.canRead())
		{
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new FileInputStream(item));
				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getElementsByTagName("NameList");
				this.setListName(nodeList.item(0).getTextContent());
				nodeList = doc.getElementsByTagName("ColorList");
				this.setListColor(nodeList.item(0).getTextContent());
				
				nodeList = doc.getElementsByTagName("Item");
				this.list.clear();
				for (int i = 0; i < nodeList.getLength(); i++) {
					this.addItem(nodeList.item(i).getTextContent());
				}
			} 
			catch (ParserConfigurationException e) {
				e.printStackTrace();
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (SAXException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public boolean save()
	{
		 File item_folder = new File(ListItem.files_directory);
		 item_folder.mkdirs();
		 XmlSerializer serializer = Xml.newSerializer();
		 StringWriter writer = new StringWriter();
		 try {
				 serializer.setOutput(writer);
			     serializer.startDocument("UTF-8", true);
			     serializer.startTag("", "XML");
				     serializer.startTag("", "NameList");
				     	serializer.text(this.getListName());
				     serializer.endTag("", "NameList");
				     serializer.startTag("", "ColorList");
				     	serializer.text(this.getListColor().toString());
				     serializer.endTag("", "ColorList");				     
				     serializer.startTag("", "ItemList");
					     for (Item item: this.getList()){
					            serializer.startTag("", "Item");
					            serializer.text(item.toString());
					            serializer.endTag("", "Item");
					     }
				     serializer.endTag("", "ItemList");
				 serializer.endTag("", "XML");    
			     serializer.endDocument();
			     FileWriter fw = new FileWriter(item_folder + "/" + listName + ".xml");
			     fw.write(writer.toString());
			     fw.close();
			     return true;
		    } 
		 catch (Exception e) 
		    {
		        throw new RuntimeException(e);
		    } 
	}

	// Getters&Setters
	public String getListName() 
	{
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public String getListColor() {
		return listColor;
	}
	public void setListColor(String listColor) {
		this.listColor = listColor;
	}
	public ArrayList<Item> getList() {
		return list;
	}
}
