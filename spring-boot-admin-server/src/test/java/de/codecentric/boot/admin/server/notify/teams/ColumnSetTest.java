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

class ColumnSetTest {

	@Test
	void serializesColumnsWithWidths() {
		TeamsJson.assertJson(
				ColumnSet.of(Column.of(Width.AUTO, TextBlock.of("a")), Column.of(Width.weight(2), TextBlock.of("b"))),
				"{\"type\":\"ColumnSet\",\"columns\":[{\"type\":\"Column\",\"width\":\"auto\",\"items\":[{\"type\":\"TextBlock\",\"text\":\"a\"}]},"
						+ "{\"type\":\"Column\",\"width\":2,\"items\":[{\"type\":\"TextBlock\",\"text\":\"b\"}]}]}");
	}

	@Test
	void minVersionIsHighestOfColumns() {
		assertThat(ColumnSet.of(Column.of(Width.pixels(50), TextBlock.of("a"))).minVersion()).isEqualTo("1.0");
		assertThat(ColumnSet.of(Column.of(Width.AUTO, TextBlock.of("a")), Column.of(Width.AUTO, ActionSet.of()))
			.minVersion()).isEqualTo("1.2");
	}

}
