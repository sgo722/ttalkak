package com.ttalkak.deployment.deployment.framework.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebHookRepository {
    private String name;

    private String url;
}
