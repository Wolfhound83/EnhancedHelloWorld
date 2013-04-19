package com.appjangle.examples.enhancedhelloworld;

import io.nextweb.Link;
import io.nextweb.ListQuery;
import io.nextweb.Node;
import io.nextweb.NodeList;
import io.nextweb.Query;
import io.nextweb.Session;
import io.nextweb.jre.Nextweb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class RetrievalExample {

	private static final String FIRST_LANGUAGE = "English";
	private static final String SECOND_LANGUAGE = "French";

	/**
	 * @param English names of the languages of the translations to retrieve
	 * If missing, two default translations will be retrieved. 
	 */
	public static void main(String[] args) {
		
		if(args==null || args.length==0)
			args = new String[]{FIRST_LANGUAGE, SECOND_LANGUAGE};
		
		String seedNodeUri;
		try {
			seedNodeUri = loadUri();
		} catch (IOException e) {
			System.out.println("Cannot retrieve seedNode uri: ");
			System.out.println(e);
			return;
		}
		if(seedNodeUri == null){
			System.out.println("No translations uploaded.");
			return;
		}
		
		
		Session session = Nextweb.createSession();	
		//Node seedNode = session.node(seedNodeUri).get();
		Link aLanguage = session.node(seedNodeUri+"/aLanguage");
		Link aTranslation = session.node(seedNodeUri+"/aTranslation");
		Node translations = session.node(seedNodeUri+"/translations").get();
		List<Node> translationsList = translations.selectAll(aTranslation).get().asList();
		for(Node node : translationsList){			
			String language = (String) node.select(aLanguage).get().getValue();	
			for(String l : args){
				if(l.equals(language)){
					System.out.print(node.getValue());
					System.out.print(", language ");
					System.out.println(l);
				}
			}	
		}
		
		session.close().get();
		

	}

	private static String loadUri() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("uri.properties"));
		return prop.getProperty("seedNode");
	}

}
