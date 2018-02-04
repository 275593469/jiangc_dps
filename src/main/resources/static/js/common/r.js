
// JavaScript Document
(function() {
    urlParameters = (function(script) {
        var l = script.length;
        for (var i = 0; i < l; i++) {
            me = !!document.querySelector ? script[i].src: script[i].getAttribute('src', l);
            if (me.substr(me.lastIndexOf('/')).indexOf('xyx_a') !== -1) {
                break;
            }
        }
        return me;
    })(document.getElementsByTagName('script'));
	
	var xyx = xyx || [];
    var xyx_jspa = ['xyx_a', 'xyx_domain'];
    for (var i = 0; i < xyx_jspa.length; i++) {
        xyx.push([xyx_jspa[i], getJsParam(xyx_jspa[i])]);
    }
	
    var xyx_pa = ['xyx_o', 'xyx_c', 'xyx_h', 'xyx_e'];
    for (var i = 0; i < xyx_pa.length; i++) {
        xyx.push([xyx_pa[i], getUrlParam(xyx_pa[i])]);
    }
    function getUrlParam(f) {
        if (f == "" || f == null) {
            return
        }
        var s = window.location.href,
        d = s.split(f),
        e = "";
        if (d.length > 1) {
            s = d[1];
            e = s.split("&")[0].replace("=", "");
            return e;
        }
        var p = new RegExp("(^|&)" + f + "=([^&]*)(&|$)"),
        q = window.location.search.substr(1).match(p);
        if (q != null && q) {
            return q[2]
        }
        var u = window.location.hash.substr(1).match(p);
        if (u != null && u) {
            return u[2]
        }
        return ""
    }
    function getJsParam(f) {
        if (f == "" || f == null) {
            return
        }
        var s = urlParameters,
        d = s.split(f),
        e = "";
        if (d.length > 1) {
            s = d[1];
            e = s.split("&")[0].replace("=", "");
            return e;
        }
        return ""
    }
    var xyx_img = "xyx_" + (new Date()).getTime();
    var url = location.href;
    var ref = document.referrer;
    function type(d) {
        return Object.prototype.toString.call(d) === "[object Array]";
    }
    xyx.getLast = function(f) {
        for (var d = this.length - 1; 0 <= d; d--) {
            if (type(this[d])) {
                if (this[d][0] == f) {
                    return this[d][1];
                }
            }
        }
    };
    xyx.serialize = function() {
        var p = ['xyx_o', 'xyx_c', 'xyx_h', 'xyx_e', 'xyx_m', 'xyx_r'];
        var s = [];
        for (var i = 0; i < p.length; i++) {
            if (xyx.getLast(p[i])) {
                s.push(xyx.getLast(p[i]));
            } else {
                s.push('');
            }
        }
        if (s == '') {
            return false;
        }
        s = s.join('_');
        return s;
    }
 
    function startRts() {
        var seri = xyx.serialize();
		var cs='',hs='';
		cs=xyx.getLast('xyx_c');hs=xyx.getLast('xyx_h');
        if (seri.length > 5&&cs!=''&&hs!=''&&!isNaN(cs)&&!isNaN(hs)) {
         
            var c = window[xyx_img] = new Image();
            c.onload = (c.onerror = function() {
                window[xyx_img] = null;
            });
            c.src = '//' + xyx.getLast('xyx_domain') + '/r?a=' + encodeURIComponent(xyx.getLast('xyx_a')) + '&r=r&u=' + encodeURIComponent(url) + '&f=' + encodeURIComponent(ref) + '&i=' +encodeURIComponent(seri);
            c = null;
        } else {
       
                var c = window[xyx_img] = new Image();
                c.onload = (c.onerror = function() {
                    window[xyx_img] = null;
                });
                c.src = '//' + xyx.getLast('xyx_domain') + '/r?a=' + encodeURIComponent(xyx.getLast('xyx_a')) + '&r=v&u=' + encodeURIComponent(url) + '&f=' + encodeURIComponent(ref);
                c = null;
        }
    }
    startRts();
})();