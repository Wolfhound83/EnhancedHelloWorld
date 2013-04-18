package com.appjangle.examples.enhancedhelloworld;

import io.nextweb.Query;
import io.nextweb.Session;
import io.nextweb.jre.Nextweb;

public class HelloWorld {

	
	public static void main(String[] args) {
		
	Session session = Nextweb.createSession();
        Query hello = session.seed().append("Hello, Java!");
        System.out.println("Created:\n"+hello.get());
        session.close().get();
        
	}

}
