package com.genie.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ccubukcu
 *
 */
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uniqueImageFileName = request.getPathInfo().substring(1);
		byte[] imageContent = (byte[]) request.getSession().getAttribute(uniqueImageFileName);
		response.setContentType("image/png");
		response.setContentLength(imageContent.length);		
		response.setHeader("Content-Disposition",  
                "attachment; filename=\"Export.png\""); 
		
		ServletOutputStream sos = response.getOutputStream();
		sos.write(imageContent);
		sos.close();
	}

}
