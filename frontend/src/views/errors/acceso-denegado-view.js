import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
class AccesoDenegadoView extends PolymerElement {
  static get template() {
    return html`
    <h3 style="text-align: center">
      Acceso denegado
    </h3>
`;
  }

  static get is() {
    return 'acceso-denegado-view';
  }
}
customElements.define(AccesoDenegadoView.is, AccesoDenegadoView);