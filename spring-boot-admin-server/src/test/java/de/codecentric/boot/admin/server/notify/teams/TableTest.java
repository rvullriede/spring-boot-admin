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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableTest {

	@Test
	void serializesTable() {
		Table table = Table.builder()
			.column(Width.weight(1))
			.column(Width.weight(3))
			.row(TableCell.of(TextBlock.of("Key").withWeight(TextWeight.BOLDER)), TableCell.of(TextBlock.of("Summary")))
			.firstRowAsHeader(true)
			.showGridLines(false)
			.gridStyle(ContainerStyle.ACCENT)
			.build();
		TeamsJson.assertJson(table, """
				{"type":"Table","columns":[{"width":1},{"width":3}],
				"rows":[{"type":"TableRow","cells":[
				{"type":"TableCell","items":[{"type":"TextBlock","text":"Key","weight":"Bolder"}]},
				{"type":"TableCell","items":[{"type":"TextBlock","text":"Summary"}]}]}],
				"firstRowAsHeader":true,"showGridLines":false,"gridStyle":"Accent"}""");
	}

	@Test
	void omitsUnsetTableOptions() {
		TeamsJson.assertJson(Table.builder().column(Width.weight(1)).build(),
				"{\"type\":\"Table\",\"columns\":[{\"width\":1}],\"rows\":[]}");
	}

	@Test
	void tableRaisesCardVersion() {
		assertThat(AdaptiveCard.of(TextBlock.of("t")).withBody(Table.builder().build()).version()).isEqualTo("1.5");
	}

	@Test
	void nestedTableRaisesCardVersion() {
		Table table = Table.builder().build();
		assertThat(AdaptiveCard.of(Container.of(table)).version()).isEqualTo("1.5");
		assertThat(AdaptiveCard.of(ColumnSet.of(Column.of(Width.AUTO, table))).version()).isEqualTo("1.5");
		assertThat(AdaptiveCard.of(Container.of(Container.of(table))).version()).isEqualTo("1.5");
	}

}
