package com.adobe.aem.guides.core.listeners;


import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service = EventListener.class, immediate = true)
public class SimpleEventListener implements EventListener {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(SimpleEventListener.class);
	
	/**
	 * Resource Resolver Factory
	 */
	@Reference
	private ResourceResolverFactory resolverFactory;
	
	/**
	 * Resource Resolver
	 */
	private ResourceResolver resolver;
	
	@Reference
	private SlingRepository repository;
	
	/**
	 * Session object
	 */
	private Session session;
	
	/**
	 * Activate method to initialize stuff
	 */
	@Activate
	protected void activate(ComponentContext componentContext) {
		
		log.info("Activating the observation");
		
		try {
			
			/**
			 * This map will be used to get session via getServiceResourceResolver() method
			 */
			Map<String, Object> params = new HashMap<>();
			
			/**
			 * Adding the subservice name in the param map
			 */
			params.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
			
			/**
			 * Getting resource resolver from the service factory
			 */
			resolver = resolverFactory.getServiceResourceResolver(params);
			
			/**
			 * Adapting the resource resolver to session object
			 */
			session = resolver.adaptTo(Session.class);
			
			log.info("Session created");
			
			/**
			 * Adding the event listener
			 */
			session.getWorkspace().getObservationManager().addEventListener(this,Event.PROPERTY_ADDED | Event.NODE_ADDED, "/content/wknd/us/en", true, null, null, false);			
			
		} catch (Exception e) {	
			log.error(e.getMessage(), e);
		}
	}
	
	@Deactivate
	protected void deactivate() {
		if(session != null) {
			session.logout();
		}
	}
	
	@Override
	public void onEvent(EventIterator events) {
		try {
			while(events.hasNext()) {
				log.info("Hemanth Has Added Something New In The Field: {} ", events.nextEvent().getPath() );
			}
		} catch (Exception e) {
			log.error("Exception occurred", e);
		}
	}

}