package com.mkts.waac.api;

import com.mkts.waac.Dto.ActivityKindDto;
import com.mkts.waac.services.ActivityKindService;
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
@RequestMapping(value = "api/activity-kind",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class ActivityKindApi {


	private ActivityKindService activityKindService;

	private ErrorDataService errorDataService;

	@Autowired
	public ActivityKindApi(ActivityKindService activityKindService, ErrorDataService errorDataService) {
		this.activityKindService = activityKindService;
		this.errorDataService = errorDataService;
	}

	/*@GetMapping("/{sorting}")
	public ResponseEntity<List<ScoreBean>> getAllScores(@PathVariable Boolean sorting) {
		List<ScoreBean> allScores = scoreService.getAllScore(sorting);
		ResponseEntity<List<ScoreBean>> response = new ResponseEntity<>(allScores, HttpStatus.OK);

		return response;
	}*/

	@PostMapping
	public ResponseEntity<Map<String, String>> addActivityKind(@Valid @RequestBody ActivityKindDto activityKindDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = errorDataService.fillErrorMap(bindingResult);
			return new ResponseEntity<>(errorMap, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		} else {
			activityKindService.save(activityKindDto);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

	@DeleteMapping(value = "/{activityKindId}",
			consumes = MediaType.ALL_VALUE)
	public ResponseEntity<String> removeActivityKind(@PathVariable Integer activityKindId) {
		activityKindService.delete(activityKindId);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@GetMapping(value = "/{activityKindId}",
			consumes = MediaType.ALL_VALUE)
	public ResponseEntity<ActivityKindDto> getActivityKind(@PathVariable Integer activityKindId) {
		ActivityKindDto activityKindDto = activityKindService.getOne(activityKindId);
		return new ResponseEntity<>(activityKindDto, HttpStatus.OK);
	}
}
