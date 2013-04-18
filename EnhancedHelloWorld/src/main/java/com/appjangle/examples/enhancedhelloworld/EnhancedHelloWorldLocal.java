package com.appjangle.examples.enhancedhelloworld;

import io.nextweb.Node;
import io.nextweb.NodeList;
import io.nextweb.Query;
import io.nextweb.Session;
import io.nextweb.common.LocalServer;
import io.nextweb.jre.Nextweb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class EnhancedHelloWorldLocal {

	private static HashMap<String, String> translationsMap = new HashMap<String, String>();
	
	public static void main(String[] args) {
		
		if(args!=null && args.length>0)
			try {
				initTranslations(args[0]);
			} catch (IOException e) {
				System.out.println("Invalid filename");
				initTranslations();
			}
		else
			initTranslations();
		
		LocalServer server = Nextweb.startServer(9999);
		Session session = Nextweb.createSession();
		Query aTranslation = session.seed("local").append("a translation", "./aTranslation");
		Query alanguage = session.seed("local").append("a translation", "./aLanguage");
		Query translations = session.seed("local").append("translations");
		for(Entry<String, String> entry : translationsMap.entrySet()){
			Query trans = translations.append(entry.getValue()).append(aTranslation);
			trans.append(entry.getKey()).append(alanguage);
		}
		
		session.commit();
		NodeList translationsList = translations.selectAll().get();
		
		Iterator<Node> iterator = translationsList.iterator();
		while(iterator.hasNext()){
			Node node = iterator.next();
			System.out.print("Translation defined: " + node.getValue());
			System.out.println(", uri: " + node.uri());
		}	
		
		session.close();
		server.shutdown();
	    
		
	}

	private static void initTranslations(String fileName) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(fileName));
		for(Object language : prop.keySet()){
			translationsMap.put(language.toString(), prop.get(language).toString());
		}
	}

	private static void initTranslations() {
		translationsMap.put("English", "Hello, world!");
		translationsMap.put("Italian", "Ciao, mondo!");
		translationsMap.put("French", "Salut, monde!");
		translationsMap.put("Spanish", "Hola, mundo!");
		translationsMap.put("Latin", "Salve, mundi!");		
	}

}
