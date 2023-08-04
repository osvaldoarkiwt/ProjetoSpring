package br.com.iwt.pizzaria.domain.repository;

import java.rmi.server.UID;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.iwt.pizzaria.domain.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UID> {

	Optional<Cliente> findById(UUID id);

	void deleteById(UUID id);
}
