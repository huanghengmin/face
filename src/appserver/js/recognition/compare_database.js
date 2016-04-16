Ext.onReady(function() {

    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';

    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name:'id',			mapping:'id'},
        {name:'name',		mapping:'name'},
        {name:'type',		mapping:'type'},
        {name:'depart',		mapping:'depart'},
        {name:'describe',		mapping:'describe'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../../CompareDBAction_find.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"total",
        root:"rows"
    },record);
    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy : proxy,
        reader : reader
    });
    store.load({
        params:{
            start:start,limit:pageSize
        }
    });
//    var boxM = new Ext.grid.CheckboxSelectionModel({singleSelect: true});   //复选框
    var rowNumber = new Ext.grid.RowNumberer();
    var colM = new Ext.grid.ColumnModel([
//        boxM,
        rowNumber,
        {header:'名称',		dataIndex:'name',		   align:'center',sortable:true,menuDisabled:true},
        {header:"类型",	dataIndex:"type",       align:'center',sortable:true,menuDisabled:true},
        {header:"部门",	dataIndex:"depart",       align:'center',sortable:true,menuDisabled:true},
        {header:"场所",	dataIndex:"place",       align:'center',sortable:true,menuDisabled:true},
        {header:"描述",	dataIndex:"describe",       align:'center',sortable:true,menuDisabled:true},
        {header:'操作标记', dataIndex:'flag', width:120,sortable:true, menuDisabled:true, renderer:show_flag}
    ]);
    var tbar = new Ext.Toolbar({
        autoWidth :true,
        autoHeight:true,
        items:[ {
            id:'add.info',
            xtype:'button',
            iconCls:'add',
            text:'新增比对库',
            handler:function () {
                add(store);
            }
        }]
    });

    var tb = new Ext.Toolbar({
        height : 30,
        items : ['名称：', {
            id : 'name.tb.info',
            xtype : 'textfield',
            name : 'name',
            emptyText : '输入比对库名称'
        },{
            xtype : 'tbspacer',
            width : 10
        },'类型：', {
            id : 'type.tb.info',
            xtype : 'textfield',
            name : 'type',
            emptyText : '输入比对库类型'
        }, {
            xtype : 'tbspacer',
            width : 10
        },'部门：', {
            id : 'depart.tb.info',
            xtype : 'textfield',
            name : 'depart',
            emptyText : '输入比对库部门'
        },{
            text : '查询',
            iconCls:'query',
            listeners : {
                click : function() {
                    var name = Ext.fly("name.tb.info").dom.value == '点击输入名称'
                        ? null
                        : Ext.fly('name.tb.info').dom.value;
                    var type = Ext.fly("type.tb.info").dom.value == '点击输入类型'
                        ? null
                        : Ext.fly('type.tb.info').dom.value;
                    var depart = Ext.fly("depart.tb.info").dom.value == '点击输入部门'
                        ? null
                        : Ext.fly('depart.tb.info').dom.value;

                    store.setBaseParam('name', name);
                    store.setBaseParam('type', type);
                    store.setBaseParam('depart', depart);

                    store.load({
                        params : {
                            start : start,
                            limit : pageSize
                        }
                    });
                }
            }
        }]
    });


    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:store,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });

    function setHeight() {
        var h = document.body.clientHeight - 8;
        return h;
    };

    function setWidth(){
        return document.body.clientWidth-8;
    }

    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.info',
        plain:true,
        height:setHeight(),
        width:setWidth(),
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:false,
        collapsible:false,
        cm:colM,
//        sm:boxM,
        store:store,
        stripeRows:true,
        autoExpandColumn:'Position',
        disableSelection:true,
        bodyStyle:'width:100%',
        enableDragDrop: true,
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        },
        tbar:tbar,
        listeners: {
            render: function () {
                tb.render(this.tbar);
            }
        },
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });

    var port = new Ext.Viewport({
        layout:'fit',
        renderTo: Ext.getBody(),
        items:[grid_panel]
    });
});

