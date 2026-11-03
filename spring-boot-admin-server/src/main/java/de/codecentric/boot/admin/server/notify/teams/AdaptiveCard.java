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
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * An <a href="https://adaptivecards.microsoft.com/?topic=AdaptiveCard">AdaptiveCard</a>.
 * Unless set explicitly, {@link #version()} is the highest minimum version of the card's
 * elements and actions, at least 1.2. It is the value serialized as {@code version}.
 *
 * @param body the card elements
 * @param actions the card actions
 * @param msteams the Microsoft Teams specific properties
 * @param version the explicit schema version, or {@code null} to compute it
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdaptiveCard(List<CardElement> body, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<CardAction> actions,
		@Nullable MsTeams msteams, @Nullable String version) {

	/**
	 * Creates a card with defensive copies of the given lists.
	 * @throws IllegalArgumentException if {@code version} is not of the form
	 * {@code <major>.<minor>}
	 */
	public AdaptiveCard {
		body = List.copyOf(body);
		actions = List.copyOf(actions);
		if (version != null) {
			CardVersions.requireValid(version);
		}
	}

	/**
	 * Creates a card with the given body and no actions.
	 */
	public static AdaptiveCard of(CardElement... body) {
		return new AdaptiveCard(List.of(body), List.of(), null, null);
	}

	/**
	 * Returns a new card with the elements appended to the body.
	 */
	public AdaptiveCard withBody(CardElement... elements) {
		List<CardElement> merged = new ArrayList<>(this.body);
		merged.addAll(List.of(elements));
		return new AdaptiveCard(merged, this.actions, this.msteams, this.version);
	}

	/**
	 * Returns a new card with the actions appended.
	 */
	public AdaptiveCard withActions(CardAction... additional) {
		List<CardAction> merged = new ArrayList<>(this.actions);
		merged.addAll(List.of(additional));
		return new AdaptiveCard(this.body, merged, this.msteams, this.version);
	}

	/**
	 * Returns a new card with the Teams properties replaced.
	 */
	public AdaptiveCard withMsTeams(MsTeams msTeams) {
		return new AdaptiveCard(this.body, this.actions, msTeams, this.version);
	}

	/**
	 * Returns a new card with the version set explicitly.
	 * @throws IllegalArgumentException if the version is not of the form
	 * {@code <major>.<minor>}
	 */
	public AdaptiveCard withVersion(String explicitVersion) {
		return new AdaptiveCard(this.body, this.actions, this.msteams, explicitVersion);
	}

	@JsonProperty("$schema")
	public String schema() {
		return "http://adaptivecards.io/schemas/adaptive-card.json";
	}

	@JsonProperty("type")
	public String type() {
		return "AdaptiveCard";
	}

	@Override
	public String version() {
		if (this.version != null) {
			return this.version;
		}
		return CardVersions.max(Stream.concat(Stream.of(CardVersions.V1_2), Stream.concat(
				this.body.stream().map(CardElement::minVersion), this.actions.stream().map(CardAction::minVersion))));
	}

}
