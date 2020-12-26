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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.Technologylearningandtrackingappsprint2.TechnologyLearningAndTrackingAppSprint2Application;
import com.capgemini.tlta.model.Assessment;
import com.capgemini.tlta.sevice.AssessmentActivityService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = TechnologyLearningAndTrackingAppSprint2Application.class)
@AutoConfigureMockMvc 
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class AssessmentControllerIntegrationTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private AssessmentActivityService service;

	/**
	 * When post assessment then create assessment.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void whenPostAssessment_thenCreateAssessment() throws Exception {
		Assessment jpa = new Assessment("jpa");
		given(service.addAssessmentActivity(Mockito.any())).willReturn(jpa);

		mvc.perform(post("/api/assessments/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(jpa)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.assessmentName", is("jpa")));

		verify(service, VerificationModeFactory.times(1)).addAssessmentActivity(Mockito.any());
		reset(service);
	}
	
	@Test
	public void givenAssessments_whenGetAssessments_thenReturnJsonArray() throws Exception {
		Assessment jpa = new Assessment("jpa");
		Assessment java = new Assessment("java");
		Assessment cpp = new Assessment("cpp");

		List<Assessment> allAssessments = Arrays.asList(jpa, java, cpp);

		given(service.getAllAssessmentActivity()).willReturn(allAssessments);

		mvc.perform(get("/api/assessments/")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].assessmentName", is(jpa.getAssessmentName())))
				.andExpect(jsonPath("$[1].assessmentName", is(java.getAssessmentName())))
				.andExpect(jsonPath("$[2].assessmentName", is(cpp.getAssessmentName())));
		verify(service, VerificationModeFactory.times(1)).getAllAssessmentActivity();
		reset(service);
	}


}
