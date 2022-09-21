package ua.epam.akoreshev.finalproject.web.command;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.*;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;
import ua.epam.akoreshev.finalproject.web.service.IntervalService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserPageCommandTest {
    private Command command;
    private ActivityService activityService;
    private IntervalService intervalService;
    private HttpSession session;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        activityService = Mockito.mock(ActivityService.class);
        intervalService = Mockito.mock(IntervalService.class);
        command = new UserPageCommand(activityService, intervalService);
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("user")).thenReturn(new User());
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see UserPageCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @ParameterizedTest
    @MethodSource("testCases")
    void testExecuteShouldReturnUrlAndPutToSessionAttributes() throws ServiceException, CommandException {
        List<Activity> activities = new LinkedList<>();
        Map<Category, List<Activity>> allActivitiesByCategories = new LinkedHashMap<>();
        Map<Activity, Interval> activityIntervalMap = new LinkedHashMap<>();
        when(activityService.findAllActivitiesByCategories()).thenReturn(allActivitiesByCategories);
        when(intervalService.findIntervalsByUserAndActivities(any(), anyList())).thenReturn(activityIntervalMap);
        when(activityService.findAllActivitiesByUser(any())).thenReturn(activities);
        assertEquals(Path.USER_PAGE, command.execute(req, resp));
        verify(req).setAttribute("allActivitiesByCategories", allActivitiesByCategories);
        verify(req).setAttribute("activityIntervalMap", activityIntervalMap);
    }

    /**
     * @see UserPageCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenServiceThrowException() throws ServiceException {
        when(activityService.findAllActivitiesByCategories()).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));

        when(intervalService.findIntervalsByUserAndActivities(any(), anyList())).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));

        when(activityService.findAllActivitiesByUser(any())).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));
    }

    /**
     * @see UserPageCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenSessionOrUserIsNotExists() throws ServiceException {
        Mockito.reset(session);
        List<Activity> activities = new LinkedList<>();
        Map<Category, List<Activity>> allActivitiesByCategories = new LinkedHashMap<>();
        Map<Activity, Interval> activityIntervalMap = new LinkedHashMap<>();
        when(activityService.findAllActivitiesByCategories()).thenReturn(allActivitiesByCategories);
        when(intervalService.findIntervalsByUserAndActivities(any(), anyList())).thenReturn(activityIntervalMap);
        when(activityService.findAllActivitiesByUser(any())).thenReturn(activities);
        assertThrows(CommandException.class, () -> command.execute(req, resp));
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(100, 20, "true", false, null, "activities.name_en", "0", 1),
                Arguments.of(0, 0, "false", true, "1111", "1111", "-100", 1),
                Arguments.of(1, 1, "true", false, "", "activities.name_en", "one_hundred", 1),
                Arguments.of(4, 1, "true", false, "activities.name_uk", "activities.name_uk", "1", 1),
                Arguments.of(5, 1, "true", false, "a", "a", Long.MAX_VALUE + "", 1),
                Arguments.of(100, 20, "true", false, "activities.name_uk", "activities.name_uk", "100", 100)
        );
    }
}