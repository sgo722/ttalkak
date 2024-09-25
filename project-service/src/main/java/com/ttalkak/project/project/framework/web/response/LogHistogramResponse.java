package com.ttalkak.project.project.framework.web.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LogHistogramResponse {

    private String timestamp;
    private long docCount;

}
