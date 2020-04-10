package com.simoc.api.controllers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simoc.api.common.Constants;
import com.simoc.api.common.ServiceResponse;
import com.simoc.api.models.User;
import com.simoc.api.services.IUserService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class UserController {
	// private static Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserService userService;

	@GetMapping(value = "/usuarios")
	public ResponseEntity<ServiceResponse> list() {
		List<User> list;
		ServiceResponse response = new ServiceResponse();
		try {
			list = userService.findAll();
		} catch (DataAccessException e) {
			return new ResponseEntity<>(
					response.internalError(Constants.messageInternalError, Objects.requireNonNull(e.getMessage())
							.concat(": ").concat(e.getMostSpecificCause().getMessage())),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (list.isEmpty()) {
			return new ResponseEntity<>(response.notFound(Constants.messageNotFound, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response.ok(Constants.messageOk, list), HttpStatus.OK);
	}

	@GetMapping(value = "/usuarios/page/{page}")
	public ResponseEntity<ServiceResponse> list(@PathVariable Integer page) {
		Page<User> list;
		ServiceResponse response = new ServiceResponse();
		try {
			list = userService.findAll(PageRequest.of(page, 10));
		} catch (DataAccessException e) {
			return new ResponseEntity<>(
					response.internalError(Constants.messageInternalError, Objects.requireNonNull(e.getMessage())
							.concat(": ").concat(e.getMostSpecificCause().getMessage())),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (list.isEmpty()) {
			return new ResponseEntity<>(response.notFound(Constants.messageNotFound, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response.ok(Constants.messageOk, list), HttpStatus.OK);
	}

	@GetMapping(value = "/usuarios/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		User user;
		ServiceResponse response = new ServiceResponse();
		try {
			user = userService.findById(id);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(
					response.internalError(Constants.messageInternalError, Objects.requireNonNull(e.getMessage())
							.concat(": ").concat(e.getMostSpecificCause().getMessage())),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (user == null) {
			return new ResponseEntity<>(response.notFound(Constants.messageNotFound, null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(response.ok(Constants.messageOk, user), HttpStatus.OK);
	}

	@PostMapping(value = "/usuarios")
	public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
		User userResponse;
		ServiceResponse response = new ServiceResponse();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			return new ResponseEntity<>(response.badRequest(Constants.messageBadRequest, errors),
					HttpStatus.BAD_REQUEST);
		}
		try {
			userResponse = userService.add(user);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(
					response.internalError(Constants.messageInternalError, Objects.requireNonNull(e.getMessage())
							.concat(": ").concat(e.getMostSpecificCause().getMessage())),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response.ok(Constants.messageOk, userResponse), HttpStatus.CREATED);
	}

	@PutMapping(value = "/usuarios/{id}")
	public ResponseEntity<ServiceResponse> update(@PathVariable Integer id, @RequestBody User userRequest) {
		User user;
		ServiceResponse response = new ServiceResponse();
		User userResponse = userService.findById(id);

		if (userResponse == null) {
			return new ResponseEntity<>(response.notFound(Constants.messageNotFound, null), HttpStatus.NOT_FOUND);
		}

		try {
			userResponse.setUsername(userRequest.getUsername());
			userResponse.setPassword(userRequest.getPassword());
			user = userService.add(userResponse);

		} catch (DataAccessException e) {
			return new ResponseEntity<>(
					response.internalError(Constants.messageInternalError, Objects.requireNonNull(e.getMessage())
							.concat(": ").concat(e.getMostSpecificCause().getMessage())),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response.ok(Constants.messageOk, user), HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/usuarios/{id}")
	public ResponseEntity<ServiceResponse> delete(@PathVariable Integer id) {
		ServiceResponse response = new ServiceResponse();

		try {
			userService.delete(id);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(
					response.internalError(Constants.messageInternalError, Objects.requireNonNull(e.getMessage())
							.concat(": ").concat(e.getMostSpecificCause().getMessage())),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response.ok(Constants.messageOk, null), HttpStatus.OK);
	}

}
