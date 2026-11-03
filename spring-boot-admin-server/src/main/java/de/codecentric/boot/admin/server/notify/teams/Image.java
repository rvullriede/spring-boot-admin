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
 * An <a href="https://adaptivecards.microsoft.com/?topic=Image">Image</a>.
 *
 * @param url the URL of the image
 * @param altText the alternative text describing the image
 * @param size the image size
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Image(String url, @Nullable String altText, @Nullable ImageSize size) implements CardElement {

	/**
	 * Creates an image without alternative text and with the default size.
	 */
	public static Image of(String url) {
		return new Image(url, null, null);
	}

	/**
	 * Returns a new image with the alternative text replaced.
	 */
	public Image withAltText(String text) {
		return new Image(this.url, text, this.size);
	}

	/**
	 * Returns a new image with the size replaced.
	 */
	public Image withSize(ImageSize imageSize) {
		return new Image(this.url, this.altText, imageSize);
	}

	@Override
	public String type() {
		return "Image";
	}

	@Override
	public String minVersion() {
		return CardVersions.V1_0;
	}

}
