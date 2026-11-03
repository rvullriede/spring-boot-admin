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

class TeamsMessageTest {

	@Test
	void reproducesDefaultStatusChangedCard() throws Exception {
		AdaptiveCard card = AdaptiveCard.of(
				TextBlock.of("Status Changed")
					.withSize(TextSize.LARGE)
					.withWeight(TextWeight.BOLDER)
					.withColor(TextColor.GOOD),
				TextBlock.of("Test App").withSize(TextSize.MEDIUM).withWeight(TextWeight.BOLDER),
				TextBlock.of("Test App with id TestAppId changed status from UNKNOWN to UP").withWrap(true),
				FactSet.of(Fact.of("Status", "UNKNOWN"), Fact.of("Service URL", "https://service"),
						Fact.of("Health URL", "https://health"), Fact.of("Management URL", "https://management")));
		TeamsJson.assertJson(TeamsMessage.of(card),
				TeamsJson.fixture("/de/codecentric/boot/admin/server/notify/ms-teams-status-changed.json"));
	}

}
