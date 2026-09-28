package com.foodiego.controller;

import com.foodiego.entity.ContactMessage;
import com.foodiego.repository.ContactMessageRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactMessageRepository contactMessageRepository;

    public ContactController(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    @PostMapping
    public ContactMessage send(@RequestBody ContactMessage message) {
        return contactMessageRepository.save(message);
    }
}
