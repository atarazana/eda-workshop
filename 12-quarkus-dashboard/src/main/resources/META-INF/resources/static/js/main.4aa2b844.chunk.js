(this.webpackJsonpwebapp=this.webpackJsonpwebapp||[]).push([[0],{12:function(e,t,c){},15:function(e,t,c){"use strict";c.r(t);var n=c(1),r=c.n(n),a=c(3),i=c.n(a),l=(c(12),c(13),c(4)),o=c(5),s=c(7),j=c(6),b=c(0),u=function(e){var t=e.particles;return Object(b.jsxs)("div",{children:[Object(b.jsx)("center",{children:Object(b.jsx)("h1",{children:"Particles List"})}),Object(b.jsxs)("table",{class:"pf-c-table pf-m-grid-md",role:"grid","aria-label":"Supersonic Subatomic Particles",id:"table-basic",children:[Object(b.jsx)("caption",{children:"Supersonic Subatomic Particles"}),Object(b.jsx)("thead",{children:Object(b.jsx)("tr",{role:"row",children:Object(b.jsx)("th",{role:"columnheader",scope:"col",children:"Name"})})}),t.map((function(e){return Object(b.jsx)("tbody",{role:"rowgroup",children:Object(b.jsx)("tr",{role:"row",children:Object(b.jsx)("td",{role:"cell","data-label":"Particle name",children:e.name})})})}))]})]})},h=function(e){Object(s.a)(c,e);var t=Object(j.a)(c);function c(){var e;Object(l.a)(this,c);for(var n=arguments.length,r=new Array(n),a=0;a<n;a++)r[a]=arguments[a];return(e=t.call.apply(t,[this].concat(r))).state={particles:[]},e}return Object(o.a)(c,[{key:"componentDidMount",value:function(){var e=this;fetch("/particles").then((function(e){return e.json()})).then((function(t){e.setState({particles:t})})).catch(console.log)}},{key:"render",value:function(){return Object(b.jsx)(u,{particles:this.state.particles})}}]),c}(n.Component),d=function(e){e&&e instanceof Function&&c.e(3).then(c.bind(null,16)).then((function(t){var c=t.getCLS,n=t.getFID,r=t.getFCP,a=t.getLCP,i=t.getTTFB;c(e),n(e),r(e),a(e),i(e)}))};i.a.render(Object(b.jsx)(r.a.StrictMode,{children:Object(b.jsx)(h,{})}),document.getElementById("root")),d()}},[[15,1,2]]]);
//# sourceMappingURL=main.4aa2b844.chunk.js.map