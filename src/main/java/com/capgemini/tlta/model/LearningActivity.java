package com.capgemini.tlta.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import com.capgemini.tlta.sevice.LearningActivityDO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class LearningActivity.
 */
@Entity
@Table(name = "learning_activity")
@NoArgsConstructor
@Getter 
@Setter
public class LearningActivity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")	
	private Integer id;

	@NotNull
	@Size(min=3,max=30, message="Activity name should have atleast 3 characters")
	@Column(name = "activity_name")
	private String activityName;

	
	@URL
	@Column(name = "activity_link")
	private String activityLink;

	@NotBlank(message="Activity level should be Beginner/Intermediate/Expert")
	@Size(min=6)
	@Column(name = "activity_level")
	private String activityLevel;

	@NotNull(message="Please provide activity duration in hours")
	@Column(name = "activity_time")
	private Double activityTime;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull(message="Please provide a date in yyyy-MM-dd format")
	@Temporal(TemporalType.DATE)
	@Column(name = "activity_realsedate")
	private Date activityReleaseDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "assesment_id", nullable = true)
	private Assessment assesment;

	@JsonIgnore
	@OneToMany(mappedBy = "learningActivity", cascade = CascadeType.ALL)
	private Set<UserActivity> userActivity = new HashSet<UserActivity>();
	
	public LearningActivity(LearningActivityDO learningDo) {
		this.activityName = learningDo.getActivityName();
		this.activityLink = learningDo.getActivityLink();
		this.activityLevel = learningDo.getActivityLevel();
		this.activityReleaseDate = learningDo.getActivityReleaseDate();
		this.activityTime = learningDo.getActivityTime();
	}
	
	public LearningActivity(String activity_name) {
		this.activityName = activity_name;
	}
	
	/**
	 * Gets the serial version uid.
	 *
	 * @return the serial version uid
	 */
	public static long getSerialVersionUid() {
		return serialVersionUID;
	}
}
