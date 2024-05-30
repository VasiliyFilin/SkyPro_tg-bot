package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    final private NotificationTaskService notificationTaskService;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(NotificationTaskService notificationTaskService, TelegramBot telegramBot) {
        this.notificationTaskService = notificationTaskService;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message().text() != null) {
                String message = update.message().text();
                Long chatId = update.message().chat().id();
                if(message.equals("/start")){
                    sendMessage(chatId, "Привет! Я помогу тебе создать напоминание. " +
                            "Введи дату, время и название напоминания в формате 01.01.2025 20:00 Сделать домашнюю работу " +
                            "и я пришлю тебе напоминание в 20:00 1 января 2025 года с текстом “Сделать домашнюю работу”");
                } else{
                    Pattern pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)(\\W+)");
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.matches()) {
                        String date = matcher.group(1);
                        String text = matcher.group(3);
                        notificationTaskService.addTask(chatId, date, text);
                        sendMessage(chatId, "Задача успешно добавлена");
                    } else {
                        sendMessage(chatId, "Сообщение должно иметь вид: 01.01.2025 20:00 Сделать домашнюю работу");
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse response = telegramBot.execute(message);
        logger.info("Response: {}", response.isOk());
        logger.info("Error code: {}", response.errorCode());
    }
    @Scheduled(cron = "0 0/1 * * * *")
    public void sendReminders() {
        LocalDateTime current = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> remindersList = notificationTaskService.findNotificationTasksByTaskDate(current);
        remindersList.forEach((NotificationTask -> {
            sendMessage(NotificationTask.getChatId(),NotificationTask.getTaskText());
        }));
    }
}
