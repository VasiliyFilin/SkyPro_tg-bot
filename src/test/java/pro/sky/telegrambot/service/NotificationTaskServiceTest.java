package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationTaskServiceTest {

    @Mock
    private NotificationTaskRepository repository;

    @InjectMocks
    private NotificationTaskService service;

    public LocalDateTime TASK_DATE = LocalDateTime.parse("30.05.2024 12:00",
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    public NotificationTask TASK1 = new NotificationTask(1L, 1L, "TASK_1_TEXT", TASK_DATE);
    public NotificationTask TASK2 = new NotificationTask(2L, 2L, "TASK_2_TEXT", TASK_DATE);
    public NotificationTask TASK3 = new NotificationTask(3L, 3L, "TASK_3_TEXT", TASK_DATE);

    public List<NotificationTask> tasks = List.of(
            TASK1,
            TASK2,
            TASK3
    );
/**
 * !!! Не смог реализовать этот тест из-за проблемы с id=null в строке 43. Если есть решение, будет интересно узнать реализацию.
 *
 *
    @Test
    void addTaskTest() {
        when(repository.save(TASK1)).thenReturn(TASK1);
        assertThat(TASK1).isSameAs(service.addTask(1l, "TASK_1_TEXT", "30.05.2024 12:00"));

        verify(repository).save(TASK1);
        verify(repository, times(1)).save(TASK1);
    }
 **/

    @Test
    void findNotificationTasksByTaskDateTest() {
        when(repository.findNotificationTasksByTaskDate(TASK_DATE)).thenReturn(tasks);

        assertThat(repository.findNotificationTasksByTaskDate(TASK_DATE))
                .hasSize(3)
                .containsExactlyInAnyOrder(TASK1, TASK2, TASK3);
    }
}