function show_flag(value, p, r) {
    return String.format(
        '<a id="del.info" href="javascript:void(0);" onclick="del();return false;"style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;' +
        '<a id="mod.info" href="javascript:void(0);" onclick="mod();return false;"style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;'

    );
}

function add(grid_panel,store){
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:120,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults:{
            anchor:'90%',
             allowBlank:false,
             blankText:'该项不能为空！'
        },
        items:[
            new Ext.form.TextField({
                fieldLabel : '日志服务器主机',
                name : 'sysLogServer.host',
                allowBlank:false,
                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText:'请输入正确的IP地址',
                blankText:"接收日志IP地址"
            }),
            new Ext.form.TextField({
                fieldLabel:'日志服务器端口',
                regex: /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/,
                regexText:'请输入0-65535的端口',
                name : 'sysLogServer.port',
                allowBlank:false,
                blankText:"日志服务器端口"
            })
        ]
    });

    var win = new Ext.Window({
        title:"新增日志服务器",
        width:500,
        layout:'fit',
        height:150,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'insert_win.info',
                text:'保存',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../SysLogConfigAction_add.action",
                            method:'POST',
                            waitTitle:'系统提示',
                            waitMsg:'正在连接...',
                            success:function (form,action) {
                                Ext.MessageBox.show({
                                    title:'信息',
                                   
                                    msg:action.result.msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function (e) {
                                        grid_panel.render();
                                        store.reload();
                                        win.close();
                                    }
                                });
                            },
                            failure:function (form,action) {
                                Ext.MessageBox.show({
                                    title:'信息',
                                   
                                    msg:action.result.msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            buttons:Ext.MessageBox.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }, {
                text:'重置',
                handler:function () {
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}

function mod(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var host = recode.get('host');
    var port = recode.get('port');
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:120,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults:{
             allowBlank:false,
            anchor:'90%',
             blankText:'该项不能为空！'
        },
        items:[
            new Ext.form.TextField({
                fieldLabel : '日志服务器主机',
                name : 'sysLogServer.host',
                value:recode.get('host'),
                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText:'请输入正确的IP地址',
                allowBlank:false,
                blankText:"日志服务器IP地址"
            }),
            new Ext.form.TextField({
                fieldLabel:'日志服务器端口',
                name : 'sysLogServer.port',
                regex: /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/,
                regexText:'请输入0-65535的端口',
                value:recode.get('port'),
                allowBlank:false,
                blankText:"日志服务器端口"
            })
        ]
    });

    var win = new Ext.Window({
        title:"修改日志服务器",
        width:500,
        layout:'fit',
        height:150,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'insert_win.info',
                text:'保存',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../SysLogConfigAction_modify.action",
                            method:'POST',
                            params:{host:host,port:port},
                            waitTitle:'系统提示',
                            waitMsg:'正在连接...',
                            success:function (form,action) {
                                Ext.MessageBox.show({
                                    title:'信息',
                                   
                                    msg:action.result.msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function (e) {
                                        grid_panel.render();
                                        grid_panel.getStore().reload();
                                        win.close();
                                    }
                                });
                            },
                            failure:function (form,action) {
                                Ext.MessageBox.show({
                                    title:'信息',
                                   
                                    msg:action.result.msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            buttons:Ext.MessageBox.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            }, {
                text:'重置',
                handler:function () {
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}

function del() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var host = recode.get('host');
    var port = recode.get('port');
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("警告", "确认删除吗?", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url:"../../SysLogConfigAction_delete.action",
                    method:"POST",
                    params:{host:host,port:port},
                    success:function (form, action) {
                        Ext.Msg.alert("提示", "删除成功!");
                        grid_panel.getStore().reload();

                    },
                    failure:function (form, action) {
                        Ext.Msg.alert("提示", "删除失败!");
                        grid_panel.getStore().reload();

                    }
                });
            }
        });
    }
}


