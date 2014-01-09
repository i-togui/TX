package core;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;

import android.util.Xml;

public class ClassHtml {
	
    ArrayList<Sequence> SL;
	public ClassHtml(Meeting m)
	{
		m = m.reload();
	    m.read_sequences();
	    SL = m.sequencesList;
	    
		File item_folder = new File(Tools.meetings_directory + "/" + m.getDirectoryName());
		
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
				serializer.setOutput(writer);
			    serializer.startDocument("UTF-8", true);
			    serializer.startTag("", "html");
					
			    	serializer.startTag("", "head");
					serializer.endTag("", "head");
					
					serializer.startTag("", "body");
					
						serializer.startTag("", "center");
							serializer.startTag("", "h1");
								serializer.text(m.getTitle());
								serializer.startTag("", "br");
								serializer.endTag("", "br");
							serializer.endTag("", "h1");
							serializer.startTag("", "h2");
								serializer.text("Meeting Done at : " + m.getPlace() + ", " + new Date());
								serializer.startTag("", "br");
								serializer.endTag("", "br");
							serializer.endTag("", "h2");
							serializer.startTag("", "h2");
								serializer.text("Attendees : " + generateAttendees(m));
								serializer.startTag("", "br");
								serializer.endTag("", "br");
							serializer.endTag("", "h2");
							serializer.startTag("", "h2");
								serializer.text("Reporter : " + Sequence.getReporter(m.getAttendeesList()));
								serializer.startTag("", "br");
								serializer.endTag("", "br");
							serializer.endTag("", "h2");
								serializer.startTag("", "hr");
								serializer.endTag("", "hr");
								serializer.startTag("", "br");
								serializer.endTag("", "br");
						serializer.endTag("", "center");
						
						
						
						 for(int i=0;i<Sequence.getQuestions(SL).size();i++)
						  {
							 serializer.startTag("", "p");
							 serializer.startTag("", "h2");
								serializer.text((String)Sequence.getQuestions(SL).get(i));
								serializer.startTag("", "br");
								serializer.endTag("", "br");
							 serializer.endTag("", "h2");				 
							ArrayList<Sequence> MINI_SL = Sequence.getSequences(SL, (String)Sequence.getQuestions(SL).get(i));
							serializer.startTag("", "ul");
							for(int j=0;j<MINI_SL.size();j++)
							  {
								serializer.startTag("", "li");
									serializer.startTag("", "u");
									serializer.text(MINI_SL.get(j).getWhoSpeacking().getName());
									serializer.endTag("", "u");
									serializer.startTag("", "br");
									serializer.endTag("", "br");
									serializer.text("            Criteria : "+MINI_SL.get(j).getItemsList().toString());
									serializer.startTag("", "br");
									serializer.endTag("", "br");
								String tmpS = MINI_SL.get(j).getComment();
								  if(!tmpS.isEmpty())
								  	{
										serializer.text("            Comment : "+tmpS);
								  	}
								  serializer.endTag("", "li");	
										
							  	}
							 serializer.endTag("", "ul");
							 serializer.endTag("", "p");
						  }
						
						
					serializer.endTag("", "body");
				    
				serializer.endTag("", "html");    
			    serializer.endDocument();
			     FileWriter fw = new FileWriter(item_folder + "/rapport.html");
			     fw.write(writer.toString());
			     fw.close();
		} 
		catch (Exception e) 
	    {
	        throw new RuntimeException(e);
	    } 
	
	}
	
	String generateAttendees(Meeting m)
	{
	    String s = "";
	    boolean tmp = false;
	    for(int i = 0; i< Sequence.getAttendees(SL).size(); i++)
	    {
	    	if(tmp)
	    		s = s + ", ";
	    	else
	    		tmp = true;
	    	
	    	s = s + (String)Sequence.getAttendees(SL).get(i);
	    	
	    }
	    return s;
	}
}
