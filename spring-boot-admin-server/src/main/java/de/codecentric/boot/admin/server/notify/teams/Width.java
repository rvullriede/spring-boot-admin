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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The width of a <a href="https://adaptivecards.microsoft.com/?topic=Column">Column</a>:
 * a relative weight, a pixel width, {@code auto} or {@code stretch}.
 */
public final class Width {

	/**
	 * Makes the column as wide as its content.
	 */
	public static final Width AUTO = new Width("auto");

	/**
	 * Makes the column take the remaining width.
	 */
	public static final Width STRETCH = new Width("stretch");

	private final Object value;

	private Width(Object value) {
		this.value = value;
	}

	/**
	 * Creates a width relative to the weights of the sibling columns.
	 * @param weight the relative weight
	 * @return the width
	 * @throws IllegalArgumentException if {@code weight} is not positive
	 */
	public static Width weight(int weight) {
		return new Width(requirePositive(weight));
	}

	/**
	 * Creates a width in pixels.
	 * @param pixels the width in pixels
	 * @return the width
	 * @throws IllegalArgumentException if {@code pixels} is not positive
	 */
	public static Width pixels(int pixels) {
		return new Width(requirePositive(pixels) + "px");
	}

	private static int requirePositive(int number) {
		if (number <= 0) {
			throw new IllegalArgumentException("Width must be positive, was " + number);
		}
		return number;
	}

	@JsonValue
	public Object value() {
		return this.value;
	}

	@Override
	public boolean equals(Object other) {
		return this == other || (other instanceof Width width && this.value.equals(width.value));
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.value);
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

}
