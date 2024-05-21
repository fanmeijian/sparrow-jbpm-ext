package cn.sparrowmini.bpm.ext;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用于构建通用过滤器使用
 */
public class SparrowJpaFilter implements Serializable {
    private static final long serialVersionUID = 1L;
    private FilterTreeBean filterTreeBean;
    private List<SparrowJpaFilter> children;

    public SparrowJpaFilter() {
        super();
    }

    public SparrowJpaFilter(FilterTreeBean filterTreeBean) {
        super();
        this.filterTreeBean = filterTreeBean;
    }

    public FilterTreeBean getFilterTreeBean() {
        return filterTreeBean;
    }

    public void setFilterTreeBean(FilterTreeBean filterTreeBean) {
        this.filterTreeBean = filterTreeBean;
    }

    public List<SparrowJpaFilter> getChildren() {
        return children;
    }

    public void setChildren(List<SparrowJpaFilter> children) {
        this.children = children;
    }

    public static class FilterTreeBean implements Serializable {
        private static final long serialVersionUID = 1L;
        private LevelTypeEnum type;
        private String name;
        private String op;
        private Object value;
        private boolean not;

        public FilterTreeBean() {
            super();
            // TODO Auto-generated constructor stub
        }

        public FilterTreeBean(LevelTypeEnum type, String name, String op, Object value, boolean not) {
            super();
            this.type = type;
            this.name = name;
            this.op = op;
            this.value = value;
            this.not = not;
        }

        public LevelTypeEnum getType() {
            return type;
        }

        public void setType(LevelTypeEnum type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOp() {
            return op;
        }

        public void setOp(String op) {
            this.op = op;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public boolean isNot() {
            return not;
        }

        public void setNot(boolean not) {
            this.not = not;
        }

        @SuppressWarnings({"unchecked"})
        public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            if (this.name == null) {
                return null;
            }
            Path<?> path = null;
            if (this.name.contains(".")) {
                String[] pnames = this.name.split("\\.");
//				path = root.get(pnames[0]);
                for (int i = 0; i < pnames.length; i++) {
                    if (i == 0) {
                        path = root.get(pnames[i]);
                    } else {
                        path = path.get(pnames[i]);
                    }

                }
            } else {
                path = root.get(this.name);
            }
            Predicate predicate = null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//            System.out.println(path.getJavaType().getCanonicalName());
            Object columnValue = null;
            if (path.getJavaType().isEnum()) {
                Object[] a = path.getJavaType().getEnumConstants();
                if (value instanceof List) {
                    columnValue = new ArrayList<>();
                    for (Object v : (List) value) {
                        Object enum1 = Arrays.stream(path.getJavaType().getEnumConstants()).filter(f -> ((Enum<?>) f).name().equals(v)).collect(Collectors.toList()).get(0);
                        ((List) columnValue).add(enum1);
                    }
                } else {
                    columnValue = Arrays.stream(path.getJavaType().getEnumConstants()).filter(f -> ((Enum<?>) f).name().equals(value)).collect(Collectors.toList()).get(0);
                }
            } else {
                columnValue = value;
            }
            switch (op) {
                case "=":
//                    Object enum1 = null;
//                    if(path.getJavaType().isEnum()){
//                        Object[] a=path.getJavaType().getEnumConstants();
//                        enum1 = Arrays.stream(path.getJavaType().getEnumConstants()).filter(f->((Enum<?> )f).name().equals(value)).collect(Collectors.toList()).get(0);
//                    }else{
//                        enum1 = value;
//                    }
                    predicate = criteriaBuilder.equal(path, columnValue);
                    break;
                case "contain":
                    predicate = criteriaBuilder.like(path.as(String.class), "%" + columnValue.toString() + "%");
                    break;
                case "end":
                    predicate = criteriaBuilder.like(path.as(String.class), "%" + columnValue.toString());
                    break;
                case "start":
                    predicate = criteriaBuilder.like(path.as(String.class), columnValue.toString() + "%");
                    break;
                case ">":
                    try {
                        predicate = path.getJavaType().getCanonicalName().equals("java.util.Date")
                                ? criteriaBuilder.greaterThan(path.as(Date.class), formatter.parse(columnValue.toString()))
                                : criteriaBuilder.gt((Expression<? extends Number>) path, (Number) columnValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case ">=":
                    try {
                        predicate = path.getJavaType().getCanonicalName().equals("java.util.Date")
                                ? criteriaBuilder.greaterThanOrEqualTo(path.as(Date.class),
                                formatter.parse(value.toString()))
                                : criteriaBuilder.ge((Expression<? extends Number>) path, (Number) columnValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "<":
                    try {
                        predicate = path.getJavaType().getCanonicalName().equals("java.util.Date")
                                ? criteriaBuilder.lessThan(path.as(Date.class), formatter.parse(columnValue.toString()))
                                : criteriaBuilder.lt((Expression<? extends Number>) path, (Number) columnValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "<=":
                    try {
                        predicate = path.getJavaType().getCanonicalName().equals("java.util.Date")
                                ? criteriaBuilder.lessThanOrEqualTo(path.as(Date.class), formatter.parse(columnValue.toString()))
                                : criteriaBuilder.le((Expression<? extends Number>) path, (Number) columnValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "in":
                    predicate = path.in(columnValue);
                    break;
                default:
                    predicate = criteriaBuilder.conjunction();
                    break;
            }
//			predicate = this.type == LevelTypeEnum.AND ? criteriaBuilder.and(predicate) : criteriaBuilder.or(predicate);
            if (this.not) {
                return predicate.not();
            } else {
                return predicate;
            }
        }
    }
}
