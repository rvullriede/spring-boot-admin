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

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A user mention of {@link MsTeams#entities()}, see <a href=
 * "https://learn.microsoft.com/microsoftteams/platform/task-modules-and-cards/cards/cards-format">Cards
 * format</a>.
 *
 * @param text the mention as written in the card, e.g. {@code <at>Adele Vance</at>}
 * @param id the id of the mentioned user
 * @param name the name of the mentioned user
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Mention(String text, @JsonIgnore String id, @JsonIgnore String name) {

	@JsonProperty("type")
	public String type() {
		return "mention";
	}

	@JsonProperty("mentioned")
	public Map<String, String> mentioned() {
		return Map.of("id", this.id, "name", this.name);
	}

}
