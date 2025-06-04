package cn.sparrowmini.bpm.ext;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table
@Entity
public class VariableArchive {

    @EmbeddedId
    private VariableArchiveId id;

    private LocalDateTime completedTime;

    @Column(columnDefinition = "TEXT")
    private String variableJson; // 保存变量的 JSON 字符串

    @Embeddable
    public static class VariableArchiveId implements Serializable {
        private Long processInstanceId;
        private String processId;
        private String deploymentId;

        public VariableArchiveId() {
        }

        public VariableArchiveId(Long processInstanceId, String processId, String deploymentId) {
            this.processInstanceId = processInstanceId;
            this.processId = processId;
            this.deploymentId = deploymentId;
        }

        public Long getProcessInstanceId() {
            return processInstanceId;
        }

        public void setProcessInstanceId(Long processInstanceId) {
            this.processInstanceId = processInstanceId;
        }

        public String getProcessId() {
            return processId;
        }

        public void setProcessId(String processId) {
            this.processId = processId;
        }

        public String getDeploymentId() {
            return deploymentId;
        }

        public void setDeploymentId(String deploymentId) {
            this.deploymentId = deploymentId;
        }
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public String getVariableJson() {
        return variableJson;
    }

    public void setVariableJson(String variableJson) {
        this.variableJson = variableJson;
    }

    public VariableArchiveId getId() {
        return id;
    }

    public void setId(VariableArchiveId id) {
        this.id = id;
    }
}
