package cn.sparrowmini.bpm.ext;

import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog_;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.jbpm.services.task.impl.model.*;
import org.kie.api.task.model.OrganizationalEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PorcessInstanceServiceImplExt implements PorcessInstanceServiceExt {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private RuntimeDataService runtimeDataService;


    @Override
    public PageImpl<ProcessInstanceDesc> MyProcessInstances(Pageable pageable, List<SparrowJpaFilter> filters) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        EntityManager em = this.entityManagerFactory.createEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<ProcessInstanceLog> criteriaQuery = cb.createQuery(ProcessInstanceLog.class);
            Root<ProcessInstanceLog> root = criteriaQuery.from(ProcessInstanceLog.class);
            Predicate predicate = cb.and(new SparrowCriteriaBuilderHelper<ProcessInstanceLog>(filters).toPredicate(root, criteriaQuery, cb), cb.equal(root.get(ProcessInstanceLog_.identity), username));

            criteriaQuery.select(root).where(predicate);
            Query query = em.createQuery(criteriaQuery);

            // count
            CriteriaQuery<Long> count = cb.createQuery(Long.class);
            count.select(cb.count(count.from(ProcessInstanceLog.class)));
            count.where(predicate);
            Long counta = em.createQuery(count).getSingleResult();

            List<ProcessInstanceLog> processInstanceLogs = query.setFirstResult(pageable.getPageIndex() * pageable.getPageSize()).setMaxResults(pageable.getPageSize()).getResultList();

            return new PageImpl<ProcessInstanceDesc>(counta, processInstanceLogs.stream().map(m->runtimeDataService.getProcessInstanceById(m.getProcessInstanceId())).collect(Collectors.toList()), pageable.getPageIndex(), pageable.getPageSize());

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public PageImpl<SparrowTaskInstance> MyTasks(Pageable pageable, List<SparrowJpaFilter> filters) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Set<String> entityIds = new HashSet<>();
        entityIds.add(username);
        auth.getAuthorities().forEach(f -> {
            entityIds.add(f.getAuthority());
        });

        EntityManager em = this.entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<TaskImpl> criteriaQuery = cb.createQuery(TaskImpl.class);
            Root<TaskImpl> root = criteriaQuery.from(TaskImpl.class);
            Join<TaskImpl, OrganizationalEntityImpl> join = root.join(TaskImpl_.peopleAssignments).join(PeopleAssignmentsImpl_.POTENTIAL_OWNERS);

            Predicate predicate = new SparrowCriteriaBuilderHelper<TaskImpl>(filters).toPredicate(root, criteriaQuery, cb);

            criteriaQuery.select(root).distinct(true).where(cb.and(predicate), join.get(OrganizationalEntityImpl_.id).in(entityIds));
            Query query = em.createQuery(criteriaQuery);
            List<TaskImpl> tasks = query.setFirstResult(pageable.getPageIndex() * pageable.getPageSize()).setMaxResults(pageable.getPageSize()).getResultList();


            // count
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<TaskImpl> countRoot = countQuery.from(TaskImpl.class);
            Join<TaskImpl, OrganizationalEntityImpl> countJoin = countRoot.join(TaskImpl_.peopleAssignments).join(PeopleAssignmentsImpl_.POTENTIAL_OWNERS);

            countQuery.select(cb.countDistinct(countRoot));
            countQuery.where(cb.and(predicate, countJoin.get(OrganizationalEntityImpl_.id).in(entityIds)));
            Long counta = em.createQuery(countQuery).getSingleResult();

            List<SparrowTaskInstance> taskInstances = tasks.stream().map(task -> {
                SparrowTaskInstance taskInstance = new SparrowTaskInstance();
                taskInstance.setProcessInstanceId(task.getTaskData().getProcessInstanceId());
                taskInstance.setActualOwner(task.getTaskData().getActualOwner() == null ? null : task.getTaskData().getActualOwner().getId());
                taskInstance.setCreatedOn(task.getTaskData().getCreatedOn());
                taskInstance.setId(task.getId());
                taskInstance.setDeploymentId(task.getTaskData().getDeploymentId());
                taskInstance.setPotentialOwners(task.getPeopleAssignments().getPotentialOwners().stream().map(OrganizationalEntity::getId).collect(Collectors.toList()));
                taskInstance.setProcessId(task.getTaskData().getProcessId());
                taskInstance.setStatus(task.getTaskData().getStatus().name());
                ProcessInstanceDesc processInstance = this.runtimeDataService.getProcessInstanceById(task.getTaskData().getProcessInstanceId());
                taskInstance.setProcessName(processInstance.getProcessName());
                taskInstance.setName(task.getName());
                return taskInstance;
            }).collect(Collectors.toList());

            return new PageImpl<SparrowTaskInstance>(counta, taskInstances, pageable.getPageIndex(), pageable.getPageSize());

        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }


    }
}
