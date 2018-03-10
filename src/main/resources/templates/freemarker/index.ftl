<#import "macros/layout.ftl" as index>
<@index.layout>	

    <!-- Navigation -->
    <#import "macros/navigation.ftl" as navigate>
    <@navigate.navigation nav=nav username=username />		
		
    <!-- Page Content -->
    <#import "macros/routerview.ftl" as router>
    <@router.routerview view=view content=content />

</@index.layout>
