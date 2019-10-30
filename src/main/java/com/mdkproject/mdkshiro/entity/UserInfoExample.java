package com.mdkproject.mdkshiro.entity;

import java.util.ArrayList;
import java.util.List;

public class UserInfoExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(Integer value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(Integer value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(Integer value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(Integer value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(Integer value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(Integer value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<Integer> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<Integer> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(Integer value1, Integer value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(Integer value1, Integer value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andAccountIsNull() {
            addCriterion("account is null");
            return (Criteria) this;
        }

        public Criteria andAccountIsNotNull() {
            addCriterion("account is not null");
            return (Criteria) this;
        }

        public Criteria andAccountEqualTo(String value) {
            addCriterion("account =", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotEqualTo(String value) {
            addCriterion("account <>", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThan(String value) {
            addCriterion("account >", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThanOrEqualTo(String value) {
            addCriterion("account >=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThan(String value) {
            addCriterion("account <", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThanOrEqualTo(String value) {
            addCriterion("account <=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLike(String value) {
            addCriterion("account like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotLike(String value) {
            addCriterion("account not like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountIn(List<String> values) {
            addCriterion("account in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotIn(List<String> values) {
            addCriterion("account not in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountBetween(String value1, String value2) {
            addCriterion("account between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotBetween(String value1, String value2) {
            addCriterion("account not between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andAgeIsNull() {
            addCriterion("age is null");
            return (Criteria) this;
        }

        public Criteria andAgeIsNotNull() {
            addCriterion("age is not null");
            return (Criteria) this;
        }

        public Criteria andAgeEqualTo(String value) {
            addCriterion("age =", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotEqualTo(String value) {
            addCriterion("age <>", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThan(String value) {
            addCriterion("age >", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThanOrEqualTo(String value) {
            addCriterion("age >=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThan(String value) {
            addCriterion("age <", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThanOrEqualTo(String value) {
            addCriterion("age <=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLike(String value) {
            addCriterion("age like", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotLike(String value) {
            addCriterion("age not like", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeIn(List<String> values) {
            addCriterion("age in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotIn(List<String> values) {
            addCriterion("age not in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeBetween(String value1, String value2) {
            addCriterion("age between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotBetween(String value1, String value2) {
            addCriterion("age not between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andSaltIsNull() {
            addCriterion("salt is null");
            return (Criteria) this;
        }

        public Criteria andSaltIsNotNull() {
            addCriterion("salt is not null");
            return (Criteria) this;
        }

        public Criteria andSaltEqualTo(String value) {
            addCriterion("salt =", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltNotEqualTo(String value) {
            addCriterion("salt <>", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltGreaterThan(String value) {
            addCriterion("salt >", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltGreaterThanOrEqualTo(String value) {
            addCriterion("salt >=", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltLessThan(String value) {
            addCriterion("salt <", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltLessThanOrEqualTo(String value) {
            addCriterion("salt <=", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltLike(String value) {
            addCriterion("salt like", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltNotLike(String value) {
            addCriterion("salt not like", value, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltIn(List<String> values) {
            addCriterion("salt in", values, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltNotIn(List<String> values) {
            addCriterion("salt not in", values, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltBetween(String value1, String value2) {
            addCriterion("salt between", value1, value2, "salt");
            return (Criteria) this;
        }

        public Criteria andSaltNotBetween(String value1, String value2) {
            addCriterion("salt not between", value1, value2, "salt");
            return (Criteria) this;
        }

        public Criteria andSexIsNull() {
            addCriterion("sex is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("sex is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(String value) {
            addCriterion("sex =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(String value) {
            addCriterion("sex <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(String value) {
            addCriterion("sex >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(String value) {
            addCriterion("sex >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(String value) {
            addCriterion("sex <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(String value) {
            addCriterion("sex <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLike(String value) {
            addCriterion("sex like", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotLike(String value) {
            addCriterion("sex not like", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<String> values) {
            addCriterion("sex in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<String> values) {
            addCriterion("sex not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(String value1, String value2) {
            addCriterion("sex between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(String value1, String value2) {
            addCriterion("sex not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andTelphoneIsNull() {
            addCriterion("telphone is null");
            return (Criteria) this;
        }

        public Criteria andTelphoneIsNotNull() {
            addCriterion("telphone is not null");
            return (Criteria) this;
        }

        public Criteria andTelphoneEqualTo(String value) {
            addCriterion("telphone =", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotEqualTo(String value) {
            addCriterion("telphone <>", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneGreaterThan(String value) {
            addCriterion("telphone >", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneGreaterThanOrEqualTo(String value) {
            addCriterion("telphone >=", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneLessThan(String value) {
            addCriterion("telphone <", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneLessThanOrEqualTo(String value) {
            addCriterion("telphone <=", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneLike(String value) {
            addCriterion("telphone like", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotLike(String value) {
            addCriterion("telphone not like", value, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneIn(List<String> values) {
            addCriterion("telphone in", values, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotIn(List<String> values) {
            addCriterion("telphone not in", values, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneBetween(String value1, String value2) {
            addCriterion("telphone between", value1, value2, "telphone");
            return (Criteria) this;
        }

        public Criteria andTelphoneNotBetween(String value1, String value2) {
            addCriterion("telphone not between", value1, value2, "telphone");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(String value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(String value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(String value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(String value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(String value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(String value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLike(String value) {
            addCriterion("create_time like", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotLike(String value) {
            addCriterion("create_time not like", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<String> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<String> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(String value1, String value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(String value1, String value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(String value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(String value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(String value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(String value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(String value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(String value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLike(String value) {
            addCriterion("update_time like", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotLike(String value) {
            addCriterion("update_time not like", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<String> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<String> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(String value1, String value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(String value1, String value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andYuliu1IsNull() {
            addCriterion("yuliu1 is null");
            return (Criteria) this;
        }

        public Criteria andYuliu1IsNotNull() {
            addCriterion("yuliu1 is not null");
            return (Criteria) this;
        }

        public Criteria andYuliu1EqualTo(String value) {
            addCriterion("yuliu1 =", value, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1NotEqualTo(String value) {
            addCriterion("yuliu1 <>", value, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1GreaterThan(String value) {
            addCriterion("yuliu1 >", value, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1GreaterThanOrEqualTo(String value) {
            addCriterion("yuliu1 >=", value, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1LessThan(String value) {
            addCriterion("yuliu1 <", value, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1LessThanOrEqualTo(String value) {
            addCriterion("yuliu1 <=", value, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1Like(String value) {
            addCriterion("yuliu1 like", value, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1NotLike(String value) {
            addCriterion("yuliu1 not like", value, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1In(List<String> values) {
            addCriterion("yuliu1 in", values, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1NotIn(List<String> values) {
            addCriterion("yuliu1 not in", values, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1Between(String value1, String value2) {
            addCriterion("yuliu1 between", value1, value2, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu1NotBetween(String value1, String value2) {
            addCriterion("yuliu1 not between", value1, value2, "yuliu1");
            return (Criteria) this;
        }

        public Criteria andYuliu2IsNull() {
            addCriterion("yuliu2 is null");
            return (Criteria) this;
        }

        public Criteria andYuliu2IsNotNull() {
            addCriterion("yuliu2 is not null");
            return (Criteria) this;
        }

        public Criteria andYuliu2EqualTo(String value) {
            addCriterion("yuliu2 =", value, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2NotEqualTo(String value) {
            addCriterion("yuliu2 <>", value, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2GreaterThan(String value) {
            addCriterion("yuliu2 >", value, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2GreaterThanOrEqualTo(String value) {
            addCriterion("yuliu2 >=", value, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2LessThan(String value) {
            addCriterion("yuliu2 <", value, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2LessThanOrEqualTo(String value) {
            addCriterion("yuliu2 <=", value, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2Like(String value) {
            addCriterion("yuliu2 like", value, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2NotLike(String value) {
            addCriterion("yuliu2 not like", value, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2In(List<String> values) {
            addCriterion("yuliu2 in", values, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2NotIn(List<String> values) {
            addCriterion("yuliu2 not in", values, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2Between(String value1, String value2) {
            addCriterion("yuliu2 between", value1, value2, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu2NotBetween(String value1, String value2) {
            addCriterion("yuliu2 not between", value1, value2, "yuliu2");
            return (Criteria) this;
        }

        public Criteria andYuliu3IsNull() {
            addCriterion("yuliu3 is null");
            return (Criteria) this;
        }

        public Criteria andYuliu3IsNotNull() {
            addCriterion("yuliu3 is not null");
            return (Criteria) this;
        }

        public Criteria andYuliu3EqualTo(String value) {
            addCriterion("yuliu3 =", value, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3NotEqualTo(String value) {
            addCriterion("yuliu3 <>", value, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3GreaterThan(String value) {
            addCriterion("yuliu3 >", value, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3GreaterThanOrEqualTo(String value) {
            addCriterion("yuliu3 >=", value, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3LessThan(String value) {
            addCriterion("yuliu3 <", value, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3LessThanOrEqualTo(String value) {
            addCriterion("yuliu3 <=", value, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3Like(String value) {
            addCriterion("yuliu3 like", value, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3NotLike(String value) {
            addCriterion("yuliu3 not like", value, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3In(List<String> values) {
            addCriterion("yuliu3 in", values, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3NotIn(List<String> values) {
            addCriterion("yuliu3 not in", values, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3Between(String value1, String value2) {
            addCriterion("yuliu3 between", value1, value2, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu3NotBetween(String value1, String value2) {
            addCriterion("yuliu3 not between", value1, value2, "yuliu3");
            return (Criteria) this;
        }

        public Criteria andYuliu4IsNull() {
            addCriterion("yuliu4 is null");
            return (Criteria) this;
        }

        public Criteria andYuliu4IsNotNull() {
            addCriterion("yuliu4 is not null");
            return (Criteria) this;
        }

        public Criteria andYuliu4EqualTo(String value) {
            addCriterion("yuliu4 =", value, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4NotEqualTo(String value) {
            addCriterion("yuliu4 <>", value, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4GreaterThan(String value) {
            addCriterion("yuliu4 >", value, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4GreaterThanOrEqualTo(String value) {
            addCriterion("yuliu4 >=", value, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4LessThan(String value) {
            addCriterion("yuliu4 <", value, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4LessThanOrEqualTo(String value) {
            addCriterion("yuliu4 <=", value, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4Like(String value) {
            addCriterion("yuliu4 like", value, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4NotLike(String value) {
            addCriterion("yuliu4 not like", value, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4In(List<String> values) {
            addCriterion("yuliu4 in", values, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4NotIn(List<String> values) {
            addCriterion("yuliu4 not in", values, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4Between(String value1, String value2) {
            addCriterion("yuliu4 between", value1, value2, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu4NotBetween(String value1, String value2) {
            addCriterion("yuliu4 not between", value1, value2, "yuliu4");
            return (Criteria) this;
        }

        public Criteria andYuliu5IsNull() {
            addCriterion("yuliu5 is null");
            return (Criteria) this;
        }

        public Criteria andYuliu5IsNotNull() {
            addCriterion("yuliu5 is not null");
            return (Criteria) this;
        }

        public Criteria andYuliu5EqualTo(String value) {
            addCriterion("yuliu5 =", value, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5NotEqualTo(String value) {
            addCriterion("yuliu5 <>", value, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5GreaterThan(String value) {
            addCriterion("yuliu5 >", value, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5GreaterThanOrEqualTo(String value) {
            addCriterion("yuliu5 >=", value, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5LessThan(String value) {
            addCriterion("yuliu5 <", value, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5LessThanOrEqualTo(String value) {
            addCriterion("yuliu5 <=", value, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5Like(String value) {
            addCriterion("yuliu5 like", value, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5NotLike(String value) {
            addCriterion("yuliu5 not like", value, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5In(List<String> values) {
            addCriterion("yuliu5 in", values, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5NotIn(List<String> values) {
            addCriterion("yuliu5 not in", values, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5Between(String value1, String value2) {
            addCriterion("yuliu5 between", value1, value2, "yuliu5");
            return (Criteria) this;
        }

        public Criteria andYuliu5NotBetween(String value1, String value2) {
            addCriterion("yuliu5 not between", value1, value2, "yuliu5");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table user_info
     *
     * @mbg.generated do_not_delete_during_merge Wed Jun 26 15:42:03 CST 2019
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table user_info
     *
     * @mbg.generated Wed Jun 26 15:42:03 CST 2019
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}