package com.appjangle.examples.enhancedhelloworld;

import io.nextweb.Node;
import io.nextweb.NodeList;
import io.nextweb.Query;
import io.nextweb.Session;
import io.nextweb.jre.Nextweb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * 
 * This class uploads translations of "Hello, World" in five different languages 
 * on the embedded server, together with the English name of the respective languages; 
 * it prints the uploaded translations with their uri.
 *
 */
public class EnhancedHelloWorld {

	private static HashMap<String, String> translationsMap = new HashMap<String, String>();
	
	/**
	 * 
	 * @param args a filename for a file properties which contains the translations to upload.
	 * If the filename is missing or invalid, 5 default translations will be uploaded.
	 */
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
		
		Session session = Nextweb.createSession();
		Node seedNode = session.seed().get();
		try {
			store(seedNode.getUri());
		} catch (IOException e) {
			System.out.println("Cannot store uri: ");
			System.out.println(e);
		}
		Query aTranslation = seedNode.append("a translation", "./aTranslation");
		Query aLanguage = seedNode.append("a language", "./aLanguage");
		Query translations = seedNode.append("translations", "./translations");
		for(Entry<String, String> entry : translationsMap.entrySet()){
			Node message = translations.append(entry.getValue()).get();			
            message.append(aTranslation);
			message.append(entry.getKey()).append(aLanguage);
		}
		
		session.commit().get();
		NodeList translationsList = translations.selectAll().get();
		
		Iterator<Node> iterator = translationsList.iterator();
		while(iterator.hasNext()){
			Node node = iterator.next();
			System.out.print("Translation defined: " + node.getValue());
			System.out.println(", uri: " + node.uri());
		}	
		
		session.close().get();		
		
	}

	private static void store(String uri) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.put("seedNode", uri);
		prop.store(new FileOutputStream("uri.properties"), null);
		
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
