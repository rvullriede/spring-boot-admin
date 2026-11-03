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
 * A <a href="https://adaptivecards.microsoft.com/?topic=TextBlock">TextBlock</a>.
 *
 * @param text the text to display
 * @param size the text size
 * @param weight the text weight
 * @param color the text color, one of {@link TextColor} or any value the client accepts
 * @param wrap whether the text wraps onto several lines
 * @param horizontalAlignment the horizontal alignment of the text
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TextBlock(String text, @Nullable TextSize size, @Nullable TextWeight weight, @Nullable String color,
		@Nullable Boolean wrap, @Nullable HorizontalAlignment horizontalAlignment) implements CardElement {

	public static TextBlock of(String text) {
		return new TextBlock(text, null, null, null, null, null);
	}

	public TextBlock withSize(TextSize textSize) {
		return new TextBlock(this.text, textSize, this.weight, this.color, this.wrap, this.horizontalAlignment);
	}

	public TextBlock withWeight(TextWeight textWeight) {
		return new TextBlock(this.text, this.size, textWeight, this.color, this.wrap, this.horizontalAlignment);
	}

	public TextBlock withColor(TextColor textColor) {
		return withColor(textColor.value());
	}

	public TextBlock withColor(String textColor) {
		return new TextBlock(this.text, this.size, this.weight, textColor, this.wrap, this.horizontalAlignment);
	}

	public TextBlock withWrap(boolean wrapText) {
		return new TextBlock(this.text, this.size, this.weight, this.color, wrapText, this.horizontalAlignment);
	}

	public TextBlock withHorizontalAlignment(HorizontalAlignment alignment) {
		return new TextBlock(this.text, this.size, this.weight, this.color, this.wrap, alignment);
	}

	@Override
	public String type() {
		return "TextBlock";
	}

	@Override
	public String minVersion() {
		return CardVersions.V1_0;
	}

}
