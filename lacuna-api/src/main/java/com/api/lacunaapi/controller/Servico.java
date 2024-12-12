package com.api.lacunaapi.controller;

import br.com.galleriabank.jwt.common.JwtUtil;
import com.api.lacunaapi.util.CommonsUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
@CrossOrigin(origins = "*")
public class Servico {

    @GetMapping()
    private String status() {
        return "LACUNA ASSINANDO TUDO - ONLINE v1.0.2";
    }

    @GetMapping("/jwt")
    public static String generateJWT() {
        if (CommonsUtil.sistemaWindows()) return JwtUtil.generateJWTServicos();
        else return null;
    }

    @GetMapping("/jwtWebhok")
    public static String generateJWTReaWebwook() {
        if (CommonsUtil.sistemaWindows()) return JwtUtil.generateJWTWebhook(false);
        else return null;
    }

}


