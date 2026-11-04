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
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A <a href="https://adaptivecards.microsoft.com/?topic=ColumnSet">ColumnSet</a>.
 *
 * @param columns the columns of the set
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ColumnSet(List<Column> columns) implements CardElement {

	public ColumnSet {
		columns = List.copyOf(columns);
	}

	/**
	 * Creates a column set of the given columns.
	 */
	public static ColumnSet of(Column... columns) {
		return new ColumnSet(List.of(columns));
	}

	@Override
	public String type() {
		return "ColumnSet";
	}

	@Override
	public String minVersion() {
		return CardVersions
			.max(Stream.concat(Stream.of(CardVersions.V1_0), this.columns.stream().map(Column::minVersion)));
	}

}
