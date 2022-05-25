package com.example.projectserver.uniserver.controllers;

import com.example.projectserver.uniserver.User.User;
import com.example.projectserver.uniserver.User.UserDAO;
import com.example.projectserver.uniserver.reminders.Reminder;
import com.example.projectserver.uniserver.reminders.ReminderDAO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.projectserver.uniserver.exception.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {
    @Autowired
    UserDAO userDAO;
    @Autowired
    ReminderDAO reminderDAO;
    @GetMapping("/getAll")
    public List<Reminder> getAllReminders() {
        return (List<Reminder>) reminderDAO.findAll();
    }

   @GetMapping("/get/{id}")
   public List<Reminder> getRemindersByUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
       User user = userDAO
               .findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
       List<Reminder> userReminders = null;
       if (user != null) userReminders = user.getReminders();
       return userReminders;
   }

   @PostMapping(value = "/add/{id}")
   public void newReminder(@RequestBody String attributes, @PathVariable(value = "id") Long id) {
        JSONObject jso = new JSONObject(attributes);
        Optional<User> user = userDAO.findById(id);
        reminderDAO.save(new Reminder(jso.getString("title"), jso.getString("desc"), LocalTime.parse(jso.getString("date")), user.get()));
    }


   @PostMapping(value = "/update/{id}")
   public void updateReminder(@RequestBody String attributes, @PathVariable(value = "id") Long id) {
       JSONObject jso = new JSONObject(attributes);
       Optional<Reminder> reminder = reminderDAO.findById(id);
       reminder.get().setTitle(jso.getString("title"));
       reminder.get().setDesc(jso.getString("desc"));
       reminder.get().setTime(LocalTime.parse(jso.getString("time")));
       reminderDAO.save(reminder.get());
   }
    @PostMapping(value = "/check/{id}")
    public void checkReminder(@RequestBody String attributes, @PathVariable(value = "id") Long id) {
        Optional<Reminder> reminder = reminderDAO.findById(id);
        reminder.get().setDone((Boolean.valueOf(attributes)));
        reminderDAO.save(reminder.get());
    }
   @GetMapping(value="/delete/{id}")
    public void deleteReminder(@PathVariable(value = "id") Long[] id){
        for (int i=0; i<id.length; i++){
            reminderDAO.delete(reminderDAO.findById(id[i]).get());
        }
   }
}