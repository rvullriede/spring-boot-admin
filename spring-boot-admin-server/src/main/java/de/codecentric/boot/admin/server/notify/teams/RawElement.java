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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * An element written as the given JSON object, for Adaptive Card elements this model has
 * no type for, see
 * <a href="https://adaptivecards.microsoft.com/?topic=Element">Element</a>.
 *
 * @param json the properties of the element, including its {@code type}
 * @param minVersion the lowest card schema version supporting the element
 */
public record RawElement(@JsonValue Map<String, Object> json, String minVersion) implements CardElement {

	/**
	 * Creates the element.
	 * @throws IllegalArgumentException if {@code json} has no non-blank string
	 * {@code type}, or {@code minVersion} is not of the form {@code <major>.<minor>}
	 */
	public RawElement {
		if (!(json.get("type") instanceof String type) || type.isBlank()) {
			throw new IllegalArgumentException("Raw element needs a non-blank string 'type', was " + json.get("type"));
		}
		json = Collections.unmodifiableMap(new LinkedHashMap<>(json));
		minVersion = CardVersions.requireValid(minVersion);
	}

	@Override
	public String type() {
		return (String) this.json.get("type");
	}

}
