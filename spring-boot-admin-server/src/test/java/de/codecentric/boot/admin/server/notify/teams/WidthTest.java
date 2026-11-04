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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class WidthTest {

	@Test
	void serializesPixelsAsString() {
		assertThat(TeamsJson.write(Width.pixels(50))).isEqualTo("\"50px\"");
	}

	@Test
	void serializesWeightAsNumber() {
		assertThat(TeamsJson.write(Width.weight(3))).isEqualTo("3");
	}

	@Test
	void serializesKeywords() {
		assertThat(TeamsJson.write(Width.AUTO)).isEqualTo("\"auto\"");
		assertThat(TeamsJson.write(Width.STRETCH)).isEqualTo("\"stretch\"");
	}

	@Test
	void rejectsNonPositiveValues() {
		assertThatIllegalArgumentException().isThrownBy(() -> Width.weight(0));
		assertThatIllegalArgumentException().isThrownBy(() -> Width.pixels(-1));
	}

	@Test
	void comparesByValue() {
		assertThat(Width.weight(2)).isEqualTo(Width.weight(2)).hasSameHashCodeAs(Width.weight(2));
		assertThat(Width.weight(2)).isNotEqualTo(Width.pixels(2));
		assertThat(Width.pixels(2)).hasToString("2px");
	}

}
