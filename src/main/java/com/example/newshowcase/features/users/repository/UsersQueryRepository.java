package com.example.newshowcase.features.users.repository;

import com.example.newshowcase.common.dto.PaginationOutput;
import com.example.newshowcase.common.dto.PaginationParams;
import com.example.newshowcase.features.users.domain.User;
import com.example.newshowcase.features.users.dto.UserOutputModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UsersQueryRepository {

    private final MongoTemplate mongoTemplate;

    public UsersQueryRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<UserOutputModel> getById(String id) {
        User user = mongoTemplate.findById(id, User.class);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(UserOutputModel.from(user));
    }

    public Optional<User> findByLoginOrEmail(String loginOrEmail) {
        Query query = new Query(new Criteria().orOperator(
                Criteria.where("login").is(loginOrEmail),
                Criteria.where("email").is(loginOrEmail)
        ));
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }

    public PaginationOutput<UserOutputModel> getAll(PaginationParams pagination) {
        List<Criteria> filters = new ArrayList<>();

        if (pagination.getSearchEmailTerm() != null) {
            filters.add(Criteria.where("email").regex(pagination.getSearchEmailTerm(), "i"));
        }
        if (pagination.getSearchLoginTerm() != null) {
            filters.add(Criteria.where("login").regex(pagination.getSearchLoginTerm(), "i"));
        }
        if (pagination.getSearchNameTerm() != null) {
            filters.add(Criteria.where("name").regex(pagination.getSearchNameTerm(), "i"));
        }

        Query query = new Query();
        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().orOperator(filters.toArray(new Criteria[0])));
        }

        long totalCount = mongoTemplate.count(query, User.class);

        query.with(Sort.by(pagination.getSortDirection(), pagination.getSortBy()));
        query.skip(pagination.getSkipCount());
        query.limit(pagination.getPageSize());

        List<User> users = mongoTemplate.find(query, User.class);
        List<UserOutputModel> mapped = users.stream().map(UserOutputModel::from).toList();

        return new PaginationOutput<>(mapped, pagination.getPageNumber(), pagination.getPageSize(), totalCount);
    }
}
