import { css, html, LitElement } from "lit";
import { classMap } from "lit/directives/class-map.js";
import { WordsCheckChanged } from "./events.js";

export class WordsItem extends LitElement {
  static get properties() {
    return {
      word: { type: Object },
    };
  }

  constructor() {
    super();
    this.word = {};
  }

  firstUpdated(_changedProperties) {
    super.firstUpdated(_changedProperties);
  }

  checkChanged(event) {
    this.word.completed = event.target.checked;
    this.dispatchEvent(new WordsCheckChanged(this.word));
  }

  render() {
    return html`
      <span>${this.word.word}</span
      >`;
  }
}

window.customElements.define("words-item", WordsItem);
