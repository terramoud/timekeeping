package ua.epam.akoreshev.finalproject.web.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter extends TagSupport {
    private static final Logger LOG = LogManager.getLogger(LocalDateTimeFormatter.class);

    private transient Object time;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        String result = "00:00:00";
        LOG.debug("Obtained time is: {}", time);
        try {
            if (time != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                result = ((LocalDateTime) time).format(formatter);
            }
            out.print(result);
        } catch (Exception e) {
            LOG.error(e);
            throw new JspException();
        }
        return SKIP_BODY;
    }

    public void setTime(Object time) {
        this.time = time;
    }
}
