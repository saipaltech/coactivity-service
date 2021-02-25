package org.sfmis.coactivity.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.sfmis.coactivity.model.CentralActivity;
import org.sfmis.coactivity.service.CentralActivityService;
import org.sfmis.coactivity.service.SectorialActivityService;
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
@RequestMapping("coactivity/central-activity")
public class CentralActivityController {
	
	@Autowired
	CentralActivityService cas;
	
	@Autowired
	ValidationService validationService;
	
	@Autowired
	SectorialActivityService sas;
	@GetMapping("")
	public Map<String, Object> index(HttpServletRequest request) {
		return cas.index();
	}
	@GetMapping("create")
	public void create(HttpServletRequest request) {

		// to be defined
	}
	
	@PostMapping("")
	public Map<String, Object> store(HttpServletRequest request) {
		Validator validator = validationService.validate(CentralActivity.rules());
		if(validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		}
		return cas.store();
	}
	@GetMapping("/{id}")
	public void show(HttpServletRequest request, @PathVariable String id) {

		// to be defined
	}
	@GetMapping("/{id}/edit")
	public Map<String, Object> edit(HttpServletRequest request, @PathVariable String id) {
		
		return cas.edit(id);
	}
	@PutMapping("/{id}")
	public Map<String, Object> update(HttpServletRequest request, @PathVariable String id) {
		Validator validator = validationService.validate(CentralActivity.rules());
		if(validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		} else {
			return cas.update(id);
		}
	}
	@DeleteMapping("/{id}")
	public Map<String, Object> destroy(HttpServletRequest request, @PathVariable String id) {
		return cas.destroy(id);
	}
	@GetMapping("/sectorialActivities")
	public Map<String, Object> sectorialActivities(){
		List<Map<String, String>> data = sas.getSectorialActivities();
		return Messenger.getMessenger().setData(data).success();
		
	}
}
