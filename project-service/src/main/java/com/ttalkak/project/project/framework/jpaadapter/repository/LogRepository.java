package com.ttalkak.project.project.framework.jpaadapter.repository;

import com.ttalkak.project.project.domain.model.LogEntryDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LogRepository extends ElasticsearchRepository<LogEntryDocument, String> {


}
