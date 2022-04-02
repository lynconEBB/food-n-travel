package com.dev.foodtravel.controllers;

import com.dev.foodtravel.entities.Customer;
import com.dev.foodtravel.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;

/**
 * Classe controladora de uso geral responsavel por lidar com as requisições iniciddas com "/"
 * recebidas e garantir uma resposta, seja por meio de uma página html (view) ou outro tipo de arquivo
 */
@Controller
@RequestMapping("/")
public class MainController {
    private final CustomerRepository customerRepository;

    @Autowired
    public MainController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Metódo acionado ao relizar uma requisição GET para "/"
     * Exibe a página de menu principal contendo as 3 principais opções
     */
    @GetMapping
    public String showMenu() {
       return "mainMenu";
    }

    /**
     * Metódo acionado ao relizar uma requisição GET para "/report"
     * Envia um arquivo de relatório temporariamente para o cliente realizar download
     * o relatório contem todos os clientes cadastrados com seus historicos atuais de pedidos
     */
    @GetMapping("/report")
    public ResponseEntity<Resource> generateReport() throws IOException {
        File tempFile = File.createTempFile("relatorio",".txt");
        PrintWriter printWriter = new PrintWriter(new FileWriter(tempFile));

        Iterable<Customer> customers = customerRepository.findAll();
        for (Customer customer: customers) {
            printWriter.println(customer);
        }
        printWriter.close();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));
        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + tempFile.getName())
                .contentLength(tempFile.length())
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}