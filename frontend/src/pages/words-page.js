import { css, html, LitElement } from "lit";

export class WordsPage extends LitElement {
  static get styles() {
    return css``;
  }

  render() {
    return html` Words`;
  }
}

window.customElements.define("words-page", WordsPage);
