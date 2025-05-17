package cn.sparrowmini.bpm.ext;


import java.util.Date;

public class TaskCommentBean {
    private Long id;
    private Long taskId;
    private String taskName;
    private String createdBy;
    private Date createdDate;
    private String comment;

    public TaskCommentBean() {
    }

    public TaskCommentBean(Long id, Long taskId, String taskName, String createdBy, Date createdDate, String comment) {
        this.id = id;
        this.taskId = taskId;
        this.taskName = taskName;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.comment = comment;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
