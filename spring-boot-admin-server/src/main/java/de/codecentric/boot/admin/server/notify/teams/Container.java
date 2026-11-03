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
import org.jspecify.annotations.Nullable;

/**
 * A <a href="https://adaptivecards.microsoft.com/?topic=Container">Container</a>.
 *
 * @param items the elements inside the container
 * @param style the container style
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Container(List<CardElement> items, @Nullable ContainerStyle style) implements CardElement {

	public Container {
		items = List.copyOf(items);
	}

	/**
	 * Creates an unstyled container holding the given elements.
	 */
	public static Container of(CardElement... items) {
		return new Container(List.of(items), null);
	}

	/**
	 * Returns a new container with the style replaced.
	 */
	public Container withStyle(ContainerStyle containerStyle) {
		return new Container(this.items, containerStyle);
	}

	@Override
	public String type() {
		return "Container";
	}

	@Override
	public String minVersion() {
		return CardVersions
			.max(Stream.concat(Stream.of(CardVersions.V1_0), this.items.stream().map(CardElement::minVersion)));
	}

}
