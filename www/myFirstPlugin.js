var exec = require('cordova/exec');

var alert = function(arg0, success, error) {
    exec(success, error, "myFirstPlugin", "showAlert", [arg0]);
};
window.plugins = window.plugins || {};
window.plugins.myFirstPlugin = {showAlert:alert};

