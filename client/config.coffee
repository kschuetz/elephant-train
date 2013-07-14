exports.config =
  # See http://brunch.io/#documentation for docs.
  paths:
    public:   '../public'
    
  conventions:
    ignored: /^vendor(\/|\\)styles(\/|\\)_bootstrap/
  
  
    
  files:
    javascripts:
      joinTo: 'javascripts/app.js'
    stylesheets:
      joinTo: 'stylesheets/main.css'
      order:
        before: ['vendor/styles/style.less']
    templates:
      joinTo: 'app.js'
