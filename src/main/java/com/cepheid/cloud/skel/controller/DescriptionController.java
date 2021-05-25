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
import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.repository.DescriptionRepository;
import com.cepheid.cloud.skel.repository.ItemRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value="descriptionprofile", description="Operations pertaining to description")
public class DescriptionController {

	@Autowired
	ItemRepository mItemRepository;

	@Autowired
	DescriptionRepository mDescriptionRepository;

	@ApiOperation(value = "Retrieve list of available descriptions by item id",response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
	@GetMapping("/items/{itemId}/descriptions")
	public ResponseEntity<Page<Description>> getAllDescriptionsByItemId(@PathVariable(value = "itemId") Long itemId,
			Pageable pageable) {
		Page<Description> descriptions = mDescriptionRepository.findByItemId(itemId, pageable);

		if (descriptions.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(descriptions, HttpStatus.OK);
	}

	@ApiOperation(value = "Create a description by itemId")
	@PostMapping("/items/{itemId}/descriptions")
	public ResponseEntity<Description> createDescription(@PathVariable(value = "itemId") Long itemId,
			@Valid @RequestBody Description description) {
		Description savedDescription = mItemRepository.findById(itemId).map(item -> {
			description.setItem(item);
			return mDescriptionRepository.save(description);
		}).orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found."));

		return new ResponseEntity<>(savedDescription, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update a description by itemId and decriptionId")
	@PutMapping("/items/{itemId}/descriptions/{descriptionId}")
	public ResponseEntity<Description> updateDescription(@PathVariable(value = "itemId") Long itemId,
			@PathVariable(value = "descriptionId") Long descriptionId,
			@Valid @RequestBody Description descriptionRequest) {
		if (!mItemRepository.existsById(itemId)) {
			throw new NotFoundException("Item with id " + itemId + " not found.");
		}

		Description savedDescription = mDescriptionRepository.findById(descriptionId).map(description -> {
			description.setDescription(descriptionRequest.getDescription());
			return mDescriptionRepository.save(description);
		}).orElseThrow(() -> new NotFoundException("Description with id " + descriptionId + " not found."));

		return new ResponseEntity<>(savedDescription, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete a description by itemId and decriptionId")
	@DeleteMapping("/items/{itemId}/descriptions/{descriptionId}")
	public ResponseEntity<HttpStatus> deleteDescription(@PathVariable(value = "itemId") Long itemId,
			@PathVariable(value = "descriptionId") Long descriptionId) {
		Optional<Description> descriptionOptional = mDescriptionRepository.findByIdAndItemId(descriptionId, itemId);

		if (descriptionOptional.isPresent()) {
			mDescriptionRepository.delete(descriptionOptional.get());
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			throw new NotFoundException(
					"Item with id " + itemId + "and Description with id " + descriptionId + " not found.");
		}
	}
}
