package com.bestvike.pub.data.table;

import com.bestvike.portal.data.BaseData;
import com.bestvike.pub.util.JsonUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "P_SurveyItem")
//@NameStyle(Style.normal)
public class PSurveyItem extends BaseData implements Serializable {

	private static final long serialVersionUID = 8272411305338588699L;

	@Id
	private String sysId;
	private String surveyId;
	private String summary;
	private String questionId;
	private String questionType;
	private BigDecimal disOrder;
	private String content;
	private Date createTime;
	private String createUser;
	private Date lastModifyTime;
	private String remark;

	@Transient
	private List<Map> options;

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public BigDecimal getDisOrder() {
		return disOrder;
	}

	public void setDisOrder(BigDecimal disOrder) {
		this.disOrder = disOrder;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public List<Map> getOptions() {
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		return JsonUtil.toBean(content, List.class);
	}

	public void setOptions(List<Map> options) {
		if (null != options && options.size() > 0) {
			this.content = JsonUtil.toString(options);
		}
		this.options = options;
	}
}

