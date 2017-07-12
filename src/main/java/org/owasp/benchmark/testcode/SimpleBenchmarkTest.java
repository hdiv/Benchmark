package org.owasp.benchmark.testcode;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleBenchmarkTest extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		javax.servlet.RequestDispatcher rd = request.getRequestDispatcher(getClass().getAnnotation(WebServlet.class).value()+".html");
		rd.include(request, response);
	}
	
	void println(HttpServletResponse response, String line) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().println(line);
	}

}
