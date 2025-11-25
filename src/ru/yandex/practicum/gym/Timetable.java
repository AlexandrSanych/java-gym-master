package ru.yandex.practicum.gym;
import java.util.*;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;


    public Timetable() {
        this.timetable = new EnumMap<>(DayOfWeek.class);
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        timetable.putIfAbsent(day, new HashMap<>());

        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        daySchedule.putIfAbsent(time, new ArrayList<>());

        daySchedule.get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> sessions : daySchedule.values()) {
            result.addAll(sessions);
        }

        // Сортируем по времени начала
        result.sort((ts1, ts2) -> {
            TimeOfDay time1 = ts1.getTimeOfDay();
            TimeOfDay time2 = ts2.getTimeOfDay();
            if (time1.getHours() != time2.getHours()) {
                return Integer.compare(time1.getHours(), time2.getHours());
            }
            return Integer.compare(time1.getMinutes(), time2.getMinutes());
        });

        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessions = daySchedule.get(timeOfDay);
        return sessions != null ? sessions : Collections.emptyList();
    }

    // подсчет тренировок по тренерам
    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> coachCount = new HashMap<>();

        // Проходим по всем дням и времени
        for (Map<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCount.put(coach, coachCount.getOrDefault(coach, 0) + 1);
                }
            }
        }

        // Сортируем по убыванию количества тренировок
        List<Map.Entry<Coach, Integer>> sortedEntries = new ArrayList<>(coachCount.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Создаем отсортированную LinkedHashMap
        Map<Coach, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> entry : sortedEntries) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}