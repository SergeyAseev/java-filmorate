package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EventEnum;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationEnum;

import java.util.List;

@Component("FeedDaoStorage")
@RequiredArgsConstructor
public class FeedDaoStorage  implements FeedDao{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFeed(long userId, EventEnum eventEnum, OperationEnum operationEnum, long entityId) {
        String sql = "INSERT INTO FEED (user_id, event_type, operation_type, entity_id) values (?, ?, ?, ?);";
        jdbcTemplate.update(sql, userId, eventEnum.toString(), operationEnum.toString(), entityId);
    }

    @Override
    public List<Feed> getFeed(long userId) {
        String sql = "SELECT * FROM FEED where user_id = ? order by time_ts asc;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Feed(
                rs.getLong("event_id"),
                rs.getLong("user_id"),
                rs.getLong("entity_id"),
                rs.getTimestamp("time_ts").toInstant().toEpochMilli(),
                EventEnum.valueOf(rs.getString("event_type")),
                OperationEnum.valueOf(rs.getString("operation_type"))
        ), userId);
    }
}