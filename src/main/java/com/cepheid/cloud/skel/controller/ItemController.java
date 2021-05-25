package com.cepheid.cloud.skel.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cepheid.cloud.skel.exception.runtime.NotFoundException;
import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.repository.ItemRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value="itemprofile", description="Operations pertaining to item")
public class ItemController {

	@Autowired
	ItemRepository mItemRepository;

	@ApiOperation(value = "Retrieve list of available items",response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
	@GetMapping("/items")
	public ResponseEntity<Page<Item>> getItems(Pageable pageable) {
		Page<Item> items = mItemRepository.findAll(pageable);

		if (items.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(items, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieve an item with an ID",response = Item.class)
	@GetMapping("/items/{id}")
	public ResponseEntity<Item> retrieveItem(@PathVariable long id) {
		Optional<Item> user = mItemRepository.findById(id);

		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		} else {
			throw new NotFoundException("Item with id " + id + " not found.");
		}
	}

	@ApiOperation(value = "Delete an item")
	@DeleteMapping("/items/{id}")
	public ResponseEntity<HttpStatus> deleteItem(@PathVariable long id) {
		if (mItemRepository.existsById(id)) {
			mItemRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			throw new NotFoundException("Item with id " + id + " not found.");
		}
	}

	@ApiOperation(value = "Create an item")
	@PostMapping("/items")
	public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
		Item savedItem = mItemRepository.save(item);
		return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update an item")
	@PutMapping("/items/{id}")
	public ResponseEntity<Item> updateUser(@PathVariable("id") long id, @RequestBody Item itemRequest) {
		Optional<Item> itemOptional = mItemRepository.findById(id);

		if (itemOptional.isPresent()) {
			Item item = itemOptional.get();
			item.setName(itemRequest.getName());
			item.setPrice(itemRequest.getPrice());

			return new ResponseEntity<>(mItemRepository.save(item), HttpStatus.OK);
		} else {
			throw new NotFoundException("Item with id " + id + " not found.");
		}
	}
}