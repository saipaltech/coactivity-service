package org.sfmis.coactivity.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.sfmis.coactivity.model.SectorialHeading;
import org.sfmis.coactivity.service.SectorialHeadingService;
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
@RequestMapping("coactivity/sector-heading")
public class SectorialHeadingController {

	@Autowired
	SectorialHeadingService shs;
	@Autowired
	ValidationService validationService;

	@GetMapping("")
	public Map<String, Object> index(HttpServletRequest request) {
		return shs.index();
	}

	@PostMapping("")
	public Map<String, Object> store(HttpServletRequest request) {
		Validator validator = validationService.validate(SectorialHeading.rules());
		if (validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		}
		return shs.store();

	}

	@GetMapping("/{id}")
	public Map<String, Object> edit(HttpServletRequest request, @PathVariable String id) {
		return shs.edit(id);

	}

	@PutMapping("/{id}")
	public Map<String, Object> update(HttpServletRequest request, @PathVariable String id) {
		Validator validator = validationService.validate(SectorialHeading.rules());
		if (validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		} else {
			return shs.update(id);
		}

	}

	@DeleteMapping("/{id}")
	public Map<String, Object> destroy(HttpServletRequest request, @PathVariable String id) {
		return shs.destroy(id);
	}
}
