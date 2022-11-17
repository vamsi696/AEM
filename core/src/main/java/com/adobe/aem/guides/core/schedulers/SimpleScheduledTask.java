/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aem.guides.core.schedulers;

import java.util.Iterator;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.core.models.ResolverUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * A simple demo for cron-job like tasks that get executed regularly.
 * It also demonstrates how property values can be set. Users can
 * set the property values in /system/console/configMgr
 */
@Designate(ocd=SimpleScheduledTask.Config.class)
@Component(service=Runnable.class)
public class SimpleScheduledTask implements Runnable {
	
	 @Reference
	    private ResourceResolverFactory resourceResolverFactory;

    @ObjectClassDefinition(name="A scheduled task",
                           description = "Simple demo for cron-job like task with properties")
    public static @interface Config {

        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "*/30 * * * * ?";

        @AttributeDefinition(name = "Concurrent task",
                             description = "Whether or not to schedule this task concurrently")
        boolean scheduler_concurrent() default false;

        @AttributeDefinition(name = "A parameter",
                             description = "Can be configured in /system/console/configMgr")
        String myParameter() default "";
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String myParameter;
    
    @Override
    public void run() {
    	
    	   ResourceResolver resourceResolver;
		try {
			resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
		
           PageManager pageManager=resourceResolver.adaptTo(PageManager.class);
           Page page=pageManager.getPage("/content/we-retail/us/en");
           Iterator<Page> pages=page.listChildren();
           String pagepaths="";
           while(pages.hasNext()) {
           	pagepaths=pagepaths+pages.next().getTitle()+"\n";
           } 
		     logger.info("SimpleScheduledTask is now running, "+pagepaths);
		   } 
	        catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Activate
    protected void activate(final Config config) {
        myParameter = config.myParameter();
    }

}
