import { css, html, LitElement } from "lit";
import { WordsService } from "../../services/words-service.js";
import { repeat } from "lit/directives/repeat.js";
import { WordsModel } from "./words-model.js";
import { when } from "lit/directives/when.js";

export class WordsList extends LitElement {
  static get properties() {
    return {
      currentUser: { type: Object },
      model: { type: Array, state: true },
    };
  }

  constructor() {
    super();
    this.model = new WordsModel();
    this.currentUser = {};
    this.wordsService = new WordsService();
  }

  #refresh() {
    return this.wordsService.getWords(this.currentUser).then((words) => {
      this.model = new WordsModel(words);
    });
  }

  updated(changedProperties) {
    if (changedProperties.has("currentUser")) {
      this.#refresh();
    }
  }

  deleteWord(word) {
    return () => {
      this.wordsService
        .deleteWord(this.currentUser, word)
        .then(() => this.#refresh());
    };
  }

  wordChanged(word) {
    return () => {
      this.wordsService
        .updateWord(this.currentUser, word)
        .then(() => this.#refresh());
    };
  }

  createWord(event) {
    this.wordsService
      .addWord(this.currentUser, event.word)
      .then(() => this.#refresh());
  }

  render() {
    return html`
      <h2>Words</h2>
      <ul>
        ${repeat(
          this.model.words,
          (word) => word.id,
          (word) =>
            html` <li>
              <words-item
                @words-check-changed=${this.wordChanged(word)}
                .word=${word}
              ></words-item>
              <span @click=${this.deleteWord(word)} class="command cancel"
                >&#10060;</span
              >
            </li>`,
        )}
        <li>
          <words-new-item @words-completed=${this.createWord}></words-new-item>
        </li>
      </ul>
    `;
  }

  static get styles() {
    return [
      css`
        ul {
          list-style: none;
        }

        .command {
          cursor: pointer;
        }
      `,
    ];
  }
}

window.customElements.define("words-list", WordsList);
