package com.example.forum.helpers;

import java.util.*;

public class SearchHelper {

    private final String hql;
    private final Map<String, Object> parameters;

    private SearchHelper(String hql, Map<String, Object> parameters) {
        this.hql = hql;
        this.parameters = parameters;
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public String getHql() {
        return hql;
    }

    public static SearchHelper of(String username, String email, String firstName) {
        StringBuilder hql = new StringBuilder("from User u");
        List<String> conditions = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        if (username != null && !username.trim().isEmpty()) {
            conditions.add("lower(u.username) like :username");
            params.put("username", "%" + username.trim().toLowerCase() + "%");
        }
        if (email != null && !email.trim().isEmpty()) {
            conditions.add("lower(u.email) like :email");
            params.put("email", "%" + email.trim().toLowerCase() + "%");
        }
        if (firstName != null && !firstName.trim().isEmpty()) {
            conditions.add("lower(u.firstName) like :firstName");
            params.put("firstName", "%" + firstName.trim().toLowerCase() + "%");
        }

        if (!conditions.isEmpty()) {
            hql.append(" where ").append(String.join(" and ", conditions));
        }

        return new SearchHelper(hql.toString(), params);
    }
}

