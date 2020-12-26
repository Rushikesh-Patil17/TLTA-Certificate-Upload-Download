package com.capgemini.tlta.sevice;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.capgemini.tlta.exception.ActivityException;
import com.capgemini.tlta.model.LearningActivity;
import com.capgemini.tlta.model.RegisterUser;
import com.capgemini.tlta.model.UserActivity;
import com.capgemini.tlta.repository.LearningActivityRepository;
import com.capgemini.tlta.repository.RegisterUserRepository;
import com.capgemini.tlta.repository.UserActivityRepository;

@Service(value = "userActivityService")
@Transactional
public class UserActivityServiceImpl implements UserActivityService {

	@Autowired
	LearningActivityRepository learningActivityRepository;
	
	@Autowired
	RegisterUserRepository registerUserRepository;
	
	@Autowired
	UserActivityRepository userActivityRepository;
	
	@Override
	public UserActivity userRegisterToLearningActivity(UserActivityDO userActivityDo)
			throws ActivityException {
		LearningActivity learning = null;
		RegisterUser register = null;
		UserActivity user = new UserActivity(userActivityDo);
		try {

			learning = learningActivityRepository.getOne(userActivityDo.getLearningActivityId());
			register = registerUserRepository.getOne(userActivityDo.getUserId());
			
			
			user.setLearningActivity(learning);
			user.setRegisterUser(register);

			user = userActivityRepository.save(user);
			return user;
		} catch (DataAccessException e) {
			throw new ActivityException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ActivityException(e.getMessage(), e);
		}
	}

	@Override
	public UserActivity getUserActivityById(Integer id) throws ActivityException {
		Optional<UserActivity> optional = null;
		try {
			optional = userActivityRepository.findById(id);
			if (optional.isPresent()) {
				System.out.println(optional.get());
				return optional.get();
			} else {
				return null;
			}
		} catch (DataAccessException e) {
			throw new ActivityException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ActivityException(e.getMessage(), e);
		}
		
	}

	@Override
	public List<UserActivity> getAllUserActivities() throws ActivityException {
		List<UserActivity> allActivities = null;
		
		try {
			allActivities = userActivityRepository.findAll();
			return allActivities;
		} catch (DataAccessException e) {
			throw new ActivityException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ActivityException(e.getMessage(), e);
		}
	}

	@Override
	public Integer deleteUserActivityById(Integer id) throws ActivityException {
		try {
			userActivityRepository.deleteById(id);
			return 1;
		} catch (DataAccessException e) {
			throw new ActivityException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ActivityException(e.getMessage(), e);
		}
	}

	@Override
	public boolean uploadCerificate(MultipartFile file, Integer id) throws ActivityException {
		boolean isUploaded = false;
		try {
			UserActivity activity = userActivityRepository.findById(id).get();
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			activity.setFile(file.getBytes());
			
			activity.setCertificate(fileName);
			userActivityRepository.save(activity);
			
			isUploaded = true;
		} catch (DataAccessException e) {
			throw new ActivityException(e.getMessage(), e);
		} catch(IOException e) {
			throw new ActivityException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ActivityException(e.getMessage(), e);
		}
		return isUploaded;
	}

	@Override
	public String getCertificateNameById(Integer id) throws ActivityException {
		try {
			UserActivity activity = userActivityRepository.findById(id).get();
			String fileName = activity.getCertificate();
			return fileName;
		} catch (DataAccessException e) {
			throw new ActivityException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ActivityException(e.getMessage(), e);
		}
	}

	@Override
	public byte[] getCertificateById(Integer id) throws ActivityException {
		try {
			UserActivity activity = userActivityRepository.findById(id).get();
			return activity.getFile();
		} catch (DataAccessException e) {
			throw new ActivityException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ActivityException(e.getMessage(), e);
		}
	}
}
