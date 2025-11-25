package ru.yandex.practicum.gym;
import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TimetableTest {

    @Test
    public void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size()); // int vs int - ок
        assertSame(singleTrainingSession, mondaySessions.get(0)); // используем assertSame для объектов

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    public void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        assertSame(thursdayChildTrainingSession, thursdaySessions.get(0)); // используем assertSame
        assertSame(thursdayAdultTrainingSession, thursdaySessions.get(1)); // используем assertSame

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    public void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessionsAt1300 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, sessionsAt1300.size());
        assertSame(singleTrainingSession, sessionsAt1300.get(0)); // используем assertSame

        List<TrainingSession> sessionsAt1400 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(sessionsAt1400.isEmpty());
    }

    @Test
    public void testMultipleSessionsSameTime() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 60);

        TrainingSession session1 = new TrainingSession(group1, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2, DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        assertEquals(2, sessions.size());
    }

    @Test
    public void testGetCountByCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");

        Group group = new Group("Йога", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(11, 0)));

        Map<Coach, Integer> countByCoaches = timetable.getCountByCoaches();

        assertEquals(2, countByCoaches.size());
        assertEquals(Integer.valueOf(3), countByCoaches.get(coach1)); // явно указываем Integer
        assertEquals(Integer.valueOf(2), countByCoaches.get(coach2)); // явно указываем Integer
    }

    @Test
    public void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).isEmpty());
        assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(10, 0)).isEmpty());
        assertTrue(timetable.getCountByCoaches().isEmpty());
    }
}