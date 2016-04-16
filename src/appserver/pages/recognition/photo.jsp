<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>JS网页图片查看器－可控制图片放大缩小还原移动效果</title>
<META HTTP-EQUIV="imagetoolbar" CONTENT="no">
<style type="text/css">
    body { font-family: "Verdana", "Arial", "Helvetica", "sans-serif"; font-size: 12px; line-height: 180%; }
    td { font-size: 12px; line-height: 150%; }
</style>
<SCRIPT language=JavaScript type="text/javascript" src="${pageContext.request.contextPath}/js/recognition/photo.js"></SCRIPT>

<style type="text/css">
</style>
</head>
<body id="body.1" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" oncontextmenu="return false"
      ondragstart="return false" onselectstart ="return false"
      onbeforecopy="return false"
      style="overflow-y:hidden;overflow-x:hidden;" onload="initVar()">
<div id="Layer1">
    <table border="0" cellspacing="2" cellpadding="0">
        <tr>
            <td> </td>
            <td><img src="../../img/up.gif" width="20" height="20" style="cursor:hand" onClick="clickMove('up')" title="向上"></td>
            <td> </td>
        </tr>
        <tr>
            <td><img src="../../img/left.gif" width="20" height="20" style="cursor:hand" onClick="clickMove('left')" title="向左"></td>
            <td><img src="../../img/zoom.gif" width="20" height="20" style="cursor:hand" onClick="realsize();" title="还原"></td>
            <td><img src="../../img/right.gif" width="20" height="20" style="cursor:hand" onClick="clickMove('right')" title="向右"></td>
        </tr>
        <tr>
            <td> </td>
            <td><img src="../../img/down.gif" width="20" height="20" style="cursor:hand" onClick="clickMove('down')" title="向下"></td>
            <td> </td>
        </tr>
        <tr>
            <td> </td>
            <td><img src="../../img/zoom_in.gif" width="20" height="20" style="cursor:hand" onClick="bigit();" title="放大"></td>
            <td> </td>
        </tr>
        <tr>
            <td> </td>
            <td><img src="../../img/zoom_out.gif" width="20" height="20" style="cursor:hand" onClick="smallit();" title="缩小"></td>
            <td> </td>
        </tr>
    </table>
</div>
<p>

<div id='hiddenPic' style='position:absolute; left:0px; top:0px; width:0px; height:0px; z-index:1; visibility: hidden;'>
    <img id="img.2"  name='images2' src='' border='0'>
</div>
<div id='block1' onmouseout='drag=0' onmouseover='dragObj=block1; drag=1;'
     style='z-index:10; height: 0; left: 50px; position: absolute; top: 50px; width: 0' class="dragAble">
    <img id="img.1" name='images1' src='' border='0'>
</div>
</body>
</html>
