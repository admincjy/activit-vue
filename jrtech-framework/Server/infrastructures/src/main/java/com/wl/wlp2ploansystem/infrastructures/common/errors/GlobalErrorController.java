package com.wl.wlp2ploansystem.infrastructures.common.errors;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * BasicErrorController
 */
@RestController
public class GlobalErrorController extends BasicErrorController {


    public GlobalErrorController(ErrorAttributes attributes){
        super(attributes, new ErrorProperties());
    }

    /**
     * 覆盖默认的Json响应
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.ALL));

        HttpStatus status = getStatus(request);
        int code = status.value();
        /*
        if (!StringUtils.isEmpty((String)body.get("exception"))){
            String exceptionClass = body.get("exception").toString();

            if(exceptionClass.equals(NotLoginInException.class.getName())) {
                code = HttpStatus.UNAUTHORIZED.value();
            }
            else  if(exceptionClass.equals(NotLoginInException.class.getName())) {
                code = UserFriendlyException.STATUS_CODE;
            }
        }
        */
        body.put("code", code);

        return ResponseEntity.status(code).body(body);
    }

    /**
     * 覆盖默认的HTML响应
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        //请求的状态
        HttpStatus status = getStatus(request);
        response.setStatus(getStatus(request).value());

        Map<String, Object> model = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.TEXT_HTML));
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        //指定自定义的视图
        return(modelAndView == null ? new ModelAndView("error", model) : modelAndView);
    }
}
