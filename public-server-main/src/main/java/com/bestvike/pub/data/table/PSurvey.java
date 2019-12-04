package com.bestvike.pub.data.table;

import com.bestvike.portal.data.BaseData;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "P_Survey")
//@NameStyle(Style.normal)
public class PSurvey extends BaseData implements Serializable {

	private static final long serialVersionUID = 8272411305338588699L;

	@Id
	private String surveyId;
	private String surveyStatus;
	private String cityCode;
	private String divisionCode;
	private String regionId;
	private String title;
	private String summary;
	private String avatar;
	private String agentPerson;
	private String agentCertNo;
	private String agentPhone;
	private String startDate;
	private String stopDate;
	private Date createTime;
	private String createUser;
	private Date lastModifyTime;
	private String remark;

	@Transient
	private List<PSurveyItem> surveyItems;

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyStatus() {
		return surveyStatus;
	}

	public void setSurveyStatus(String surveyStatus) {
		this.surveyStatus = surveyStatus;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAgentPerson() {
		return agentPerson;
	}

	public void setAgentPerson(String agentPerson) {
		this.agentPerson = agentPerson;
	}

	public String getAgentCertNo() {
		return agentCertNo;
	}

	public void setAgentCertNo(String agentCertNo) {
		this.agentCertNo = agentCertNo;
	}

	public String getAgentPhone() {
		return agentPhone;
	}

	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PSurveyItem> getSurveyItems() {
		return surveyItems;
	}

	public void setSurveyItems(List<PSurveyItem> surveyItems) {
		this.surveyItems = surveyItems;
	}
}

