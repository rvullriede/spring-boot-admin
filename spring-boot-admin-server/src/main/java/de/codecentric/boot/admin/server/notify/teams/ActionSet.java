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
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * An <a href="https://adaptivecards.microsoft.com/?topic=ActionSet">ActionSet</a>.
 *
 * @param actions the actions of the set
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ActionSet(List<CardAction> actions) implements CardElement {

	public ActionSet {
		actions = List.copyOf(actions);
	}

	/**
	 * Creates an action set of the given actions.
	 */
	public static ActionSet of(CardAction... actions) {
		return new ActionSet(List.of(actions));
	}

	@Override
	public String type() {
		return "ActionSet";
	}

	@Override
	public String minVersion() {
		return CardVersions
			.max(Stream.concat(Stream.of(CardVersions.V1_2), this.actions.stream().map(CardAction::minVersion)));
	}

}
