//load scripts from templates
var codez = document.querySelectorAll('script[type="text/freemarker"]');
for (var i = 0; i < codez.length; i++) {
    code = codez[i].innerHTML;
    var tag = document.createElement("script");
    tag.innerHTML = code;
    document.head.appendChild(tag);
}

//post request
function submit(method, path, parameters) {
    var form = $('<form></form>');

    form.attr("method", method);
    form.attr("action", path);

    $.each(parameters, function (key, value) {
        var field = $('<input></input>');

        field.attr("type", "hidden");
        field.attr("name", key);
        field.attr("value", value);

        form.append(field);
    });

    // The form needs to be a part of the document in
    // order for us to be able to submit it.
    $(document.body).append(form);
    form.submit();
}

(function () {

    //event bus
    function EventBus() {

        this._listeners = {};
    }

    //select tasks list
    EventBus.prototype.register = function (event, target, callback) {
        if(this._listeners[event] === undefined) {
            this._listeners[event] = [];
        }
        this._listeners[event].push({target: target, onEvent: callback});
    };

    EventBus.prototype.deregister = function (event, target) {
        var i = this._listeners[event].findIndex(function(e){
            return e.target === target;
        });
        this._listeners[event].splice(i, 1);
    };

    EventBus.prototype.trigger = function (event, target, data) {
        if(this._listeners[event] !== undefined) {
            this._listeners[event].forEach(function(e){
                if(e.target && e.target === target){
                    e.onEvent.call(window, {target: e.target, data: data});
                }
            });
        }
    };

    window.EventBus = new EventBus();
})();