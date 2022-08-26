package ua.epam.akoreshev.finalproject.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/*",
	initParams = {@WebInitParam(name = "encoding", value = "UTF-8")},
	dispatcherTypes = {DispatcherType.REQUEST}
)
public class EncodingFilter implements Filter {
	private static final Logger LOG = LogManager.getLogger(EncodingFilter.class);
	private String encoding;

	@Override
	public void init(FilterConfig filterConfig) {
		encoding = filterConfig.getInitParameter("encoding");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOG.trace("before chain");
		HttpServletRequest req = (HttpServletRequest) request;
		String characterEncoding = req.getCharacterEncoding();
		LOG.debug("current characterEncoding: {}", characterEncoding);
		if (characterEncoding == null) {
			LOG.debug("set encoding: {}", encoding);
			req.setCharacterEncoding(encoding);
		}
		chain.doFilter(request, response);
		LOG.trace("after chain");
	}
}