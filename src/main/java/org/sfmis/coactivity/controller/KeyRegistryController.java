package org.sfmis.coactivity.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.sfmis.coactivity.service.KeyRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("coactivity/keyRegistry")
public class KeyRegistryController {
	
	@Autowired
	KeyRegistryService krs;
	
	@GetMapping("")
	public ResponseEntity<Map<String, Object>> index(HttpServletRequest request) {
		return krs.index();
	}
	@PostMapping("")
	public ResponseEntity<Map<String, Object>> store(HttpServletRequest request) {
		return krs.store();
	}
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> edit(HttpServletRequest request, @PathVariable String id) {
		return krs.edit(id);
	}
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(HttpServletRequest request, @PathVariable String id) {
		return krs.update(id);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> destroy(HttpServletRequest request, @PathVariable String id) {
		return krs.destroy(id);
	}
}
