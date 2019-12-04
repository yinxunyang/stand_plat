package com.bestvike.pub.data.table;

import com.bestvike.portal.data.BaseData;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "P_Question")
//@NameStyle(Style.normal)
public class PQuestion extends BaseData implements Serializable {

	private static final long serialVersionUID = 8272411305338588699L;

	@Id
	private String questionId;
	private String summary;
	private String questionType;
	private String content;
	private Date createTime;
	private String createUser;
	private Date lastModifyTime;
	private String remark;

	@Transient
	private List<Map> options;

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
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
		return options;
	}

	public void setOptions(List<Map> options) {
		this.options = options;
	}
}

