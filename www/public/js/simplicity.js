//load scripts from templates
var codez = document.querySelectorAll('script[type="text/freemarker"]');
for (var i = 0; i < codez.length; i++) {
    code = codez[i].innerHTML;
    var tag = document.createElement("script");
    tag.innerHTML = code;
    document.head.appendChild(tag);
}

(function () {

    //EventBus object
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

    window.EventBus = _eventBus = new EventBus();
    
    //RestTool object
    var RestTools = {
        ENDPOINT_SELECTED : "ep-selected",
        ENDPOINT_SUBMITTED : "ep-submitted",
        EDNPOINT_UPDATED : "ep-updated",
        ENDPOINT_CREATED : "ep-created",
        ENDPOINT_DROPPED : "ep-deleted",
        COLLECTION_SELECTED : "ep-list-selected",
        COLLECTION_CREATED : "ep-list-created",
        COLLECTION_UPDATED : "ep-list-updated",
        COLLECTION_DROPPED : "ep-list-deleted",
        
        mode : 'normal',
        data : window.pagestore,
        endpoint : {},
        collection : {},
        eventBus : bus
    }
        
    function selectCollection(event){
        var val = event.target.value;
        this.collection = this.data.model.collections.find(e=>e.collectionId===val);
        if(this.collection,endpoints){
            this.endpoint = this.collection.endpoints[0];
            this.eventBus.trigger(this.COLLECTION_SELECTED, this.collection);
            this.eventBus.trigger(this.ENDPOINT_SELECTED, this.endpoint);
        }
    }

    function showCreate(e){
        e.preventDefault();
        var mode = 'create';
        submit("post", "/rws/mvc/rest/mode/" + mode, [mode]);
     }

    function cancelCreate(e){
        e.preventDefault();
        var mode = 'normal';
        submit("post", "/rws/mvc/rest/mode/" + mode, [mode]);
    }

    function showUpdate(e){
        e.preventDefault();
        var mode = 'update';
        submit("post", "/rws/mvc/rest/mode/" + mode, [mode]);
     }

    function selectRestUser(el){
        var user = $(el).val();
        submit("post", "/rws/mvc/rest/user/" + user, [user]);
     }

    function selectCollection(el, user){
        var list = $(el).val();
        submit("post", "/rws/mvc/rest/user/" + user +"/collection/" + list, [user, list]);
     }

    function selectEndpoint(e, id){
        if(e.target.tagName === 'span'){
            alert('this will delete endpoint');
        }
        else{

        }
    }
    
    window.RestTools = _restTools = new RestTools(_eventBus);
    
    //RestRequest object
    var RestRequest = function(){
        this.name = "RestRequest";
    };

    //select tasks list
    RestRequest.prototype.toggleInputs = function(el){
        var selector = $(el).attr('for');
        $("#" + selector).toggle();
        $('span', el).toggleClass('glyphicon-chevron-up glyphicon-chevron-down');
    };

    RestRequest.prototype.addHeader = function(){
        $("ul li.headers-list-item:first").clone().appendTo("ul.headers-list");
    };

    RestRequest.prototype.removeHeader = function(el){
        $(el).closest("li").remove();
    };

    RestRequest.prototype.notifyChange = function(el){
        $(".request-body-container").toggle($(el).val() === 'POST');
    };

    RestRequest.prototype.submitRequest = function(ev){
        var form = $(ev.target);
        $.ajax({
            url: $("[name='url']", form).val(),
            type: $("[name='method']", form).val(),
            data: $("[name='endpoint']", form).val(),
            dataType: 'json',
            beforeSend: function(xhr){
                $("[name='headerName']", form).each(function(i, e){
                    var header = $(this).val();
                    var value = $("[name='headerValue']", e.closest("li")).val();
                    xhr.setRequestHeader(header, value);
                });
            },
            success: function(data, status, xhr){
                EventBus.trigger('endpoint', 'response-body', {method: $("[name='method']", form).val(), url: $("[name='url']", form).val(), status: status, data: data});
            },
            error: function(xhr, status, err){
                EventBus.trigger('endpoint', 'response-body', {method: $("[name='method']", form).val(), url: $("[name='url']", form).val(), status: status, data: err});
            }
        });
        return false;
    }

    RestRequest.prototype.submitEndpoint = function(ev){
        var form = $(ev.target);
        var endpoint = {
            url: $("[name='url']", form).val(),
            method: $("[name='method']", form).val(),
            description: $("[name='description']", form).val(),
            headers: function(){
                var result = {};
                $("[name='headerName']", form).each(function(i, e){
                    var header = $(this).val();
                    var value = $("[name='headerValue']", e.closest("li")).val();
                    if(result[header] !== undefined){
                        result[header] = result[header].concat(";").concat(value);
                    }
                    else{
                        result[header] = value;
                    }
                });    
                return result;
            }(),
            entity: $("[name='endpoint']", form).val()
        };

        $.ajax({
            url: "https://localhost:8081/rws/mvc/rest/endpoint",
            type: "POST",
            data: JSON.stringify(endpoint),
            dataType: 'json',
            headers: {
                    "Tools-Authorization-Token":"b4AtQMdJElAXPcLvanaHVQ==&&6JfuWXEmJH1dBgnO2r9d2O4F3jAB5zaf",
                    "Content-Type":"application/json",
                    "Accepts":"application/json"
            },
            success: function(data, status, xhr){
                EventBus.trigger('endpoint', 'response-body', {method: "POST", url: "https://localhost:8081/rws/mvc/rest/endpoint", status: status, data: data});
            },
            error: function(xhr, status, err){
                EventBus.trigger('endpoint', 'response-body', {method: "POST", url: "https://localhost:8081/rws/mvc/rest/endpoint", status: status, data: err});
            }
        });
        return false;
    };

    window.RestRequest = new RestRequest();
    
    //RestResponse object
    var RestResponse = function(bus){
        this.eventBus = bus;
        this.eventBus.register('endpoint', 'response-body', function(e){
        $(".response-body").html(JSON.stringify(e.data, null, "\t"));
        $(".response-body").parent().show();
    });
    };
        
    RestResponse.prototype.toggleResponse = function(el){
        $(".panel-body", $(el).closest(".panel")).toggle();
        $('span', el).toggleClass('glyphicon-chevron-up glyphicon-chevron-down');
    };

    RestResponse.prototype.clearResponse = function(el){
        $(el).closest(".panel").toggle();
    };
        
    window.RestResponse = new RestResponse(_eventBus);
    
    //fire ready event on 'app' element
    var appElement = document.getElementById("rest-view");
    if(appElement){
        var event = new Event('EventBusReady');
        appElement.dispatchEvent(event);
    }
})();