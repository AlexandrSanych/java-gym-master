package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;
    private Map<DayOfWeek, List<TrainingSession>> sortedTimetable; // Новая структура для O(1) доступа

    public Timetable() {
        this.timetable = new EnumMap<>(DayOfWeek.class);
        this.sortedTimetable = new EnumMap<>(DayOfWeek.class); // Инициализируем новую структуру
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        // 1. Добавляем в основную структуру (как было)
        timetable.putIfAbsent(day, new HashMap<>());
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        daySchedule.putIfAbsent(time, new ArrayList<>());
        daySchedule.get(time).add(trainingSession);

        // 2. Добавляем в отсортированный список для быстрого доступа
        List<TrainingSession> sortedDaySessions = sortedTimetable.computeIfAbsent(day,
                k -> new ArrayList<>());
        insertIntoSortedList(sortedDaySessions, trainingSession);
    }

    private void insertIntoSortedList(List<TrainingSession> sessions, TrainingSession newSession) {
        // Вставляем тренировку в отсортированную позицию
        int insertIndex = 0;
        TimeOfDay newTime = newSession.getTimeOfDay();

        // Ищем позицию для вставки (линейный поиск)
        while (insertIndex < sessions.size()) {
            TimeOfDay currentTime = sessions.get(insertIndex).getTimeOfDay();
            if (newTime.getHours() < currentTime.getHours() ||
                    (newTime.getHours() == currentTime.getHours() && newTime.getMinutes() < currentTime.getMinutes())) {
                break;
            }
            insertIndex++;
        }
        sessions.add(insertIndex, newSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        // O(1) - возвращаем готовый отсортированный список
        List<TrainingSession> result = sortedTimetable.get(dayOfWeek);
        return result != null ? result : Collections.emptyList();
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek,
                                                                  TimeOfDay timeOfDay) {
        // O(1) - быстрый доступ по времени (оставляем как было)
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