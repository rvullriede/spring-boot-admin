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
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A row of a {@link Table}, see
 * <a href="https://adaptivecards.microsoft.com/?topic=TableRow">TableRow</a>.
 *
 * @param cells the cells of the row
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TableRow(List<TableCell> cells) {

	public TableRow {
		cells = List.copyOf(cells);
	}

	/**
	 * Creates a row of the given cells.
	 */
	public static TableRow of(TableCell... cells) {
		return new TableRow(List.of(cells));
	}

	@JsonProperty("type")
	public String type() {
		return "TableRow";
	}

}
