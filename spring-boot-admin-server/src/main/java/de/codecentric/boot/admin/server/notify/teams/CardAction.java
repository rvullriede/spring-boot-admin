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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An action of an {@link AdaptiveCard}, see
 * <a href="https://adaptivecards.microsoft.com/?topic=Action">Action</a>.
 */
public sealed interface CardAction permits OpenUrlAction {

	/**
	 * @return the Adaptive Card action type
	 */
	@JsonProperty("type")
	String type();

	/**
	 * @return the lowest card schema version supporting this action
	 */
	String minVersion();

}
