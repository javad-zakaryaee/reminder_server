package com.example.projectserver.uniserver.reminders;

import com.example.projectserver.uniserver.User.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReminderDAO extends CrudRepository<Reminder, Long> {
    List<Reminder> findByUser(User user);
}
