package org.sfmis.coactivity.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.servlet.http.HttpServletRequest;

import org.sfmis.coactivity.model.Sector;
import org.sfmis.coactivity.service.BroadSectorService;
import org.sfmis.coactivity.service.SectorService;
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
@RequestMapping("coactivity/sector")
public class SectorController {
	@Autowired
	SectorService sectorService;

	@Autowired
	ValidationService validationService;

	@Autowired
	BroadSectorService bservice;

	@GetMapping("")
	public Map<String, Object> index(HttpServletRequest request) {
		return sectorService.index();
	}

	@GetMapping("create")
	public void create(HttpServletRequest request) {

		// to be defined
	}

	@PostMapping("")
	public Map<String, Object> store(HttpServletRequest request) {
		Validator validator = validationService.validate(Sector.rules());
		if (validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		}
		return sectorService.store();
	}

	@GetMapping("/{id}")
	public void show(HttpServletRequest request, @PathVariable String id) {

		// to be defined
	}

	@GetMapping("/{id}/edit")
	public Map<String, Object> edit(HttpServletRequest request, @PathVariable String id) {
		return sectorService.edit(id);
	}

	@PutMapping("/{id}")
	public Map<String, Object> update(HttpServletRequest request, @PathVariable String id) {
		Validator validator = validationService.validate(Sector.rules());
		if (validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		} else {
			return sectorService.update(id);
		}

	}

	@DeleteMapping("/{id}")
	public Map<String, Object> destroy(HttpServletRequest request, @PathVariable String id) {
		return sectorService.destroy(id);
	}

	@GetMapping("/broad-sector")
	public Map<String, Object> boradSector() {
		List<Map<String, String>> data = bservice.getBroadSector();
		return Messenger.getMessenger().setData(data).success();
	}

	

}
