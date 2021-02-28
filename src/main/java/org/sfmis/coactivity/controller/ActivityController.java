package org.sfmis.coactivity.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.sfmis.coactivity.model.Activity;
import org.sfmis.coactivity.service.ActivityService;
import org.sfmis.coactivity.service.CentralActivityService;
import org.sfmis.coactivity.service.CofogService;
import org.sfmis.coactivity.util.Messenger;
import org.sfmis.coactivity.util.ValidationService;
import org.sfmis.coactivity.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("coactivity/activity")
public class ActivityController {
	
	@Autowired
	ActivityService as;
	
	@Autowired
	ValidationService validationService;
	
	@Autowired
	CentralActivityService cas;
	
	@Autowired
	CofogService cs;
	
	@GetMapping("")
	public Map<String, Object> index(HttpServletRequest request) {
		return as.index();
	}

	@PostMapping("")
	public Map<String, Object> store(HttpServletRequest request) {
		Validator validator = validationService.validate(Activity.rules());
		if(validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		}
		return as.store();
	}
	
	
	@GetMapping("/{id}")
	public Map<String, Object> edit(HttpServletRequest request, @PathVariable String id) {
		
		return as.edit(id);
	}
	@PutMapping("/{id}")
	public Map<String, Object> update(HttpServletRequest request, @PathVariable String id) {
		Validator validator = validationService.validate(Activity.rules());
		if(validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		} else {
			return as.update(id);
		}
	}
	@DeleteMapping("/{id}")
	public Map<String, Object> destroy(HttpServletRequest request, @PathVariable String id) {
		return as.destroy(id);
	}
	@GetMapping("/centralActivities")
	public Map<String, Object> centralActivities() {
		List<Map<String, String>> data = cas.getCentralActivities();
	    return Messenger.getMessenger().setData(data).success();
		
		
	}
	
	@GetMapping("/cofog")
	public Map<String, Object> cofog() {
		List<Map<String, String>> data = cs.getCofog();
		return Messenger.getMessenger().setData(data).success();
		
		
	}
}
