<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:import url="/WEB-INF/jsp/Common/header.jsp" />

<style>
 html{

   background-image: url("../img/potholeroad.png"); 
  

}
</style>

<script type="text/javascript">
	$(document).ready(function () {
	
		$("form").validate({
			
			rules : {
				userName : {
					required : true
				},
				password : {
					required : true
				}
			},
			messages : {			
				confirmPassword : {
					equalTo : "Passwords do not match"
				}
			},
			errorClass : "error"
		});
	});
</script>

<div class="row">
	<div class="col-sm-4"></div>
	<div class="col-sm-4">
		<c:url var="formAction" value="/Users/login" />
		<form method="POST" action="${formAction}">
<%-- 		<input type="hidden" name="destination" value="${param.destination}"/>
		<input type="hidden" name="CSRF_TOKEN" value="${CSRF_TOKEN}"/>
		
 --%>	
 <div id="errorMessage">${UserNotInTheDataBase}</div>
 		<div class="form-group">
				<label for="userName">User Name: </label>
				<input type="text" id="userName" name="userName" placeHolder="User Name" class="form-control" />
			</div>
			<div class="form-group">
				<label for="password">Password: (Must contain 6 characters)</label>
				<input type="password" id="password" name="password" placeHolder="Password" class="form-control" />
			</div>
			<button type="submit" class="btn btn-primary">Login</button>
		</form>
	</div>
	<div class="col-sm-4"></div>
</div>
<c:import url="/WEB-INF/jsp/Common/footer.jsp" />