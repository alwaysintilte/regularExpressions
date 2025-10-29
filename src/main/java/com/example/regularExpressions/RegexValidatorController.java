package com.example.regularExpressions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RegexValidatorController {
    @Autowired
    private RegexParser regexParser;
    @PostMapping("/validate")
    public Result validateRegex(@RequestBody ValidationRequest request) {
        return regexParser.validateAndMatch(request.getPattern(), request.getText());
    }
}
