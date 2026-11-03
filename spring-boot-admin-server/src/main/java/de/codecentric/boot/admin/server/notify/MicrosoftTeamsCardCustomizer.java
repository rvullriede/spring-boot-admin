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

package de.codecentric.boot.admin.server.notify;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.teams.AdaptiveCard;

/**
 * Callback interface that can be used to customize the {@link AdaptiveCard} the
 * {@link MicrosoftTeamsNotifier} posts.
 * <p>
 * Customizers run only on the three default cards (registered, deregistered and status
 * changed), in {@code @Order} order. A customizer that throws or returns {@code null} is
 * skipped and the card of the previous customizer is posted.
 */
@FunctionalInterface
public interface MicrosoftTeamsCardCustomizer {

	/**
	 * Customizes the card. Cards are immutable, so return the changed copy.
	 * @param card the card as built so far
	 * @param event the event the card is about
	 * @param instance the instance the event belongs to
	 * @return the card to post
	 */
	AdaptiveCard customize(AdaptiveCard card, InstanceEvent event, Instance instance);

}
