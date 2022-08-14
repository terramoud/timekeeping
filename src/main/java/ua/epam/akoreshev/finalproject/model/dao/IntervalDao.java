package ua.epam.akoreshev.finalproject.model.dao;

import ua.epam.akoreshev.finalproject.model.entity.Interval;

import java.util.List;

public interface IntervalDao extends BaseDao<Interval, Long> {
    boolean setStartTimeForUserAndActivity();
    boolean setFinishTimeForUserAndActivity();
    List<Interval> findAllIntervalsByUserAndActivity();
}

