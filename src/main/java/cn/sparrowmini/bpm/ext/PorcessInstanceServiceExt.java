package cn.sparrowmini.bpm.ext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    public PageImpl<SparrowTaskInstance> MyTasks(@ParameterObject Pageable pageable, @RequestBody List<SparrowJpaFilter> filters);
}
