package cn.sparrowmini.bpm.ext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.appformer.maven.support.AFReleaseId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/jbpm-ext")
@Tag(name = "jbpm-ext",description = "jbpm扩展服务")
public interface DeploymentExtService {

    @PostMapping(value = "/kjars/deploy",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
//    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "发布kjar", operationId = "deployKjar")
    public AFReleaseId deployKjar(@RequestParam MultipartFile[] file);
}
