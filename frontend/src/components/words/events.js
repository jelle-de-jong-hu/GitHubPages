/* eslint max-classes-per-file: "off" */
export class WordsCheckChanged extends Event {
  constructor(word) {
    super("words-check-changed", {
      bubbles: true,
      composed: true,
      cancelable: true,
    });
    this.word = word;
  }
}

export class WordCompleted extends Event {
  constructor(word) {
    super("words-completed", {
      bubbles: true,
      composed: true,
      cancelable: true,
    });
    this.word = word;
  }
}
