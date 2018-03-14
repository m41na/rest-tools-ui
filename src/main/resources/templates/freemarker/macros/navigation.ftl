<#import "routerlink.ftl" as rl>
<#macro navigation nav username>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand" href="/">${nav.title}</a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
            	<#list nav.links as link>
                    <@rl.routerlink link=link/>
                </#list>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <#if !username??>
                <li><a href="/signin">Sign In</a></li>
                </#if>

                <#if username?? >
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Hi ${username} <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <#list nav.links as link>
                            <@rl.routerlink link=link/>
                        </#list>
                        <li role="separator" class="divider"></li>
                        <li><a href="/signout">Sign Out</a></li>
                        </ul>
                    </li>
                </#if>
                </ul>

            <form class="navbar-form navbar-right">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Search">
                    </div>
                <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>
</#macro>