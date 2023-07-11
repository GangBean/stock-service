package com.gangbean.stockservice

import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@Sql(value = ["/test.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = ["/cleanup.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
@interface SetTestData {

}