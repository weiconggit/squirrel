<template>
	<ul class="nav">
		<li class="nav-item">
			<div class="d-flex sidebar-profile">
				<div class="sidebar-profile-image">
					<img src="static/picture/face29.png" alt="image">
					<span class="sidebar-status-indicator"></span>
				</div>
				<div class="sidebar-profile-name">
					<!-- TODO 展示登录用户 -->
					<p class="sidebar-name">
						Kenneth Osborne
					</p>
					<p class="sidebar-designation">
						欢迎
					</p>
				</div>
			</div>
			<div class="nav-search">
				<div class="input-group">
					<input type="text" class="form-control" placeholder="搜索功能..." aria-label="search"
						aria-describedby="search">
					<div class="input-group-append">
						<span class="input-group-text" id="search">
							<i class="typcn typcn-zoom"></i>
						</span>
					</div>
				</div>
			</div>
			<!-- 2022年11月30日 暂时不用 -->
			<!-- <p class="sidebar-menu-title">Dash menu</p> -->
		</li>
		<li class="nav-item">
			<a class="nav-link" href="">
				<i class="typcn typcn-device-desktop menu-icon"></i>
				<span class="menu-title">主页 Dashboard <span class="badge badge-primary ml-3">3</span></span>
			</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" data-toggle="collapse" href="#ui-basic" aria-expanded="false" aria-controls="ui-basic">
				<i class="typcn typcn-briefcase menu-icon"></i>
				<span class="menu-title">UI Elements</span>
				<i class="typcn typcn-chevron-right menu-arrow"></i>
			</a>
			<div class="collapse" id="ui-basic">
				<ul class="nav flex-column sub-menu">
					<li class="nav-item"> <a class="nav-link" href="buttons.html">Buttons</a></li>
					<li class="nav-item"> <a class="nav-link" href="dropdowns.html">Dropdowns</a></li>
					<li class="nav-item"> <a class="nav-link" href="typography.html">Typography</a></li>
				</ul>
			</div>
		</li>
		<li class="nav-item">
			<a class="nav-link" data-toggle="collapse" href="#form-elements" aria-expanded="false"
				aria-controls="form-elements">
				<i class="typcn typcn-film menu-icon"></i>
				<span class="menu-title">Form elements</span>
				<i class="menu-arrow"></i>
			</a>
			<div class="collapse" id="form-elements">
				<ul class="nav flex-column sub-menu">
					<li class="nav-item"><a class="nav-link" href="basic_elements.html">Basic Elements</a></li>
				</ul>
			</div>
		</li>
		<li class="nav-item">
			<a class="nav-link" data-toggle="collapse" href="#charts" aria-expanded="false" aria-controls="charts">
				<i class="typcn typcn-chart-pie-outline menu-icon"></i>
				<span class="menu-title">Charts</span>
				<i class="menu-arrow"></i>
			</a>
			<div class="collapse" id="charts">
				<ul class="nav flex-column sub-menu">
					<li class="nav-item"> <a class="nav-link" href="chartjs.html">ChartJs</a></li>
				</ul>
			</div>
		</li>
		<li class="nav-item">
			<a class="nav-link" data-toggle="collapse" href="#tables" aria-expanded="false" aria-controls="tables">
				<i class="typcn typcn-th-small-outline menu-icon"></i>
				<span class="menu-title">Tables</span>
				<i class="menu-arrow"></i>
			</a>
			<div class="collapse" id="tables">
				<ul class="nav flex-column sub-menu">
					<li class="nav-item"> <a class="nav-link" href="basic-table.html">Basic table</a></li>
				</ul>
			</div>
		</li>
		<li class="nav-item">
			<a class="nav-link" data-toggle="collapse" href="#icons" aria-expanded="false" aria-controls="icons">
				<i class="typcn typcn-compass menu-icon"></i>
				<span class="menu-title">Icons</span>
				<i class="menu-arrow"></i>
			</a>
			<div class="collapse" id="icons">
				<ul class="nav flex-column sub-menu">
					<li class="nav-item"> <a class="nav-link" href="mdi.html">Mdi icons</a></li>
				</ul>
			</div>
		</li>
		<li class="nav-item">
			<a class="nav-link" data-toggle="collapse" href="#auth" aria-expanded="false" aria-controls="auth">
				<i class="typcn typcn-user-add-outline menu-icon"></i>
				<span class="menu-title">User Pages</span>
				<i class="menu-arrow"></i>
			</a>
			<div class="collapse" id="auth">
				<ul class="nav flex-column sub-menu">
					<li class="nav-item"> <a class="nav-link" href="login.html"> Login </a></li>
					<li class="nav-item"> <a class="nav-link" href="register.html"> Register </a></li>
				</ul>
			</div>
		</li>
		<li class="nav-item">
			<a class="nav-link" data-toggle="collapse" href="#error" aria-expanded="false" aria-controls="error">
				<i class="typcn typcn-globe-outline menu-icon"></i>
				<span class="menu-title">Error pages</span>
				<i class="menu-arrow"></i>
			</a>
			<div class="collapse" id="error">
				<ul class="nav flex-column sub-menu">
					<li class="nav-item"> <a class="nav-link" href="error-404.html"> 404 </a></li>
					<li class="nav-item"> <a class="nav-link" href="error-500.html"> 500 </a></li>
				</ul>
			</div>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="documentation.html">
				<i class="typcn typcn-document-text menu-icon"></i>
				<span class="menu-title">Documentation</span>
			</a>
		</li>
	</ul>
</template>

<script>
	module.exports = {
	   data: function() {
	       return {
	           
	       }
	   }
	}
</script>

<style>
</style>
