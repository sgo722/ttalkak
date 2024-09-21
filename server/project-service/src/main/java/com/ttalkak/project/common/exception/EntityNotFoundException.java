package com.ttalkak.project.common.exception;

import com.ttalkak.project.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends BusinessException {


    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
