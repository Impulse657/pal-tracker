package io.pivotal.pal.tracker;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private long idCounter = 0;

    private ConcurrentHashMap<Long, TimeEntry> timeEntries = new ConcurrentHashMap<>();

    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry timeEntryWithId = new TimeEntry(++idCounter, timeEntry);
        timeEntries.put(timeEntryWithId.getId(), timeEntryWithId);
        return timeEntryWithId;
    }

    public TimeEntry find(long id) {
        return timeEntries.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntries.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (timeEntries.get(id) == null) {
            return null;
        }

        TimeEntry updateTimeEntry = new TimeEntry(id, timeEntry);
        timeEntries.put(id, updateTimeEntry);
        return updateTimeEntry;
    }

    public void delete(long id) {
        timeEntries.clear();
    }
}
