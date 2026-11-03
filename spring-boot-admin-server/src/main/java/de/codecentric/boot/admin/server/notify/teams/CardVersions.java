/*
 * Copyright 2014-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.boot.admin.server.notify.teams;

import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Adaptive Card schema versions and their ordering.
 */
final class CardVersions {

	static final String V1_0 = "1.0";

	static final String V1_2 = "1.2";

	static final String V1_5 = "1.5";

	private static final Pattern VERSION = Pattern.compile("\\d+\\.\\d+");

	private CardVersions() {
	}

	static String max(Stream<String> versions) {
		return versions.max(Comparator.<String>comparingInt((v) -> major(v)).thenComparingInt((v) -> minor(v)))
			.orElseThrow(() -> new IllegalArgumentException("No versions given"));
	}

	static String requireValid(String version) {
		if (!VERSION.matcher(version).matches()) {
			throw new IllegalArgumentException(
					"Invalid Adaptive Card version '" + version + "', expected <major>.<minor>");
		}
		return version;
	}

	private static int major(String version) {
		return Integer.parseInt(version.substring(0, version.indexOf('.')));
	}

	private static int minor(String version) {
		return Integer.parseInt(version.substring(version.indexOf('.') + 1));
	}

}
