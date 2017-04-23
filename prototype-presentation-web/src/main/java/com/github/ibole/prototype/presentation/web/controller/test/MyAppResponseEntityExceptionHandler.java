package com.github.ibole.prototype.presentation.web.controller.test;

import com.github.ibole.infrastructure.web.spring.RestResponseEntityExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 异常处理器,该类会处理所有在执行标有@RequestMapping注解的方法时发生的异常.
 * 
 * @author bwang
 *
 */
@ControllerAdvice
public class MyAppResponseEntityExceptionHandler extends RestResponseEntityExceptionHandler {


}
