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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Message envelope posted to a Microsoft Teams webhook, see <a href=
 * "https://learn.microsoft.com/microsoftteams/platform/task-modules-and-cards/cards/cards-format">Cards
 * format</a>.
 *
 * @param attachments the attached cards
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TeamsMessage(List<Attachment> attachments) {

	public TeamsMessage {
		attachments = List.copyOf(attachments);
	}

	public static TeamsMessage of(AdaptiveCard card) {
		return new TeamsMessage(List.of(new Attachment(card)));
	}

	@JsonProperty("type")
	public String type() {
		return "message";
	}

}
