/*
 * Copyright 2014-2023 the original author or authors.
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

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tools.jackson.databind.json.JsonMapper;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceDeregisteredEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceInfoChangedEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceRegisteredEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.Info;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.domain.values.Registration;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.MicrosoftTeamsNotifier.Message;
import de.codecentric.boot.admin.server.notify.teams.AdaptiveCard;
import de.codecentric.boot.admin.server.notify.teams.TeamsMessage;
import de.codecentric.boot.admin.server.notify.teams.TextBlock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class MicrosoftTeamsNotifierTest {

	private static final String APP_NAME = "Test App";

	private static final String APP_ID = "TestAppId";

	private static final String HEALTH_URL = "https://health";

	private static final String MANAGEMENT_URL = "https://management";

	private static final String SERVICE_URL = "https://service";

	private MicrosoftTeamsNotifier notifier;

	private RestTemplate mockRestTemplate;

	private InstanceRepository repository;

	private Instance instance;

	@BeforeEach
	void setUp() {
		instance = Instance.create(InstanceId.of(APP_ID))
			.register(Registration.create(APP_NAME, HEALTH_URL)
				.managementUrl(MANAGEMENT_URL)
				.serviceUrl(SERVICE_URL)
				.build());

		repository = mock(InstanceRepository.class);
		when(repository.find(instance.getId())).thenReturn(Mono.just(instance));

		mockRestTemplate = mock(RestTemplate.class);
		notifier = new MicrosoftTeamsNotifier(repository, mockRestTemplate);
		notifier.setWebhookUrl("https://example.com");
	}

	@Test
	void test_onClientApplicationDeRegisteredEvent_resolve() throws Exception {
		assertPostedJson(new InstanceDeregisteredEvent(instance.getId(), 1L), instance, "ms-teams-deregistered.json");
	}

	@Test
	void test_onApplicationRegisteredEvent_resolve() throws Exception {
		assertPostedJson(new InstanceRegisteredEvent(instance.getId(), 1L, instance.getRegistration()), instance,
				"ms-teams-registered.json");
	}

	@Test
	void test_onApplicationStatusChangedEvent_resolve() throws Exception {
		assertPostedJson(new InstanceStatusChangedEvent(instance.getId(), 1L, StatusInfo.ofUp()), instance,
				"ms-teams-status-changed.json");
	}

	@Test
	void test_onApplicationStatusChangedEvent_withSource_resolve() throws Exception {
		Instance instanceWithSource = Instance.create(InstanceId.of(APP_ID))
			.register(Registration.create(APP_NAME, HEALTH_URL)
				.managementUrl(MANAGEMENT_URL)
				.serviceUrl(SERVICE_URL)
				.source("http-api")
				.build());

		assertPostedJson(new InstanceStatusChangedEvent(instanceWithSource.getId(), 1L, StatusInfo.ofDown()),
				instanceWithSource, "ms-teams-status-changed-with-source.json");
	}

	@Test
	void test_shouldNotifyWithRegisteredEventReturns_true() {
		InstanceRegisteredEvent event = new InstanceRegisteredEvent(instance.getId(), 1L, instance.getRegistration());
		assertThat(notifier.shouldNotify(event, instance)).isTrue();
	}

	@Test
	void test_shouldNotifyWithDeRegisteredEventReturns_true() {
		InstanceDeregisteredEvent event = new InstanceDeregisteredEvent(instance.getId(), 1L);
		assertThat(notifier.shouldNotify(event, instance)).isTrue();
	}

	@Test
	@SuppressWarnings("removal")
	void test_getDeregisteredMessageForAppReturns_correctContent() {
		Message message = notifier.getDeregisteredMessage(instance,
				notifier.createEvaluationContext(new InstanceDeregisteredEvent(instance.getId(), 1L), instance));

		assertMessage(message, notifier.getDeregisteredTitle(),
				"Test App with id TestAppId has de-registered from Spring Boot Admin", "Accent");
	}

	@Test
	@SuppressWarnings("removal")
	void test_getRegisteredMessageForAppReturns_correctContent() {
		Message message = notifier.getRegisteredMessage(instance,
				notifier.createEvaluationContext(new InstanceDeregisteredEvent(instance.getId(), 1L), instance));

		assertMessage(message, notifier.getRegisteredTitle(),
				"Test App with id TestAppId has registered with Spring Boot Admin", "Accent");
	}

	@Test
	@SuppressWarnings("removal")
	void test_getStatusChangedMessageForAppReturns_correctContent() {
		Message message = notifier.getStatusChangedTextExpression(instance, notifier.createEvaluationContext(
				new InstanceStatusChangedEvent(instance.getId(), 1L, StatusInfo.ofDown()), instance));

		assertMessage(message, notifier.getStatusChangedTitle(),
				"Test App with id TestAppId changed status from UNKNOWN to DOWN", "Attention");
	}

	@Test
	@SuppressWarnings("removal")
	void test_getStatusChangedMessageForAppReturns_UP_to_DOWN() {
		notifier.updateLastStatus(new InstanceStatusChangedEvent(instance.getId(), 1L, StatusInfo.ofUp()));

		Message message = notifier.getStatusChangedTextExpression(instance, notifier.createEvaluationContext(
				new InstanceStatusChangedEvent(instance.getId(), 1L, StatusInfo.ofDown()), instance));

		assertMessage(message, notifier.getStatusChangedTitle(),
				"Test App with id TestAppId changed status from UP to DOWN", "Attention");
	}

	@Test
	@SuppressWarnings("removal")
	void test_getStatusChangedMessageWithExtraFormatArgumentReturns_activitySubtitlePatternWithAppName() {
		notifier.setStatusChangedTextExpression("STATUS_ACTIVITY_PATTERN_#{instance.registration.name}");
		Message message = notifier.getStatusChangedTextExpression(instance,
				notifier.createEvaluationContext(new InstanceDeregisteredEvent(instance.getId(), 1L), instance));

		assertThat(getActivitySubtitleFromMessage(message)).isEqualTo("STATUS_ACTIVITY_PATTERN_" + APP_NAME);
	}

	@Test
	@SuppressWarnings("removal")
	void test_getRegisterMessageWithExtraFormatArgumentReturns_activitySubtitlePatternWithAppName() {
		notifier.setRegisteredTextExpression("REGISTER_ACTIVITY_PATTERN_#{instance.registration.name}");
		Message message = notifier.getRegisteredMessage(instance,
				notifier.createEvaluationContext(new InstanceDeregisteredEvent(instance.getId(), 1L), instance));

		assertThat(getActivitySubtitleFromMessage(message)).isEqualTo("REGISTER_ACTIVITY_PATTERN_" + APP_NAME);
	}

	@Test
	@SuppressWarnings("removal")
	void test_getDeRegisterMessageWithExtraFormatArgumentReturns_activitySubtitlePatternWithAppName() {
		notifier.setDeregisteredTextExpression("DEREGISTER_ACTIVITY_PATTERN_#{instance.registration.name}");
		Message message = notifier.getDeregisteredMessage(instance,
				notifier.createEvaluationContext(new InstanceDeregisteredEvent(instance.getId(), 1L), instance));

		assertThat(getActivitySubtitleFromMessage(message)).isEqualTo("DEREGISTER_ACTIVITY_PATTERN_" + APP_NAME);
	}

	@Test
	@SuppressWarnings("removal")
	void test_getStatusChangedMessage_parsesThemeColorFromSpelExpression() {
		notifier.setTitleColorExpression(
				"#{event.type == 'STATUS_CHANGED' ? (event.statusInfo.status=='UP' ? 'green' : 'red') : 'blue'}");

		Message message = notifier.getStatusChangedTextExpression(instance, notifier.createEvaluationContext(
				new InstanceStatusChangedEvent(instance.getId(), 1L, StatusInfo.ofUp()), instance));

		assertThat(getColorFromMessage(message)).isEqualTo("green");
	}

	@Test
	@SuppressWarnings("removal")
	void test_messageSerializesToExpectedJsonStructure() throws Exception {
		// Update instance to UP status
		Instance upInstance = Instance.create(instance.getId())
			.register(instance.getRegistration())
			.withStatusInfo(StatusInfo.ofUp());

		Message message = notifier.getStatusChangedTextExpression(upInstance, notifier.createEvaluationContext(
				new InstanceStatusChangedEvent(upInstance.getId(), 1L, StatusInfo.ofUp()), upInstance));

		JsonMapper mapper = JsonMapper.builder().build();
		String actual = mapper.writeValueAsString(message);

		// Build expected JSON structure
		String expectedJson = """
				{
					"type": "message",
					"attachments": [{
						"contentType": "application/vnd.microsoft.card.adaptive",
						"content": {
							"$schema": "http://adaptivecards.io/schemas/adaptive-card.json",
							"type": "AdaptiveCard",
							"version": "1.2",
							"body": [
								{
									"type": "TextBlock",
									"text": "Status Changed",
									"size": "Large",
									"weight": "Bolder",
									"color": "Good"
								},
								{
									"type": "TextBlock",
									"text": "Test App",
									"size": "Medium",
									"weight": "Bolder"
								},
								{
									"type": "TextBlock",
									"text": "Test App with id TestAppId changed status from UNKNOWN to UP",
									"wrap": true
								},
								{
									"type": "FactSet",
									"facts": [
										{"title": "Status", "value": "UP"},
										{"title": "Service URL", "value": "https://service"},
										{"title": "Health URL", "value": "https://health"},
										{"title": "Management URL", "value": "https://management"}
									]
								}
							]
						}
					}]
				}
				""";

		JSONAssert.assertEquals(expectedJson, actual, JSONCompareMode.NON_EXTENSIBLE);
	}

	@Test
	void test_createCard_returnsNullForOtherEvents() {
		InstanceInfoChangedEvent event = new InstanceInfoChangedEvent(instance.getId(), 1L, Info.empty());

		assertThat(notifier.createCard(event, instance, notifier.createEvaluationContext(event, instance))).isNull();
	}

	@Test
	void test_doNotify_ignoresOtherEvents() {
		StepVerifier
			.create(notifier.doNotify(new InstanceInfoChangedEvent(instance.getId(), 1L, Info.empty()), instance))
			.verifyComplete();

		verifyNoInteractions(mockRestTemplate);
	}

	@Test
	void test_doNotify_postsTypedMessage() {
		StepVerifier
			.create(notifier.doNotify(new InstanceRegisteredEvent(instance.getId(), 1L, instance.getRegistration()),
					instance))
			.verifyComplete();

		assertThat(capturePostedEntity().getBody()).isInstanceOf(TeamsMessage.class);
	}

	@Test
	void test_titleColorExpression_passesArbitraryStringThrough() {
		notifier.setTitleColorExpression("#{event.type == 'STATUS_CHANGED' ? 'green' : 'blue'}");

		StepVerifier
			.create(notifier.doNotify(new InstanceStatusChangedEvent(instance.getId(), 1L, StatusInfo.ofUp()),
					instance))
			.verifyComplete();

		JsonMapper mapper = JsonMapper.builder().build();
		String json = mapper.writeValueAsString(capturePostedEntity().getBody());
		assertThat(mapper.readTree(json).at("/attachments/0/content/body/0/color").asString()).isEqualTo("green");
	}

	@Test
	void test_postMessage_postsSubclassCard() {
		TeamsMessage own = TeamsMessage.of(AdaptiveCard.of(TextBlock.of("Deployed")));
		MicrosoftTeamsNotifier deploymentNotifier = new MicrosoftTeamsNotifier(repository, mockRestTemplate) {
			@Override
			protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
				return postMessage(own);
			}
		};
		deploymentNotifier.setWebhookUrl("https://example.com");

		StepVerifier.create(deploymentNotifier.doNotify(new InstanceDeregisteredEvent(instance.getId(), 1L), instance))
			.verifyComplete();

		HttpEntity<Object> entity = capturePostedEntity();
		assertThat(entity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		assertThat(entity.getBody()).isSameAs(own);
	}

	@Test
	void test_postMessage_withoutWebhookUrl_errors() {
		notifier.setWebhookUrl(null);

		StepVerifier.create(notifier.postMessage(TeamsMessage.of(AdaptiveCard.of())))
			.verifyErrorMessage("'webhookUrl' must not be null.");
		verifyNoInteractions(mockRestTemplate);
	}

	@Test
	void test_defaultStatusChangedCard_isWellUnderWebhookLimit() {
		StepVerifier
			.create(notifier.doNotify(new InstanceStatusChangedEvent(instance.getId(), 1L, StatusInfo.ofDown()),
					instance))
			.verifyComplete();

		String json = JsonMapper.builder().build().writeValueAsString(capturePostedEntity().getBody());
		assertThat(json.getBytes(StandardCharsets.UTF_8)).hasSizeLessThan(28 * 1024);
	}

	private void assertPostedJson(InstanceEvent event, Instance instance, String fixture) throws Exception {
		StepVerifier.create(notifier.doNotify(event, instance)).verifyComplete();

		HttpEntity<Object> entity = capturePostedEntity();
		assertThat(entity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

		String actual = JsonMapper.builder().build().writeValueAsString(entity.getBody());
		JSONAssert.assertEquals(readFixture(fixture), actual, JSONCompareMode.STRICT);
	}

	@SuppressWarnings("unchecked")
	private HttpEntity<Object> capturePostedEntity() {
		ArgumentCaptor<HttpEntity<Object>> entity = ArgumentCaptor.forClass(HttpEntity.class);
		verify(mockRestTemplate).postForEntity(eq(URI.create("https://example.com")), entity.capture(), eq(Void.class));
		return entity.getValue();
	}

	private String readFixture(String name) throws IOException {
		return StreamUtils.copyToString(getClass().getResourceAsStream(name), StandardCharsets.UTF_8);
	}

	@SuppressWarnings("removal")
	private String getActivitySubtitleFromMessage(Message message) {
		return message.getAttachments().get(0).getContent().getBody().get(2).getText();
	}

	@SuppressWarnings("removal")
	private String getColorFromMessage(Message message) {
		return message.getAttachments().get(0).getContent().getBody().get(0).getColor();
	}

	@SuppressWarnings("removal")
	private void assertMessage(Message message, String expectedTitle, String expectedSubTitle, String expectedColor) {
		assertThat(message.getType()).isEqualTo("message");
		assertThat(message.getAttachments()).hasSize(1);

		var attachment = message.getAttachments().get(0);
		assertThat(attachment.getContentType()).isEqualTo("application/vnd.microsoft.card.adaptive");
		assertThat(attachment.getContentUrl()).isNull();

		var card = attachment.getContent();
		assertThat(card.getType()).isEqualTo("AdaptiveCard");
		assertThat(card.getVersion()).isEqualTo("1.2");
		assertThat(card.getSchema()).isEqualTo("http://adaptivecards.io/schemas/adaptive-card.json");

		var body = card.getBody();
		assertThat(body).hasSize(4);

		// Title
		assertThat(body.get(0).getType()).isEqualTo("TextBlock");
		assertThat(body.get(0).getText()).isEqualTo(expectedTitle);
		assertThat(body.get(0).getSize()).isEqualTo("Large");
		assertThat(body.get(0).getWeight()).isEqualTo("Bolder");
		assertThat(body.get(0).getColor()).isEqualTo(expectedColor);

		// Service Name
		assertThat(body.get(1).getType()).isEqualTo("TextBlock");
		assertThat(body.get(1).getText()).isEqualTo(instance.getRegistration().getName());
		assertThat(body.get(1).getSize()).isEqualTo("Medium");
		assertThat(body.get(1).getWeight()).isEqualTo("Bolder");

		// Activity Subtitle
		assertThat(body.get(2).getType()).isEqualTo("TextBlock");
		assertThat(body.get(2).getText()).isEqualTo(expectedSubTitle);
		assertThat(body.get(2).getWrap()).isTrue();

		// Facts
		assertThat(body.get(3).getType()).isEqualTo("FactSet");
		assertThat(body.get(3).getFacts()).hasSize(4).anySatisfy((fact) -> {
			assertThat(fact.title()).isEqualTo("Status");
			assertThat(fact.value()).isEqualTo("UNKNOWN");
		}).anySatisfy((fact) -> {
			assertThat(fact.title()).isEqualTo("Service URL");
			assertThat(fact.value()).isEqualTo(SERVICE_URL);
		}).anySatisfy((fact) -> {
			assertThat(fact.title()).isEqualTo("Health URL");
			assertThat(fact.value()).isEqualTo(HEALTH_URL);
		}).anySatisfy((fact) -> {
			assertThat(fact.title()).isEqualTo("Management URL");
			assertThat(fact.value()).isEqualTo(MANAGEMENT_URL);
		});
	}

}
