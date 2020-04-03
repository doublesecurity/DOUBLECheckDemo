package com.doublesecuritytechnologies.doublecheckdemo.worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class ADSSClientManager {
	
	// Constants for the ADSS Web Service. 
	// TODO: Consider a configuration file for constants required.
	private static final String dataDirectory = "data/";
	private static final String keyStoreFilename = dataDirectory + "demo-web.p12";  // certificate for: DEMO/*/web (valid until: Jul 30, 2020)
	private static final String keyStorePassword = "demo";
	private static final String trustStoreFilename = dataDirectory + "cacerts";  // when in production; this should be the official 'cacerts' file.
	//private static final String trustStorePassword = null;
	
	private static final String adssTarget = "https://dev.apidoublecheck.com/v1";
	
	private KeyStore keyStore = null;
	private KeyStore trustStore = null;
	private Client adssClient = null;
	private WebTarget target = null;
	
	private static Logger log = Logger.getLogger("com.doublesecuritytechnologies.doublecheckdemo.worker");
	
	
	public ADSSClientManager() {
		
		super();
		log.finest("ADSS: ADSSClientManager (working bean) instantiated.");

		// Prepare TLS communication. ADSS Web Service requires Client-Certificate Authentication
		try {
			// Prepare keystore for SSL exchange (client key)
			keyStore = KeyStore.getInstance("PKCS12");
			InputStream keyStoreFile = new FileInputStream(new File(keyStoreFilename));
			keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
			
			// Prepare truststore for SSL exchange (server trusted certificates)
			trustStore = KeyStore.getInstance("JKS");
			InputStream trustStoreFile = new FileInputStream(new File(trustStoreFilename));
			trustStore.load(trustStoreFile, null);
	
			// Initialize the Web Services Client
			ClientBuilder adssClientBuilder = ClientBuilder.newBuilder()
                    .keyStore(keyStore, keyStorePassword.toCharArray())
                    .trustStore(trustStore);
			this.adssClient = adssClientBuilder.build();
			this.target = adssClient.target(adssTarget);

		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setPath(String path) {
		this.target = adssClient.target(adssTarget).path(path);
	}
	
	public void resolveTemplate(String element, Object content) {
		this.target = this.target.resolveTemplate(element, content);
	}
	
	public void queryParam(String element, String value) {
		this.target = this.target.queryParam(element, value);
	}
	
	public WebTarget getTarget() {
		return this.target;
	}
	
	public void close() {
		this.adssClient.close();
	}
	
}
