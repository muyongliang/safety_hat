(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-48d9ca36"],{"1c20":function(t,e,n){},"716b":function(t,e,n){"use strict";var o=n("bc69"),a=n.n(o);a.a},"948b":function(t,e,n){"use strict";var o=n("1c20"),a=n.n(o);a.a},bc69:function(t,e,n){},d504:function(t,e,n){"use strict";n.r(e);var o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"indexContainer"},[n("div",{staticClass:"top"},[n("div",[n("i",{style:{backgroundImage:"url("+t.logoUrl+")"}}),n("span",[t._v(t._s(t.systemName||"智能安全生产管控平台系统后台"))])]),n("div",[n("i",{on:{click:t.toinfo}}),n("i",{on:{click:t.toinformation}},[n("el-badge",{staticClass:"item2",attrs:{"is-dot":t.hasNews}})],1),n("i",[n("el-dropdown",[n("span",{staticClass:"el-dropdown-link"}),n("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[n("el-dropdown-item",[n("button",{staticClass:"btns",on:{click:function(e){t.changeName2=!0}}},[t._v("修改姓名")]),n("el-dialog",{attrs:{title:"修改姓名",visible:t.changeName2,"append-to-body":"",width:"30%"},on:{"update:visible":function(e){t.changeName2=e}}},[n("el-form",{attrs:{model:t.form3}},[n("el-form-item",{attrs:{label:"姓名:","label-width":t.formLabelWidth,required:""}},[n("el-input",{attrs:{autocomplete:"off",placeholder:"admin"},model:{value:t.form3.name,callback:function(e){t.$set(t.form3,"name",e)},expression:"form3.name"}})],1)],1),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(e){t.changeName2=!1}}},[t._v("取 消")]),n("el-button",{on:{click:function(e){t.changeName2=!1,t.updateName2()}}},[t._v("确 定")])],1)],1)],1),n("el-dropdown-item",[n("button",{staticClass:"btns",on:{click:function(e){t.changePassword=!0}}},[t._v("修改密码")]),n("el-dialog",{attrs:{title:"修改密码",visible:t.changePassword,"append-to-body":"",width:"30%"},on:{"update:visible":function(e){t.changePassword=e}}},[n("el-form",{attrs:{model:t.form4}},[n("el-form-item",{attrs:{label:"原密码:","label-width":t.width,required:""}},[n("el-input",{attrs:{autocomplete:"off",placeholder:"请输入原密码"},model:{value:t.form4.old,callback:function(e){t.$set(t.form4,"old",e)},expression:"form4.old"}})],1),n("el-form-item",{attrs:{label:"新密码:","label-width":t.width,required:""}},[n("el-input",{attrs:{autocomplete:"off",placeholder:"请输入新密码，6-20位，数字、字母、字符组成"},model:{value:t.form4.new,callback:function(e){t.$set(t.form4,"new",e)},expression:"form4.new"}})],1),n("el-form-item",{attrs:{label:"重复密码:","label-width":t.width,required:""}},[n("el-input",{attrs:{autocomplete:"off",placeholder:"再次输入密码"},model:{value:t.form4.again,callback:function(e){t.$set(t.form4,"again",e)},expression:"form4.again"}})],1)],1),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(e){t.changePassword=!1}}},[t._v("取 消")]),n("el-button",{attrs:{type:"primary"},on:{click:function(e){t.changePassword=!1,t.updatePassword()}}},[t._v("确 定")])],1)],1)],1),n("el-dropdown-item",[n("button",{staticClass:"btns",on:{click:t.quit}},[t._v("退出登录")])])],1)],1)],1),n("i"),n("i",[t._v(t._s(t.username))])])]),n("div",{staticClass:"centerBody"},[n("div",{staticClass:"left"},[n("div",{staticClass:"left-top"},[n("el-row",{staticClass:"tac"},[n("el-col",{attrs:{span:3}},[n("el-menu",{staticClass:"el-menu-vertical-demo",attrs:{"default-active":"2","background-color":"#011C31","text-color":"#fff","active-text-color":"#FFFFFF",router:""}},[n("el-menu-item",{attrs:{index:"/Index/company"}},[n("i",{staticClass:"el-icon-menu"}),n("span",{attrs:{slot:"title"},slot:"title"},[t._v("企业管理")])]),n("el-menu-item",{attrs:{index:"/Index/deploy"}},[n("i",{staticClass:"el-icon-lollipop"}),n("span",{attrs:{slot:"title"},slot:"title"},[t._v("独立部署授权管理")])]),n("el-submenu",{attrs:{index:"3"}},[n("template",{slot:"title"},[n("i",{staticClass:"el-icon-setting"}),n("span",[t._v("系统设置")])]),n("el-menu-item-group",[n("el-menu-item",{attrs:{index:"/Index/message"}},[t._v("官方消息管理")]),n("el-menu-item",{attrs:{index:"/Index/dictionary"}},[t._v("字典配置")]),n("el-menu-item",{attrs:{index:"/Index/update"}},[t._v("app更新设置")]),n("el-menu-item",{attrs:{index:"/Index/log"}},[t._v("操作日志")]),n("el-menu-item",{attrs:{index:"/Index/control"}},[t._v("远程控制")]),n("el-menu-item",{attrs:{index:"/Index/pageSet"}},[t._v("页面设置")]),n("el-menu-item",{attrs:{index:"/Index/otherSet"}},[t._v("其它设置")])],1)],2)],1)],1)],1)],1),t._m(0)]),n("div",{staticClass:"right"},[n("router-view")],1)])])},a=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"left-bottom"},[n("p",[t._v("当前版本 V1.0")]),n("p",[t._v("技术支持：成都四象联创科技有限公司")])])}],s=(n("8e6e"),n("ac6a"),n("456d"),n("7f7f"),n("96cf"),n("3b8d")),r=n("bd86"),i=n("2f62");function l(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(t);e&&(o=o.filter(function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable})),n.push.apply(n,o)}return n}function c(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?l(n,!0).forEach(function(e){Object(r["a"])(t,e,n[e])}):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):l(n).forEach(function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))})}return t}var u=Object(i["a"])("center"),d=u.mapState,m=u.mapActions,f={data:function(){return{changeName2:!1,form3:{name:""},formLabelWidth:"60px",changePassword:!1,form4:{old:"",new:"",again:""},width:"100px",logoUrl:"",systemName:"",hasNews:!1}},computed:c({},d(["backgroundUrl"]),{username:function(){return sessionStorage.getItem("name")}}),mounted:function(){var t=sessionStorage.getItem("setNewsFl");console.log(t),1==t&&(this.hasNews=!1),this.getLogo(),this.getNews()},methods:c({},m(["changeNameAsync","changePasswordAsync","logoMeta","newsStatusAsync"]),{quit:function(){this.$router.push("/Login")},toinfo:function(){this.$router.push("/Info")},toinformation:function(){this.$router.push("/Index/informationCenter"),this.hasNews=!1},getLogo:function(){var t=Object(s["a"])(regeneratorRuntime.mark(function t(){return regeneratorRuntime.wrap(function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,this.logoMeta();case 2:this.systemName=this.backgroundUrl.data.systemName,this.logoUrl=this.$download+this.backgroundUrl.data.logoUrl;case 4:case"end":return t.stop()}},t,this)}));function e(){return t.apply(this,arguments)}return e}(),getNews:function(){var t=Object(s["a"])(regeneratorRuntime.mark(function t(){var e;return regeneratorRuntime.wrap(function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,this.newsStatusAsync();case 2:e=t.sent,e.length>0?this.hasNews=!0:0==e.length&&(this.hasNews=!1);case 4:case"end":return t.stop()}},t,this)}));function e(){return t.apply(this,arguments)}return e}(),updateName2:function(){var t=Object(s["a"])(regeneratorRuntime.mark(function t(){var e,n;return regeneratorRuntime.wrap(function(t){while(1)switch(t.prev=t.next){case 0:return e=this.form3.name,t.next=3,this.changeNameAsync({userName:e});case 3:n=t.sent,200===n.code?(this.form3.name="",this.$message("修改成功"),this.$router.push("/Login")):202===n.code?this.$message("用户名已经存在"):this.$message("修改失败");case 5:case"end":return t.stop()}},t,this)}));function e(){return t.apply(this,arguments)}return e}(),updatePassword:function(){var t=Object(s["a"])(regeneratorRuntime.mark(function t(){var e,n,o,a;return regeneratorRuntime.wrap(function(t){while(1)switch(t.prev=t.next){case 0:return e=this.form4.old,n=this.form4.new,o=this.form4.again,t.next=5,this.changePasswordAsync({oldPwd:e,newPwd:n,verPwd:o});case 5:a=t.sent,200===a.data.code?(this.form4.old="",this.form4.new="",this.form4.again="",this.$message("修改成功"),this.$router.push("/Login")):201===a.data.code?(this.form4.old="",this.form4.new="",this.form4.again="",this.$message("三条数据不完整，请全部输入")):202===a.data.code?(this.form4.old="",this.form4.new="",this.form4.again="",this.$message("初始密码错误，请重新输入")):203===a.data.code?(this.form4.old="",this.form4.new="",this.form4.again="",this.$message("两次输入的密码不一致，请重新输入")):204===a.data.code&&(this.form4.old="",this.form4.new="",this.form4.again="",this.$message("密码格式错误，请重新输入"));case 7:case"end":return t.stop()}},t,this)}));function e(){return t.apply(this,arguments)}return e}()})},h=f,p=(n("948b"),n("716b"),n("2877")),g=Object(p["a"])(h,o,a,!1,null,"8efddcde",null);e["default"]=g.exports}}]);
//# sourceMappingURL=chunk-48d9ca36.cd75f0d9.js.map