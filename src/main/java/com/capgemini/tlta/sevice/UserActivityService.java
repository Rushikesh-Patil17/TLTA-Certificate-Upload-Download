package com.capgemini.tlta.sevice;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.capgemini.tlta.exception.ActivityException;
import com.capgemini.tlta.model.UserActivity;

public interface UserActivityService {
	//public UserActivity updateStatus(UserActivity userActivity,String status) throws ActivityException;
	public UserActivity userRegisterToLearningActivity(UserActivityDO userActivityDo) throws ActivityException;
	public UserActivity getUserActivityById(Integer id)throws ActivityException;
	public List<UserActivity> getAllUserActivities() throws ActivityException;
	public Integer deleteUserActivityById(Integer id) throws ActivityException;
	public boolean uploadCerificate(MultipartFile file, Integer activityId) throws ActivityException;
	public String getCertificateNameById(Integer activityId) throws ActivityException;
	public byte[] getCertificateById(Integer activityId) throws ActivityException;
}
