package cn.sparrowmini.bpm.ext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Entity
@Table
public class ProcessDraft implements Serializable {
    @Id
    @GenericGenerator(name = "id-generator", strategy = "uuid")
    @GeneratedValue(generator = "id-generator")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    private Long processInstanceId;

    private String deploymentId;
    private String processId;
    @Column(name = "created_date", insertable = true, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdDate; // 创建时间

    @Column(name = "modified_date", insertable = true, updatable = true)
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date modifiedDate; // 最后更新时间

    @GeneratorType(type = LoggedUserGenerator.class, when = GenerationTime.INSERT)
    @Column(name = "created_by", insertable = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @GeneratorType(type = LoggedUserGenerator.class, when = GenerationTime.ALWAYS)
    @Column(name = "modified_by", insertable = true, updatable = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String modifiedBy;

    @Convert(converter = JsonMapConverter.class)
    @Column(columnDefinition = "TEXT") // or VARCHAR(n), or CLOB if needed
    private Map<String, Object> processData;


    public ProcessDraft(String deploymentId, String processId, Map<String, Object> body) {
        this.deploymentId=deploymentId;
        this.processId=processId;
        this.processData=body;
    }

    public ProcessDraft() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Map<String, Object> getProcessData() {
        return processData;
    }

    public void setProcessData(Map<String, Object> processData) {
        this.processData = processData;
    }

}
