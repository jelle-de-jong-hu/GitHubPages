package nl.hu.s4.project.words.presentation;

import nl.hu.s4.project.words.application.WordService;
import nl.hu.s4.project.words.domain.exception.WordLengthNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/words")
public class WordController {
    private final WordService service;

    public WordController(WordService service) {
        this.service = service;
    }

    @GetMapping
    public List<WordResult> getWords() {
        return service.getWords().stream().map(WordResult::new).toList();
    }

    @GetMapping("/random")
    public WordResult getRandomWord(@RequestParam Integer length) {
        try {
            String word = service.provideRandomWord(length);
            return new WordResult(word);
        } catch (WordLengthNotSupportedException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @DeleteMapping("/{word}")
    public void deleteWord(@PathVariable String word) {
        service.deleteWord(word);
    }
}
