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

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class CardVersionsTest {

	@Test
	void comparesNumerically() {
		assertThat(CardVersions.max(Stream.of("1.9", "1.10", "1.2"))).isEqualTo("1.10");
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "1", "1.", "1.x", "v1.2" })
	void rejectsMalformed(String version) {
		assertThatIllegalArgumentException().isThrownBy(() -> CardVersions.requireValid(version));
	}

}
