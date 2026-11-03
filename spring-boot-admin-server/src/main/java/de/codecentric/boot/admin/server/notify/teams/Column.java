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
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * A <a href="https://adaptivecards.microsoft.com/?topic=Column">Column</a> of a
 * {@link ColumnSet}.
 *
 * @param width the column width
 * @param items the elements inside the column
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Column(@Nullable Width width, List<CardElement> items) {

	public Column {
		items = List.copyOf(items);
	}

	/**
	 * Creates a column of the given width holding the given elements.
	 */
	public static Column of(Width width, CardElement... items) {
		return new Column(width, List.of(items));
	}

	@JsonProperty("type")
	public String type() {
		return "Column";
	}

	/**
	 * @return the lowest card schema version supporting this column and its elements
	 */
	String minVersion() {
		return CardVersions
			.max(Stream.concat(Stream.of(CardVersions.V1_0), this.items.stream().map(CardElement::minVersion)));
	}

}
