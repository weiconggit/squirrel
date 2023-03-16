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
		</li>

		<li class="nav-item">
			<a class="nav-link" href="">
				<i class="typcn typcn-device-desktop menu-icon"></i>
				<span class="menu-title"> squirrel 主页  <span class="badge badge-primary ml-3">3</span></span>
			</a>
		</li>

		<div v-for="menu1 in menus" :key="menu1.menu">
		<li class="nav-item">
			<a class="nav-link" data-toggle="collapse" href="#ui-basic" aria-expanded="false" aria-controls="ui-basic">
				<i class="typcn typcn-briefcase menu-icon"></i>
				<span class="menu-title"> {{menu.name}} </span>
				<i class="typcn typcn-chevron-right menu-arrow"></i>
			</a>
			<div class="collapse" id="ui-basic" v-for="subMenu1 in subMenus" :key="subMenu1.subMenu">
				<ul class="nav flex-column sub-menu">
					<li class="nav-item"> <a class="nav-link" href="buttons.html">{{subMenu.subMenu}}</a></li>
					<li class="nav-item"> <a class="nav-link" href="dropdowns.html">Dropdowns</a></li>
					<li class="nav-item"> <a class="nav-link" href="typography.html">Typography</a></li>
				</ul>
			</div>
		</li>
		</div>

	</ul>
</template>

<script>
	module.exports = {
	   data: function() {
	       return {
			   menus: [
				   { menu: {name:"菜单1",subMenus:[{subMenu:"子菜单1"}, {subMenu:"子菜单2"}, {subMenu:"子菜单3"}]} },
				   { menu: {name:"子菜单1"} }
			   ]
	       }
	   }
	}
</script>

<style>
</style>
