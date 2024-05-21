package cn.sparrowmini.bpm.ext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SparrowCriteriaBuilderHelper<T> {
    private List<SparrowJpaFilter> filters = null;

    public SparrowCriteriaBuilderHelper(List<SparrowJpaFilter> filters) {
        this.filters = filters;
    }

    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        buildCondition(root, query, criteriaBuilder, filters, predicates);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void buildCondition(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder,
                                List<SparrowJpaFilter> filterTrees, List<Predicate> predicates) {
        for (SparrowJpaFilter filterTree : filterTrees) {
            if (filterTree.getChildren() != null && filterTree.getChildren().size() > 0) {
                buildCondition(root, query, criteriaBuilder, filterTree.getChildren(), predicates);
            } else {
                if (filterTree.getFilterTreeBean().getType().equals(LevelTypeEnum.OR)) {
                    predicates.add(criteriaBuilder
                            .or(filterTree.getFilterTreeBean().toPredicate(root, query, criteriaBuilder)));
                } else {
                    predicates.add(criteriaBuilder
                            .and(filterTree.getFilterTreeBean().toPredicate(root, query, criteriaBuilder)));
                }
            }
        }

    }
}
