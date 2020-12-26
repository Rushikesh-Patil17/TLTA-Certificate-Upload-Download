package com.capgemini.tlta.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.capgemini.tlta.exception.ActivityException;
import com.capgemini.tlta.model.UserActivity;
import com.capgemini.tlta.repository.UserActivityRepository;
import com.capgemini.tlta.sevice.UserActivityDO;
import com.capgemini.tlta.sevice.UserActivityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class LearningActivityController.
 */
@Api
@RestController
@RequestMapping("/api/userActivity")
public class UserActivityController {
	
	@Autowired(required = false)
	@Qualifier(value = "userActivityService")
	private UserActivityService userActivityService;
	
	/**
	 * Gets the learning activity by id.
	 *
	 * @param id the id
	 * @return the learning activity by id
	 */
	// http://localhost:8081/springfox/api/userActivity/1
	@ApiOperation(value = "Get User Activities By Id", 
			response = UserActivity.class, 
			tags = "get-User-Activity", 
			consumes = "UserActivityId", 
			httpMethod = "GET")
	@GetMapping("/{id}")
	public ResponseEntity<UserActivity> getUserActivityById(@PathVariable Integer id) {
		try {
			UserActivity userActivity = userActivityService.getUserActivityById(id);
			return new ResponseEntity<>(userActivity, HttpStatus.OK);
		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * Gets the all user activities.
	 *
	 * @return the all user activity
	 */
	// http://localhost:8081/springfox/api/userActivity
	@ApiOperation(value = "Get All User Activity", 
			response = List.class, 
			tags = "get-All-Use-Activity", 
			httpMethod = "GET")

	@GetMapping("/")
	public ResponseEntity<List<UserActivity>> getAllUserActivity() {
		try {
			List<UserActivity> userActivityList = userActivityService.getAllUserActivities();
			return new ResponseEntity<>(userActivityList, HttpStatus.OK);
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
	// http://localhost:8081/springfox/api/userActivity/
	@ApiOperation(value = "Add a User Activity", 
			response = UserActivity.class, 
			tags = "add-User-Activity", 
			consumes = "receives UserActivity object as request body", 
			httpMethod = "POST")

	@PostMapping("/")
	public UserActivity addUserActivity(@RequestBody UserActivityDO userActivityDo) {
		UserActivity status = null;
		try {
			status = userActivityService.userRegisterToLearningActivity(userActivityDo);
			return status;
		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@ApiOperation(value = "Delete UserActivity By Id",
			response = String.class,
			tags = "delete-user-activity",
			consumes = "User Activity Id",
			httpMethod = "DELETE") 
	
	@DeleteMapping("/{id}")
	public String deleteUserActivity(@PathVariable Integer id) {
		try {
			Integer status = userActivityService.deleteUserActivityById(id);
			if (status == 1) {
				return "UserActivity: " + id + " deleted from database";
			} else {
				return "Unable to delete UserActivity from database";
			}

		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PutMapping("/upload/{id}")
	public String uploadToDB(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
		try {
			boolean isUploaded = userActivityService.uploadCerificate(file, id);

			if (isUploaded) {
				String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
						.path("/api/userActivity/download/" + id).toUriString();
				return fileDownloadUri;
			} else {
				return "Could not upload certificate!";
			}
		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity downloadFromDB(@PathVariable Integer id) {
		
		try {
			String certificateName = userActivityService.getCertificateNameById(id);
			String type = URLConnection.guessContentTypeFromName(certificateName);
			byte[] certificate = userActivityService.getCertificateById(id);
			
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(type))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" 
							+ certificateName + "\"")
					.body(certificate);
	
		} catch (ActivityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
