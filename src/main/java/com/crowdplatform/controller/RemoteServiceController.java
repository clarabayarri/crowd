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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.crowdplatform.model.Execution;
import com.crowdplatform.model.ExecutionInfo;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.ProjectUser;
import com.crowdplatform.model.Task;
import com.crowdplatform.model.TaskInfo;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.ProjectUserService;
import com.crowdplatform.service.TaskRetrievalStrategy;
import com.crowdplatform.service.TaskService;

@Controller
@RequestMapping("/API")
public class RemoteServiceController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TaskRetrievalStrategy taskRetrieval;
	
	@Autowired
	private ProjectUserService userService;
	
	@RequestMapping(value="/project/{projectId}/task", method=RequestMethod.GET)
	public @ResponseBody TaskInfo[] provideTask(@PathVariable("projectId") Integer projectId,
			@RequestParam(value="count", required=false) Integer count) {
		if (count == null) {
			count = 1;
		}
		List<Task> tasks = taskRetrieval.retrieveTasksForExecution(projectId, count);
		TaskInfo[] data = new TaskInfo[tasks.size()];
		for (int i = 0; i < tasks.size(); ++i) {
			data[i] = new TaskInfo(tasks.get(i));
		}
		return data;
	}
	
	
	@RequestMapping(value="/project/{projectId}/execution", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void saveExecution(@PathVariable("projectId") Integer projectId, @RequestBody ExecutionInfo info) {
		// TODO: check project
		Task task = taskService.getTask(info.getTaskId());
		Execution execution = new Execution(info.getContents());
		task.getExecutions().add(execution);
		taskService.saveTask(task);
	}
	
	@RequestMapping(value="/project/{projectId}/user", method=RequestMethod.POST)
	public @ResponseBody Integer saveUser(@PathVariable("projectId") Integer projectId, @RequestBody ProjectUser user) {
		Project project = projectService.getProject(projectId);
		if (project != null) {
			project.addUser(user);
			projectService.saveProject(project);
			return user.getId();
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
