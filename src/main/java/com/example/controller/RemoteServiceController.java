package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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

import com.example.model.Execution;
import com.example.model.Task;
import com.example.service.TaskService;

@Controller
@RequestMapping("/API")
public class RemoteServiceController {

	@Autowired
	private TaskService taskService;
	
	@RequestMapping(value="/task", method=RequestMethod.GET)
	public @ResponseBody Task provideTask(@PathVariable String projectId) {
		Task task = taskService.getTask();
		return task;
	}
	
	
	@RequestMapping(value="/execution", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void saveExecution(@RequestBody Execution execution, BindingResult result, HttpServletResponse response)
            throws BindException {
		if(result.hasErrors()) {
			throw new BindException(result);
		}
		
		// TODO: save execution
		
		// TODO: add execution ID to the end
		response.setHeader("Location", "/execution/");
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
