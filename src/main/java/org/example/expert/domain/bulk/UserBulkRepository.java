package org.example.expert.domain.bulk;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.User;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkInsert(List<User> users) {
        String sql = "INSERT INTO users (nickname, password, email, user_role) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                User user = users.get(i);
                ps.setString(1, user.getNickname());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getUserRole().name());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }
}
