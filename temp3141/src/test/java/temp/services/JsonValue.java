package com.sprint.iice_tests.lib.dao.vo;

import com.google.gson.JsonObject;

@FunctionalInterface
public interface JsonValue<T>
{
	T value(JsonObject jsonObj);
}
