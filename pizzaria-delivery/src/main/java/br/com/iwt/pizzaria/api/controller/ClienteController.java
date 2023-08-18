package br.com.iwt.pizzaria.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
		
		List<ClienteModel> clientes = assembler.toCollectionModel(service.retornarClientes(pageable));
		
		Page<ClienteModel> clientePageModel = new PageImpl<>(clientes,pageable, clientes.size());
		
		return ResponseEntity.ok(clientePageModel);
	}
	
	@GetMapping("/{clienteId}")
	public ResponseEntity<ClienteModel> listarPorId(@PathVariable UUID clienteId) {
		
		Cliente clienteResposta = service.retornarCliente(clienteId).orElse(null);
		
		if(clienteResposta != null) {
			
			ClienteModel model = assembler.toModel(clienteResposta);
			
			return ResponseEntity.ok(model);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	//@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ClienteInput> cadastrarCliente(@RequestBody ClienteInput clienteInput) {
		
		Cliente clienteResposta = assembler.toEntity(clienteInput);
				
		service.salvar(clienteResposta);
		
		return ResponseEntity.ok(clienteInput);		
	}
	
	@Transactional
	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Cliente> deletarCliente(@PathVariable UUID clienteId) {
		
		service.deletar(clienteId);
		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{clienteId}")
	public ResponseEntity<ClienteModel> editarCliente(@PathVariable UUID clienteId, @RequestBody ClienteInput clienteInput) {
		
		Cliente clienteRes = service.retornarCliente(clienteId).orElse(null);
		
		if(clienteRes != null) {
			
			Cliente entidade = assembler.toEntity(clienteInput);
	
			BeanUtils.copyProperties(entidade, clienteRes,"id");
			
			Cliente clienteSalvo = service.salvar(clienteRes);
			
			ClienteModel modelo = assembler.toModel(clienteSalvo);
			
			return ResponseEntity.ok(modelo);
		}
		
		return ResponseEntity.notFound().build();
	}
}
