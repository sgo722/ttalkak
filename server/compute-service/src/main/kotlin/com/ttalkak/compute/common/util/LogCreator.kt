package com.ttalkak.compute.common.util

import org.slf4j.LoggerFactory

abstract class LoggerCreator {
    val log = LoggerFactory.getLogger(this.javaClass)!!
}