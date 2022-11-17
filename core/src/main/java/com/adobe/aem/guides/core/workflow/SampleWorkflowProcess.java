package com.adobe.aem.guides.core.workflow;


	import java.util.Arrays;

	import org.apache.commons.lang3.StringUtils;
	import org.osgi.service.component.annotations.Component;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;

	import com.adobe.granite.workflow.WorkflowException;
	import com.adobe.granite.workflow.WorkflowSession;
	import com.adobe.granite.workflow.exec.WorkItem;
	import com.adobe.granite.workflow.exec.WorkflowProcess;
	import com.adobe.granite.workflow.metadata.MetaDataMap;

	
	@Component(service = WorkflowProcess.class, property = { "process.label=" + "Process Step Example Naruto" })
	public class SampleWorkflowProcess implements WorkflowProcess {

	 private final Logger log = LoggerFactory.getLogger(this.getClass());

	 @Override
	 public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
	   throws WorkflowException {

	
	   String path = workItem.getWorkflowData().getPayload().toString();
	   log.info("Payload path: {}", path);
	  }
}
