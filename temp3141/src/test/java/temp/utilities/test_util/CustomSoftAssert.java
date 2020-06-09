package com.sprint.iice_tests.utilities.test_util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;

import com.sprint.iice_tests.web.browser.Browser;

public class CustomSoftAssert extends SoftAssert {
	private final Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();
	Set<String> failedMethods = new HashSet<String>();

	@Override
	protected void doAssert(IAssert<?> a) {
		onBeforeAssert(a);
		try {
			a.doAssert();
			onAssertSuccess(a);
		} catch (AssertionError ex) {
			onAssertFailure(a, ex);
			String method = a.getMessage().replaceAll("(.*)(in row.*)", "$1").replaceAll(" ", "_")
					.replaceAll("(.*)(in position.*)", "$1");
			if (Browser.runIsDigital() && failedMethods.add(method)) {
				String className = Browser.classMap.get(Thread.currentThread().getId()).toString()
						.replaceAll("Runner.json", "");
				Screenshot.takeFullscreenShot(DataFileFinder.getScreenshotPath(),
						className + method + System.currentTimeMillis());
			}
			m_errors.put(ex, a);
		} finally {
			onAfterAssert(a);
		}
	}

	public void assertAll() {
		if (!m_errors.isEmpty()) {
			StringBuilder sb = new StringBuilder("The following asserts failed:");
			boolean first = true;
			for (Map.Entry<AssertionError, IAssert<?>> ae : m_errors.entrySet()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append("\n\t");
				sb.append(ae.getKey().getMessage());
			}
			throw new AssertionError(sb.toString());
		}
	}

}
