package com.cepheid.cloud.skel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.cepheid.cloud.skel.model.Item;

public class ItemControllerTest extends TestBase {

	@Test
	public void testGetItems() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/items", HttpMethod.GET, entity,
				String.class);

		assertNotNull(response.getBody());
	}

	@Test
	public void testGetItemById() {
		Item item = restTemplate.getForObject(getRootUrl() + "/items/1", Item.class);
		assertNotNull(item);
	}

	@Test
	public void testCreateItem() {
		Item item = new Item();
		item.setName("Washing Machine");
		item.setPrice(1000);

		ResponseEntity<Item> postResponse = restTemplate.postForEntity(getRootUrl() + "/items", item, Item.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdateEmployee() {
		int id = 1;
		Item item = restTemplate.getForObject(getRootUrl() + "/items/" + id, Item.class);
		item.setName("Radio");
		item.setPrice(50);

		restTemplate.put(getRootUrl() + "/items/" + id, item);

		Item updatedItem = restTemplate.getForObject(getRootUrl() + "/items/" + id, Item.class);
		assertNotNull(updatedItem);
	}

	@Test
	public void testDeleteEmployee() {
		int id = 2;
		Item item = restTemplate.getForObject(getRootUrl() + "/items/" + id, Item.class);
		assertNotNull(item);

		restTemplate.delete(getRootUrl() + "/items/" + id);

		try {
			item = restTemplate.getForObject(getRootUrl() + "/items/" + id, Item.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
