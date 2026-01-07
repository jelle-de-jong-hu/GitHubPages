/* eslint class-methods-use-this: "off" */
// The current user is now passed as an argument, but that's quite plausible to change.
// with WordsService as a class any later changes will have less public API impact

export class WordsService {
  getWords(currentUser) {
    return fetch("/api/words", {
      headers: {
        Authorization: `Bearer ${currentUser.token}`,
        Accept: "application/json",
      },
    }).then((response) => response.json());
  }

  updateWord(currentUser, word) {
    return fetch(`/api/words/${word.id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${currentUser.token}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(word),
    });
  }

  deleteWord(currentUser, word) {
    return fetch(`/api/words/${word.word}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${currentUser.token}`,
      },
    });
  }

  addWord(currentUser, word) {
    return fetch("/api/words", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${currentUser.token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(word),
    });
  }
}
