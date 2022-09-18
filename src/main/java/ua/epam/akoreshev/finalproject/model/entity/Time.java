package ua.epam.akoreshev.finalproject.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class Time implements Serializable, Comparable<Time> {
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private long hours;
    private long minutes;
    private long seconds;

    public Time(long hours, long minutes, long seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Time(long seconds) {
        this.hours = seconds / SECONDS_PER_HOUR;
        long remainderSeconds = seconds % SECONDS_PER_HOUR;
        this.minutes = remainderSeconds / SECONDS_PER_MINUTE;
        this.seconds = remainderSeconds % SECONDS_PER_MINUTE;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    @Override
    public int compareTo(Time t) {
        Long thisTotal = (getHours() * 3600) + (getMinutes() * 60) + getSeconds();
        Long tTotal = (t.getHours() * 3600) + (t.getMinutes() * 60) + t.getSeconds();
        if ((thisTotal - tTotal) > 0)
            return 1;
        if ((thisTotal - tTotal) < 0)
            return -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return hours == time.hours &&
                minutes == time.minutes &&
                seconds == time.seconds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes, seconds);
    }

    @Override
    public String toString() {
        return "Time{" +
                "hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }
}
