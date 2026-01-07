package nl.hu.s4.project.words.application;

import jakarta.transaction.Transactional;
import nl.hu.s4.project.words.data.WordRepository;
import nl.hu.s4.project.words.domain.Word;
import nl.hu.s4.project.words.domain.exception.WordLengthNotSupportedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public String provideRandomWord(Integer length) {
        return wordRepository
                .findRandomWordByLength(length)
                .orElseThrow(() -> new WordLengthNotSupportedException(length))
                .getValue();
    }

    public boolean wordExists(String word) {
        return wordRepository.existsById(word);
    }

    public List<String> getWords() {
        List<Word> all = wordRepository.findAll();
        return all.stream().map(Word::getValue).toList();
    }

    public void deleteWord(String word) {
        wordRepository.deleteById(word);
    }
}
