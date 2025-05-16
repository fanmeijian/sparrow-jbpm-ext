package cn.sparrowmini.bpm.ext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequestMapping("/jbpm-ext")
@Tag(name = "jbpm-ext", description = "jbpm扩展服务")
public interface PorcessInstanceServiceExt {

    @PostMapping(value = "/process-instances")
    @ResponseBody
    @Operation(summary = "我发起的流程", operationId = "process-instances")
    public PageImpl<ProcessInstanceDesc> MyProcessInstances(@ParameterObject Pageable pageable, @RequestBody List<SparrowJpaFilter> filters);

    @PostMapping(value = "/task-instances")
    @ResponseBody
    @Operation(summary = "我的任务", operationId = "task-instances")
    public PageImpl<SparrowTaskInstance> MyTasks(@ParameterObject Pageable pageable,boolean withInput, boolean withOutput, @RequestBody List<SparrowJpaFilter> filters);

    @GetMapping(value = "/all-task-instances")
    @ResponseBody
    @Operation(summary = "所有任务", operationId = "all-task-instances")
    public List<SparrowTaskInstance> taskInstancesByProcess(long processInstanceId);

    @PostMapping("/process/draft")
    @ResponseBody
    @Operation(summary = "保存流程草稿", operationId = "saveProcessAsDraft")
    public String saveProcessAsDraft(String deploymentId, String processId, @RequestBody Map<String, Object> body);

    @GetMapping("/process/draft/{id}")
    @ResponseBody
    @Operation(summary = "流程草稿详情")
    public ProcessDraft getProcessDraft(@PathVariable String id);

    @GetMapping("/process/draft")
    @ResponseBody
    @Operation(summary = "流程草稿列表")
    public PageImpl<ProcessDraft> getProcessDraftList(String deploymentId, String processId, @RequestParam(required = false) boolean withInput, @RequestParam(required = false) List<String> variableName , @ParameterObject Pageable pageable);

    @PostMapping("/process/{id}/submit")
    @ResponseBody
    @Operation(summary = "提交流程", operationId = "saveProcessAsDraft")
    public void submitProcess(@PathVariable String id,@RequestBody Map<String, Object> body);

    @PostMapping(value="/process/start", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @Operation(summary = "提交流程", operationId = "startProcess")
    public Long startProcess(String deploymentId, String processId,@RequestBody Map<String, Object> body);

    @PostMapping("/process/draft/delete")
    @ResponseBody
    @Operation(summary = "删除草稿", operationId = "deleteDraft")
    public void deleteDraft(@RequestBody Set<String> ids);
}
