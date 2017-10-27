Following changes have been made to bower_components
----------------------------------------------------

1. Following changes have been made to quill.js
  var listener = function listener(e) {
    <<<<<<< HEAD
    if (!document.body.contains(quill.root)) {
    =======
    if (document !== quill.root.ownerDocument) {
    >>>>>>>
      return document.body.removeEventListener('click', listener);
    }
    if (_this.tooltip != null && !_this.tooltip.root.contains(e.target) && document.activeElement !== _this.tooltip.textbox && !_this.quill.hasFocus()) {
      _this.tooltip.hide();
    }
    if (_this.pickers != null) {
      var target = e.target.shadowRoot && (e.path && e.path[0]) || e.target;
      _this.pickers.forEach(function (picker) {
        <<<<<<< HEAD
        if (!picker.container.contains(e.target)) {
        =======
        if (!picker.container.contains(target)) {
        >>>>>>>
          picker.close();
        }
      });
    }
  };

2. Add following code to iron-overlay-manager to close all overlay on document click
  Polymer.IronOverlayManagerClass.prototype.__onCaptureClick
    = Polymer.IronOverlayManagerClass.prototype._onCaptureClick;
  Polymer.IronOverlayManagerClass.prototype._onCaptureClick = function(event) {
    if (this._overlays.length > 0) {
      let overlays = this._overlays.filter(function(overlay) {
        let path = Polymer.dom(event).path;
        return this._overlayInPath(path) === overlay;
      }.bind(this));

      if (overlays.length === 0) {
        this._overlays.forEach(function(overlay) {
          overlay._onCaptureClick(event);
        });
      } else {
        this.__onCaptureClick(event);
      }
    }
  };