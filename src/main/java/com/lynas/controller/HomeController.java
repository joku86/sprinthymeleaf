package com.lynas.controller;

import com.lynas.data.ContactDAO;
import com.lynas.data.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Iterator;


@Controller
public class HomeController {
    @Autowired
    private ContactDAO userRepository;

    @RequestMapping(value = "/")
    public String home() {
        return "index";
    }

    @RequestMapping("/hi")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
userRepository.save(new Contact("ddd","dd","ddd","dddd"));
        model.addAttribute("name", name+"von dem code");
        return "greeting";
    }

    @RequestMapping("/get")
    public String greeting2(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        Iterator<Contact> all = userRepository.findAll().iterator();

        int i = 0;
        while(all.hasNext()) {
            i++;
            all.next();
        }
        model.addAttribute("name", name+"von dem code: "+i);
        return "greeting";
    }
}
