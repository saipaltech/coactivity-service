package org.sfmis.coactivity.sql;

import org.sfmis.coactivity.sql.sqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;;
@RestController
public class sqlController {
	@Autowired
	private sqlService record;
	@PostMapping("/openSQL")
	public Map<String,Object> openSQL() {
		return record.open();
	}
}
