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

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

/**
 * The Microsoft Teams specific {@code msteams} property of an {@link AdaptiveCard}, see
 * <a href=
 * "https://learn.microsoft.com/microsoftteams/platform/task-modules-and-cards/cards/cards-format">Cards
 * format</a>.
 *
 * @param width the card width, {@code "Full"} for full width
 * @param entities the user mentions used in the card
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MsTeams(@Nullable String width, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<Mention> entities) {

	public MsTeams {
		entities = List.copyOf(entities);
	}

	public static MsTeams fullWidth() {
		return new MsTeams("Full", List.of());
	}

	public MsTeams withMentions(Mention... mentions) {
		List<Mention> merged = new ArrayList<>(this.entities);
		merged.addAll(List.of(mentions));
		return new MsTeams(this.width, merged);
	}

}
