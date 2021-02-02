package com.mkts.waac.api;

import com.mkts.waac.services.utils.ErrorDataService;
import com.mkts.waac.Dto.DepartmentDto;
import com.mkts.waac.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping(value = "api/department",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentApi {


	private DepartmentService departmentService;

	private ErrorDataService errorDataService;

	@Autowired
	public DepartmentApi(DepartmentService departmentService, ErrorDataService errorDataService) {
		this.departmentService = departmentService;
		this.errorDataService = errorDataService;
	}

	/*@GetMapping("/{sorting}")
	public ResponseEntity<List<ScoreBean>> getAllScores(@PathVariable Boolean sorting) {
		List<ScoreBean> allScores = scoreService.getAllScore(sorting);
		ResponseEntity<List<ScoreBean>> response = new ResponseEntity<>(allScores, HttpStatus.OK);

		return response;
	}*/

	@PostMapping
	public ResponseEntity<Map<String, String>> addDepartment(@Valid @RequestBody DepartmentDto departmentDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = errorDataService.fillErrorMap(bindingResult);
			return new ResponseEntity<>(errorMap, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		} else {
			departmentService.save(departmentDto);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

	@DeleteMapping(value = "/{departmentId}",
			consumes = MediaType.ALL_VALUE)
	public ResponseEntity<String> removeDepartment(@PathVariable Integer departmentId) {
		departmentService.delete(departmentId);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@GetMapping(value = "/{departmentId}",
			consumes = MediaType.ALL_VALUE)
	public ResponseEntity<DepartmentDto> getUser(@PathVariable Integer departmentId) {
		DepartmentDto departmentDto = departmentService.getOne(departmentId);

		return new ResponseEntity<>(departmentDto, HttpStatus.OK);
	}
}
