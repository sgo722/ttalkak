package com.ttalkak.project.project.framework.web.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DashBoardResponse {

    private final LogPageResponse pages;
    private final List<LogHistogramResponse> histograms;
}
