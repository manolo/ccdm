import {css, customElement, html, LitElement} from 'lit-element';

@customElement('empty-view')
export class EmptyViewElement extends LitElement {
  static get styles() {
    return css`
      :host {
        display: block;
      }
    `;
  }

  render() {
    return html`
      <br />
      Content placeholder
    `;
  }
}
