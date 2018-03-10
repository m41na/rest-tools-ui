<#macro restresponse>
<div class="panel panel-default">
<style scoped>
    .response-body{
        border: none;
        overflow: auto;
        outline: none;
        width: 100%;
        min-height: 400px;
    }
</style>
    <div class="panel-heading">
        <h4>Response - show request url
            <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="edit" onclick="RestResponse.clearResponse(this);return false;"><span class="glyphicon glyphicon-remove"></span></a></span>
        </h4>
    </div>
    <div class="panel-body">
        <div class="panel panel-default">
            <div class="panel-heading" onclick="RestResponse.toggleResponse(this)">Response Body &nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span></div>
            <div class="panel-body" style="display:none;">
                <textarea class="response-body"></textarea>
            </div>
        </div>
    </div>
</div>

<script>
 (function(){
     
    //singleton function
    function RestResponse(){};
        
    RestResponse.prototype.toggleResponse = function(el){
        $(".panel-body", $(el).closest(".panel")).toggle();
        $('span', el).toggleClass('glyphicon-chevron-up glyphicon-chevron-down');
    }

    RestResponse.prototype.clearResponse = function(el){
        $(el).closest(".panel").toggle();
    }
        
    window.RestResponse = new RestResponse();
        
    EventBus.register('endpoint', 'response-body', function(e){
        $(".response-body").html(JSON.stringify(e.data, null, "\t"));
        $(".response-body").parent().show();
    });
 })();
</script>
</#macro>