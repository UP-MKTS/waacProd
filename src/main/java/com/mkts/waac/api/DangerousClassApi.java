package com.mkts.waac.api;

import com.mkts.waac.Dto.DangerousClassDto;
import com.mkts.waac.services.DangerousClassService;
import com.mkts.waac.services.utils.ErrorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping(value = "api/dangerous-class",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class DangerousClassApi {


	private DangerousClassService dangerousClassService;

	private ErrorDataService errorDataService;

	@Autowired
	public DangerousClassApi(DangerousClassService dangerousClassService, ErrorDataService errorDataService) {
		this.dangerousClassService = dangerousClassService;
		this.errorDataService = errorDataService;
	}

	/*@GetMapping("/{sorting}")
	public ResponseEntity<List<ScoreBean>> getAllScores(@PathVariable Boolean sorting) {
		List<ScoreBean> allScores = scoreService.getAllScore(sorting);
		ResponseEntity<List<ScoreBean>> response = new ResponseEntity<>(allScores, HttpStatus.OK);

		return response;
	}*/

	@PostMapping
	public ResponseEntity<Map<String, String>> addDangerousClass(@Valid @RequestBody DangerousClassDto dangerousClassDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = errorDataService.fillErrorMap(bindingResult);
			return new ResponseEntity<>(errorMap, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		} else {
			dangerousClassService.save(dangerousClassDto);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

	@DeleteMapping(value = "/{dangerousClassId}",
			consumes = MediaType.ALL_VALUE)
	public ResponseEntity<String> removeDangerousClass(@PathVariable Integer dangerousClassId) {
		dangerousClassService.delete(dangerousClassId);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@GetMapping(value = "/{dangerousClassId}",
			consumes = MediaType.ALL_VALUE)
	public ResponseEntity<DangerousClassDto> getDangerousClass(@PathVariable Integer dangerousClassId) {
		DangerousClassDto dangerousClassDto = dangerousClassService.getOne(dangerousClassId);
		return new ResponseEntity<>(dangerousClassDto, HttpStatus.OK);
	}
}
