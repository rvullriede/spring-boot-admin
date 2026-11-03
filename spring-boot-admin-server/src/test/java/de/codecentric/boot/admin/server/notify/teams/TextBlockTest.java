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

class TextBlockTest {

	@Test
	void omitsUnsetProperties() {
		TeamsJson.assertJson(TextBlock.of("Hello"), "{\"type\":\"TextBlock\",\"text\":\"Hello\"}");
	}

	@Test
	void serializesAllProperties() {
		TeamsJson.assertJson(
				TextBlock.of("Hello")
					.withSize(TextSize.EXTRA_LARGE)
					.withWeight(TextWeight.LIGHTER)
					.withColor(TextColor.ATTENTION)
					.withWrap(true)
					.withHorizontalAlignment(HorizontalAlignment.CENTER),
				"{\"type\":\"TextBlock\",\"text\":\"Hello\",\"size\":\"ExtraLarge\",\"weight\":\"Lighter\","
						+ "\"color\":\"Attention\",\"wrap\":true,\"horizontalAlignment\":\"Center\"}");
	}

	@Test
	void colorAcceptsArbitraryString() {
		assertThat(TextBlock.of("t").withColor("green").color()).isEqualTo("green");
	}

}
