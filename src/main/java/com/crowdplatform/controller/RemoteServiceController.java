package com.crowdplatform.controller;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.crowdplatform.aux.ExecutionInfo;
import com.crowdplatform.aux.ProjectUserInfo;
import com.crowdplatform.aux.TaskInfo;
import com.crowdplatform.aux.TaskRequest;
import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.ProjectUser;
import com.crowdplatform.model.Task;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.TaskRetrievalStrategy;

@Controller
@RequestMapping("/API")
public class RemoteServiceController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BatchExecutionService batchService;
	
	@Autowired
	private TaskRetrievalStrategy taskRetrieval;
	
	@RequestMapping(value="/project/{projectId}/task", method=RequestMethod.POST)
	public @ResponseBody TaskInfo[] provideTask(@PathVariable("projectId") String projectId, 
			@RequestBody TaskRequest request) {
		Integer count = request.getCount();
		if (count == null) {
			count = 1;
		}
		
		Project project = projectService.getProject(projectId);
		if (project.getUid().equals(request.getProjectUid())) {
			List<Task> tasks = taskRetrieval.retrieveTasksForExecution(projectId, count);
			TaskInfo[] data = new TaskInfo[tasks.size()];
			for (int i = 0; i < tasks.size(); ++i) {
				data[i] = new TaskInfo(tasks.get(i));
			}
			return data;
		} else {
			System.out.println("RemoteServiceController: Provide task attempted with wrong uid for project " + projectId + ".");
		}
		
		return null;
	}
	
	
	@RequestMapping(value="/project/{projectId}/execution", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void saveExecution(@PathVariable("projectId") String projectId, 
			@RequestBody ExecutionInfo info) {
		Project project = projectService.getProject(projectId);
		if (project.getUid().equals(info.getProjectUid())) {
			Batch batch = project.getBatch(info.getBatchId());
			if (batch != null && batch.getState() == Batch.State.RUNNING) {
				Task task = batch.getTask(info.getTaskId());
				if (task != null) {
					Execution execution = new Execution(info.getContents());
					execution.setProjectUserId(info.getUserId());
					execution.setTaskId(task.getId());
					BatchExecutionCollection collection = batchService.getExecutions(batch.getExecutionCollectionId());
					collection.addExecution(execution);
					batchService.saveExecutions(collection);
					task.setNumExecutions(task.getNumExecutions() + 1);
					if (task.getNumExecutions() == batch.getExecutionsPerTask()) {
						batch.setNumCompletedTasks(batch.getNumCompletedTasks() + 1);
					}
					projectService.saveProject(project);
				} else {
					System.out.println("RemoteServiceController: Save execution attempted with unexisting task " + 
							projectId + " - " + info.getBatchId() + " - " + info.getTaskId() + ".");
				}
			} else {
				System.out.println("RemoteServiceController: Save execution attempted with unexisting or not running batch " + 
						projectId + " - " + info.getBatchId() + ".");
			}
		} else {
			System.out.println("RemoteServiceController: Save execution attempted with wrong uid for project " + projectId + ".");
		}
	}
	
	@RequestMapping(value="/project/{projectId}/user", method=RequestMethod.POST)
	public @ResponseBody Integer saveUser(@PathVariable("projectId") String projectId, 
			@RequestBody ProjectUserInfo user) {
		Project project = projectService.getProject(projectId);
		if (project.getUid().equals(user.getProjectUid())) {
			ProjectUser newUser = new ProjectUser(user);
			project.addUser(newUser);
			projectService.saveProject(project);
			return newUser.getId();
		} else {
			System.out.println("RemoteServiceController: Save user attempted with wrong uid for project " + projectId + ".");
		}
		return 0;
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ErrorMessage handleException(MethodArgumentNotValidException ex) {
	    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
	    List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
	    List<String> errors = new ArrayList<String>(fieldErrors.size() + globalErrors.size());
	    String error;
	    for (FieldError fieldError : fieldErrors) {
	        error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
	        errors.add(error);
	    }
	    for (ObjectError objectError : globalErrors) {
	        error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
	        errors.add(error);
	    }
	    return new ErrorMessage(errors);
	}
	
	@XmlRootElement
	public class ErrorMessage {
	 
	    private List<String> errors;
	 
	    public ErrorMessage() {
	    }
	 
	    public ErrorMessage(List<String> errors) {
	        this.errors = errors;
	    }
	 
	    public List<String> getErrors() {
	        return errors;
	    }
	 
	    public void setErrors(List<String> errors) {
	        this.errors = errors;
	    }
	}
}
