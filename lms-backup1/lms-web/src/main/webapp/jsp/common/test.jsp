<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <script src="/static/bower_components/polymer-quill/quill.js"></script>
  <link href="//cdn.quilljs.com/1.3.2/quill.snow.css" rel="stylesheet">
</head>
<body>
<div id="toolbar-container">
  <span class="ql-formats">
    <select class="ql-size">
      <option value="10px">Small</option>
      <option selected>Normal</option>
      <option value="18px">Large</option>
      <option value="32px">Huge</option>
    </select>
  </span>
  <span class="ql-formats">
    <select class="ql-color">
      <option selected></option>
      <option value="red"></option>
      <option value="orange"></option>
      <option value="yellow"></option>
      <option value="green"></option>
      <option value="blue"></option>
      <option value="purple"></option>
    </select>
    <select class="ql-background">
      <option selected></option>
      <option value="red"></option>
      <option value="orange"></option>
      <option value="yellow"></option>
      <option value="green"></option>
      <option value="blue"></option>
      <option value="purple"></option>
    </select>
  </span>
</div>
<div id="editor-container">
  <p>Quill uses classes for most inline styles.</p>
  <p><br></p>
  <p>The exception is <span class="ql-bg-yellow">background</span> and <span class="ql-color-purple">color</span>,</p>
  <p>where it uses inline styles.</p>
  <p><br></p>
  <p>This <span style="font-size: 18px">demo</span> shows how to <span style="font-size: 32px">change</span> this.</p>
</div>
<script>
var BackgroundClass = Quill.import('attributors/class/background');
var ColorClass = Quill.import('attributors/class/color');
var SizeStyle = Quill.import('attributors/style/size');
Quill.register(BackgroundClass, true);
Quill.register(ColorClass, true);
Quill.register(SizeStyle, true);

var quill = new Quill('#editor-container', {
  modules: {
    toolbar: '#toolbar-container'
  },
  placeholder: 'Compose an epic...',
  theme: 'snow'
});
</script>
</body>
</html>
