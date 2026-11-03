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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class RawElementTest {

	@Test
	void serializesMapAsIs() {
		TeamsJson.assertJson(new RawElement(Map.of("type", "RichTextBlock", "inlines", List.of("Hi")), "1.2"),
				"{\"type\":\"RichTextBlock\",\"inlines\":[\"Hi\"]}");
	}

	@Test
	void serializesNullValue() {
		Map<String, @Nullable Object> json = new LinkedHashMap<>();
		json.put("type", "X");
		json.put("key", null);
		TeamsJson.assertJson(new RawElement(json, "1.0"), "{\"type\":\"X\",\"key\":null}");
	}

	@Test
	void exposesTypeFromMap() {
		assertThat(new RawElement(Map.of("type", "X"), "1.0").type()).isEqualTo("X");
	}

	@Test
	void raisesCardVersion() {
		assertThat(AdaptiveCard.of(new RawElement(Map.of("type", "X"), "1.6")).version()).isEqualTo("1.6");
	}

	@Test
	void rejectsMissingType() {
		assertThatIllegalArgumentException().isThrownBy(() -> new RawElement(Map.of("text", "x"), "1.0"));
	}

	@Test
	void rejectsNonStringType() {
		assertThatIllegalArgumentException().isThrownBy(() -> new RawElement(Map.of("type", 1), "1.0"));
	}

	@Test
	void rejectsBlankType() {
		assertThatIllegalArgumentException().isThrownBy(() -> new RawElement(Map.of("type", " "), "1.0"));
	}

	@Test
	void rejectsMalformedVersion() {
		assertThatIllegalArgumentException().isThrownBy(() -> new RawElement(Map.of("type", "X"), "latest"));
	}

	@Test
	void isDefensivelyCopied() {
		Map<String, Object> json = new LinkedHashMap<>(Map.of("type", "X"));
		RawElement raw = new RawElement(json, "1.0");
		json.put("extra", true);
		assertThat(raw.json()).containsOnlyKeys("type");
	}

}
