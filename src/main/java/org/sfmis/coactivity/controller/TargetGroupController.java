package org.sfmis.coactivity.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.sfmis.coactivity.model.TargetGroup;
import org.sfmis.coactivity.service.TargetGroupService;
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
@RequestMapping("coactivity/target-group")
public class TargetGroupController {
	@Autowired
	TargetGroupService tgs;

	@Autowired
	ValidationService validationService;

	@GetMapping("")
	public Map<String, Object> index(HttpServletRequest request) {
		return tgs.index();
	}

	@PostMapping("")
	public Map<String, Object> store(HttpServletRequest request) {
		Validator validator = validationService.validate(TargetGroup.rules());
		if (validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		} else {
			return tgs.store();
		}

	}

	@GetMapping("/{id}")
	public Map<String, Object> edit(HttpServletRequest request, @PathVariable String id) {
		return tgs.edit(id);
	}

	@PutMapping("/{id}")
	public Map<String, Object> update(HttpServletRequest request, @PathVariable String id) {
		Validator validator = validationService.validate(TargetGroup.rules());
		if (validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		} else {
			return tgs.update(id);
		}

	}

	@DeleteMapping("/{id}")
	public Map<String, Object> destroy(HttpServletRequest request, @PathVariable String id) {
		return tgs.destroy(id);
	}
}
