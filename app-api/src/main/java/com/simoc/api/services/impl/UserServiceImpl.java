package com.simoc.api.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simoc.api.models.Role;
import com.simoc.api.models.User;
import com.simoc.api.repositories.IUserRepository;
import com.simoc.api.services.IUserService;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	private static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private IUserRepository repo;

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return repo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public User findById(Integer id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public User add(User user) {
		return repo.save(user);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		repo.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = repo.findByUsername(username);

		if (user == null) {
			LOG.error("Error en login: No existe el usuario '" + username + "' en el sistema");
			throw new UsernameNotFoundException(
					"Error en login: No existe el usuario '" + username + "' en el sistema");
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role role : user.getRoles()) {
			LOG.info("Role: ".concat(role.getName()));
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		// List<GrantedAuthority> authorities = user.getRoles()
		// .stream()
		// .map(role -> new SimpleGrantedAuthority(role.getName()))
		// .peek(authority -> LOG.info("Rol: " + authority.getAuthority() ) )
		// .collect(Collectors.toList());

		if (authorities.isEmpty()) {
			LOG.error("Error en el Login: Usuario '" + username + "' no tiene roles asignados!");
			throw new UsernameNotFoundException(
					"Error en el Login: usuario '" + username + "' no tiene roles asignados!");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.isDeleted(), true, true, true, authorities);
	}

	@Override
	@Transactional(readOnly = true)
	public User findByUsername(String username) {
		return repo.findByUsername(username);
	}
}
