package com.sprint.iice_tests.lib.dao.vo;

import io.restassured.response.Response;

@FunctionalInterface
public interface JsonResponse<T>
{
	T response(Response response);
}
