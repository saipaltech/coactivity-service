package org.sfmis.coactivity.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.sfmis.coactivity.model.BroadSector;
import org.sfmis.coactivity.service.BroadSectorService;
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
@RequestMapping("")
public class BroadSectorController {

	@Autowired
	BroadSectorService broadSectorService;

	@Autowired
	ValidationService validationService;

	@GetMapping("")
	public Map<String, Object> index(HttpServletRequest request) {
		return broadSectorService.index();
	}

	@GetMapping("create")
	public void create(HttpServletRequest request) {

		// to be defined
	}

	@PostMapping("")
	public Map<String, Object> store(HttpServletRequest request) {

		Validator validator = validationService.validate(BroadSector.rules());
		if (validator.isFailed()) {

			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		}
		return broadSectorService.store();
	}

	@GetMapping("/{id}")
	public void show(HttpServletRequest request, @PathVariable String id) {

		// to be defined
	}

	@GetMapping("/{id}/edit")
	public Map<String, Object> edit(HttpServletRequest request, @PathVariable String id) {
		return broadSectorService.edit(id);
	}

	@PutMapping("/{id}")
	public Map<String, Object> update(HttpServletRequest request, @PathVariable String id) {
		Validator validator = validationService.validate(BroadSector.rules());
		if (validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();

		} else {
			return broadSectorService.update(id);
		}
	}

	@DeleteMapping("/{id}")
	public Map<String, Object> destroy(HttpServletRequest request, @PathVariable String id) {
		return broadSectorService.destroy(id);
	}
}
