package nl.hu.s4.project.words.application;

import jakarta.transaction.Transactional;
import nl.hu.s4.project.words.data.WordRepository;
import nl.hu.s4.project.words.domain.exception.WordLengthNotSupportedException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public String provideRandomWord(Integer length) {
        return this.wordRepository
                .findRandomWordByLength(length)
                .orElseThrow(() -> new WordLengthNotSupportedException(length))
                .getValue();
    }

    public boolean wordExists(String word) {
        return this.wordRepository.existsById(word);
    }
}
