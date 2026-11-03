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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

/**
 * A <a href="https://adaptivecards.microsoft.com/?topic=Table">Table</a>.
 *
 * @param columns the column definitions
 * @param rows the rows of the table
 * @param firstRowAsHeader whether the first row is rendered as a header
 * @param showGridLines whether grid lines are shown between cells
 * @param gridStyle the style of the grid
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Table(List<TableColumn> columns, List<TableRow> rows, @Nullable Boolean firstRowAsHeader,
		@Nullable Boolean showGridLines, @Nullable ContainerStyle gridStyle) implements CardElement {

	public Table {
		columns = List.copyOf(columns);
		rows = List.copyOf(rows);
	}

	/**
	 * Creates a builder for a table.
	 */
	public static Builder builder() {
		return new Builder();
	}

	@Override
	public String type() {
		return "Table";
	}

	@Override
	public String minVersion() {
		return CardVersions.max(Stream.concat(Stream.of(CardVersions.V1_5),
				this.rows.stream().flatMap((row) -> row.cells().stream()).map(TableCell::minVersion)));
	}

	/**
	 * Builds a {@link Table}.
	 */
	public static final class Builder {

		private final List<TableColumn> columns = new ArrayList<>();

		private final List<TableRow> rows = new ArrayList<>();

		private @Nullable Boolean firstRowAsHeader;

		private @Nullable Boolean showGridLines;

		private @Nullable ContainerStyle gridStyle;

		private Builder() {
		}

		/**
		 * Appends a column of the given width and returns this builder.
		 * @param width the column width
		 * @return this builder
		 */
		public Builder column(Width width) {
			this.columns.add(new TableColumn(width));
			return this;
		}

		/**
		 * Appends a row of the given cells and returns this builder.
		 * @param cells the cells of the row
		 * @return this builder
		 */
		public Builder row(TableCell... cells) {
			this.rows.add(TableRow.of(cells));
			return this;
		}

		/**
		 * Replaces whether the first row is a header and returns this builder.
		 * @param firstRowAsHeader whether the first row is a header
		 * @return this builder
		 */
		public Builder firstRowAsHeader(boolean firstRowAsHeader) {
			this.firstRowAsHeader = firstRowAsHeader;
			return this;
		}

		/**
		 * Replaces whether grid lines are shown and returns this builder.
		 * @param showGridLines whether grid lines are shown
		 * @return this builder
		 */
		public Builder showGridLines(boolean showGridLines) {
			this.showGridLines = showGridLines;
			return this;
		}

		/**
		 * Replaces the grid style and returns this builder.
		 * @param gridStyle the grid style
		 * @return this builder
		 */
		public Builder gridStyle(ContainerStyle gridStyle) {
			this.gridStyle = gridStyle;
			return this;
		}

		/**
		 * Creates a new table from the current builder state.
		 * @return the new table
		 */
		public Table build() {
			return new Table(this.columns, this.rows, this.firstRowAsHeader, this.showGridLines, this.gridStyle);
		}

	}

}
