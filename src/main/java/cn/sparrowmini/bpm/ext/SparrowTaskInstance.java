package cn.sparrowmini.bpm.ext;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SparrowTaskInstance implements Serializable {
    private Long id;
    private String name;
    private String deploymentId;
    private String processId;
    private Long processInstanceId;
    private String processName;
    private String status;
    private List<String> potentialOwners;
    private String actualOwner;
    private Date createdOn;

    public SparrowTaskInstance() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getPotentialOwners() {
        return potentialOwners;
    }

    public void setPotentialOwners(List<String> potentialOwners) {
        this.potentialOwners = potentialOwners;
    }

    public String getActualOwner() {
        return actualOwner;
    }

    public void setActualOwner(String actualOwner) {
        this.actualOwner = actualOwner;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
