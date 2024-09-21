package com.ttalkak.deployment.deployment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnvEvent implements Serializable {

    private String key;

    private String value;
}
