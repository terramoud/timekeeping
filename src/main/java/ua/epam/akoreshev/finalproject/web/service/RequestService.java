package ua.epam.akoreshev.finalproject.web.service;

import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.Request;

public interface RequestService {
    boolean createRequest(Request request) throws ServiceException;
}
