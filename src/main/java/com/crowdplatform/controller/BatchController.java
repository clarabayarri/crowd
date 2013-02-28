package com.crowdplatform.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crowdplatform.model.Batch;
import com.crowdplatform.service.BatchService;

@Controller
@RequestMapping("/batches")
public class BatchController {

	@Autowired
    private BatchService batchService;

    @RequestMapping("/")
    public String listBatches(Map<String, Object> map) {
        map.put("batchList", batchService.listBatches());
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
    public String getBatch(@PathVariable("batchId") Integer batchId, Map<String, Object> map) {
    	map.put("batch", batchService.getBatch(batchId));
    	return "batch";
    }
}
