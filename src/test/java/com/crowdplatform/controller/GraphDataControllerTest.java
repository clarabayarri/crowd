package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.util.DataMiner;

@RunWith(MockitoJUnitRunner.class)
public class GraphDataControllerTest {

	@InjectMocks
	private GraphDataController controller = new GraphDataController();
	
	@Mock
	private ProjectService projectService;
	
	@Mock
	private BatchExecutionService batchService;
	
	@Mock
	private PlatformUserService userService;
	
	@Mock
	private DataMiner dataMiner;
	
	private Project project;
	private static final String projectOwnerId = "owner";
	private PlatformUser user;
	private Batch batch;
	private static final Integer batchId = 3;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    project = new Project();
	    project.setOwnerId(projectOwnerId);
	    user = new PlatformUser();
	    batch = new Batch();
	    project.addBatch(batch);
	    batch.setId(batchId);
	    Mockito.when(projectService.getProject(Mockito.anyString())).thenReturn(project);
	    Mockito.when(userService.getCurrentUser()).thenReturn(user);
	}
	
	@Test
	public void testShowGraphsHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.showGraphs(model, "project");
		
		assertEquals("graphs", result);
	}
	
	@Test
	public void testShowGraphsAddsProjectToModelIfAuthorized() {
		user.setUsername(projectOwnerId);
		Model model = Mockito.mock(Model.class);
		
		controller.showGraphs(model, "project");
		
		Mockito.verify(model).addAttribute("project", project);
	}
	
	@Test
	public void testShowGraphsDoesntAddProjectToModelIfNotAuthorized() {
		user.setUsername("other username");
		Model model = Mockito.mock(Model.class);
		
		controller.showGraphs(model, "project");
		
		Mockito.verify(model, Mockito.never()).addAttribute("project", project);
	}
	
	@Test
	public void testShowBatchGraphsHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.showBatchGraphs(model, "project", batchId);
		
		assertEquals("graphs", result);
	}
	
	@Test
	public void testShowBatchGraphsAddsBatchToModelIfAuthorized() {
		user.setUsername(projectOwnerId);
		Model model = Mockito.mock(Model.class);
		
		controller.showBatchGraphs(model, "project", batchId);
		
		Mockito.verify(model).addAttribute("project", project);
	}
	
	@Test
	public void testShowBatchGraphsDoesntAddBatchToModelIfNotAuthorized() {
		user.setUsername("other username");
		Model model = Mockito.mock(Model.class);
		
		controller.showBatchGraphs(model, "project", batchId);
		
		Mockito.verify(model, Mockito.never()).addAttribute("batch", batch);
	}
	
	@Test
	public void testObtainFieldDataIfNotAuthorized() {
		user.setUsername("other user");
		Map<Object, Object> result = controller.obtainFieldData("project", "field");
		
		assertNull(result);
	}
	
	@Test
	public void testObtainFieldData() {
		user.setUsername(projectOwnerId);
		controller.obtainFieldData("project", "field");
		
		Mockito.verify(dataMiner).aggregateByField(project, "field");
	}
	
	@Test
	public void testObtainBatchFieldDataIfNotAuthorized() {
		user.setUsername("other user");
		Map<Object, Object> result = controller.obtainBatchFieldData("project", batchId, "field");
		
		assertNull(result);
	}
	
	@Test
	public void testObtainBatchFieldData() {
		user.setUsername(projectOwnerId);
		controller.obtainBatchFieldData("project", batchId, "field");
		
		Mockito.verify(dataMiner).aggregateByField(project, batch, "field");
	}
}
