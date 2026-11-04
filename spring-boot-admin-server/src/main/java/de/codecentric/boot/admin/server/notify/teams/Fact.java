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

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A <a href="https://adaptivecards.microsoft.com/?topic=Fact">Fact</a> of a
 * {@link FactSet}.
 *
 * @param title the fact's title
 * @param value the fact's value
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Fact(String title, String value) {

	/**
	 * Creates a fact with the given title and value.
	 */
	public static Fact of(String title, String value) {
		return new Fact(title, value);
	}

}
