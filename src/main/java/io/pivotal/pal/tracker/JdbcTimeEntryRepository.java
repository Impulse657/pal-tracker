package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository{

    private DataSource repo;
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource repo){
        this.repo = repo;
        jdbcTemplate = new JdbcTemplate(repo);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        String query = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";
        timeEntry.setId(executeInsert (query, timeEntry));
        return timeEntry;
    }

    private long executeInsert(String query, TimeEntry timeEntry) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, java.sql.Date.valueOf(timeEntry.getDate()));
            ps.setInt(4, timeEntry.getHours());

            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }


        @Override
    public TimeEntry find(long timeEntryId) {
            TimeEntry timeEntry = null;
        String query = "Select id, project_id, user_id, date, hours from time_entries where id = " + timeEntryId;
        try {
            Map<String, Object> foundEntry = jdbcTemplate.queryForMap(query);


            timeEntry = new TimeEntry(((Long) foundEntry.get("id")).longValue(),
                    ((Long) foundEntry.get("project_id")).longValue(),
                    ((Long) foundEntry.get("user_id")).longValue(),
                    ((Date) foundEntry.get("date")).toLocalDate(),
                    ((Integer) foundEntry.get("hours")).intValue());
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }

           return timeEntry;

    }

    @Override
    public List<TimeEntry> list() {

        String query = "Select id, project_id, user_id, date, hours from time_entries";

        return jdbcTemplate.query(
                query,
                (rs, rowNum) ->
                        new TimeEntry(
                                rs.getLong("id"),
                                rs.getLong("project_id"),
                                rs.getLong("user_id"),
                                (rs.getDate("date")).toLocalDate(),
                                rs.getInt("hours")
                        )
        );
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry newTimeEntry) {
        String query = "update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = " + timeEntryId;

        executeUpdate(query, newTimeEntry);

        newTimeEntry.setId(timeEntryId);
        return newTimeEntry;
    }

    private void executeUpdate(String query, TimeEntry timeEntry) {

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, java.sql.Date.valueOf(timeEntry.getDate()));
            ps.setInt(4, timeEntry.getHours());

            return ps;
        });
    }


    @Override
    public void delete(long timeEntryId) {

        String query = "delete from time_entries where id = " + timeEntryId;

        int rows = jdbcTemplate.update(query);

    }
}
