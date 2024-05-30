package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationTaskService {
    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }
    public NotificationTask addTask(Long chatId, String taskText, String date) {
        LocalDateTime taskDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        NotificationTask task = new NotificationTask();
        task.setChatId(chatId);
        task.setTaskText(taskText);
        task.setTaskDate(taskDate);
        return notificationTaskRepository.save(task);
    }

    public List<NotificationTask> findNotificationTasksByTaskDate(LocalDateTime dateTime) {
        return notificationTaskRepository.findNotificationTasksByTaskDate(dateTime);
    }
}
