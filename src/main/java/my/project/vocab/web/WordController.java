package my.project.vocab.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import my.project.vocab.domain.WordRepository;

@Controller
public class WordController {
    @Autowired
    private WordRepository repository; 
    
    @RequestMapping(value= {"/", "/wordlist"})
    public String wordList(Model model) {    
        model.addAttribute("words", repository.findAll());
        return "wordList";
    }
  
}
