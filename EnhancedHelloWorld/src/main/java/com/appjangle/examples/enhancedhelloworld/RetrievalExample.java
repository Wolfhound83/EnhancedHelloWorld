package com.appjangle.examples.enhancedhelloworld;

import io.nextweb.Link;
import io.nextweb.Node;
import io.nextweb.Session;
import io.nextweb.jre.Nextweb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

public class RetrievalExample {

	private static final String FIRST_LANGUAGE = "English";
	private static final String SECOND_LANGUAGE = "Italian";
	private static TreeMap<String, String> translationsToDownload;
	private static final String CANNOT_FIND_TRANSLATION_MESSAGE = "This translation has not been uploaded.";

	/**
	 * @param English names of the languages of the translations to retrieve
	 * If missing, two default translations will be downloaded. 
	 */
	public static void main(String[] args) {
		
		//instantiating translationsToDownload with the requested languages
		init(args);
			
		//retrieving the uri to the seed node of the application stored in uri.properties file
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
		
		// browsing the children of the seed node to retrieve the requested translations
		Session session = Nextweb.createSession();	
		Link aLanguage = session.node(seedNodeUri+"/aLanguage");
		Link aTranslation = session.node(seedNodeUri+"/aTranslation");
		Node translations = session.node(seedNodeUri+"/translations").get();
		List<Node> translationsList = translations.selectAll(aTranslation).get().asList();
		for(Node node : translationsList){			
			String language = (String) node.select(aLanguage).get().getValue();	
			if(translationsToDownload.containsKey(language))
				translationsToDownload.put(language, (String) node.value());
		}
		session.close().get();
		
		// printing the downloaded translations
		for(String language : translationsToDownload.keySet()){
			System.out.print("Language: "+language+", ");
			System.out.println("translation: "+translationsToDownload.get(language));
		}
		

	}

	
	private static void init(String[] args) {
		if(args==null || args.length==0)
			args = new String[]{FIRST_LANGUAGE, SECOND_LANGUAGE};
		translationsToDownload = new TreeMap<String, String>();
		for(String s : args)
			translationsToDownload.put(s, CANNOT_FIND_TRANSLATION_MESSAGE);		
	}

	private static String loadUri() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("uri.properties"));
		return prop.getProperty("seedNode");
	}

}
