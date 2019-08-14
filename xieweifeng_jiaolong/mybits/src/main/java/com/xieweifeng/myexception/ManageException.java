package com.xieweifeng.myexception;/*
@author 谢唯峰
@create 2019-08-12-14:19
*/

import com.xieweifeng.entity.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ManageException {

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ResponseResult myManageException(Exception e){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(500);
        if(e instanceof MyException){
            MyException MyException = (MyException) e;
            responseResult.setSuccess(MyException.getMessage());
        }
        responseResult.setSuccess("未知错误");
        return responseResult;
    }
}
