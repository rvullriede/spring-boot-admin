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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class AdaptiveCardTest {

	@Test
	void defaultVersionIsFloor() {
		assertThat(AdaptiveCard.of(TextBlock.of("t")).version()).isEqualTo("1.2");
	}

	@Test
	void explicitVersionWins() {
		assertThat(AdaptiveCard.of(TextBlock.of("t")).withVersion("1.0").version()).isEqualTo("1.0");
	}

	@Test
	void explicitVersionSurvivesWithBody() {
		assertThat(AdaptiveCard.of().withVersion("1.4").withBody(TextBlock.of("t")).version()).isEqualTo("1.4");
	}

	@Test
	void rejectsMalformedVersion() {
		assertThatIllegalArgumentException().isThrownBy(() -> AdaptiveCard.of().withVersion("1.x"));
	}

	@Test
	void withBodyLeavesOriginalUnchanged() {
		AdaptiveCard original = AdaptiveCard.of(TextBlock.of("a"));
		AdaptiveCard extended = original.withBody(TextBlock.of("b"));
		assertThat(original.body()).containsExactly(TextBlock.of("a"));
		assertThat(extended.body()).containsExactly(TextBlock.of("a"), TextBlock.of("b"));
	}

	@Test
	void bodyIsDefensivelyCopied() {
		List<CardElement> body = new ArrayList<>(List.of(TextBlock.of("a")));
		AdaptiveCard card = new AdaptiveCard(body, List.of(), null, null);
		body.add(TextBlock.of("b"));
		assertThat(card.body()).containsExactly(TextBlock.of("a"));
		assertThatExceptionOfType(UnsupportedOperationException.class)
			.isThrownBy(() -> card.body().add(TextBlock.of("c")));
	}

	@Test
	void rejectsNullElement() {
		assertThatNullPointerException().isThrownBy(() -> AdaptiveCard.of(TextBlock.of("a"), null));
	}

	@Test
	void serializesActionsAndMsTeams() {
		AdaptiveCard card = AdaptiveCard.of(TextBlock.of("t"))
			.withActions(OpenUrlAction.of("Open", "https://example.com"))
			.withMsTeams(MsTeams.fullWidth());
		TeamsJson.assertJson(card,
				"{\"$schema\":\"http://adaptivecards.io/schemas/adaptive-card.json\",\"type\":\"AdaptiveCard\","
						+ "\"version\":\"1.2\",\"body\":[{\"type\":\"TextBlock\",\"text\":\"t\"}],"
						+ "\"actions\":[{\"type\":\"Action.OpenUrl\",\"title\":\"Open\",\"url\":\"https://example.com\"}],"
						+ "\"msteams\":{\"width\":\"Full\"}}");
	}

}
