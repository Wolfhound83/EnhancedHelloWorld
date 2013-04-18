package com.appjangle.examples.enhancedhelloworld;

import io.nextweb.Node;
import io.nextweb.NodeList;
import io.nextweb.Query;
import io.nextweb.Session;
import io.nextweb.jre.Nextweb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		Query aTranslation = session.seed().append("a translation", "./aTranslation");
		Query alanguage = session.seed().append("a translation", "./aLanguage");
		Query translations = session.seed().append("translations");
		for(Entry<String, String> entry : translationsMap.entrySet()){
			Query message = translations.append(entry.getValue());
                        Query trans = message.append(aTranslation);
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
		System.out.print("Hello"); 
		
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
