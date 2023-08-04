package br.com.iwt.pizzaria.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.iwt.pizzaria.domain.model.Cliente;
import br.com.iwt.pizzaria.domain.service.CadastroClienteService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	CadastroClienteService service;
	
	@GetMapping
	public List<Cliente> listar(){
		
		return service.retornarClientes();
	}
	
	@GetMapping("/{clienteId}")
	public Cliente listarPorId(@PathVariable UUID clienteId) {
		
		return service.retornarCliente(clienteId).get();
	}
	
	@PostMapping
	public Cliente cadastrarCliente(@RequestBody Cliente cliente) {
		
		return service.salvar(cliente);		
	}
	
	@Transactional
	@DeleteMapping("/{clienteId}")
	public void deletarCliente(@PathVariable UUID clienteId) {
		service.deletar(clienteId);
	}
	
	@PutMapping("/{clienteId}")
	public void editarCliente(@PathVariable UUID clienteId, @RequestBody Cliente cliente) {
		
		Cliente clienteRes = service.retornarCliente(clienteId).get();
		
		BeanUtils.copyProperties(cliente, clienteRes,"id");
		
		service.salvar(clienteRes);		
	}
}
