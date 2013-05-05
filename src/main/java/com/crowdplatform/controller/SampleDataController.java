package com.crowdplatform.controller;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.Task;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.google.common.collect.Sets;

@Controller
@RequestMapping("/sample")
public class SampleDataController {

	@Autowired
	private PlatformUserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BatchExecutionService batchService;
	
	private Random random = new Random();
	
	@RequestMapping("/")
	public String createSampleData(Map<String, Object> map) {
		createSampleProject();
		
		return "redirect:/projects";
	}
	
	@RequestMapping("/clean")
	public String cleanSampleData() {
		List<PlatformUser> users = userService.listUsers();
		for (PlatformUser user : users) {
			List<Project> projects = projectService.getProjectsForUser(user.getUsername());
			for (Project project : projects) {
				projectService.removeProject(project.getId());
			}
			userService.removeUser(user.getUsername());
		}
		List<BatchExecutionCollection> collections = batchService.listCollections();
		for (BatchExecutionCollection collection : collections) {
			batchService.removeCollection(collection);
		}
		return "redirect:/projects";
	}
	
	private static final String[] definitions = {"{\"answers\":[],\"id\":1,\"word\":\"a lot\",\"level\":1,\"type\":\"separation\",\"language\":\"EN\",\"display\":\"alot\"}",
		"{\"answers\":[\"a\",\"s\",\"i\",\"u\"],\"id\":3,\"word\":\"boasted\",\"level\":1,\"type\":\"substitution\",\"language\":\"EN\",\"display\":\"boested\"}",
		"{\"answers\":[\"ua\",\"ie\",\"i\",\"ei\"],\"id\":4,\"word\":\"actually\",\"level\":1,\"type\":\"substitution\",\"language\":\"EN\",\"display\":\"act|ai|lly\"}",
		"{\"answers\":[],\"id\":5,\"word\":\"bacon\",\"level\":1,\"type\":\"omission\",\"language\":\"EN\",\"display\":\"baecon\"}",
		"{\"answers\":[\"t\",\"e\",\"y\",\"h\"],\"id\":7,\"word\":\"trust\",\"level\":1,\"type\":\"insertion1\",\"language\":\"EN\",\"display\":\"trus_\"}",
		"{\"answers\":[\"t\"],\"id\":8,\"word\":\"trust\",\"level\":1,\"type\":\"insertion\",\"language\":\"EN\",\"display\":\"trus\"}",
		"{\"answers\":[\"iness\",\"izer\",\"ition\",\"less\"],\"id\":10,\"word\":\"scariness\",\"level\":1,\"type\":\"derivation\",\"language\":\"EN\",\"display\":\"scar\"}",
		"{\"answers\":[],\"id\":16,\"word\":\"jueves\",\"level\":1,\"type\":\"omission\",\"language\":\"ES\",\"display\":\"jruerves\"}"};
	private static final Batch.State[] states = {Batch.State.RUNNING, Batch.State.PAUSED};
	
	private void createSampleProject() {
		Project project = new Project();
		project.setName("Dyseggxia project");
		SecureRandom random = new SecureRandom();
		project.setUid(random.nextLong());
		createInputFields(project);
		createOutputFields(project);
		createUserFields(project);
		projectService.addProject(project);
		
		for (int i = 0; i < 1; ++i) {
			Batch batch = createSampleBatch();
			batch.setId(i + 1);
			project.addBatch(batch);
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	    	String username = auth.getName();
		    PlatformUser user = userService.getUser(username);
		    project.setOwnerId(user.getUsername());
		    userService.saveUser(user);
	    }
	    projectService.saveProject(project);
	}
	
	private void createInputFields(Project project) {
		Field field0 = new Field();
		field0.setName("id");
		field0.setType(Field.Type.INTEGER);
		field0.setFieldType(Field.FieldType.INPUT);
		project.addInputField(field0);
		Field field1 = new Field();
		field1.setName("type");
		field1.setType(Field.Type.STRING);
		field1.setFieldType(Field.FieldType.INPUT);
		project.addInputField(field1);
		Field field2 = new Field();
		field2.setName("level");
		field2.setType(Field.Type.INTEGER);
		field2.setFieldType(Field.FieldType.INPUT);
		project.addInputField(field2);
		Field field3 = new Field();
		field3.setName("language");
		field3.setType(Field.Type.STRING);
		field3.setFieldType(Field.FieldType.INPUT);
		project.addInputField(field3);
		Field field4 = new Field();
		field4.setName("word");
		field4.setType(Field.Type.STRING);
		field4.setFieldType(Field.FieldType.INPUT);
		project.addInputField(field4);
		Field field5 = new Field();
		field5.setName("display");
		field5.setType(Field.Type.STRING);
		field5.setFieldType(Field.FieldType.INPUT);
		project.addInputField(field5);
		Field field6 = new Field();
		field6.setName("answers");
		field6.setType(Field.Type.MULTIVALUATE_STRING);
		field6.setFieldType(Field.FieldType.INPUT);
		field6.setColumnNames(Sets.newHashSet("correct", "dis_1", "dis_2", "dis_3", "dis_4", "dis_5", "dis_6"));
		project.addInputField(field6);
	}
	
	private void createOutputFields(Project project) {
		Field field0 = new Field();
		field0.setName("timeSpent");
		field0.setType(Field.Type.INTEGER);
		field0.setFieldType(Field.FieldType.OUTPUT);
		project.addOutputField(field0);
		Field field1 = new Field();
		field1.setName("failedAttempts");
		field1.setType(Field.Type.INTEGER);
		field1.setFieldType(Field.FieldType.OUTPUT);
		project.addOutputField(field1);
		Field field2 = new Field();
		field2.setName("wrongAnswers");
		field2.setType(Field.Type.MULTIVALUATE_STRING);
		field2.setFieldType(Field.FieldType.OUTPUT);
		project.addOutputField(field2);
	}
	
	private void createUserFields(Project project) {
		Field field = new Field();
		field.setName("dyslexic");
		field.setType(Field.Type.BOOL);
		field.setFieldType(Field.FieldType.USER);
		project.addUserField(field);
	}
	
	private Batch createSampleBatch() {
		Batch batch = new Batch();
		batch.setName("Wonderful" + random.nextInt(99));
		int exPerTask = random.nextInt(10) + 1;
		batch.setExecutionsPerTask(exPerTask);
		int stateIndex = random.nextInt(states.length);
		batch.setState(states[stateIndex]);
		
		BatchExecutionCollection collection = new BatchExecutionCollection();
		batchService.saveExecutions(collection);
		batch.setExecutionCollectionId(collection.getId());
		
		int numTasks = random.nextInt(15) + 1;
		for (int i = 0; i < numTasks; ++i) {
			Task task = new Task();
			int numExecutions = random.nextInt(exPerTask + 1);
			task.setNumExecutions(numExecutions);
			int index = random.nextInt(definitions.length);
			task.setContents(definitions[index]);
			batch.addTask(task);
			
			for (int j = 0; j < numExecutions; ++ j) {
				Execution execution = new Execution("{\"timeSpent\":300,\"failedAttempts\":1,\"wrongAnswers\":[\"answer\"]}");
				execution.setTaskId(task.getId());
				collection.addExecution(execution);
			}
		}
		batchService.saveExecutions(collection);
		
		return batch;
	}
}
