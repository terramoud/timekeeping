//package ua.epam.akoreshev.finalproject.web.command;
//
//import org.junit.jupiter.api.*;
//import ua.epam.akoreshev.finalproject.exceptions.DaoException;
//import ua.epam.akoreshev.finalproject.model.dao.DBUtil;
//import ua.epam.akoreshev.finalproject.model.dao.impl.RequestDaoImpl;
//import ua.epam.akoreshev.finalproject.web.service.UserService;
//import ua.epam.akoreshev.finalproject.web.service.impl.RequestServiceImpl;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ChangeUserCommandTest {
//    private static ChangeUserCommand adminDashboardCommand;
//    private static Connection connection;
//
//    @BeforeAll
//    public static void setUpBeforeAll() throws DaoException {
//        connection = DBUtil.getConnection();
//        adminDashboardCommand = new ChangeUserCommand((UserService) new RequestServiceImpl(new RequestDaoImpl(connection)));
//    }
//
//    @BeforeEach
//    public void setUp() throws SQLException {
//        connection.setAutoCommit(false);
//    }
//
//    @AfterEach
//    public void tearDown() throws SQLException {
//        connection.rollback();
//        connection.setAutoCommit(true);
//    }
//
//    @AfterAll
//    public static void tearDownBeforeAll() throws SQLException {
//        connection.close();
//    }
//    @Test
//    void execute() {
//    }
//}