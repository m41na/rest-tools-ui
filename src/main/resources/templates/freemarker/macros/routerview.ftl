<#macro routerview view content>
    <#if view=='rest'>
        <#import "restview.ftl" as rest/>
        <@rest.restview content=content/>
    <#elseif view=='notes'>
        <#import "notesview.ftl" as notes/>
        <@notes.notesview content=content/>
    <#else>
        <#import "restview.ftl" as default/>
        <@default.restview content=content/>
    </#if>
</#macro>