package ru.yandex.practicum.gym;
import java.util.Objects;

public class TimeOfDay {

    //часы (от 0 до 23)
    private int hours;
    //минуты (от 0 до 59)
    private int minutes;


    public TimeOfDay(int hours, int minutes) {
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Указано недопустимое время");
        }
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TimeOfDay time = (TimeOfDay) o;
        return hours == time.hours && minutes == time.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }
}
