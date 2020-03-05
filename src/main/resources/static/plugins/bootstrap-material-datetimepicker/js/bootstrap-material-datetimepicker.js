/*
 * autor: Miller Augusto S. Martins
 * e-mail: miller.augusto@gmail.com
 * github: miamarti
 * */
(function(k,m,e,f){function l(a,b){this.currentView=0;this.maxDate=this.minDate=void 0;this._attachedEvents=[];this.element=a;this.$element=e(a);this.params={date:!0,time:!0,format:"YYYY-MM-DD",minDate:null,maxDate:null,currentDate:null,lang:"en",weekStart:0,disabledDays:[],shortTime:!0,clearButton:!0,nowButton:!1,cancelText:"Cancel",okText:"OK",clearText:"Clear",nowText:"Now",switchOnClick:!1,triggerEvent:"click",monthPicker:!1,year:!0};this.params=e.fn.extend(this.params,b);this.name="dtp_"+this.setName();
this.$element.attr("data-dtp",this.name);f.locale(this.params.lang);this.init()}f.locale("en");e.fn.bootstrapMaterialDatePicker=function(a,b){this.each(function(){if(e.data(this,"plugin_bootstrapMaterialDatePicker")){if("function"===typeof e.data(this,"plugin_bootstrapMaterialDatePicker")[a])e.data(this,"plugin_bootstrapMaterialDatePicker")[a](b);"destroy"===a&&e(this).removeData("plugin_bootstrapMaterialDatePicker")}else e.data(this,"plugin_bootstrapMaterialDatePicker",new l(this,a))});return this};
l.prototype={init:function(){this.initDays();this.initDates();this.initTemplate();this.initButtons();this._attachEvent(e(k),"resize",this._centerBox.bind(this));this._attachEvent(this.$dtpElement.find(".dtp-content"),"click",this._onElementClick.bind(this));this._attachEvent(this.$dtpElement,"click",this._onBackgroundClick.bind(this));this._attachEvent(this.$dtpElement.find(".dtp-close > a"),"click",this._onCloseClick.bind(this));this._attachEvent(this.$element,this.params.triggerEvent,this._fireCalendar.bind(this))},
initDays:function(){this.days=[];for(var a=this.params.weekStart;7>this.days.length;a++)6<a&&(a=0),this.days.push(a.toString())},initDates:function(){if(0<this.$element.val().length)this.currentDate="undefined"!==typeof this.params.format&&null!==this.params.format?f(this.$element.val(),this.params.format).locale(this.params.lang):f(this.$element.val()).locale(this.params.lang);else if("undefined"!==typeof this.$element.attr("value")&&null!==this.$element.attr("value")&&""!==this.$element.attr("value"))"string"===
typeof this.$element.attr("value")&&(this.currentDate="undefined"!==typeof this.params.format&&null!==this.params.format?f(this.$element.attr("value"),this.params.format).locale(this.params.lang):f(this.$element.attr("value")).locale(this.params.lang));else if("undefined"!==typeof this.params.currentDate&&null!==this.params.currentDate){if("string"===typeof this.params.currentDate)this.currentDate="undefined"!==typeof this.params.format&&null!==this.params.format?f(this.params.currentDate,this.params.format).locale(this.params.lang):
f(this.params.currentDate).locale(this.params.lang);else if("undefined"===typeof this.params.currentDate.isValid||"function"!==typeof this.params.currentDate.isValid){var a=this.params.currentDate.getTime();this.currentDate=f(a,"x").locale(this.params.lang)}else this.currentDate=this.params.currentDate;this.$element.val(this.currentDate.format(this.params.format))}else this.currentDate=f();"undefined"!==typeof this.params.minDate&&null!==this.params.minDate?"string"===typeof this.params.minDate?this.minDate=
"undefined"!==typeof this.params.format&&null!==this.params.format?f(this.params.minDate,this.params.format).locale(this.params.lang):f(this.params.minDate).locale(this.params.lang):"undefined"===typeof this.params.minDate.isValid||"function"!==typeof this.params.minDate.isValid?(a=this.params.minDate.getTime(),this.minDate=f(a,"x").locale(this.params.lang)):this.minDate=this.params.minDate:null===this.params.minDate&&(this.minDate=null);"undefined"!==typeof this.params.maxDate&&null!==this.params.maxDate?
"string"===typeof this.params.maxDate?this.maxDate="undefined"!==typeof this.params.format&&null!==this.params.format?f(this.params.maxDate,this.params.format).locale(this.params.lang):f(this.params.maxDate).locale(this.params.lang):"undefined"===typeof this.params.maxDate.isValid||"function"!==typeof this.params.maxDate.isValid?(a=this.params.maxDate.getTime(),this.maxDate=f(a,"x").locale(this.params.lang)):this.maxDate=this.params.maxDate:null===this.params.maxDate&&(this.maxDate=null);this.isAfterMinDate(this.currentDate)||
(this.currentDate=f(this.minDate));this.isBeforeMaxDate(this.currentDate)||(this.currentDate=f(this.maxDate))},initTemplate:function(){for(var a="",b=this.currentDate.year(),c=b-3;c<b+4;c++)a+='<div class="year-picker-item" data-year="'+c+'">'+c+"</div>";this.midYear=b;this.template='<div class="dtp hidden" id="'+this.name+'"><div class="dtp-content"><div class="dtp-date-view"><header class="dtp-header"><div class="dtp-actual-day">Lundi</div><div class="dtp-close"><a href="javascript:void(0);"><i class="material-icons">clear</i></a></div></header><div class="dtp-date hidden"><div><div class="left center p10"><a href="javascript:void(0);" class="dtp-select-month-before"><i class="material-icons">chevron_left</i></a></div><div class="dtp-actual-month p80">MAR</div><div class="right center p10"><a href="javascript:void(0);" class="dtp-select-month-after"><i class="material-icons">chevron_right</i></a></div><div class="clearfix"></div></div><div class="dtp-actual-num">13</div><div><img class="dtp-btn-calendar-ico" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAABIAAAASABGyWs+AAAACXZwQWcAAAAYAAAAGAB4TKWmAAAAe0lEQVRIx+1SwQ2AIBDrGceQhdjDuDewR/1IQhBiCILB0Gcf7fVa4DcgqUka1sOQ1F5XAgMLYHvpXiciKk5Akkwkq+KX1q8f3yAsmTVCN2ER6ZJgzTnHyUp5j/FLnit6xFzR9wZhBxaAyq2pkHepBPtlUgsL4Gj9mX44AS9ro3738HcBAAAAAElFTkSuQmCC"><div class="left center p10"><a href="javascript:void(0);" class="dtp-select-year-before"><i class="material-icons">chevron_left</i></a></div><div class="dtp-actual-year p80'+
(this.params.year?"":" disabled")+'">2014</div><div class="right center p10"><a href="javascript:void(0);" class="dtp-select-year-after"><i class="material-icons">chevron_right</i></a></div><img class="dtp-btn-clock-ico" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAABIAAAASABGyWs+AAAACXZwQWcAAAAYAAAAGAB4TKWmAAABVklEQVRIx9WVvUoDQRDH/3shARHE1sI0lqkUHyGFlckF8wC2sTCPYCem8QnSaS1i6UMomC6ptNAnECLIz2YWN0fuSy9FBo6Bnf/H3rC7I617uKwi0JDUkRRL2pe0a6U3SU+S7iTdO+e+SjsDPWBKfkyBXhnhGjAKBCbAOdACNu1r2dokwI2AWhEDLz4HzoAoAxsZZu5N8sTjQLxd4q/bgUmcBqoDMwMNUjAAlym1gdVnQH0ZoG+Al7Re5hjUjAvQ9+thf7uWx86576Lt8WGccUJrweDQ8mNZ8SA812stGOxYfv2Hged6LUV/EDkCWkXBocG75WYG/ljSlqRn4BrYTtSbCa3fAG7sBAyzdgRsABfAJ/ABnAa1oWncLiOe5B3TBH4PePBiacc0JORetBSjhuXsi2agzsqeigB8tbLHLiB5k+qf68AormLgFB2ZXUkHqnJkrk38AMR9KFTxyLc2AAAAAElFTkSuQmCC"><div class="clearfix"></div></div></div><div class="dtp-time hidden"><div class="dtp-actual-maxtime">23:55</div></div><div class="dtp-picker"><div class="dtp-picker-calendar"></div><div class="dtp-picker-datetime hidden"><div class="dtp-actual-meridien"><div class="left p20"><a class="dtp-meridien-am" href="javascript:void(0);">AM</a></div><div class="dtp-actual-time p60"></div><div class="right p20"><a class="dtp-meridien-pm" href="javascript:void(0);">PM</a></div><div class="clearfix"></div></div><div id="dtp-svg-clock"></div></div>'+
('<div class="dtp-picker-year hidden" ><div><a href="javascript:void(0);" class="btn btn-default dtp-select-year-range before" style="margin: 0;"><i class="material-icons">keyboard_arrow_up</i></a></div>'+a+'<div><a href="javascript:void(0);" class="btn btn-default dtp-select-year-range after" style="margin: 0;"><i class="material-icons">keyboard_arrow_down</i></a></div></div>')+'</div></div><div class="dtp-buttons"><button class="dtp-btn-now btn btn-flat hidden">'+this.params.nowText+'</button><button class="dtp-btn-clear btn btn-flat hidden">'+
this.params.clearText+'</button><button class="dtp-btn-cancel btn btn-flat hidden">'+this.params.cancelText+'</button><button class="dtp-btn-ok btn btn-flat">'+this.params.okText+'</button><div class="clearfix"></div></div></div></div>';0>=e("body").find("#"+this.name).length&&(e("body").append(this.template),this&&(this.dtpElement=e("body").find("#"+this.name)),this.$dtpElement=e(this.dtpElement))},initButtons:function(){this._attachEvent(this.$dtpElement.find(".dtp-btn-cancel"),"click",this._onCancelClick.bind(this));
this._attachEvent(this.$dtpElement.find(".dtp-btn-calendar-ico"),"click",function(){e(this).hasClass("disabled")||e(".dtp-btn-cancel").trigger("click")});this._attachEvent(this.$dtpElement.find(".dtp-btn-ok"),"click",this._onOKClick.bind(this));this._attachEvent(this.$dtpElement.find(".dtp-btn-clock-ico"),"click",function(){e(this).hasClass("disabled")||e(".dtp-btn-ok").trigger("click")});this._attachEvent(this.$dtpElement.find("a.dtp-select-month-before"),"click",this._onMonthBeforeClick.bind(this));
this._attachEvent(this.$dtpElement.find("a.dtp-select-month-after"),"click",this._onMonthAfterClick.bind(this));this._attachEvent(this.$dtpElement.find("a.dtp-select-year-before"),"click",this._onYearBeforeClick.bind(this));this._attachEvent(this.$dtpElement.find("a.dtp-select-year-after"),"click",this._onYearAfterClick.bind(this));this._attachEvent(this.$dtpElement.find(".dtp-actual-year"),"click",this._onActualYearClick.bind(this));this._attachEvent(this.$dtpElement.find("a.dtp-select-year-range.before"),
"click",this._onYearRangeBeforeClick.bind(this));this._attachEvent(this.$dtpElement.find("a.dtp-select-year-range.after"),"click",this._onYearRangeAfterClick.bind(this));this._attachEvent(this.$dtpElement.find("div.year-picker-item"),"click",this._onYearItemClick.bind(this));!0===this.params.clearButton&&(this._attachEvent(this.$dtpElement.find(".dtp-btn-clear"),"click",this._onClearClick.bind(this)),this.$dtpElement.find(".dtp-btn-clear").removeClass("hidden"));!0===this.params.nowButton&&(this._attachEvent(this.$dtpElement.find(".dtp-btn-now"),
"click",this._onNowClick.bind(this)),this.$dtpElement.find(".dtp-btn-now").removeClass("hidden"));!0===this.params.nowButton&&!0===this.params.clearButton?this.$dtpElement.find(".dtp-btn-clear, .dtp-btn-now, .dtp-btn-cancel, .dtp-btn-ok").addClass("btn-xs"):!0!==this.params.nowButton&&!0!==this.params.clearButton||this.$dtpElement.find(".dtp-btn-clear, .dtp-btn-now, .dtp-btn-cancel, .dtp-btn-ok").addClass("btn-sm")},initMeridienButtons:function(){this.$dtpElement.find("a.dtp-meridien-am").off("click").on("click",
this._onSelectAM.bind(this));this.$dtpElement.find("a.dtp-meridien-pm").off("click").on("click",this._onSelectPM.bind(this))},initDate:function(a){this.currentView=0;!1===this.params.monthPicker&&(this.$dtpElement.find(".dtp-picker-calendar").removeClass("hidden"),this.$dtpElement.find(".dtp-btn-calendar-ico").addClass("disabled"),this.$dtpElement.find(".dtp-btn-clock-ico").removeClass("disabled"));this.$dtpElement.find(".dtp-picker-datetime").addClass("hidden");this.$dtpElement.find(".dtp-picker-year").addClass("hidden");
a="undefined"!==typeof this.currentDate&&null!==this.currentDate?this.currentDate:null;var b=this.generateCalendar(this.currentDate);"undefined"!==typeof b.week&&"undefined"!==typeof b.days&&(b=this.constructHTMLCalendar(a,b),this.$dtpElement.find("a.dtp-select-day").off("click"),this.$dtpElement.find(".dtp-picker-calendar").html(b),this.$dtpElement.find("a.dtp-select-day").on("click",this._onSelectDate.bind(this)),this.toggleButtons(a));this._centerBox();this.showDate(a)},initHours:function(){this.currentView=
1;this.showTime(this.currentDate);this.initMeridienButtons();12>this.currentDate.hour()?this.$dtpElement.find("a.dtp-meridien-am").click():this.$dtpElement.find("a.dtp-meridien-pm").click();var a=this.params.shortTime?"h":"H";this.$dtpElement.find(".dtp-picker-datetime").removeClass("hidden");this.$dtpElement.find(".dtp-picker-calendar").addClass("hidden");this.$dtpElement.find(".dtp-btn-calendar-ico").removeClass("disabled");this.$dtpElement.find(".dtp-btn-clock-ico").addClass("disabled");this.$dtpElement.find(".dtp-picker-year").addClass("hidden");
for(var b=this.createSVGClock(!0),c=0;12>c;c++){var d=-(162*Math.sin(c/12*-Math.PI*2));var e=-(162*Math.cos(c/12*-Math.PI*2));var f=this.currentDate.format(a)==c?"#8156AC":"transparent";var g=this.currentDate.format(a)==c?"#fff":"#000";f=this.createSVGElement("circle",{id:"h-"+c,"class":"dtp-select-hour",style:"cursor:pointer",r:"30",cx:d,cy:e,fill:f,"data-hour":c});g=this.createSVGElement("text",{id:"th-"+c,"class":"dtp-select-hour-text","text-anchor":"middle",style:"cursor:pointer","font-weight":"bold",
"font-size":"20",x:d,y:e+7,fill:g,"data-hour":c});g.textContent=0===c?this.params.shortTime?12:c:c;if(this.toggleTime(c,!0))f.addEventListener("click",this._onSelectHour.bind(this)),g.addEventListener("click",this._onSelectHour.bind(this));else{try{f.className+=" disabled",g.className+=" disabled"}catch(n){}g.setAttribute("fill","#bdbdbd")}b.appendChild(f);b.appendChild(g)}if(!this.params.shortTime){for(c=0;12>c;c++)d=-(110*Math.sin(c/12*-Math.PI*2)),e=-(110*Math.cos(c/12*-Math.PI*2)),f=this.currentDate.format(a)==
c+12?"#8156AC":"transparent",g=this.currentDate.format(a)==c+12?"#fff":"#000",f=this.createSVGElement("circle",{id:"h-"+(c+12),"class":"dtp-select-hour",style:"cursor:pointer",r:"25",cx:d,cy:e,fill:f,"data-hour":c+12}),g=this.createSVGElement("text",{id:"th-"+(c+12),"class":"dtp-select-hour-text","text-anchor":"middle",style:"cursor:pointer","font-weight":"bold","font-size":"22",x:d,y:e+7,fill:g,"data-hour":c+12}),g.textContent=c+12,this.toggleTime(c+12,!0)?(f.addEventListener("click",this._onSelectHour.bind(this)),
g.addEventListener("click",this._onSelectHour.bind(this))):(f.className+=" disabled",g.className+=" disabled",g.setAttribute("fill","#bdbdbd")),b.appendChild(f),b.appendChild(g);this.$dtpElement.find("a.dtp-meridien-am").addClass("hidden");this.$dtpElement.find("a.dtp-meridien-pm").addClass("hidden")}this._centerBox()},initMinutes:function(){this.currentView=2;this.showTime(this.currentDate);this.initMeridienButtons();12>this.currentDate.hour()?this.$dtpElement.find("a.dtp-meridien-am").click():this.$dtpElement.find("a.dtp-meridien-pm").click();
this.$dtpElement.find(".dtp-picker-year").addClass("hidden");this.$dtpElement.find(".dtp-picker-calendar").addClass("hidden");this.$dtpElement.find(".dtp-picker-datetime").removeClass("hidden");for(var a=this.createSVGClock(!1),b=0;60>b;b++){var c=0===b%5?162:158;var d=-(c*Math.sin(b/60*-Math.PI*2));var e=-(c*Math.cos(b/60*-Math.PI*2));c=this.currentDate.format("m")==b?"#8156AC":"transparent";d=this.createSVGElement("circle",{id:"m-"+b,"class":"dtp-select-minute",style:"cursor:pointer",r:20,cx:d,
cy:e,fill:c,"data-minute":b});if(this.toggleTime(b,!1))d.addEventListener("click",this._onSelectMinute.bind(this));else try{d.className+=" disabled"}catch(h){}a.appendChild(d)}for(b=0;60>b;b++)if(0===b%5){d=-(162*Math.sin(b/60*-Math.PI*2));e=-(162*Math.cos(b/60*-Math.PI*2));c=this.currentDate.format("m")==b?"#fff":"#000";d=this.createSVGElement("text",{id:"tm-"+b,"class":"dtp-select-minute-text","text-anchor":"middle",style:"cursor:pointer","font-weight":"bold","font-size":"20",x:d,y:e+7,fill:c,"data-minute":b});
d.textContent=b;if(this.toggleTime(b,!1))d.addEventListener("click",this._onSelectMinute.bind(this));else{try{d.className+=" disabled"}catch(h){}d.setAttribute("fill","#bdbdbd")}a.appendChild(d)}this._centerBox()},animateHands:function(){var a=this.currentDate.hour(),b=this.currentDate.minute();this.$dtpElement.find(".hour-hand")[0].setAttribute("transform","rotate("+360*a/12+")");this.$dtpElement.find(".minute-hand")[0].setAttribute("transform","rotate("+360*b/60+")")},createSVGClock:function(a){var b=
this.params.shortTime?-120:-90;var c=this.createSVGElement("svg",{"class":"svg-clock",viewBox:"0,0,400,400"}),d=this.createSVGElement("g",{transform:"translate(200,200) "}),e=this.createSVGElement("circle",{r:"192",fill:"#eee",stroke:"#bdbdbd","stroke-width":2}),f=this.createSVGElement("circle",{r:"15",fill:"#757575"});d.appendChild(e);a?(a=this.createSVGElement("line",{"class":"minute-hand",x1:0,y1:0,x2:0,y2:-150,stroke:"#EEEEEE","stroke-width":2}),b=this.createSVGElement("line",{"class":"hour-hand",
x1:0,y1:0,x2:0,y2:-150,stroke:"#8156AC","stroke-width":4}),d.appendChild(a),d.appendChild(b)):(a=this.createSVGElement("line",{"class":"minute-hand",x1:0,y1:0,x2:0,y2:-150,stroke:"#8156AC","stroke-width":2}),b=this.createSVGElement("line",{"class":"hour-hand",x1:0,y1:0,x2:0,y2:b,stroke:"#EEEEEE","stroke-width":8}),d.appendChild(b),d.appendChild(a));d.appendChild(f);c.appendChild(d);this.$dtpElement.find("#dtp-svg-clock").empty();this.$dtpElement.find("#dtp-svg-clock")[0].appendChild(c);this.animateHands();
return d},createSVGElement:function(a,b){var c=m.createElementNS("http://www.w3.org/2000/svg",a),d;for(d in b)c.setAttribute(d,b[d]);return c},isAfterMinDate:function(a,b,c){var d=!0;"undefined"!==typeof this.minDate&&null!==this.minDate&&(d=f(this.minDate),a=f(a),b||c||(d.hour(0),d.minute(0),a.hour(0),a.minute(0)),d.second(0),a.second(0),d.millisecond(0),a.millisecond(0),c||(a.minute(0),d.minute(0)),d=parseInt(a.format("X"))>=parseInt(d.format("X")));return d},isBeforeMaxDate:function(a,b,c){var d=
!0;"undefined"!==typeof this.maxDate&&null!==this.maxDate&&(d=f(this.maxDate),a=f(a),b||c||(d.hour(0),d.minute(0),a.hour(0),a.minute(0)),d.second(0),a.second(0),d.millisecond(0),a.millisecond(0),c||(a.minute(0),d.minute(0)),d=parseInt(a.format("X"))<=parseInt(d.format("X")));return d},rotateElement:function(a,b){e(a).css({WebkitTransform:"rotate("+b+"deg)","-moz-transform":"rotate("+b+"deg)"})},showDate:function(a){a&&(this.$dtpElement.find(".dtp-actual-day").html(a.locale(this.params.lang).format("dddd")),
this.$dtpElement.find(".dtp-actual-month").html(a.locale(this.params.lang).format("MMM").toUpperCase()),this.$dtpElement.find(".dtp-actual-num").html(a.locale(this.params.lang).format("DD")),this.$dtpElement.find(".dtp-actual-year").html(a.locale(this.params.lang).format("YYYY")))},showTime:function(a){if(a){var b=a.minute(),b=(this.params.shortTime?a.format("hh"):a.format("HH"))+":"+(2==b.toString().length?b:"0"+b)+(this.params.shortTime?" "+a.format("A"):"");this.params.date?this.$dtpElement.find(".dtp-actual-time").html(b):
(this.params.shortTime?this.$dtpElement.find(".dtp-actual-day").html(a.format("A")):this.$dtpElement.find(".dtp-actual-day").html("&nbsp;"),this.$dtpElement.find(".dtp-actual-maxtime").html(b))}},selectDate:function(a){a&&(this.currentDate.date(a),this.showDate(this.currentDate),this.$element.trigger("dateSelected",this.currentDate))},generateCalendar:function(a){var b={};if(null!==a){var c=f(a).locale(this.params.lang).startOf("month");a=f(a).locale(this.params.lang).endOf("month");var d=c.format("d");
b.week=this.days;b.days=[];for(var e=c.date();e<=a.date();e++){if(e===c.date()){var h=b.week.indexOf(d.toString());if(0<h)for(var g=0;g<h;g++)b.days.push(0)}b.days.push(f(c).locale(this.params.lang).date(e))}}return b},constructHTMLCalendar:function(a,b){var c=""+('<div class="dtp-picker-month">'+a.locale(this.params.lang).format("MMMM YYYY")+"</div>");c+='<table class="table dtp-picker-days"><thead>';for(var d=0;d<b.week.length;d++)c+="<th>"+f(parseInt(b.week[d]),"d").locale(this.params.lang).format("ddd").substring(0,
1)+"</th>";c+="</thead><tbody><tr>";for(d=0;d<b.days.length;d++)0==d%7&&(c+="</tr><tr>"),c+='<td data-date="'+f(b.days[d]).locale(this.params.lang).format("D")+'">',0!=b.days[d]&&(c=!1===this.isBeforeMaxDate(f(b.days[d]),!1,!1)||!1===this.isAfterMinDate(f(b.days[d]),!1,!1)||-1!==this.params.disabledDays.indexOf(b.days[d].isoWeekday())?c+('<span class="dtp-select-day">'+f(b.days[d]).locale(this.params.lang).format("DD")+"</span>"):f(b.days[d]).locale(this.params.lang).format("DD")===f(this.currentDate).locale(this.params.lang).format("DD")?
c+('<a href="javascript:void(0);" class="dtp-select-day selected">'+f(b.days[d]).locale(this.params.lang).format("DD")+"</a>"):c+('<a href="javascript:void(0);" class="dtp-select-day">'+f(b.days[d]).locale(this.params.lang).format("DD")+"</a>"),c+="</td>");return c+"</tr></tbody></table>"},setName:function(){for(var a="",b=0;5>b;b++)a+="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(Math.floor(62*Math.random()));return a},isPM:function(){return this.$dtpElement.find("a.dtp-meridien-pm").hasClass("selected")},
setElementValue:function(){this.$element.trigger("beforeChange",this.currentDate);"undefined"!==typeof e.material&&this.$element.removeClass("empty");this.$element.val(f(this.currentDate).locale(this.params.lang).format(this.params.format));this.$element.trigger("change",this.currentDate)},toggleButtons:function(a){if(a&&a.isValid()){var b=f(a).locale(this.params.lang).startOf("month"),c=f(a).locale(this.params.lang).endOf("month");this.isAfterMinDate(b,!1,!1)?this.$dtpElement.find("a.dtp-select-month-before").removeClass("invisible"):
this.$dtpElement.find("a.dtp-select-month-before").addClass("invisible");this.isBeforeMaxDate(c,!1,!1)?this.$dtpElement.find("a.dtp-select-month-after").removeClass("invisible"):this.$dtpElement.find("a.dtp-select-month-after").addClass("invisible");b=f(a).locale(this.params.lang).startOf("year");a=f(a).locale(this.params.lang).endOf("year");this.isAfterMinDate(b,!1,!1)?this.$dtpElement.find("a.dtp-select-year-before").removeClass("invisible"):this.$dtpElement.find("a.dtp-select-year-before").addClass("invisible");
this.isBeforeMaxDate(a,!1,!1)?this.$dtpElement.find("a.dtp-select-year-after").removeClass("invisible"):this.$dtpElement.find("a.dtp-select-year-after").addClass("invisible")}},toggleTime:function(a,b){if(b){var c=f(this.currentDate);c.hour(this.convertHours(a)).minute(0).second(0);c=!(!1===this.isAfterMinDate(c,!0,!1)||!1===this.isBeforeMaxDate(c,!0,!1))}else c=f(this.currentDate),c.minute(a).second(0),c=!(!1===this.isAfterMinDate(c,!0,!0)||!1===this.isBeforeMaxDate(c,!0,!0));return c},_attachEvent:function(a,
b,c){a.on(b,null,null,c);this._attachedEvents.push([a,b,c])},_detachEvents:function(){for(var a=this._attachedEvents.length-1;0<=a;a--)this._attachedEvents[a][0].off(this._attachedEvents[a][1],this._attachedEvents[a][2]),this._attachedEvents.splice(a,1)},_fireCalendar:function(){this.currentView=0;this.$element.blur();this.initDates();this.show();this.params.date?(this.$dtpElement.find(".dtp-date").removeClass("hidden"),this.initDate()):this.params.time&&(this.$dtpElement.find(".dtp-time").removeClass("hidden"),
this.initHours())},_onBackgroundClick:function(a){a.stopPropagation();this.hide()},_onElementClick:function(a){a.stopPropagation()},_onKeydown:function(a){27===a.which&&this.hide()},_onCloseClick:function(){this.hide()},_onClearClick:function(){this.currentDate=null;this.$element.trigger("beforeChange",this.currentDate);this.hide();"undefined"!==typeof e.material&&this.$element.addClass("empty");this.$element.val("");this.$element.trigger("change",this.currentDate)},_onNowClick:function(){this.currentDate=
f();!0===this.params.date&&(this.showDate(this.currentDate),0===this.currentView&&this.initDate());if(!0===this.params.time){this.showTime(this.currentDate);switch(this.currentView){case 1:this.initHours();break;case 2:this.initMinutes()}this.animateHands()}},_onOKClick:function(){switch(this.currentView){case 0:!0===this.params.time?this.initHours():(this.setElementValue(),this.hide());break;case 1:this.initMinutes();break;case 2:this.setElementValue(),this.hide()}},_onCancelClick:function(){if(this.params.time)switch(this.currentView){case 0:this.hide();
break;case 1:this.params.date?this.initDate():this.hide();break;case 2:this.initHours()}else this.hide()},_onMonthBeforeClick:function(){this.currentDate.subtract(1,"months");this.initDate(this.currentDate);this._closeYearPicker()},_onMonthAfterClick:function(){this.currentDate.add(1,"months");this.initDate(this.currentDate);this._closeYearPicker()},_onYearBeforeClick:function(){this.currentDate.subtract(1,"years");this.initDate(this.currentDate);this._closeYearPicker()},_onYearAfterClick:function(){this.currentDate.add(1,
"years");this.initDate(this.currentDate);this._closeYearPicker()},refreshYearItems:function(){var a=this.currentDate.year(),b=this.midYear,c=1850;"undefined"!==typeof this.minDate&&null!==this.minDate&&(c=f(this.minDate).year());var d=2200;"undefined"!==typeof this.maxDate&&null!==this.maxDate&&(d=f(this.maxDate).year());this.$dtpElement.find(".dtp-picker-year .invisible").removeClass("invisible");this.$dtpElement.find(".year-picker-item").each(function(f,h){var g=b-3+f;e(h).attr("data-year",g).text(g).data("year",
g);a==g?e(h).addClass("active"):e(h).removeClass("active");(g<c||g>d)&&e(h).addClass("invisible")});c>=b-3&&this.$dtpElement.find(".dtp-select-year-range.before").addClass("invisible");d<=b+3&&this.$dtpElement.find(".dtp-select-year-range.after").addClass("invisible");this.$dtpElement.find(".dtp-select-year-range").data("mid",b)},_onActualYearClick:function(){this.params.year&&(0<this.$dtpElement.find(".dtp-picker-year.hidden").length?(this.$dtpElement.find(".dtp-picker-datetime").addClass("hidden"),
this.$dtpElement.find(".dtp-picker-calendar").addClass("hidden"),this.$dtpElement.find(".dtp-picker-year").removeClass("hidden"),this.midYear=this.currentDate.year(),this.refreshYearItems()):this._closeYearPicker())},_onYearRangeBeforeClick:function(){this.midYear-=7;this.refreshYearItems()},_onYearRangeAfterClick:function(){this.midYear+=7;this.refreshYearItems()},_onYearItemClick:function(a){a=e(a.currentTarget).data("year");var b=this.currentDate.year();this.currentDate.add(a-b,"years");this.initDate(this.currentDate);
this._closeYearPicker();this.$element.trigger("yearSelected",this.currentDate)},_closeYearPicker:function(){this.$dtpElement.find(".dtp-picker-calendar").removeClass("hidden");this.$dtpElement.find(".dtp-picker-year").addClass("hidden")},enableYearPicker:function(){this.params.year=!0;this.$dtpElement.find(".dtp-actual-year").reomveClass("disabled")},disableYearPicker:function(){this.params.year=!1;this.$dtpElement.find(".dtp-actual-year").addClass("disabled");this._closeYearPicker()},_onSelectDate:function(a){this.$dtpElement.find("a.dtp-select-day").removeClass("selected");
e(a.currentTarget).addClass("selected");this.selectDate(e(a.currentTarget).parent().data("date"));!0===this.params.switchOnClick&&!0===this.params.time&&setTimeout(this.initHours.bind(this),200);!0===this.params.switchOnClick&&!1===this.params.time&&setTimeout(this._onOKClick.bind(this),200)},_onSelectHour:function(a){if(!e(a.target).hasClass("disabled")){var b=e(a.target).data("hour");a=e(a.target).parent();for(var c=a.find(".dtp-select-hour"),d=0;d<c.length;d++)e(c[d]).attr("fill","transparent");
c=a.find(".dtp-select-hour-text");for(d=0;d<c.length;d++)e(c[d]).attr("fill","#000");e(a.find("#h-"+b)).attr("fill","#8156AC");e(a.find("#th-"+b)).attr("fill","#fff");this.currentDate.hour(parseInt(b));!0===this.params.shortTime&&this.isPM()&&this.currentDate.add(12,"hours");this.showTime(this.currentDate);this.animateHands();!0===this.params.switchOnClick&&setTimeout(this.initMinutes.bind(this),200)}},_onSelectMinute:function(a){if(!e(a.target).hasClass("disabled")){var b=e(a.target).data("minute");
a=e(a.target).parent();for(var c=a.find(".dtp-select-minute"),d=0;d<c.length;d++)e(c[d]).attr("fill","transparent");c=a.find(".dtp-select-minute-text");for(d=0;d<c.length;d++)e(c[d]).attr("fill","#000");e(a.find("#m-"+b)).attr("fill","#8156AC");e(a.find("#tm-"+b)).attr("fill","#fff");this.currentDate.minute(parseInt(b));this.showTime(this.currentDate);this.animateHands();!0===this.params.switchOnClick&&setTimeout(function(){this.setElementValue();this.hide()}.bind(this),200)}},_onSelectAM:function(a){e(".dtp-actual-meridien").find("a").removeClass("selected");
e(a.currentTarget).addClass("selected");12<=this.currentDate.hour()&&this.currentDate.subtract(12,"hours")&&this.showTime(this.currentDate);this.toggleTime(1===this.currentView)},_onSelectPM:function(a){e(".dtp-actual-meridien").find("a").removeClass("selected");e(a.currentTarget).addClass("selected");12>this.currentDate.hour()&&this.currentDate.add(12,"hours")&&this.showTime(this.currentDate);this.toggleTime(1===this.currentView)},_hideCalendar:function(){this.$dtpElement.find(".dtp-picker-calendar").addClass("hidden")},
convertHours:function(a){var b=a;!0===this.params.shortTime&&12>a&&this.isPM()&&(b+=12);return b},setDate:function(a){this.params.currentDate=a;this.initDates()},setMinDate:function(a){this.params.minDate=a;this.initDates()},setMaxDate:function(a){this.params.maxDate=a;this.initDates()},destroy:function(){this._detachEvents();this.$dtpElement.remove()},show:function(){this.$dtpElement.removeClass("hidden");this._attachEvent(e(k),"keydown",this._onKeydown.bind(this));this._centerBox();this.$element.trigger("open");
!0===this.params.monthPicker&&this._hideCalendar()},hide:function(){e(k).off("keydown",null,null,this._onKeydown.bind(this));this.$dtpElement.addClass("hidden");this.$element.trigger("close")},_centerBox:function(){var a=(this.$dtpElement.height()-this.$dtpElement.find(".dtp-content").height())/2;this.$dtpElement.find(".dtp-content").css("marginLeft",-(this.$dtpElement.find(".dtp-content").width()/2)+"px");this.$dtpElement.find(".dtp-content").css("top",a+"px")},enableDays:function(){var a=this.params.enableDays;
a&&e(".dtp-picker-days tbody tr td").each(function(){0<=e.inArray(e(this).index(),a)||e(this).find("a").css({background:"#e3e3e3",cursor:"no-drop",opacity:"0.5"}).off("click")})}};
// angular&&angular.module&&angular.module("ngDatetimepicker",["ng"]).directive("ngDatetimepicker",[function(){return{restrict:"E",scope:{ngModel:"=ngModel",ngOpen:"=ngOpen",ngMindate:"=ngMindate",ngCancelLabel:"@ngCancelLabel",ngCleanLabel:"@ngCleanLabel",ngOkLabel:"@ngOkLabel",ngLang:"@ngLang"},link:function(a,b){var c={component:e(b).find(".datepicker").bootstrapMaterialDatePicker({format:"dddd DD MMMM YYYY - HH:mm",
// cancelText:a.ngCancelLabel?a.ngCancelLabel:"Cancel",clearText:a.ngCleanLabel?a.ngCleanLabel:"Clean",okText:a.ngOkLabel?a.ngOkLabel:"OK",lang:a.ngLang?a.ngLang:"en"}).on("change",function(b,c){a.$apply(function(){a.ngModel=c&&c.toDate?c.toDate():void 0})}),ngOpen:function(){setTimeout(function(){e(b).find(".datepicker").trigger("click")},100)},setMinDate:function(a){a&&e(b).find(".datepicker").bootstrapMaterialDatePicker&&e(b).find(".datepicker").bootstrapMaterialDatePicker("setMinDate",a)}};a.ngOpen=
// c.ngOpen.bind(c);a.$watch("ngMindate",c.setMinDate)},template:'<input type="hidden" class="datepicker hide">'}}])
})(window,document,jQuery,moment);
