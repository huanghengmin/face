package com.hzih.face.recognition.web.servlet;

import com.hzih.face.recognition.constant.ServiceConstant;
import com.hzih.face.recognition.domain.SafePolicy;
import com.hzih.face.recognition.service.SafePolicyService;
import com.hzih.face.recognition.syslog.SysLogSendService;
import com.hzih.face.recognition.web.SiteContext;
import com.hzih.face.recognition.constant.ServiceConstant;
import com.hzih.face.recognition.domain.SafePolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import javax.servlet.*;
import java.io.IOException;

public class SiteContextLoaderServlet extends DispatcherServlet {
	public static SysLogSendService sysLogSendService = new SysLogSendService();
	public static boolean isRunSyslogSendService = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(SiteContextLoaderServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext servletContext = config.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        SiteContext.getInstance().contextRealPath = config.getServletContext().getRealPath("/");
        SafePolicyService service = (SafePolicyService)context.getBean(ServiceConstant.SAFEPOLICY_SERVICE);
        SafePolicy data = service.getData();
        SiteContext.getInstance().safePolicy = data;

		if(!sysLogSendService.isRunning()) {
			sysLogSendService.start();
			isRunSyslogSendService = true;
		}
	}

	@Override
	public ServletConfig getServletConfig() {
		// do nothing
		return null;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// do nothing
	}

	@Override
	public String getServletInfo() {
		// do nothing
		return null;
	}

	@Override
	public void destroy() {

	}

}
