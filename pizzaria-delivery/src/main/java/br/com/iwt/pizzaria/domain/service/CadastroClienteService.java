package br.com.iwt.pizzaria.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.iwt.pizzaria.domain.model.Cliente;
import br.com.iwt.pizzaria.domain.repository.ClienteRepository;

@Service
public class CadastroClienteService {

	@Autowired
	ClienteRepository repositorio;	
	
	public Cliente salvar(Cliente cliente) {
		
		return repositorio.save(cliente);		
	}
	
	public List<Cliente> retornarClientes(Pageable pageable){
		
		Page<Cliente> clientePage =  repositorio.findAll(pageable);
		
		return clientePage.getContent();
	}
	
	public Optional<Cliente> retornarCliente(UUID id) {
		return repositorio.findById(id);
	}
	
	public void deletar(UUID id) {
		
		repositorio.deleteById(id);
	}
	
}
