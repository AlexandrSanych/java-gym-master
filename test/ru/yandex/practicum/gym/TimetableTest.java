package ru.yandex.practicum.gym;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TimetableTest {

    private Timetable timetable;
    private Group groupChild;
    private Group groupAdult;
    private Coach coachVasilyev;
    private Coach coachIvanov;
    private Coach coachPetrov;
    private Coach coachSidorov;

    @Before
    public void setUp() {
        timetable = new Timetable();

        // Группы
        groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        // Тренеры
        coachVasilyev = new Coach("Васильев", "Николай", "Сергеевич");
        coachIvanov = new Coach("Иванов", "Иван", "Иванович");
        coachPetrov = new Coach("Петров", "Петр", "Петрович");
        coachSidorov = new Coach("Сидоров", "Алексей", "Владимирович");
    }

    @Test
    public void testGetTrainingSessionsForDaySingleSession() {
        TrainingSession singleTrainingSession = new TrainingSession(groupChild, coachVasilyev,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertSame(singleTrainingSession, mondaySessions.get(0));

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    public void testGetTrainingSessionsForDayMultipleSessions() {
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coachVasilyev,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coachVasilyev,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coachVasilyev,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coachVasilyev,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        assertSame(thursdayChildTrainingSession, thursdaySessions.get(0));
        assertSame(thursdayAdultTrainingSession, thursdaySessions.get(1));

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    public void testGetTrainingSessionsForDayAndTime() {
        TrainingSession singleTrainingSession = new TrainingSession(groupChild, coachVasilyev,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessionsAt1300 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        assertEquals(1, sessionsAt1300.size());
        assertSame(singleTrainingSession, sessionsAt1300.get(0));

        List<TrainingSession> sessionsAt1400 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        assertTrue(sessionsAt1400.isEmpty());
    }

    @Test
    public void testMultipleSessionsSameTime() {
        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 60);

        TrainingSession session1 = new TrainingSession(group1, coachIvanov, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group2, coachPetrov, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));
        assertEquals(2, sessions.size());
    }

    @Test
    public void testGetCountByCoaches() {
        // coachIvanov: 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coachIvanov, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coachIvanov, DayOfWeek.WEDNESDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coachIvanov, DayOfWeek.FRIDAY,
                new TimeOfDay(10, 0)));

        // coachPetrov: 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coachPetrov, DayOfWeek.TUESDAY,
                new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coachPetrov, DayOfWeek.THURSDAY,
                new TimeOfDay(11, 0)));

        // coachSidorov: 4 тренировки (самый загруженный)
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coachSidorov, DayOfWeek.MONDAY,
                new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coachSidorov, DayOfWeek.TUESDAY,
                new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coachSidorov, DayOfWeek.WEDNESDAY,
                new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(groupAdult, coachSidorov, DayOfWeek.THURSDAY,
                new TimeOfDay(9, 0)));

        Map<Coach, Integer> countByCoaches = timetable.getCountByCoaches();

        // Проверяем количество тренеров
        assertEquals(3, countByCoaches.size());

        // Проверяем количество тренировок для каждого тренера
        assertEquals(Integer.valueOf(3), countByCoaches.get(coachIvanov));
        assertEquals(Integer.valueOf(2), countByCoaches.get(coachPetrov));
        assertEquals(Integer.valueOf(4), countByCoaches.get(coachSidorov));

        // Проверяем порядок тренеров по убыванию количества тренировок
        List<Map.Entry<Coach, Integer>> entries = new ArrayList<>(countByCoaches.entrySet());

        // Первый тренер должен быть coachSidorov (4 тренировки)
        assertEquals(coachSidorov, entries.get(0).getKey());
        assertEquals(Integer.valueOf(4), entries.get(0).getValue());

        // Второй тренер должен быть coachIvanov (3 тренировки)
        assertEquals(coachIvanov, entries.get(1).getKey());
        assertEquals(Integer.valueOf(3), entries.get(1).getValue());

        // Третий тренер должен быть coachPetrov (2 тренировки)
        assertEquals(coachPetrov, entries.get(2).getKey());
        assertEquals(Integer.valueOf(2), entries.get(2).getValue());
    }

    @Test
    public void testEmptyTimetable() {
        assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).isEmpty());
        assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)).isEmpty());
        assertTrue(timetable.getCountByCoaches().isEmpty());
    }

    // ТЕСТЫ НА ГРАНИЧНЫЕ ЗНАЧЕНИЯ

    @Test
    public void testBoundaryTimeValues() {
        // Тестирование крайних значений времени
        TrainingSession midnightSession = new TrainingSession(groupAdult, coachIvanov,
                DayOfWeek.MONDAY, new TimeOfDay(0, 0)); // 00:00
        TrainingSession endOfDaySession = new TrainingSession(groupAdult, coachPetrov,
                DayOfWeek.MONDAY, new TimeOfDay(23, 59)); // 23:59
        TrainingSession middaySession = new TrainingSession(groupChild, coachSidorov,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0)); // 12:00

        // Добавляем в разном порядке для проверки сортировки
        timetable.addNewTrainingSession(endOfDaySession);
        timetable.addNewTrainingSession(middaySession);
        timetable.addNewTrainingSession(midnightSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(3, mondaySessions.size());

        // Проверяем правильность сортировки по времени
        assertEquals(new TimeOfDay(0, 0), mondaySessions.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(12, 0), mondaySessions.get(1).getTimeOfDay());
        assertEquals(new TimeOfDay(23, 59), mondaySessions.get(2).getTimeOfDay());
    }

    @Test
    public void testSameTimeDifferentMinutes() {
        // Тестирование одинаковых часов с разными минутами
        TrainingSession session10_00 = new TrainingSession(groupAdult, coachIvanov,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session10_30 = new TrainingSession(groupChild, coachPetrov,
                DayOfWeek.MONDAY, new TimeOfDay(10, 30));
        TrainingSession session10_15 = new TrainingSession(groupAdult, coachSidorov,
                DayOfWeek.MONDAY, new TimeOfDay(10, 15));

        timetable.addNewTrainingSession(session10_30);
        timetable.addNewTrainingSession(session10_00);
        timetable.addNewTrainingSession(session10_15);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(3, mondaySessions.size());

        // Проверяем сортировку по минутам
        assertEquals(new TimeOfDay(10, 0), mondaySessions.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(10, 15), mondaySessions.get(1).getTimeOfDay());
        assertEquals(new TimeOfDay(10, 30), mondaySessions.get(2).getTimeOfDay());
    }

    @Test
    public void testAllDaysOfWeek() {
        // Тестирование всех дней недели
        for (DayOfWeek day : DayOfWeek.values()) {
            TrainingSession session = new TrainingSession(groupAdult, coachIvanov,
                    day, new TimeOfDay(10, 0));
            timetable.addNewTrainingSession(session);
        }

        // Проверяем, что для каждого дня есть одна тренировка
        for (DayOfWeek day : DayOfWeek.values()) {
            List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(day);
            assertEquals(1, sessions.size());
            assertEquals(new TimeOfDay(10, 0), sessions.get(0).getTimeOfDay());
        }
    }

    @Test
    public void testMultipleSessionsWithSameParameters() {
        // Тестирование дублирующихся тренировок (одинаковые параметры)
        TrainingSession session1 = new TrainingSession(groupAdult, coachIvanov,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(groupAdult, coachIvanov,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(2, mondaySessions.size());

        List<TrainingSession> timeSessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        assertEquals(2, timeSessions.size());
    }

    @Test
    public void testSingleMinuteDifference() {
        // Тестирование разницы в 1 минуту
        TrainingSession session10_00 = new TrainingSession(groupAdult, coachIvanov,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session10_01 = new TrainingSession(groupChild, coachPetrov,
                DayOfWeek.MONDAY, new TimeOfDay(10, 1));

        timetable.addNewTrainingSession(session10_01);
        timetable.addNewTrainingSession(session10_00);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(2, mondaySessions.size());

        // Проверяем, что сортировка учитывает разницу в 1 минуту
        assertEquals(new TimeOfDay(10, 0), mondaySessions.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(10, 1), mondaySessions.get(1).getTimeOfDay());
    }
}