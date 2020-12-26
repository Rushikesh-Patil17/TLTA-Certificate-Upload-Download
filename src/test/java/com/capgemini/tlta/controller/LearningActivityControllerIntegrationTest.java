package com.capgemini.tlta.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.Technologylearningandtrackingappsprint2.TechnologyLearningAndTrackingAppSprint2Application;
import com.capgemini.tlta.model.LearningActivity;
import com.capgemini.tlta.sevice.AssessmentActivityService;
import com.capgemini.tlta.sevice.LearningActivityService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = TechnologyLearningAndTrackingAppSprint2Application.class)
@AutoConfigureMockMvc
public class LearningActivityControllerIntegrationTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private LearningActivityService service;

	@MockBean
	private AssessmentActivityService assessmentService;

	@Test
	public void whenPostLearningActivity_thenCreateLearningActivity() throws Exception {
		LearningActivity java = new LearningActivity("Java");
		given(service.addLearningActivity(Mockito.any())).willReturn(java);

		mvc.perform(post("/api/learningActivity/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(java)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.activityName", is("Java")));

		verify(service, VerificationModeFactory.times(1)).addLearningActivity(Mockito.any());
		reset(service);
	}
	
	@Test
	public void givenLearningActivities_whenGetLearningActivities_thenReturnJsonArray() throws Exception {
		LearningActivity java = new LearningActivity("java");
		LearningActivity jpa = new LearningActivity("jpa");
		LearningActivity cpp = new LearningActivity("cpp");

		List<LearningActivity> allLearningActivities = Arrays.asList(java, jpa, cpp);

		given(service.getAllLearningActivity()).willReturn(allLearningActivities);

		mvc.perform(get("/api/learningActivity/")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].activityName", is(java.getActivityName())))
				.andExpect(jsonPath("$[1].activityName", is(jpa.getActivityName())))
				.andExpect(jsonPath("$[2].activityName", is(cpp.getActivityName())));
		verify(service, VerificationModeFactory.times(1)).getAllLearningActivity();
		reset(service);
	}

}
