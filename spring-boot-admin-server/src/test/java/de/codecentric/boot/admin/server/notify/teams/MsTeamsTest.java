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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class MsTeamsTest {

	@Test
	void fullWidthOmitsEntities() {
		TeamsJson.assertJson(MsTeams.fullWidth(), "{\"width\":\"Full\"}");
	}

	@Test
	void serializesMention() {
		TeamsJson.assertJson(
				MsTeams.fullWidth()
					.withMentions(new Mention("<at>Adele Vance</at>", "AdeleV@contoso.onmicrosoft.com", "Adele Vance")),
				"{\"width\":\"Full\",\"entities\":[{\"type\":\"mention\",\"text\":\"<at>Adele Vance</at>\","
						+ "\"mentioned\":{\"id\":\"AdeleV@contoso.onmicrosoft.com\",\"name\":\"Adele Vance\"}}]}");
	}

	@Test
	void mentionSerializesIdBeforeName() {
		assertThat(TeamsJson.write(new Mention("<at>A</at>", "a@x", "A")))
			.contains("\"mentioned\":{\"id\":\"a@x\",\"name\":\"A\"}");
	}

	@Test
	void mentionRejectsMissingIdAndName() {
		assertThatNullPointerException().isThrownBy(() -> new Mention("<at>A</at>", null, "A"));
		assertThatNullPointerException().isThrownBy(() -> new Mention("<at>A</at>", "a@x", null));
	}

}
