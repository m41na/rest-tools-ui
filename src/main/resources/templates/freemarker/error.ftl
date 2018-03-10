<#import "macros/layout.ftl" as index>
<@index.layout>	
	 
	<!-- Navigation -->
	<#import "macros/navigation.ftl" as navigate>
	<@navigate.navigation nav=nav/>		
		
    <!-- Page Content -->
    <h1>${error}</h1>

</@index.layout>