package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.Batch;
import com.example.service.BatchService;

@Controller
public class BatchController {

	@Autowired
    private BatchService batchService;

    @RequestMapping("/batches/")
    public String listBatches(Map<String, Object> map) {

        map.put("batch", new Batch());
        map.put("batchList", batchService.listBatches());

        return "batches";
    }

    @RequestMapping(value = "/batches/add", method = RequestMethod.POST)
    public String addBatch(@ModelAttribute("batch") Batch batch, BindingResult result) {

        batchService.addBatch(batch);

        return "redirect:/batches/";
    }

    @RequestMapping("/batches/delete/{batchId}")
    public String deleteBatch(@PathVariable("batchId") Integer batchId) {

        batchService.removeBatch(batchId);

        return "redirect:/batches/";
    }
}
