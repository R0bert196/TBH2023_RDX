package com.rdx.rdxserver.controllers;

import com.rdx.rdxserver.entities.ContractEntity;
import com.rdx.rdxserver.services.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contract")
@CrossOrigin
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping(produces = {"application/json"})
    private ResponseEntity<ContractEntity> getContract(@RequestParam int id){
        ContractEntity contractEntity = contractService.getContractById(id);
        return contractEntity == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(contractEntity);
    }

    @PostMapping(value="/register",produces = {"application/json"})
    private ResponseEntity<String> saveContract(@RequestBody ContractEntity tempContractEntity){
        ContractEntity contractEntity = contractService.registerContract(tempContractEntity);
        return contractEntity == null ? ResponseEntity.status(HttpStatus.CONFLICT).body("Contract Allready registered") : ResponseEntity.status(HttpStatus.CREATED).body("Contract Created");
    }

    @GetMapping(value="/all",produces = {"application/json"})
    private ResponseEntity<List<ContractEntity>> getAll(){
        return ResponseEntity.ok(contractService.getAll());
    //TODO: DELETE UNSAFE !
    }

    @GetMapping(value="/getByCompanyId",produces = {"application/json"})
    private ResponseEntity<List<ContractEntity>> getByCompanyId(@RequestParam int companyId){
        return ResponseEntity.ok(contractService.findByCompanyId(companyId));
    }

    @PutMapping(produces = {"application/json"})
    private ResponseEntity<ContractEntity> modifyContract(@RequestBody ContractEntity newContract){
        //Validate if owner !
        ContractEntity updatedContract = contractService.update(newContract);
        return newContract == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updatedContract);
    }
}
