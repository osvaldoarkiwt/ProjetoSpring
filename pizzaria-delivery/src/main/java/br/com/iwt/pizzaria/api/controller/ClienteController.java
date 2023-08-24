package br.com.iwt.pizzaria.api.controller;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.iwt.pizzaria.api.assembler.ClienteAssembler;
import br.com.iwt.pizzaria.api.model.ClienteModel;
import br.com.iwt.pizzaria.api.model.input.ClienteInput;
import br.com.iwt.pizzaria.domain.model.Cliente;
import br.com.iwt.pizzaria.domain.service.CadastroClienteService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	CadastroClienteService service;
	
	@Autowired
	ClienteAssembler assembler;
	
	@GetMapping
	public ResponseEntity<Page<ClienteModel>> listar(Pageable pageable){
		
		Page<ClienteModel> clientes = service.retornarClientes(pageable);
			
		return ResponseEntity.ok(clientes);
	}
	
	@GetMapping("/{clienteId}")
	public ResponseEntity<ClienteModel> listarPorId(@PathVariable UUID clienteId) {
		
			Cliente cliente = service.listarOrThrow(clienteId);
			
			return ResponseEntity.ok(assembler.toModel(cliente));
	}
	
	@PostMapping
	//@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ClienteModel> cadastrarCliente(@RequestBody ClienteInput clienteInput) {
		
		return ResponseEntity.ok(service.salvar(clienteInput));		
	}
	
	@Transactional
	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Cliente> deletarCliente(@PathVariable UUID clienteId) {
		
		service.deletar(clienteId);
		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{clienteId}")
	public ResponseEntity<ClienteModel> editarCliente(@PathVariable UUID clienteId, @RequestBody ClienteInput clienteInput) {
		
		Cliente clienteRes = service.listarOrThrow(clienteId);
			
		Cliente entidade = assembler.toEntity(clienteInput);
	
		BeanUtils.copyProperties(entidade, clienteRes,"id");
		
		ClienteInput input = assembler.toInput(clienteRes);
		
		return ResponseEntity.ok(service.salvar(input));
	}
}
