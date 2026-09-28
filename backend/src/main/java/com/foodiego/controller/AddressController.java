package com.foodiego.controller;

import com.foodiego.entity.Address;
import com.foodiego.entity.User;
import com.foodiego.repository.AddressRepository;
import com.foodiego.security.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressRepository addressRepository;
    private final AuthUtil authUtil;

    public AddressController(AddressRepository addressRepository, AuthUtil authUtil) {
        this.addressRepository = addressRepository;
        this.authUtil = authUtil;
    }

    @GetMapping
    public List<Address> list(HttpServletRequest request) {
        return addressRepository.findByUser(requireUser(request));
    }

    @PostMapping
    public Address create(HttpServletRequest request, @RequestBody Address address) {
        address.setUser(requireUser(request));
        return addressRepository.save(address);
    }

    @DeleteMapping("/{id}")
    public void delete(HttpServletRequest request, @PathVariable Long id) {
        User user = requireUser(request);
        Address address = addressRepository.findById(id).orElseThrow();
        if (!address.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not authorized");
        }
        addressRepository.delete(address);
    }

    private User requireUser(HttpServletRequest request) {
        return authUtil.getCurrentUser(request)
                .orElseThrow(() -> new IllegalArgumentException("Please log in to continue."));
    }
}
