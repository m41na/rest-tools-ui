<#macro restrequest>
<#assign item = content.getUserEndpoint(content.currentUser, content.currentCollection, content.currentEndpoint)/>
<#assign methods = ['GET','POST','PUT','DELETE','OPTIONS','HEAD','PATCH','JSONP']>
<div class="panel panel-default">
    <style scoped>
        .sub-section{
            margin-top: 15px;
        }
        
        ul.headers-list{
            margin: 0px;
            padding: 0px;
        }
        
        ul.headers-list li{
            list-style: none;
        }
    </style>

    <div class="panel-heading">
        <h4>Requests
            <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="edit"><span class="glyphicon glyphicon-cog"></span></a></span>
            <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="edit"><span class="glyphicon glyphicon-plus"></span></a></span>
        </h4>
    </div>
    <div class="panel-body">
        <form role="form" class="form-horizontal" onsubmit="RestRequest.submitEndpoint(event);return false;">
            <div class="form-group">
                <label class="col-sm-3 control-label" for="url">URL</label>
                <div class="col-sm-9"><input name="url" class="form-control" id="url" placeholder="https://localhost:8081/rws/mvn/rest/endpoint" type="text" value="${item.url}"></div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label" for="method">Method</label>
                <div class="col-sm-6">
                    <select class="form-control" name="method" onchange="RestRequest.notifyChange(this)">
                        <#list methods as method >
                        <option <#if item.method?matches(method, 'i')>selected</#if>>${method}</option>
                        </#list>
                    </select>
                </div>
                <div class="col-sm-3"><button type="submit" class="btn btn-primary">Send Request</button></div>
            </div>

            <label class="col-sm-12 sub-section" for="description-input" onclick="RestRequest.toggleInputs(this)"><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span> Description</label>
            <div style="display:none;" class="form-group" id="description-input">
                <div class="col-sm-12">
                    <textarea class="form-control" name="description" rows="10"></textarea>
                </div>
            </div>

            <label class="col-sm-12 sub-section" for="headers-input" onclick="RestRequest.toggleInputs(this)"><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span> Headers</label>
            <div style="display:none;" class="form-group" id="headers-input">           
                <#if item.headers?size gt 0>
                <ul class="headers-list">
                <#list item.headers?keys as key>     
                    <li class="headers-list-item">
                        <div class="col-sm-5">
                            <input type="text" class="form-control" name="headerName" value="${key}" placeholder="Name">
                        </div>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="headerValue" value="${item.headers[key]}" placeholder="Value">
                        </div>
                        <div class="col-sm-1">
                            <button type="button" class="btn btn-danger btn-sm" aria-label="Left Align" onclick="RestRequest.removeHeader(this)">
                                <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                            </button>
                        </div>
                        <div class="col-sm-12">&nbsp;</div>
                    </li>
                </#list>
                </ul>
                </#if>

                <div class="col-sm-12">
                    <button type="button" class="btn btn-default btn-sm" aria-label="Left Align" style="border:none;" onclick="RestRequest.addHeader()">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;&nbsp;Add Header
                    </button>
                </div>
            </div>

            <label class="col-sm-12 sub-section" for="basic-auth-input" onclick="RestRequest.toggleInputs(this)"><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span> Basic Auth</label>
            <div style="display:none;" class="form-group" id="basic-auth-input">
                <div class="col-sm-6">
                    <input type="text" class="form-control" id="basic-username" placeholder="Username">
                    </div>
                <div class="col-sm-6">
                    <input type="password" class="form-control" id="basic-password" placeholder="Password">
                    </div>
                </div>

            <div class="request-body-container" style="display:none;">
                <label class="col-sm-12 sub-section" for="request-body-input"><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span> Request Body</label>
                <div class="form-group" id="request-body-input">
                    <div class="col-sm-12">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox"> Use form data
                            </label>
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <textarea class="form-control" rows="10" name="endpoint"></textarea>
                    </div>
                </div>
            </div>

            <div style="display:none;" class="form-group" id="add-param-input">
                <div class="col-sm-12">
                    <button type="button" class="btn btn-default btn-sm" aria-label="Left Align" style="border:none;margin-top:15px;">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;&nbsp;Add Parameter
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
 (function(){
	
       //singleton function
        function RestRequest(){};
	 
        //select tasks list
        RestRequest.prototype.toggleInputs = function(el){
            var selector = $(el).attr('for');
            $("#" + selector).toggle();
            $('span', el).toggleClass('glyphicon-chevron-up glyphicon-chevron-down');
        }
            
        RestRequest.prototype.addHeader = function(){
            $("ul li.headers-list-item:first").clone().appendTo("ul.headers-list");
        }
            
        RestRequest.prototype.removeHeader = function(el){
            $(el).closest("li").remove();
        }
            
        RestRequest.prototype.notifyChange = function(el){
            $(".request-body-container").toggle($(el).val() == 'POST')
        }
            
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
        }
	 
        window.RestRequest = new RestRequest();
})();
    </script>
</#macro>