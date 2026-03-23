package my.project.vocab.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import my.project.vocab.domain.Word;
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
    @RequestMapping("/add")
    public String addWord(Model model) {
        model.addAttribute("word", new Word());
        return "addword";
    }
    // CREATE - save
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Word word) {
        repository.save(word);
        return "redirect:/wordlist";
    }

    // DELETE
    @RequestMapping("/delete/{id}")
    public String deleteWord(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return "redirect:/wordlist";
    }

    // UPDATE - show edit form
    @RequestMapping("/edit/{id}")
    public String editWord(@PathVariable("id") Long id, Model model) {
        model.addAttribute("word", repository.findById(id).orElse(null));
        return "editword";
    }

  
}
