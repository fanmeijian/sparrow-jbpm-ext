package cn.sparrowmini.bpm.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessDefinition;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class VariableBackupListener extends DefaultProcessEventListener {
    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private RuntimeDataService runtimeDataService;

    @Override
    public void afterProcessCompleted(ProcessCompletedEvent event) {
        EntityManager entityManager = emf.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            WorkflowProcessInstance pi = (WorkflowProcessInstance) event.getProcessInstance();
            ProcessDefinition processDefinition= this.runtimeDataService.getProcessesByDeploymentIdProcessId(pi.getDeploymentId(), pi.getProcessId());
            Map<String, Object> variables = new HashMap<>();

            processDefinition.getProcessVariables().forEach((k,v)->{
                Object v1= pi.getVariable(k);
                variables.put(k,v1);
            });

            VariableArchive archive = new VariableArchive();
            archive.setId(new VariableArchive.VariableArchiveId(pi.getId(),pi.getProcessId(),pi.getDeploymentId()));
            archive.setCompletedTime(LocalDateTime.now());
            archive.setVariableJson(new Gson().toJson(variables));
            entityManager.persist(archive);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if(entityManager.isOpen()){
                entityManager.close();
            }
        }

    }
}