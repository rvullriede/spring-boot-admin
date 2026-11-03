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

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jspecify.annotations.Nullable;

/**
 * An
 * <a href="https://adaptivecards.microsoft.com/?topic=Action.OpenUrl">Action.OpenUrl</a>.
 *
 * @param title the label of the action
 * @param url the URL to open
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OpenUrlAction(@Nullable String title, String url) implements CardAction {

	public static OpenUrlAction of(String title, String url) {
		return new OpenUrlAction(title, url);
	}

	@Override
	public String type() {
		return "Action.OpenUrl";
	}

	@Override
	public String minVersion() {
		return CardVersions.V1_0;
	}

}
