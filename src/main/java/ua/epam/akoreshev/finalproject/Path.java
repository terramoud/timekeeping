package ua.epam.akoreshev.finalproject;

public final class Path {
    public static final String ERROR_PAGE404 = "/WEB-INF/jsp/error404.jsp";
    public static final String ERROR_PAGE500 = "/WEB-INF/jsp/error500.jsp";
    public static final String USER_PAGE = "/WEB-INF/jsp/user/user_page.jsp";
    public static final String ADMIN_PAGE = "/WEB-INF/jsp/admin/admin_dashboard.jsp";
    public static final String LIST_ACTIVITIES_PAGE = "/WEB-INF/jsp/admin/list_activities.jsp";
    public static final String LIST_CATEGORIES_PAGE = "/WEB-INF/jsp/admin/list_categories.jsp";
    public static final String LIST_USERS_PAGE = "/WEB-INF/jsp/admin/list_users.jsp";
    public static final String REPORT_PAGE = "/WEB-INF/jsp/admin/timekeeping_report.jsp";
    public static final String PROFILE_PAGE = "/WEB-INF/jsp/profile.jsp";
    public static final String INDEX_PAGE = "/index.jsp";


    public static final String INDEX_PAGE_COMMAND = "index_page";
    public static final String LOGIN_COMMAND = "login";
    public static final String CHANGE_LOCALE_COMMAND = "change_locale";
    public static final String LOGOUT_COMMAND = "logout";
    public static final String REGISTER_COMMAND = "register";
    public static final String USER_PAGE_COMMAND = "?command=user_page";
    public static final String ADMIN_PAGE_COMMAND = "?command=admin_dashboard";
}
