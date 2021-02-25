package org.sfmis.coactivity.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.sfmis.coactivity.model.LocalActivity;
import org.sfmis.coactivity.service.ActivityService;
import org.sfmis.coactivity.service.LocalActivityService;
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
@RequestMapping("coactivity/local-activity")
public class LocalActivityController {
	
	@Autowired
	LocalActivityService las;
	
	@Autowired
	ValidationService validationService;
	
	@Autowired
	ActivityService as;
	
	@GetMapping("")
	public Map<String, Object> index(HttpServletRequest request) {
		return las.index();
	}
	
	@GetMapping("create")
	public void create(HttpServletRequest request) {

		// to be defined
	}
	
	@PostMapping("")
	public Map<String, Object> store(HttpServletRequest request) {
		Validator validator = validationService.validate(LocalActivity.rules());
		if(validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		}
		return las.store();
	}
	
	@GetMapping("/{id}")
	public void show(HttpServletRequest request, @PathVariable String id) {

		// to be defined
	}
	
	@GetMapping("/{id}/edit")
	public Map<String, Object> edit(HttpServletRequest request, @PathVariable String id) {
		return las.edit(id);
	}
	
	@PutMapping("/{id}")
	public Map<String, Object> update(HttpServletRequest request, @PathVariable String id) {
		Validator validator = validationService.validate(LocalActivity.rules());
		if(validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		} else {
			return las.update(id);
		}
	}
	@DeleteMapping("/{id}")
	public Map<String, Object> destroy(HttpServletRequest request, @PathVariable String id) {
		return las.destroy(id);
	}
	
	@GetMapping("/activities")
	public Map<String, Object> activities() {
		List<Map<String, String>> data = as.getActivities();
		return Messenger.getMessenger().setData(data).success();
	}
	
}
