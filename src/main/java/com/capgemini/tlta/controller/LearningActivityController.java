package com.capgemini.tlta.controller;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.capgemini.tlta.exception.ActivityException;
import com.capgemini.tlta.model.LearningActivity;
import com.capgemini.tlta.sevice.LearningActivityDO;
import com.capgemini.tlta.sevice.LearningActivityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class LearningActivityController.
 */
@Api
@RestController
@RequestMapping("/api/learningActivity")
public class LearningActivityController {
	
	@Autowired(required = false)
	@Qualifier(value = "learningActivityService")
	private LearningActivityService learningService;

	/**
	 * Gets the learning activity by id.
	 *
	 * @param id the id
	 * @return the learning activity by id
	 */
	// http://localhost:8081/springfox/api/learningActivity/1
	@ApiOperation(value = "Get Learning Activities By Id", 
			response = LearningActivity.class, 
			tags = "get-Learning-Activity", 
			consumes = "learningActivityId", 
			httpMethod = "GET")
	@GetMapping("/{id}")
	public ResponseEntity<LearningActivity> getLearningActivityById(@PathVariable Integer id) {
		try {
			LearningActivity learningActivity = learningService.searchLearningActivityById(id);
			return new ResponseEntity<>(learningActivity, HttpStatus.OK);
		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * Gets the all learning activity.
	 *
	 * @return the all learning activity
	 */
	// http://localhost:8081/springfox/api/learningActivity/getall/
	@ApiOperation(value = "Get All learning activities", 
			response = LearningActivity.class, 
			tags = "get-LearningActivity", 
			httpMethod = "GET")

	@GetMapping("/")
	public ResponseEntity<List<LearningActivity>> getAllLearningActivity() {
		try {
			List<LearningActivity> learningActivityList = learningService.getAllLearningActivity();
			return new ResponseEntity<>(learningActivityList, HttpStatus.OK);
		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * Adds the learning activity.
	 *
	 * @param learningActivity the learning activity
	 * @return the string
	 */
	// http://localhost:8081/springfox/api/learningActivity/

	@ApiOperation(value = "Add a learning activity", 
			response = LearningActivity.class, 
			tags = "get-learningActivity", 
			consumes = "receives learningActivity object as request body", 
			httpMethod = "POST")

	@PostMapping("/")
	public LearningActivity addLearningActivityWithAssessment(@RequestBody LearningActivityDO learningActivityDo) {
		LearningActivity status = null;
		try {
			status = learningService.addLearningActivityWithAssessment(learningActivityDo);
			return status;
		
		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
//	@ApiOperation(value = "Add a learning activity", 
//			response = LearningActivity.class, 
//			tags = "get-learningActivity", 
//			consumes = "receives learningActivity object as request body", 
//			httpMethod = "POST")
//
//	@PostMapping("/")
//	public LearningActivity addLearningActivity(@RequestBody LearningActivity learningActivity) {
//		LearningActivity status = null;
//		try {
//			status = learningService.addLearningActivity(learningActivity);
//			return status;
//			} catch (ActivityException e) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//		}
//	}

	
	/**
	 * Delete assessment.
	 *
	 * @param id the id
	 * @return the string
	 */
	// http://localhost:8081/springfox/api/learningActivity/1
	@ApiOperation(value = "Delete learning activity By Id", 
			response = String.class, 
			tags = "delete-learning-activity", 
			consumes = "learning activity Id", 
			httpMethod = "DELETE")

	@DeleteMapping("/{id}")
	public String deleteAssessment(@PathVariable Integer id) {
		try {
			Integer status = learningService.deleteLearningActivity(id);
			if (status == 1) {
				return "Activity : " + id + " deleted from database";
			} else {
				return "Unable to delete activity from database";
			}

		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * Update assessment.
	 *
	 * @param learningActivity the learning activity
	 * @return the response entity
	 */
	// http://localhost:8081/springfox/api/learningActivity/
	@ApiOperation(value = "Update learning activity", 
			response = LearningActivity.class, 
			tags = "update-learning-activity", 
			consumes = "learningActivity object sents as response body", 
			httpMethod = "PUT")
	@PutMapping("/")
	public ResponseEntity<LearningActivity> updateAssessment(@Valid @RequestBody LearningActivity learningActivity) {
		try {
			LearningActivity updatedActivity = learningService.updateLearningActivity(learningActivity);
			return new ResponseEntity<>(updatedActivity, HttpStatus.OK);

		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
