package com.crowdplatform.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.crowdplatform.model.Batch;
import com.crowdplatform.service.BatchService;

@Controller
@RequestMapping("/batches")
public class BatchController {

	@Autowired
    private BatchService batchService;

    @RequestMapping("/")
    public String listBatches(Model model) {
    	model.addAttribute(batchService.listBatches());
        return "batches";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBatch(@ModelAttribute("batch") Batch batch, BindingResult result) {
        batchService.addBatch(batch);
        return "redirect:/batches/";
    }

    @RequestMapping("/delete/{batchId}")
    public String deleteBatch(@PathVariable("batchId") Integer batchId) {
        batchService.removeBatch(batchId);
        return "redirect:/batches/";
    }
    
    @RequestMapping("/batch/{batchId}")
    public String getBatch(@PathVariable("batchId") Integer batchId, Model model) {
    	model.addAttribute(batchService.getBatch(batchId));
    	return "batch";
    }
    
    @RequestMapping("/batch/{batchId}/start")
    public String startBatch(@PathVariable("batchId") Integer batchId) {
    	batchService.startBatch(batchId);
    	return "redirect:/batches/";
    }
    
    @RequestMapping("/batch/{batchId}/pause")
    public String pauseBatch(@PathVariable("batchId") Integer batchId) {
    	batchService.pauseBatch(batchId);
    	return "redirect:/batches/";
    }
    
    @RequestMapping("/new")
    public String newBatch(Model model) {
    	model.addAttribute(new Batch());
    	return "create";
    }
    
    @RequestMapping(value="create", method = RequestMethod.POST)
    public String createBatch(@Valid Batch batch, BindingResult bindingResult, @RequestParam(value="taskFile", required=false) MultipartFile taskFile) {
    	if (bindingResult.hasErrors()) {
    		return "create";
    	}
    	
    	batchService.addBatch(batch);
    	
    	if (taskFile != null && !taskFile.isEmpty()) {
    		if (validateFileFormat(taskFile)) {
    			System.out.println("OK");
    		} else {
    			bindingResult.reject("error.file.format");
    			return "create";
    		}
    	}
    	
    	return "redirect:/batches/";
    }
    
    private boolean validateFileFormat(MultipartFile file) {
    	if (!file.getContentType().equals("text/csv")) return false;
    	return true;
    }
}
