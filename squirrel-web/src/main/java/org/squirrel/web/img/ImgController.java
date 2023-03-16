package org.squirrel.web.img;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.squirrel.framework.auth.SquirrelAuthInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description 
 * @author weicong
 * @time   2021年1月24日 下午4:16:19
 * @version 1.0
 */
@Controller
public class ImgController {

	@GetMapping("img/{token}/{imguri}")
	public void img(HttpServletRequest request, HttpServletResponse response
			, @PathVariable(value = "token") String token
			, @PathVariable(value = "imguri") String imguri) {
		request.setAttribute(SquirrelAuthInterceptor.AUTHORIZATION, token);
		request.setAttribute(SquirrelAuthInterceptor.IMG_URL_SIGN, imguri);
		try {
			request.getRequestDispatcher(imguri).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
