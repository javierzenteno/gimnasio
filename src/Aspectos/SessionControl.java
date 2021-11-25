package Aspectos;


import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
@Component
@Aspect
public class SessionControl{
	@Around("execution(String Controladores.*C.*(..))")
	public String sessionCheckS(ProceedingJoinPoint p){
		HttpServletRequest request=(HttpServletRequest)p.getArgs()[0];
		if(request.getSession().getAttribute("user")==null)return "redirect:../principal/login";
		try {
			return p.proceed().toString();
		} catch (Throwable e) {
			e.printStackTrace();
			return "redirect:../principal/login";
		}
	}
}
