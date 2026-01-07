export class WordsModel {
  constructor(words = []) {
    this.words = words;
  }

  addWord(word) {
    this.words.push(word);
  }
}
