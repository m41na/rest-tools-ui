<#import "resttools.ftl" as rest/>
<#import "restrequest.ftl" as req/>
<#import "restresponse.ftl" as resp/>
<#macro restview content>
<div class="container" id="app">
    <div class="row">
        <div class="col-md-4">
            <@rest.resttools content />
        </div>

        <div class="col-md-8">
            <@req.restrequest/>

            <@resp.restresponse/>
        </div>
    </div>
</div>
</#macro>