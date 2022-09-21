package ua.epam.akoreshev.finalproject.web.command;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ua.epam.akoreshev.finalproject.Path;
import ua.epam.akoreshev.finalproject.exceptions.CommandException;
import ua.epam.akoreshev.finalproject.exceptions.ServiceException;
import ua.epam.akoreshev.finalproject.model.entity.ActivityCategoryBean;
import ua.epam.akoreshev.finalproject.model.entity.Category;
import ua.epam.akoreshev.finalproject.web.service.ActivityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListActivitiesCommandTest {
    private Command command;
    private ActivityService activityService;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public void setUp() {
        activityService = Mockito.mock(ActivityService.class);
        command = new ListActivitiesCommand(activityService);
        req = Mockito.mock(HttpServletRequest.class);
        resp = Mockito.mock(HttpServletResponse.class);
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownBeforeAll() {
    }

    /**
     * @see ListActivitiesCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @ParameterizedTest
    @MethodSource("testCases")
    void testExecuteShouldReturnUrlAndPutToSessionAttributes(int totalRowsInTable,
                                                             int expectedTotalPages,
                                                             String sortOrder,
                                                             boolean expectedSortOrder,
                                                             String columnName,
                                                             String expectedColumnName,
                                                             String inputPageNumber,
                                                             int expectedPageNumber) throws ServiceException, CommandException {
        when(req.getParameter("desc")).thenReturn(sortOrder);
        when(req.getParameter("order_by")).thenReturn(columnName);
        when(req.getParameter("pageNumber")).thenReturn(inputPageNumber);
        List<ActivityCategoryBean> activityCategoryBeans = new LinkedList<>();
        List<Category> categories = new LinkedList<>();
        when(activityService.getNumberActivities()).thenReturn(totalRowsInTable);
        when(activityService.getActivities(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(activityCategoryBeans);
        when(activityService.findAllCategories()).thenReturn(categories);
        assertEquals(Path.LIST_ACTIVITIES_PAGE, command.execute(req, resp));
        verify(req).setAttribute("categories", categories);
        verify(req).setAttribute("activityCategoryBeans", activityCategoryBeans);
        verify(req).setAttribute("desc", expectedSortOrder);
        verify(req).setAttribute("order_by", expectedColumnName);
        verify(req).setAttribute("pageNumber", expectedPageNumber);
        verify(req).setAttribute("totalPages", expectedTotalPages);
    }

    /**
     * @see ListActivitiesCommand#execute(HttpServletRequest, HttpServletResponse)
     */
    @Test
    void testExecuteShouldThrowExceptionWhenServiceThrowException() throws ServiceException {
        when(activityService.getNumberActivities()).thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));

        when(activityService.getActivities(anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(ServiceException.class);
        assertThrows(CommandException.class, () -> command.execute(req, resp));

        when(activityService.findAllCategories()).thenThrow(ServiceException.class);
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