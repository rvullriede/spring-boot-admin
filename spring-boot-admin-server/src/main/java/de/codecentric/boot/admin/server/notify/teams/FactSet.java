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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A <a href="https://adaptivecards.microsoft.com/?topic=FactSet">FactSet</a>.
 *
 * @param facts the facts to display
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FactSet(List<Fact> facts) implements CardElement {

	public FactSet {
		facts = List.copyOf(facts);
	}

	public static FactSet of(Fact... facts) {
		return new FactSet(List.of(facts));
	}

	@Override
	public String type() {
		return "FactSet";
	}

	@Override
	public String minVersion() {
		return CardVersions.V1_0;
	}

}
