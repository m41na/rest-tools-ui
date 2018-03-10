<#macro userslist view users current>
	<section class="container">
		<div class="row">
		    <div class="well well-sm">
		        <select class="form-control" onchange="UsersList.selectUser(this)" id="users-list">
		            <option selected hidden>Select user</option>
		            <#list users as user>
		            <option value="${user.userId}" <#if user.userId == current>selected</#if>>${user.userName}</option>
		            </#list>
		        </select>
		    </div>
	    <div class="row">
    </section>

	<script type="text/freemarker">
	  (function(){
		
		//singleton function
		 function UsersList(){};
		 
		 //select user
		 UsersList.prototype.selectUser = function(el){
			var user = $(el).val();
			submit("post", "/rws/mvc/${view}/user/" + user, [user]);
		 }
		 
		 window.UsersList = new UsersList();
	})();
	</script>
</#macro>