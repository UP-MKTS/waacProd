package com.mkts.waac.api;

import com.mkts.waac.Dto.DangerousPowDto;
import com.mkts.waac.services.DangerousPowService;
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
@RequestMapping(value = "api/dangerous-pow",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class DangerousPowApi {


	private DangerousPowService dangerousPowService;

	private ErrorDataService errorDataService;

	@Autowired
	public DangerousPowApi(DangerousPowService dangerousPowService, ErrorDataService errorDataService) {
		this.dangerousPowService = dangerousPowService;
		this.errorDataService = errorDataService;
	}

	/*@GetMapping("/{sorting}")
	public ResponseEntity<List<ScoreBean>> getAllScores(@PathVariable Boolean sorting) {
		List<ScoreBean> allScores = scoreService.getAllScore(sorting);
		ResponseEntity<List<ScoreBean>> response = new ResponseEntity<>(allScores, HttpStatus.OK);

		return response;
	}*/

	@PostMapping
	public ResponseEntity<Map<String, String>> addDangerousPow(@Valid @RequestBody DangerousPowDto dangerousPowDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = errorDataService.fillErrorMap(bindingResult);
			return new ResponseEntity<>(errorMap, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		} else {
			dangerousPowService.save(dangerousPowDto);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

	@DeleteMapping(value = "/{dangerousPowId}",
			consumes = MediaType.ALL_VALUE)
	public ResponseEntity<String> removeDangerousPow(@PathVariable Integer dangerousPowId) {
		dangerousPowService.delete(dangerousPowId);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@GetMapping(value = "/{dangerousPowId}",
			consumes = MediaType.ALL_VALUE)
	public ResponseEntity<DangerousPowDto> getDangerousPow(@PathVariable Integer dangerousPowId) {
		DangerousPowDto dangerousPowDto = dangerousPowService.getOne(dangerousPowId);
		return new ResponseEntity<>(dangerousPowDto, HttpStatus.OK);
	}
}
