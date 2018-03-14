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
            <span class="pull-right" style="display:inline-block;padding-left:10px"><a href="/#" title="edit" onclick="(function(e){RestResponse.clearResponse(e);})(event)"><span class="glyphicon glyphicon-remove"></span></a></span>
        </h4>
    </div>
    <div class="panel-body">
        <div class="panel panel-default">
            <div class="panel-heading" onclick="(function(e){RestResponse.toggleResponse(e);})(event)">Response Body &nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span></div>
            <div class="panel-body" style="display:none;">
                <textarea class="response-body"></textarea>
            </div>
        </div>
    </div>
</div>
</#macro>