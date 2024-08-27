package com.appliances.recyle.controller;

import com.appliances.recyle.dto.ItemDTO;
import com.appliances.recyle.dto.PageRequestDTO;
import com.appliances.recyle.dto.PageResponseDTO;
import com.appliances.recyle.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequestMapping("/echopickup/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ItemService itemService;

    @GetMapping("/product_register")
    public void registerGet() {


    }

    @PostMapping("/product_register")
    public String registerPost(@Valid ItemDTO itemDTO) {
        Long ino = itemService.insert(itemDTO);

        return "redirect:/echopickup/admin/product";
    }

    @GetMapping("/product")
    public void productGet(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<ItemDTO> responseDTO
                = itemService.productList(pageRequestDTO);

        log.info("AdminController 확인 중, responseDTO : " + responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

}
